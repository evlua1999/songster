# Songster
Tools to build your own Hitster game (use your own playlist)

This repo consists of two parts, the app for your android, as well as a python script to generate the csv files for printing your own cards (via form letter in MS word).
Currently, the form letter is not yet working (this is a todo).

## Generating your own cards
First, install:
* python (and al)


Until the Authentication Mode is changed, you need to generate your own Client Secret and ID in the Spotify Developer Dashboard and add them to the credentials_template.py and change its name to credentials.py

Then just execute generate.py "playlist-url" --file "filename.csv"

Please note, the script takes as the year for the song the year of its album. So make sure, your playlist has the songs from the oldest available album. For example, you should not have any remastered songs in your playlist. If you want those in your playlist for some reason (or in the case of classical music), you have to change the years (and composer) by yourself in the csv.

Also, remove any non-utf-8 chars from the csv.

In the future, this script should also have the option to take a csv and convert it to pdf cards. 