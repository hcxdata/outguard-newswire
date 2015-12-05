spark-submit --jars /opt/apps/page_rank/libs/mysql-connector-java-5.1.17.jar  --class com.jetyun.newswire.main.BootStrap --master yarn-cluster /opt/apps/page_rank/libs/outguard-newswire_2.10-1.0.jar yarn-cluster 1000 /user/root/pagerank/model/tfidf.model

mysqldump -B outguard_crawler --ignore-table=outguard_crawler.webpage -h rds5hhm9neufn4xn2mix.mysql.rds.aliyuncs.com -ucrawler -pR5mTSEUJLZtwLbGf -r db1.sql

mysql -h rds5hhm9neufn4xn2mix.mysql.rds.aliyuncs.com -ucrawler -pR5mTSEUJLZtwLbGf 