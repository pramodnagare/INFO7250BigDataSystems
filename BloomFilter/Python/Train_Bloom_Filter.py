#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Tue Dec 10 01:36:16 2019

@author: pramodnagare
"""

from BloomFilter import BloomFilter
import time
import pickle
import spacy
spacy.load('en')

spacy_stopwords = spacy.lang.en.stop_words.STOP_WORDS

n = len(spacy_stopwords) #no of items to add 
p = 0.0001 #false positive probability 
  
bloomf = BloomFilter(n,p) 
print("Size of bit array:{}".format(bloomf.size)) 
print("False positive Probability:{}".format(bloomf.fp_prob)) 
print("Number of hash functions:{}".format(bloomf.hash_count))
  
start = time.time()
for item in spacy_stopwords: 
    bloomf.add(item)
end = time.time()

print("Time for Bloom filter training is {} ".format(end-start))

with open('bloomFilter.txt', 'wb') as bloom_filter:
    pickle.dump(bloomf, bloom_filter)