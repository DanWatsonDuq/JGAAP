# TODO: Add comment
# 
# Author: Dan
###############################################################################

convertToNum = function(data){

	if(typeof(data)=="double")
		return(data)
	#print(data)
	data = data[,-1]
	data = data[-1,]
	return(apply(as.matrix.noquote(data),2,as.numeric))
}