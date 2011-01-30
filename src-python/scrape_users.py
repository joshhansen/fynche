import urllib
import urllib2
import cjson
import os
def all_users(include_comments=True):
    url = 'http://www.colourlovers.com/api/lovers'
    num_results = 100
    offset = 0
    while(True):
        data_dict = {'resultOffset':offset, 'numResults':num_results, 'format':'json'}
        if include_comments: data_dict['comments'] = 1
        data = urllib.urlencode(data_dict)
        response = urllib2.urlopen(url, data=data)
        text = response.read()
        response.close()
        offset += num_results
        
        users = cjson.decode(text)
        if len(users) == 0:
            break
        for user in users:
            yield user

if __name__=='__main__':
    output_dir = '/home/jjfresh/Courses/cs673/cs673svn/data/users'
    if not os.path.exists(output_dir): os.mkdir(output_dir)
    
    count = 0
    for user in all_users():
        print user
        if count % 100 == 0: print count,
        if count % 4000 == 0 and count > 0: print
        id = user['userName']
        subdir = output_dir + '/' + id[0]
        if not os.path.exists(subdir): os.mkdir(subdir)
        filename = subdir + '/' + id + '.json'
        if not os.path.exists(filename):
            open(filename, 'w').write(cjson.encode(user))
        count += 1