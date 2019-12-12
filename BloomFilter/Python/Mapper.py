#!/usr/bin/env python
import sys
import re

stop_words = []
sw_file = open('stop_words.txt', 'r')
for line in sw_file:
    words = line.replace(' ','').replace('\'', '').replace('\n', '').split(',')
    stop_words.extend(words)

RE_D = re.compile('\d')

#--- get all lines from stdin ---
for line in sys.stdin:
    #--- remove leading and trailing whitespace---
    line = line.strip().split("\t")
    
    if len(line) >= 13:

        #--- split the line into words ---
        review = re.findall(r'\w+', line[13])
        for word in review:
            if word not in stop_words and len(word) > 1 and not RE_D.search(word): 
                print('%s\t%s' % (word.lower(), "1"))