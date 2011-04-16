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
import time

class AffinityVisualizer(object):
    def __init__(self):
        self.g = getGraph()
        self.g.clear()
        self.will_be_first = True
    
    def node(self, id):
        n = self.g.getNode(id)
        if n is None:
            n = newNode(id)
            d = n.getNodeData()
            d.setLabel(id)
            d.setSize(10)
            if self.will_be_first:
                #d.setEnabled(False)
                self.will_be_first = False
            self.g.addNode(n)
        return n
    
    def edge(self, id1, id2):
        n1 = self.node(id1)
        n2 = self.node(id2)
        e = self.g.getEdge(n1,n2)
        if e is None:
            e = newEdge(n1, n2)
            self.g.addEdge(e)
        return e
    
    def visualize(self, affins_filename):
        f = open(affins_filename)
        agents = None
        it = 0
        try:
            while(True):
                print "Iteration: " + str(it)
                agents_ = f.readline().split()
                if len(agents_) == 0: break
                if agents is None:
                    agents = agents_
                else:
                    for i in range(0,len(agents)):
                        if agents_[i] != agents[i]: raise Exception("Agent mismatch")
                
                for agent in agents:
                    line = f.readline().split()
                    if line[0].strip() != agent.strip(): raise Exception("Agent mismatch")
                    
                    for i,other_agent in enumerate(agents):
                        if agent != other_agent:
                            affin = float(line[i+1])
                            self.edge(agent,other_agent).setWeight(affin)
                time.sleep(1)
                it += 1
        except StopIteration:
            print 'Done'
        
        
    
if __name__=='__main__':
    filename = '/home/jjfresh/Courses/cs673/cs673svn/output/affinities/iterations100_STANDARD10_stepSize:0.03_maxArtInit:5000_copycat:0.01_top:10_offset:2.0_artRandOrder_suckUpTo:5_moderateEntropy_uniformAffinInit6.txt'
    vizor = AffinityVisualizer()
    vizor.visualize(filename)