import urllib
import urllib2
import cjson
import os
def all_palettes():
    url = 'http://www.colourlovers.com/api/palettes'
    num_results = 100
    offset = 0
    while(True):
        data = urllib.urlencode({'resultOffset':offset, 'numResults':num_results, 'format':'json'})
        response = urllib2.urlopen(url, data=data)
        text = response.read()
        response.close()
        offset += num_results
        
        palettes = cjson.decode(text)
        if len(palettes) == 0:
            break
        for palette in palettes:
            yield palette

if __name__=='__main__':
    output_dir = '/home/jjfresh/Courses/cs673/cs673svn/data/palettes'
    if not os.path.exists(output_dir): os.mkdir(output_dir)
    
    count = 0
    for palette in all_palettes():
        print palette
        if count % 100 == 0: print count,
        if count % 4000 == 0 and count > 0: print
        id = palette['id']
        subdir = output_dir + '/' + str(id%100)
        if not os.path.exists(subdir): os.mkdir(subdir)
        filename = subdir + '/' + str(id) + '.json'
        if not os.path.exists(filename):
            open(filename, 'w').write(cjson.encode(palette))
        count += 1
