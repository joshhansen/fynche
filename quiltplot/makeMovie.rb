#!/usr/bin/ruby

# Get the arguments from the command-line (the URL)
if ARGV.size < 3
	puts "Proper usage: makeMovie.rb <input file> <output file> <temporary image dir>"
	exit
end

inputFile = File.new(ARGV[0], "r")
readingTable = false
tables = Array.new
currentTable = nil
maxklValue = 0.0

# Go through the input file line by line, looking for KL-divergence tables
while line = inputFile.gets

	if ! readingTable && line =~ /KL-Divergence table for round \d+/
		readingTable = true
		currentTable = Array.new
	elsif readingTable
#     [java] 1.199414	1.236958	1.113065	1.245541	0.013011	1.106330	1.087179	1.168981	1.171472	0.974496	1.014882	1.348041	1.409439	1.142340	1.171472

		if (line =~ /\[java\] (\d+.\d+(\t)?)+/)
			#puts "+++ #{line}" 
			currentValues = line.split
			currentRow = Array.new
			currentValues.each do |value|
				if value =~ /(\d+\.\d+)/
					klValue = $1.to_f
					#puts klValue
					currentRow.push klValue		
					if klValue > maxklValue
						maxklValue = klValue
					end
				end
			end
			#puts
			currentTable.push currentRow
		else
			readingTable = false
			tables.push currentTable
			#puts
		end
	end
end

puts "Maximal divergence: #{maxklValue}"

# Normalize the values by dividing by the max.  Substract that number
# from 1, so that mins become maxes.  Then, we are ready to create the
# images
# This could be folded into the next loop, I thought I'd keep it separate
# so that there is a convenient place to put more complex processing, should
# we feel it necessary.
tables.each do |currentTable|
	currentTable.each do |currentRow|
		for i in (0..currentRow.length-1)
			begin
				currentRow[i] = (currentRow[i] / maxklValue)
			rescue => errorObject
				puts "Found a nil value in current table, column #{i}." 
			end
		end
		#puts currentTable.to_s
	end
end

# Now, take each of the tables and create an image plot for each one
`mkdir -p #{ARGV[2]}`
for i in (0..tables.length-1)
	tableList = ""
	for j in (0..tables[i].length-1) 
		for k in (0..tables[i][j].length-1)
			delimiter = ", "
			if(j == (tables[i].length-1) && k == (tables[i][j].length-1) )	
				delimiter = ""
			end
			tableList = tableList + "#{tables[i][j][k]}#{delimiter}"
		end	
	end
	dim = tables[0].length
	`r -e '	R_Visible = TRUE;
		colorLine<-function(sr,sg,sb,er,eg,eb,n) {
       			a = rgb2hsv(matrix(c(seq(sr,er,length=n),seq(sg,eg,length=n),seq(sb,eb,length=n)),nrow=3,ncol=n,byrow=TRUE))
			hsv(a[1,],a[2,],a[3,])
		}
		library("grDevices"); library("methods"); library("fields"); library("GDD"); x<- 1:#{dim}; y<- 1:#{dim}; 
		z <- matrix(nrow=#{dim}, c(#{tableList}), byrow=TRUE);
		GDD("#{ARGV[2]}/#{sprintf("%07d",i)}.jpg", type="jpeg", width=800, height=800); 
		image.plot(x,y,z, zlim=c(0.0,1.0), col=colorLine(0,0,0,255,255,255,20)); dev.off();'`
end

`mencoder "mf://#{ARGV[2]}/*.jpg" -mf fps=4 -o #{ARGV[1]} -ovc lavc -lavcopts vcodec=msmpeg4v2:vbitrate=1800`
