generateMu <-function(m,muBar, muVar){
  alpha <- ((1 - muBar) / muVar - 1 / muBar) * muBar ^ 2
  beta <- alpha * (1 / muBar - 1)
  return (rbeta(m, alpha, beta))
}