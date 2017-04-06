# TODO: Add comment
# 
# Author: Dan
###############################################################################


process = function(dir){
	files = list.files(dir, ".*csv")
	#setwd(dir)
	print(files)
	i = 1
	
	
	
	while(i<length(files))
	{
		assign(files[i],read.csv(files[i], header=TRUE)[,-1],.GlobalEnv)
		i=i+1
	}
}
