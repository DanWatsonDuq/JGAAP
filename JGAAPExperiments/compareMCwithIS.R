# TODO: Add comment
# 
# Author: Dan
###############################################################################


compMCwithIS = function(binData, k){
	funct = evalFunct(binData)
	
	n = ncol(binData)
	
	candIds = seq.int(from=1, to=n, by=1)
	subsets = combs(candIds, k)
	res = matrix(nrow=nrow(subsets),ncol= 2)
	
	for(i in 1:dim(subsets)[1])
	{	
		mc = funct(subsets[i,], 1)
		is = err(binData, subsets[i,], colMeans(binData))
		res[i,1]=mc
		res[i,2]=is
	}
	
	return(res)
}

compMCwithOOS = function(binData1,binData2, k){
	funct = evalFunct(binData1)
	
	n = ncol(binData1)
	
	candIds = seq.int(from=1, to=n, by=1)
	subsets = combs(candIds, k)
	res = matrix(nrow=nrow(subsets),ncol= 2)
	
	for(i in 1:dim(subsets)[1])
	{	
		mc = funct(subsets[i,], 1)
		is = err(binData2, subsets[i,], colMeans(binData1))
		res[i,1]=mc
		res[i,2]=is
	}
	
	return(res)
}

compISwithOOS = function(binData1, binData2, k){
	n = ncol(binData1)
	
	candIds = seq.int(from=1, to=n, by=1)
	subsets = combs(candIds, k)
	res = matrix(nrow=nrow(subsets),ncol= 2)
	
	for(i in 1:dim(subsets)[1])
	{
		is = err(binData1, subsets[i,], colMeans(binData1))
		oos = err(binData2, subsets[i,], colMeans(binData2))
		res[i,1]=is
		res[i,2]=oos
	}
	return(res)
}
