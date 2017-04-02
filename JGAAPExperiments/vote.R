vote <-function(n, mu, cov){
  
 
  normresults = mvrnorm(n, numeric(length(mu)), cov)
  #print("result has")
  Y = matrix(nrow = n, ncol = length(mu))
  for(j in 1:n)
  {
	  normresult = normresults[j,]
	  for(i in 1:length(mu)){
		if(normresult[i]<=qnorm(mu[i])){
			Y[j,i] = 1
		}
		else{
			Y[j,i] = -1
	 	}
	  }
	}
return(Y)
}
