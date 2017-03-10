RhoBounds <-function(mu1, mu2){
	lower = max(-sqrt(mu1*mu2/((1-mu1)*(1-mu2))),-sqrt(((1-mu1)*(1-mu2))/(mu1*mu2)))
	upper = min(sqrt((mu1*(1-mu2))/(mu2*(1-mu1))), sqrt((mu2*(1-mu1))/(mu1*(1-mu2))))
	return(as.numeric(c(lower, upper)))
}