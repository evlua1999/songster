class Song():
    name = ''
    artist = ''
    year = ''
    link = ''
    id = ''

    def __init__(self, name, artist, year, link, id):
        self.name = name
        self.artist = artist
        self.year = year
        self.link = link
        self.id = id

    def toDict(self):
        return {'name': "{" + self.name.replace("&", "\&") + "}",
                'artist': "{" + self.artist.replace("&", "\&") + "}",
                'year': "{" + self.year + "}",
                'link': "{" + self.link.replace("&", "\&") + "}"
                }