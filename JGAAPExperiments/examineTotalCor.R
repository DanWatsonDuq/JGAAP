# Finds my Total Correlation metric
# @author Derek S. Prijatelj
# TODO, find better Total Cor measurement if it exists.

#computes In-Sample Performance
compIS = function(binData, k){
    require(caTools)
	
    candIds = seq.int(from=1, to=ncol(binData), by=1)
	subsets = combs(candIds, k)
	res = matrix(nrow=nrow(subsets),ncol= 2)

    
	for(i in 1:dim(subsets)[1]){
		is = err(binData, subsets[i,], colMeans(binData))
		res[i,1]= is
		res[i,2]= 0
	}
	
    return(cbind(res, subsets))
}

# Total Correlation : Mutual Information
# returns [mean, total cor, subset]
examineTotalCor = function(binData, k){
    require(CTT)

    # Random Permute and Halve
    binData = binData[sample(nrow(binData)),]
    
    # halves roughly: odd total = uneven content count in diff halves.
    #binData1 = binData[1:(floor(nrow(binData)/2)-1),]
    #binData2 = binData[floor(nrow(binData)/2):nrow(binData),]

    # res [n, 2] mat of accuracy
    # subsets [n, k] for n = total # of combos
    result = compIS(binData, k)
    
    # overwrites 2nd col w/ root mean square of each classifier's item-total cor
    for (ensemble in 1:nrow(result)){
        #result[ensemble, 2] = mean(abs(reliability(
        #    binData[, result[ensemble, -(1:2)]], itemal = TRUE
        #    )$pBis))
        result[ensemble, 2] = sqrt(mean(reliability(
            binData[, result[ensemble, -(1:2)]], itemal = TRUE
            )$pBis^2))
    }

    return(result)
}
