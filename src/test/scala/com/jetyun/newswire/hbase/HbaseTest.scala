package com.jetyun.newswire.hbase

import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client.Result
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableInputFormat

import com.jetyun.newswire.TestBase

/**
 * @author Administrator
 */
object HbaseTest extends TestBase {

  def main(args: Array[String]): Unit = {
    val conf = HBaseConfiguration.create()
    conf.set("hbase.zookeeper.property.clientPort", "2181")
    conf.set("hbase.zookeeper.quorum", args(0))

    //设置查询的表名
    conf.set(TableInputFormat.INPUT_TABLE, args(1))
    val hbaseData = sc.newAPIHadoopRDD(conf, classOf[TableInputFormat],
      classOf[org.apache.hadoop.hbase.io.ImmutableBytesWritable],
      classOf[org.apache.hadoop.hbase.client.Result]).collect()
    println("------------------------------------")
    hbaseData.foreach(println _)
  }
}