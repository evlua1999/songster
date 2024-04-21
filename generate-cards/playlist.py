from authenticate import *
from song import *
import re
import requests
import json

class Playlist():
    id = ""
    type = ""
    songs = []

    def __init__(self, link):

        split_link = re.split('/', link)
        self.type = split_link[-2]
        self.id = re.split('\?',split_link[-1])[0]
        
    def get_songs(self):
        if(len(self.songs) == 0):
            self.request_songs()
        return self.songs

    def request_songs(self):
        auth = get_token_type() +' '+ get_access_token()
        headers = {'Authorization': get_token_type() +' '+ get_access_token()}
        offset = 0
        tracks = []

        #get Number of tracks in playlist
        url = 'https://api.spotify.com/v1/playlists/'+self.id+'/tracks?fields=total'
        r = requests.get(url, headers=headers)
        number_tracks = r.json()['total']
        print(number_tracks)

        #request all tracks of a playlist (spotify limits results per request)
        while(offset < number_tracks):
            url = 'https://api.spotify.com/v1/playlists/'+self.id+'/tracks?fields=items%28track%28artists%28name%29%2C+id%2C+name%2C+external_urls%2C+album%28release_date%29%29%29' + '&offset=' + str(offset)
            r = requests.get(url, headers=headers)
            new_tracks = r.json()['items']
            offset += len(new_tracks)
            print("Querried " + str(len(new_tracks)) + " tracks")
            tracks.extend(new_tracks)

        
        #converts tracks to songs
        for track in tracks:
            track = track['track'] #don't ask...
            name = track['name']
            id = track['id']
            link = track['external_urls']['spotify']
            year = re.split('-',track['album']['release_date'])[0]
            artists = []
            for artist in track['artists']:
                artists.append(artist['name'])
            artist_string = ' & '.join(artists)
            new_song = Song(name, artist_string, year, link, id)
            self.songs.append(new_song)
            print(new_song.name)

