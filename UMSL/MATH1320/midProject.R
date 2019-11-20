#Required Libraries
library(lsr)
library(RVAideMemoire)

#performs the one mean z-test
#presumes standard deviation of 0.37ppm
oneMeanZTest <-function(dataValues){
  
  problem.sd <- 0.37
  problem.mu.sample <- mean(dataValues)
  problem.mu.pop <- 0.5
  problem.sl <- 0.05
  problem.n <- length(dataValues)
  problem.stderror = problem.sd / sqrt(problem.n)
  problem.zscore <- (problem.mu.sample - problem.mu.pop) / problem.stderror
  problem.zalphahalf <- qnorm(1 - problem.sl/2)
  problem.zint <- problem.stderror *  problem.zalphahalf
  problem.cl.lower <- problem.mu.sample - problem.zint
  problem.cl.upper <- problem.mu.sample + problem.zint
  
  #print results to console
  cat("Here's the data that was given: \n")
  cat(dataValues, "\n")
  cat("\n")
  cat("##Given Values##\n")
  cat("Population Standard deviation: ", problem.sd, "\n")
  cat("Significance Level: ", (problem.sl * 100), "% \n")
  cat("Population Mean: ", problem.mu.pop, "\n")
  cat("\n")
  cat("##Calculated Values##\n")
  cat("Sample Mean: ", problem.mu.sample, "\n")
  cat("Standard Error: ", problem.stderror, "\n")
  cat("#trials: ", problem.n, "\n")
  cat("Z-Score: ", problem.zscore, "\n")
  cat("Z-Interval: ", problem.zint, "\n")
  cat("Z-alpha/2: ", problem.zalphahalf, "\n")
  cat("Confidence intervals(95%): (", problem.cl.lower, ",", problem.cl.upper, ")\n")
  
}

#Performs the on mean T-test
oneMeanTTest <-function(dataValues){
  
  cat("Here's the data that was given: \n")
  cat(dataValues, "\n")
  oneSampleTTest(dataValues, mean(dataValues))
}