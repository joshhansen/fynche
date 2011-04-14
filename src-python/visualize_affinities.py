import ubigraph
import time
import os

class AffinityVisualizer(object):
    def __init__(self):
        self.U = ubigraph.Ubigraph()
        self.U.clear()
        self.nodes = {}
        self.edges = {}
    
    def node(self, id):
        try:
            return self.nodes[id]
        except KeyError:
            node = self.U.newVertex(label=id, shape="sphere", color="#00ff00")
            self.nodes[id] = node
            return node
    
    def edge(self, id1, id2):
        try:
            return self.edges[(id1,id2)]
        except KeyError:
            n1 = self.node(id1)
            n2 = self.node(id2)
            v = self.U.newEdge(n1, n2, color="ff0000", stroke="dashed")
            self.edges[(id1,id2)] = v
            return v
    
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
                            self.edge(agent,other_agent).set(strength=affin,label=line[i+1][0:5])
                time.sleep(5)
                it += 1
        except StopIteration:
            print 'Done'
        
        
    
if __name__=='__main__':
    filename = os.environ['HOME'] + '/Courses/cs673/cs673svn/output/affinities/iterations100_STANDARD10_stepSize:0.03_maxArtInit:5000_copycat:0.01_top:10_offset:2.0_artRandOrder_suckUpTo:5_moderateEntropy_uniformAffinInit6.txt'
    vizor = AffinityVisualizer()
    vizor.visualize(filename)