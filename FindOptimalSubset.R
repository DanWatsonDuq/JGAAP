findOptimalSubsets = function(mu, sigma){
	evalFunct = function(cand){
		#print(cand)
		Muse = mu[cand]
		#print(Muse)
		if(mean(Muse)<.5)
			return(0)
		
		newSigma = sigma[cand, cand]
		#print("evaluating")
		return(mean(takeVotes(100, Muse, newSigma)))
	}

	return(optimalSubset(evalFunct, length(mu)))
}