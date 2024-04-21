import csv
from song import *

def export_csv(songs, file):
    with open(file, "w", newline='') as csvfile:
        fieldnames = ['name', 'artist', 'year', 'link']
        writer = csv.DictWriter(csvfile, fieldnames=fieldnames, quoting=csv.QUOTE_ALL)
        writer.writeheader()

        for song in songs:
            writer.writerow(song.toDict())