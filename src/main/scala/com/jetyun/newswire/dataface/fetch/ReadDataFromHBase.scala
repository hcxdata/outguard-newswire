package com.jetyun.newswire.dataface.fetch

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client.Result
import org.apache.hadoop.hbase.client.Scan
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.protobuf.ProtobufUtil
import org.apache.hadoop.hbase.util.Base64
import org.apache.hadoop.hbase.util.Bytes
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

/**
 * @author 杨勇
 */
object ReadDataFromHBase {
  def fetchDataFromHBase(sc: SparkContext, conf: Configuration = HBaseConfiguration.create(), tableName: String, scan: Scan): RDD[(ImmutableBytesWritable, Result)] = {
    conf.set(TableInputFormat.INPUT_TABLE, tableName)
    val proto = ProtobufUtil.toScan(scan)
    val ScanToString = Base64.encodeBytes(proto.toByteArray())
    conf.set(TableInputFormat.SCAN, ScanToString)
    val data = sc.newAPIHadoopRDD(conf, classOf[TableInputFormat], classOf[ImmutableBytesWritable], classOf[Result])
    data
  }
}