import urllib
import urllib2
import cjson
import os
def all_colors():
    url = 'http://www.colourlovers.com/api/colors'
    num_results = 100
    offset = 0
    while(True):
        data = urllib.urlencode({'resultOffset':offset, 'numResults':num_results, 'format':'json'})
        response = urllib2.urlopen(url, data=data)
        text = response.read()
        response.close()
        offset += num_results
        
        colors = cjson.decode(text)
        if len(colors) == 0:
            break
        for color in colors:
            yield color

if __name__=='__main__':
    output_dir = '/home/jjfresh/Courses/cs673/cs673svn/data/colors'
    if not os.path.exists(output_dir): os.mkdir(output_dir)
    
    count = 0
    for color in all_colors():
        if count % 100 == 0: print count,
        if count % 4000 == 0 and count > 0: print
        hex = color['hex']
        subdir = output_dir + '/' + hex[0]
        if not os.path.exists(subdir): os.mkdir(subdir)
        filename = subdir + '/' + hex + '.json'
        if not os.path.exists(filename):
            open(filename, 'w').write(cjson.encode(color))
        count += 1