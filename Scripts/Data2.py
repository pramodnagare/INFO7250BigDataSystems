#!/usr/bin/env python
# coding: utf-8

# In[1]:


#This jupyter notebok is for downloading data parallelly from s3 bucket to local file system:


# In[2]:


import boto3
import botocore
import time
import os
import pandas as pd
from multiprocessing.pool import ThreadPool


# In[3]:


data_link = "amazon-reviews-pds"
output_dir = "../Data2/"

s3 = boto3.client('s3')
response = s3.list_objects_v2(Bucket=data_link)


# In[12]:


def downloadFile(fileItem):
    head, file = os.path.split(fileItem)
    filename = output_dir + file
    s = time.time()
    s3.download_file(Bucket=data_link, Key=fileItem, Filename=filename)
    e = time.time()
    print("Downloaded file {} in {} secs!".format(fileItem, e-s))
    return None


# In[5]:


data = []
for fileItem in response['Contents']:
    if str(fileItem['Key']).startswith('tsv'):
        head, file = os.path.split(fileItem['Key'])
        if file != "":
            data.append(fileItem)


# In[6]:


df = pd.DataFrame(data)
df = df.sort_values('Size').reset_index()


# In[47]:


batches = {}
batch_num = 0
data = []
size_sum = 0
for i in range(df.shape[0]):
    size_sum += int(df['Size'][i])
    data.append(df['Key'][i])
    if (size_sum/1024/1024 > 200) or (len(data) > 8):
        if len(data) > 1:
            data.pop()
            batches[batch_num] = data
            size_sum = int(df['Size'][i])
            data = [df['Key'][i]]
        else:
            batches[batch_num] = data
            size_sum = 0
            data = []
        batch_num += 1
batches[batch_num] = data


# In[13]:


start = time.time()
for batch in batches.values():
    results = ThreadPool(len(batch)).imap_unordered(downloadFile, batch)
    for result in results:
        while result != None:
            continue
end = time.time()
print("Total Time for Downloading files is {} secs!".format(end-start))

