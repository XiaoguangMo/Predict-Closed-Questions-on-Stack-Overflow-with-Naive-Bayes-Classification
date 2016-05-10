# Predict-Closed-Questions-on-Stack-Overflow-with-Naive-Bayes-Classification
Predict which new questions asked on Stack Overflow will be closed

1. SSH to dc’s server
2. Setup Hadoop environment by typing: setup cdh.5.2
3. Get the output file by running preprocessing program to get the output.txt
4. Store the output file as output.txt in the root directory of hdfs
5. Start spark by typing: spark-shell —-master yarn-client —-num-executors 60
6. Copy all code in spark.scala into the terminal and you will get the accuracy as result
