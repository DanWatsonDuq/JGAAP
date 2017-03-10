gamma <-function(mu, sigma){
	m = length(mu)
	gammasub = numeric(m^2);
	for(i in 1:m)
	{	
		for(j in i:m){
			bounds = RhoBounds(mu[i], mu[j])
			lower = bounds[1]
			
			upper = bounds[2]
			
			rho = sigma[i,j]
			
			gsub = (rho-lower)/(upper-lower)
			print(gsub)
			
			
			gammasub[m*(i-1)+j] = if(is.nan(gsub)) 0 else gsub
		}
	
	}
	
	return(gammasub)

}