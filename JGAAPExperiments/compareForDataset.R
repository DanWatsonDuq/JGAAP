# TODO: Add comment
# 
# Author: Dan
###############################################################################


cmpFords = function(binData, ks){
	
	MCeval = evalFunct(binData)
	for(i in 1:length(ks)){
		cat("For K = ", ks[i],"\n")

		cat("MCSet:\n")
		MCset = MCoptimalSubset(binData, ks[i])
		mcRes = MCeval(MCset, 1)
		inSamp = err(binData, MCset, colMeans(binData))
		cat(MCset," MC:",mcRes, " IS:", inSamp, "\n")
		
		print("NaiveSet:")
		NaSet = naiveSelect(binData, ks[i])
		mcRes = MCeval(NaSet, 10)
		inSamp = err(binData, NaSet, colMeans(binData))
		cat(NaSet," MC:",mcRes, " IS:", inSamp, "\n")
	}
}
