# Fynche - a Framework for Multiagent Computational Creativity
# Copyright 2011 Josh Hansen
# 
# This file is part of the Fynche <https://github.com/joshhansen/fynche>.
# 
# Fynche is free software: you can redistribute it and/or modify it
# under the terms of the GNU Affero General Public License as published by the
# Free Software Foundation, either version 3 of the License, or (at your
# option) any later version.
# 
# Fynche is distributed in the hope that it will be useful, but
# WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
# FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License
# for more details.
# 
# You should have received a copy of the GNU Affero General Public License
# along with Fynche.  If not, see <http://www.gnu.org/licenses/>.
# 
# If you have inquiries regarding any further use of Fynche, please
# contact Josh Hansen <http://joshhansen.net/>
import os
import cjson
import sqlite3
import sys

class SmartWriter:
    def __init__(self, length=200):
        self.i = 0
        self.length = length
    
    def write(self, c):
        sys.stdout.write(c)
        sys.stdout.flush()
        self.i += 1
        if self.i == self.length:
            print
            self.i = 0

w = SmartWriter()
class ColorDB:
    def __init__(self, db_filename):
        need_init = not os.path.exists(db_filename)
        self.conn = sqlite3.connect(db_filename)
        if need_init:
            c = self.conn.cursor()
            c.execute('''create table users(
            id integer primary key autoincrement,
            username varchar(32) not null);''')
            c.execute('''create table colors(
                id integer primary key autoincrement,
                user_id integer not null references users(id),
                hex char(6) not null,
                r integer not null,
                g integer not null,
                b integer not null,
                title varchar(64) not null);''')
            c.execute("create index username_idx on users(username);")
            c.execute("create index hex_idx on colors(hex);")
            self.conn.commit()
    
    def color_exists(self, hex):
        c = self.conn.cursor()
        c.execute('select * from "colors" where hex=?', (hex,))
        try:
            c.next()
            return True
        except StopIteration:
            return False
    
    def add_color(self, user_id, hex, r, g, b, title):
        c = self.conn.cursor()
        c.execute('insert into "colors"("user_id","hex", "r","g","b","title") values(?, ?, ?, ?, ?, ?)', (user_id, hex, r, g, b, title))
#        self.conn.commit()
        
    def add_user(self, username):
        c = self.conn.cursor()
        c.execute('insert into "users" ("username") values(?)', (username,))
        self.conn.commit()
    
    def user_id(self, username):
        c = self.conn.cursor()
        c.execute('select "id" from "users" where "username"=?', (username,))
        try:
            return c.next()[0]
        except StopIteration:
            return None
    
    def index_color(self, hex, color):
        username = color["userName"]
        rgb = color["rgb"]
        r = rgb['red']
        g = rgb['green']
        b = rgb['blue']
        title = color['title']
        if username is not None and r is not None and g is not None and b is not None and title is not None:
#            print username + ":" + title,
            #description = color['description']
#            if not self.color_exists(title):
            user_id = self.user_id(username)
            if user_id is None:
                self.add_user(username)
                user_id = self.user_id(username)
            self.add_color(user_id, hex, r, g, b, title)
            w.write('+')
#            else:
#                sys.stdout.write('.')
        else:
            w.write('e')
#        i += 1
#        if i % 200 == 0:
#            print



if __name__ == '__main__':
    db = ColorDB('/home/jjfresh/Courses/cs673/colors2.db')
    data_dir = '/home/jjfresh/Courses/cs673/data/colors'
    
    def index_color(hex, full_filename):
        if not db.color_exists(hex):
            color = cjson.decode(open(full_filename).read())
            db.index_color(hex, color)
        else:
            w.write('.')
#            i += 1
#            if i % 200 == 0:
#                print
    
    for subdir in os.listdir(data_dir):
        print subdir
        full_subdir = data_dir + '/' + subdir
        for filename in sorted(os.listdir(full_subdir)):
            hex = filename.replace('.json','')
            full_filename = full_subdir + '/' + filename
            index_color(hex, full_filename)
#            print filename
#            color = cjson.decode(open(full_subdir+'/'+filename).read())
#            db.index_color(color)
        print
        
    #Create additional indexes that we didn't want to bother with until all the data was in:
    c = db.conn.cursor()
    c.execute("create index r_idx on colors(r);")
    c.execute("create index g_idx on colors(g);")
    c.execute("create index b_idx on colors(b);")
    c.execute("create index title_idx on colors(title);")
    db.conn.commit()
    
