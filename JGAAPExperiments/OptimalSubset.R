optimalSubset= function(evalFunction, candidates, k){
	n = candidates
	candIds = seq.int(from=1, to=n, by=1)
	subsets = combs(candIds, k)
	#print(subsets)
	bestSet = 0
	bestEval = 0
	
	for(i in 2:dim(subsets)[1]-1)
	{
		prelim = evalFunction(subsets[i,],.05)
		if(prelim>.9*bestEval){
			actual = evalFunction(subsets[i,], 1)
			if(actual>bestEval){
				bestSet = subsets[i,]
				bestEval = actual
			}
		}
	}
	
	
	print(bestEval)
	return(bestSet)
}
		