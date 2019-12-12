#!/usr/bin/env python
import sys
import re
import pickle

with open('bloomFilter', 'rb') as Bloom_Filter:
    bloomf = pickle.load(Bloom_Filter)

for line in sys.stdin:
    #--- remove leading and trailing whitespace---
    line = line.strip().split("\t")
    
    if len(line) >= 13:

        #--- split the line into words ---
        review = re.findall(r'\w+', line[13])
        for word in review:
            if not bloomf.check(word) and len(word) > 1: 
                print('%s\t%s' % (word.lower(), "1"))