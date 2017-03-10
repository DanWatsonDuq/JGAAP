takeVotes <- function(n, mu, covmat){
	plurality = numeric(length=n)
	for(i in 1:n){
		theVote = vote(mu,covmat)
		theVote = theVote %*% mu
		if(theVote>0){
			plurality[i]=1
		}
		else{
			plurality[i]=0
		}
	}

return(plurality)
	
}

