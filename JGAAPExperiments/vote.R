vote <-function(n, mu, cov){
  
  #print(cov)

  normresults = mvrnorm(n, mu, cov)
  print("result has")
  Y = matrix(nrow = n, ncol = length(mu))
  for(j in 1:n)
  {
	  normresult = normresults[j,]
	  for(i in 1:length(mu)){
		if(normresult[i]<=qnorm(mu[i])){
			Y[j,i] = 1
		}
		else{
			Y[j,i] = 0
	 	}
	  }
	}
return(Y)
}
