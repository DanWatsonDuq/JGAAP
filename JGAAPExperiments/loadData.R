# TODO: Add comment
# 
# Author: Dan
###############################################################################


loadData = function(dir){
	files = list.files(dir, ".*csv")
	print(files)
	wd = getwd()
	setwd(dir)
	for(i in 1:length(files))
	{
		assign(files[i],read.csv(files[i], header=FALSE),.GlobalEnv)
	}
	setwd(wd)
}
