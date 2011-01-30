import urllib
import urllib2
import cjson
import os
def all_patterns():
    url = 'http://www.colourlovers.com/api/patterns'
    num_results = 100
    offset = 0
    while(True):
        data = urllib.urlencode({'resultOffset':offset, 'numResults':num_results, 'format':'json'})
        response = urllib2.urlopen(url, data=data)
        text = response.read()
        response.close()
        offset += num_results
        
        patterns = cjson.decode(text)
        if len(patterns) == 0:
            break
        for pattern in patterns:
            yield pattern

if __name__=='__main__':
    output_dir = '/home/jjfresh/Courses/cs673/cs673svn/data/patterns'
    if not os.path.exists(output_dir): os.mkdir(output_dir)
    
    for count, pattern in enumerate(all_patterns()):
        if count % 100 == 0: print count,
#        if count % 4000 == 0 and count > 0: print
        id = pattern['id']
        subdir = output_dir + '/' + str(id%10)
        if not os.path.exists(subdir): os.mkdir(subdir)
        filename = subdir + '/' + str(id) + '.json'
        if not os.path.exists(filename):
            open(filename, 'w').write(cjson.encode(pattern))
