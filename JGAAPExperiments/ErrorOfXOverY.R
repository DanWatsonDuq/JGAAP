# TODO: Add comment
# 
# Author: Dan
###############################################################################


err = function (binData, subset, w){
	binData = binData[,subset]
	binData[binData == 0] <- -1
	votes = apply(binData,1,mean)
	
	votes[votes>0] = 1
	votes[votes<0] = 0
	
	#print(votes)
	
	return(mean(votes))
}
