spark-submit --jars /opt/apps/page_rank/libs/mysql-connector-java-5.1.17.jar  --class com.jetyun.newswire.main.BootStrap --master yarn-cluster /opt/apps/page_rank/libs/outguard-newswire_2.10-1.0.jar yarn-cluster 1000 /user/root/pagerank/model/tfidf.model

mysqldump -B outguard_crawler --ignore-table=outguard_crawler.webpage -h rds5hhm9neufn4xn2mix.mysql.rds.aliyuncs.com -ucrawler -pR5mTSEUJLZtwLbGf -r db1.sql

mysql -h rds5hhm9neufn4xn2mix.mysql.rds.aliyuncs.com -ucrawler -pR5mTSEUJLZtwLbGf 


mysqldump -h rds5hhm9neufn4xn2mix.mysql.rds.aliyuncs.com -ucrawler -pR5mTSEUJLZtwLbGf outguard_crawler webpage_metadata_parser > db1.sql



 SELECT  DISTINCT `webpage_metadata_parser`.`id` FROM `webpage_metadata_parser` INNER JOIN `page_rank` ON `page_rank`.`page_id` = `webpage_metadata_parser`.`id` INNER JOIN `page_keywords` ON `page_keywords`.`page_id` = `webpage_metadata_parser`.`id`  ORDER BY updated_at desc LIMIT 5 OFFSET 0
 

李祥辉 2015/12/9 21:19:08
 SELECT `webpage_metadata_parser`.`id` AS t0_r0, `webpage_metadata_parser`.`url` AS t0_r1, `webpage_metadata_parser`.`json` AS t0_r2, `webpage_metadata_parser`.`updated_at` AS t0_r3, `page_keywords`.`id` AS t1_r0, `page_keywords`.`page_id` AS t1_r1, `page_keywords`.`keywords` AS t1_r2 FROM `webpage_metadata_parser` INNER JOIN `page_rank` ON `page_rank`.`page_id` = `webpage_metadata_parser`.`id` INNER JOIN `page_keywords` ON `page_keywords`.`page_id` = `webpage_metadata_parser`.`id` WHERE `webpage_metadata_parser`.`id` IN (632, 633, 631, 630, 629)  ORDER BY updated_at desc