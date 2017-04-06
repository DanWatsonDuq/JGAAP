# TODO: Add comment
# 
# Author: Dan
###############################################################################

separateEvenly = function(bindata, evenParity){
	bindata = as.matrix(bindata)
	i=1
	m1= bindata[1:(nrow(bindata)/2),]
	m2= bindata[(nrow(bindata)/2+1):nrow(bindata),]
	while(i<nrow(bindata)){
		if(i %% 2==0){
			m1[i/2,]=bindata[i]
		}
		else{
			m2[(i+1)/2,]=bindata[i]
		}
		i = i+1
	}
	if(evenParity){
		return(m1)
	}
	else
	{
		return(m2)
	}
}

separateRandomly = function(bindata,k){
	
	perm = permute(bindata,k)
	return(c(perm[1:length(perm)/2,],perm[length(perm)/2+1 : length(perm),] ))
}

permute=function(bindata, k){
	
	indeces = 1:nrow(bindata)
	perm = permute(indeces)
	
	out = bindata
	
	for(i in 1:length(bindata)){
		out[i,] = bindata[perm[i],]
	}
	return(out)
}

