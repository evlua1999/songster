import requests
import json
import time

from credentials import *

url = 'https://accounts.spotify.com/api/token'
headers = {'content-type': 'application/x-www-form-urlencoded'}
access_token = ''
token_expires = 0
token_type = ''

def generate_access_token():
    payload = {'grant_type':'client_credentials', 'client_id': get_client_id(), 'client_secret': get_client_secret() }
    r = requests.post(url, data=payload, headers=headers)
    global access_token
    access_token = r.json()['access_token']
    global token_type
    token_type = r.json()['token_type']
    global token_expires 
    token_expires = time.time()+r.json()['expires_in']
    print('new token generated')


def get_access_token():
    if(token_expires < time.time()):
        generate_access_token()
    return access_token    

def get_token_type():
    return token_type










