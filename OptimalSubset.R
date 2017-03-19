optimalSubset= function(evalFunction, candidates){
	n = candidates	
	bestForN = numeric(length=n)
	candIds = seq.int(from=1, to=n, by=1)
	print("Workin")
	for(k in 3:n){
		subsets = combs(candIds, k)
		#print(subsets)
		best = 0
		
		for(i in 2:dim(subsets)[1])
		{
			if(evalFunction(subsets[i,])>best)
				best = subsets[i,]
		}

		print(best)
		bestForN[i] = best		
	}

	return(bestForN)
}
		