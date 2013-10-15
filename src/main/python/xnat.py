#!/usr/bin/env python

import os, sys, re, requests, urllib

xnat_auth_base='/xnat/data/JSESSION'
xnat_project_base='/xnat/data/projects/'

def project_name_to_xnat_project_id(project_name):
  return re.sub('[ ,]', '_', project_name) # Replace spaces and commas with underscores

# We URL-encode the label because XNAT labels do not allow any special characters. 
# XNAT also does not allow the '%' character so after URL-encoding we replace the it with a space.
# The recipient must replace this space with a '%' and then decode as normal.
# . not a valid XNAT ID character and URL encoding does not typically map it so we manually map.
def patient_id_to_xnat_subject_id(patient_id):
  urlencoded = urllib.quote(patient_id)
  return urlencoded.replace('.', '%2e').replace('%', '_') 

# Replace non alpha-numeric and any non .-_ with dashes. 
def patient_name_to_xnat_subject_label(patient_name):
  return re.sub('[^a-zA-Z0-9\.\-_ ]', '-', patient_name.replace('^', ' ')) 

def study_uid_to_xnat_experiment_id(study_uid):
  return study_uid.replace('.', '_') # XNAT does not like periods in its IDs

def login(xnat_base_url, user, password):
  xnat_auth_url = xnat_base_url + xnat_auth_base
  r = requests.post(xnat_auth_url, auth=(user, password))
  if r.status_code == requests.codes.ok:
    jsessionid = r.text
    return jsessionid
  else:
    print 'Error: log in to XNAT failed - status code =', r.status_code
    r.raise_for_status()

def logout(xnat_base_url, jsessionid):
  xnat_auth_url = xnat_base_url + xnat_auth_base
  cookies = dict(JSESSIONID=jsessionid)
  r = requests.delete(xnat_auth_url, cookies=cookies)
  if r.status_code != requests.codes.ok:
    print 'Warning: XNAT logout request failed - status code =', r.status_code
  
def create_project(xnat_base_url, jsessionid, project_name):
  xnat_project_id = project_name_to_xnat_project_id(project_name)
  xnat_project_url = xnat_base_url + xnat_project_base  
  payload = { 'ID': xnat_project_id, 'name': project_name } 
  cookies = dict(JSESSIONID=jsessionid)
  r = requests.post(xnat_project_url, params=payload, cookies=cookies)
  if r.status_code == requests.codes.ok: # XNAT returns 200 rather than 201 even if project does not already exist
    print 'Project', project_name, 'created with XNAT project ID', xnat_project_id
  else:
    print 'Warning: failed to create project', project_name, '- status code =', r.status_code
    r.raise_for_status()

def create_subject(xnat_base_url, jsessionid, project_name, patient_id, patient_name):
  xnat_project_id = project_name_to_xnat_project_id(project_name)
  xnat_epad_project_url = xnat_base_url + xnat_project_base + xnat_project_id
  xnat_epad_project_subject_url = xnat_epad_project_url+'/subjects/'
  xnat_subject_id = patient_id_to_xnat_subject_id(patient_id)
  xnat_subject_label = patient_name_to_xnat_subject_label(patient_name)
  cookies = dict(JSESSIONID=jsessionid)
  payload = { 'ID': xnat_subject_id, 'label': xnat_subject_label } # Do not omit seemingly redundant 'ID' in payload 
  xnat_subject_url = xnat_epad_project_subject_url + xnat_subject_id
  r = requests.put(xnat_subject_url, params=payload, cookies=cookies)
  if r.status_code == requests.codes.ok:
    print 'Subject', patient_name, 'already exists in XNAT'
  elif r.status_code == requests.codes.created:
    print 'Subject', patient_name, 'created with XNAT subject ID', xnat_subject_id, 'and XNAT subject label', xnat_subject_label 
  else:
    print 'Warning: failed to create subject with label', xnat_subject_label, 'and id', xnat_subject_id, '- status code =', r.status_code

def create_subjects(xnat_base_url, jsessionid, project_name, study_uid_patient_name_id_triples):  
  for _, patient_id, patient_name in study_uid_patient_name_id_triples:
    create_subject(xnat_base_url, jsessionid, project_name, patient_id, patient_name)

def create_experiment(xnat_base_url, jsessionid, project_name, patient_id, study_uid):
  xnat_project_id = project_name_to_xnat_project_id(project_name)
  xnat_epad_project_url = xnat_base_url + xnat_project_base + xnat_project_id
  xnat_epad_project_subject_url = xnat_epad_project_url+'/subjects/'
  xnat_subject_id = patient_id_to_xnat_subject_id(patient_id)
  xnat_experiment_id = study_uid_to_xnat_experiment_id(study_uid)
  payload = { 'label': study_uid, 'xsiType': 'xnat:otherDicomSessionData' }
  xnat_experiment_url = xnat_epad_project_subject_url + xnat_subject_id + '/experiments/' + xnat_experiment_id
  cookies = dict(JSESSIONID=jsessionid)
  r = requests.put(xnat_experiment_url, params=payload, cookies=cookies)
  if r.status_code == requests.codes.ok:
    print 'Experiment for study', study_uid, 'already existed in XNAT'
  elif r.status_code == requests.codes.created:
    print 'Experiment for study', study_uid, 'created with XNAT experiment ID', xnat_experiment_id
  else:
    print 'Warning: failed to create experiment for study', study_uid, '- status code =', r.status_code

def create_experiments(xnat_base_url, jsessionid, project_name, study_uid_patient_name_id_triples):
  for study_uid, patient_id, patient_name in study_uid_patient_name_id_triples:
    create_experiment(xnat_base_url, jsessionid, project_name, patient_name, study_uid)
