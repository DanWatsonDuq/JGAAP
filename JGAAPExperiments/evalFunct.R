
evalFunct<- function(binData){
	
	mu = colMeans(binData)
	sigma = cor(binData)
	#mu = as.numeric(mu);
	#print(mu)
	#sigma = convertToNum(sigma)
	funct = function(candlist, iter){
		#print(mu)
		cand = as.numeric(candlist)	
		Muse = mu[cand]
		newSigma = sigma[cand,cand]
		metric = mean(takeVotes(10000*iter, Muse, newSigma))
		return(metric)
	}
	
	return(funct)
}
