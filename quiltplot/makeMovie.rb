#!/usr/bin/ruby

# Get the arguments from the command-line (the URL)
if ARGV.size < 1
	puts "Proper usage: makeMovie.rb <affinities file>"
	exit
end

inputFileName = ARGV[0]
inputFile = File.new(inputFileName, "r")
outputFileName = inputFileName.gsub(".txt",".mpg").gsub("output/affinities","output/movies")
imagesDirName = inputFileName.gsub(".txt","").gsub("output/affinities","output/images")

maxaffinity = 0.0

class Round
    attr_reader :x, :y, :z
    def initialize
        @x = []
        @y = []
        @z = []
    end
    
    def add_affinity(agent1, agent2, affinity)
        @x.push(agent1)
        @y.push(agent2)
        @z.push(affinity)
    end
    
    def to_s
        s = ""
        for i in 0..@x.size-1
            s += "#{@x[i]}, #{@y[i]}, #{@z[i]}\n"
        end
        s
    end
    
    def normalize(max)
        for i in 0..@z.size-1
            @z[i] = @z[i]/max
        end
    end
end

def list_as_c(list)
    "c(#{list.join(",")})"
end

rounds = []

agent_count = nil
# Go through the input file line by line, looking for KL-divergence tables
while line = inputFile.gets
#     puts "Agents: " + line
    agents = line.split
    if agent_count==nil
        agent_count = agents.size
    end
    
    round = Round.new
    
    for i in 0..agents.size-1
        agentLine = inputFile.gets.split
#         puts agentLine
        agentLine = agentLine[1, agentLine.size-1]
        currentRow = Array.new(agentLine.size-1)
        for j in 0..agentLine.size-1
           affinity = agentLine[j].to_f
           if affinity > maxaffinity
               maxaffinity = affinity
           end
#            currentRow[j] = affinity
           round.add_affinity(i, j, affinity)
        end
#         currentTable.push(currentRow)
#         puts "\tRow: #{currentRow}"
    end
#     tables.push(currentTable)
    rounds.push(round)
    puts "Table: #{round}"
end

puts "Max affinity: #{maxaffinity}"

# Normalize the values by dividing by the max.
rounds.each{|round| round.normalize(maxaffinity)}

# Now, take each of the tables and create an image plot for each one
`mkdir -p #{imagesDirName}`

for i in 0..rounds.size-1
    round = rounds[i]
    `r -e 'local_packages_path = c(\"/home/jjfresh/.r_packages\");
    library(GDD, lib.loc=local_packages_path);
    library(spam, lib.loc=local_packages_path)
    library(fields, lib.loc=local_packages_path);
    x <- #{list_as_c(round.x)};
    y <- #{list_as_c(round.y)};
    z <- #{list_as_c(round.z)};
    GDD(\"#{imagesDirName}/#{sprintf("%07d",i)}.jpg\", type=\"jpeg\", width=800, height=800); 
    quilt.plot(cbind(x,y),z,nx=#{agent_count},ny=#{agent_count}, zlim=c(0.0,1.0));
    dev.off();'`
end

`mencoder "mf://#{imagesDirName}/*.jpg" -mf fps=4 -o #{outputFileName} -ovc lavc -lavcopts vcodec=msmpeg4v2:vbitrate=1800`
