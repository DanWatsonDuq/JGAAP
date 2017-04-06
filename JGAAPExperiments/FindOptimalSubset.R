MCoptimalSubset = function(binData, k){

	#mu = as.numeric(mu)
	#sigma = convertToNum(sigma)
	mu = colMeans(binData)
	
	n = length(mu)
	
	sigma = cor(as.matrix(binData))
	
	for(i in 1:n){
		if(mu[i]==1)
			mu[i] = .98
	}
	
	
	#print(mu)
	#print(sigma)
	#print(typeof(sigma))
	funct = evalFunct(binData)
	
	wise = optimalSubset(funct, length(mu), k)
	
	return(wise)
			
}