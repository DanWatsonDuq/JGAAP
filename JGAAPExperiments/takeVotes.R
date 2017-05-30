takeVotes <- function(n, mu, covmat){
	plurality = numeric(length=n)
	theVotes = vote(n, mu, covmat)
	for(i in 1:n){
		theVote = theVotes[i,]
		#print(theVote)
		theVote = mean(theVote) #for Unweighted Voting
		#print(theVote)
		if(theVote>0){
			plurality[i]=1
		}
		else{
			plurality[i]=0
		}
	}

return(plurality)
	
}

