# TODO: Add comment
# 
# Author: Dan
###############################################################################


testProc = function(mu, sigma){
	mu = as.numeric(mu);
	sigma = convertToNum(sigma)
	iter = 1000
		
	print(mu)
	print(sigma)
	res = vote(iter,mu, sigma)
	print(res)
	muObs = colMeans(res)
	print(mu)
	print(muObs)
}
