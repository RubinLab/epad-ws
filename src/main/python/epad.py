#!/usr/bin/env python

import os, sys, re, requests

epad_auth_base='/session/'

def login(epad_base_url, user, password):
  epad_auth_url = epad_base_url + epad_auth_base
  r = requests.post(epad_auth_url, auth=(user, password))
  if r.status_code == requests.codes.ok:
    print 'ePAD login request succeeded'
    jsessionid = r.text
    return jsessionid
  else:
    print 'Error: ePAD login request failed - status code =', r.status_code
    r.raise_for_status()

def logout(epad_base_url, jsessionid):
  epad_auth_url = epad_base_url + epad_auth_base
  cookies = dict(JSESSIONID=jsessionid)
  r = requests.delete(epad_auth_url, cookies=cookies)
  if r.status_code != requests.codes.ok:
    print 'Warning: ePAD logout request failed - status code =', r.status_code
  
