import argparse

# Command Line Interface
parser = argparse.ArgumentParser(description='Produces PDF-Files to print for the Songstar game from a given public Spotify Playlist')
parser.add_argument('link', metavar='link',
                    help='Link to public spotify playlist')
parser.add_argument('--file', required=False, dest='file',
                    help='File to save the generated pdf to')

args = parser.parse_args()
link = args.link

