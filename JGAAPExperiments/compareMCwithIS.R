# TODO: Add comment
# 
# Author: Dan
###############################################################################


compMCwithIS = function(bindata, k){
	funct = evalFunct(bindata)
	
	n = ncol(bindata)
	
	candIds = seq.int(from=1, to=n, by=1)
	subsets = combs(candIds, k)
	res = matrix(nrow=nrow(subsets),ncol= 2)
	
	for(i in 1:dim(subsets)[1])
	{	
		mc = funct(subsets[i,], 1)
		#print(mc)
		is = err(bindata, subsets[i,], rep(1,k))
		#print(is)
		res[i,1]=mc
		res[i,2]=is
	}
	
	return(res)
}

compMCwithOOS = function(bindata1,bindata2, k){
	funct = evalFunct(bindata1)
	
	n = ncol(bindata1)
	
	candIds = seq.int(from=1, to=n, by=1)
	subsets = combs(candIds, k)
	res = matrix(nrow=nrow(subsets),ncol= 2)
	
	for(i in 1:dim(subsets)[1])
	{	
		mc = funct(subsets[i,], 1)
		is = err(bindata2, subsets[i,], colMeans(bindata1))
		res[i,1]=mc
		res[i,2]=is
	}
	
	return(res)
}

compISwithOOS = function(bindata1, bindata2, k){
	n = ncol(bindata1)
	
	candIds = seq.int(from=1, to=n, by=1)
	subsets = combs(candIds, k)
	res = matrix(nrow=nrow(subsets),ncol= 2)
	
	for(i in 1:dim(subsets)[1])
	{
		is = err(bindata1, subsets[i,], colMeans(bindata1))
		oos = err(bindata2, subsets[i,], colMeans(bindata2))
		res[i,1]=is
		res[i,2]=oos
	}
	return(res)
}

MCvsISbulk = function(bindata, ks){
	k = length(ks); n=2; m = 2^(ncol(bindata)/2+1);
	toRet = list()
	cat(dim(toRet))
	count =1
	for(i in ks){
		res = compMCwithIS(bindata, i)
		print(res)
		toRet[[count]]=res
		count=count+1
		
	}
	return(toRet)
}

MCvsOOSbulk = function(bindata1, bindata2, ks){
	k = length(ks); n=2; m = 2^(ncol(bindata1)/2+1);
	toRet = list()
	cat(dim(toRet))
	count =1
	for(i in ks){
		res = compMCwithOOS(bindata1,bindata2, i)
		print(res)
		toRet[[count]]=res
		count=count+1
		
	}
	return(toRet)
}

ISvsOOSbulk = function(bindata1, bindata2, ks){
	k = length(ks); n=2; m = 2^(ncol(bindata1)/2+1);
	toRet = list()
	cat(dim(toRet))
	count =1
	for(i in ks){
		res = compISwithOOS(bindata1,bindata2, i)
		print(res)
		toRet[[count]]=res
		count=count+1
		
	}
	return(toRet)
}

doItAll = function(){

	assign("paper_SciFi_MCS_vs_IS",MCvsISbulk(scidata,3:10),.GlobalEnv)
	assign("paper_SciFi_Halved_IS_vs_OOS",ISvsOOSbulk(convertToNum(scidatafirsthalf),convertToNum(scidatasecondhalf),3:10),.GlobalEnv)
	assign("paper_SciFi_Halved_MCS_vs_OOS",MCvsOOSbulk(convertToNum(scidatafirsthalf),convertToNum(scidatasecondhalf),3:10),.GlobalEnv)
	assign("paper_SciFi_MCS_vs_Mys_IS", MCvsOOSbulk(convertToNum(SCIDATA),convertToNum(MYSDATA),3:10),.GlobalEnv)
	
	assign("paper_Mys_MCS_vs_IS",MCvsISbulk(MYSDATA,3:10),.GlobalEnv)
	assign("paper_Mys_Halved_IS_vs_OOS",ISvsOOSbulk(MYSDATA[1:670],MYSDATA[671:1378],3:10),.GlobalEnv)
	assign("paper_Mys_Halved_MCS_vs_OOS",MCvsOOSbulk(MYSDATA[1:670],MYSDATA[671:1378],3:10),.GlobalEnv)
	assign("paper_SciFi_IS_vs_Mys_IS", ISvsOOSbulk(convertToNum(SCIDATA),convertToNum(MYSDATA),3:10),.GlobalEnv)
}
