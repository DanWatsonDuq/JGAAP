# TODO: Add comment
# 
# Author: Dan
###############################################################################

naiveSelect = function(bindata, k){
	
	return(tail(order(colMeans(bindata)), k))
}

