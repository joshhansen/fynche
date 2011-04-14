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