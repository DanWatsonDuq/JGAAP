
evalFunct<- function(mu,sigma){
	
	
	mu = as.numeric(mu);
	print(mu)
	sigma = convertToNum(sigma)
	funct = function(cand, iter){
		
		Muse = mu[cand]
	
		newSigma = sigma[cand,cand]
		

		metric = mean(takeVotes(1000*iter, Muse, newSigma))
		
		
		return(metric)
	}
	return(funct)
}
