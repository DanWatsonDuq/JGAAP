vote <-function(mu, cov){
  
  normresult = mvrnorm(1, mu, cov)
  n = length(normresult)
  Y = numeric(length = n)
  for(i in 1:n){
	
	if(normresult[i]<=qnorm(mu[i])){
		Y[i] = 1
	}
	else{
		Y[i] = -1
 	}
  }
return(Y)
}
