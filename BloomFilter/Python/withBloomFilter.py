#!/usr/bin/env python
import sys
import re
from BloomFilter import BloomFilter

stop_words = []
sw_file = open('stop_words.txt', 'r')
for line in sw_file:
    words = line.replace(' ','').replace('\'', '').replace('\n', '').split(',')
    stop_words.extend(words)
    
n = len(stop_words) #no of items to add 
p = 0.0001 #false positive probability 
  
bloomf = BloomFilter(n,p) 

for item in stop_words: 
    bloomf.add(item)

RE_D = re.compile('\d')

for line in sys.stdin:
    #--- remove leading and trailing whitespace---
    line = line.strip().split("\t")
    
    if len(line) >= 13:

        #--- split the line into words ---
        review = re.findall(r'\w+', line[13])
        for word in review:
            if not bloomf.check(word) and len(word) > 1 and not RE_D.search(word): #not bloomf.check(word) and
                print('%s\t%s' % (word.lower(), "1"))