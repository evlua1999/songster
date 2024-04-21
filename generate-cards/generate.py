import argparse
from playlist import *
from export_csv import *

# Command Line Interface
parser = argparse.ArgumentParser(description='Produces csv to print for the Songstar game from a given public Spotify Playlist')
parser.add_argument('link', metavar='link',
                    help='Link to public spotify playlist')
parser.add_argument('--file', required=False, dest='file', default='files/temp.csv',
                    help='File to save the generated csv to')

args = parser.parse_args()
link = args.link

playlist = Playlist(link)
songs = playlist.get_songs()

export_csv(songs, args.file)
