generateSigma <-function(mu, gamma){
	m = length(mu)
	sum =0;
	gammaVar = gamma/2
	sigma = matrix(, nrow = m, ncol = m)
	
	betaSample = generateMu(m^2, gamma, gammaVar)
	for(i in 1:m)
	{	
		for(j in i:m){
			sample = betaSample[m*(i-1)+j]
			bounds = RhoBounds(mu[i], mu[j])
			lower = bounds[1]
			upper = bounds[2]
			
			rho = sample*(upper-lower)+lower
			
			if(i==j)
				rho = 1
			sigma[i, j] = rho
			sigma[j, i] = rho
		}
	}
	return( sigma)

}