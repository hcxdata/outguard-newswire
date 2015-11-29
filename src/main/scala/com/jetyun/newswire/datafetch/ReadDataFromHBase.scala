package com.jetyun.newswire.datafetch

import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.spark.rdd.RDD
import org.apache.spark.SparkContext
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.client.Result
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.client.Scan

/**
 * @author 杨勇
 */
object ReadDataFromHBase {
  def fetchDataFromHBase(sc: SparkContext, conf: Configuration = HBaseConfiguration.create(), tableName: String,scan:Scan): RDD[(ImmutableBytesWritable, Result)] = {
    conf.set(TableInputFormat.INPUT_TABLE, tableName)
    val data = sc.newAPIHadoopRDD(conf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    data
  }
}