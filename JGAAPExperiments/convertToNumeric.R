# TODO: Add comment
# 
# Author: Dan
###############################################################################

convertToNum = function(data){

	if(typeof(data)=="double")
		return(data)
	#print(data)
	
	return(apply(as.matrix.noquote(data),2,as.numeric))
}