package com.jetyun.newswire.main

import com.jetyun.newswire.sorting.rule.Page2VectorFunction
import com.jetyun.newswire.sorting.rule.UrlWeightRule
import com.jetyun.newswire.sorting.rule.PageLocationRule
import org.apache.spark.SparkContext
import com.jetyun.newswire.sorting.Page2VectorDriver
import com.jetyun.newswire.sorting.SortingDriver
import com.jetyun.newswire.sorting.rule.PublishTimeRule
import com.jetyun.newswire.dataface.fetch.ReadDataFromHBase
import org.apache.hadoop.hbase.HBaseConfiguration
import com.jetyun.newswire.sorting.HttpPage
import com.jetyun.newswire.util.JsonTools
import org.apache.hadoop.hbase.client.Scan
import org.apache.hadoop.hbase.util.Bytes
import org.apache.spark.rdd.RDD
import org.apache.hadoop.hbase.client.Result
import org.apache.hadoop.hbase.client.Put

/**
 * @author 杨勇
 */
object BootStrap {

  private def fetchPage(sc: SparkContext): RDD[HttpPage] = {
    val conf = HBaseConfiguration.create()
    conf.set("hbase.zookeeper.property.clientPort", "2181")
    conf.set("hbase.zookeeper.quorum", "ubuntu3,ubuntu1,ubuntu2")
    conf.set("hbase.master", "ubuntu1:60000")
    val tableName = ""
    val scan = new Scan
    scan.setStartRow("".getBytes)
    scan.setStopRow("".getBytes)
    val hbaseData = ReadDataFromHBase.fetchDataFromHBase(sc, conf, tableName, scan)
    val pages = hbaseData.map(kv => {
      val rowKey = kv._1
      val result = kv._2
      result2Page(result)
    })
    pages
  }

  private def result2Page(result: Result): HttpPage = {
    HttpPage(1, "", "", "", 1l, "", "")
  }

  private def page2Result(page: HttpPage): Put = {
    val put = new Put("".getBytes)
    put
  }

  private def sorting(pages: RDD[HttpPage]): RDD[(Int, String, Double)] = {
    val vectorFunction = Array[Page2VectorFunction](UrlWeightRule, PublishTimeRule, PageLocationRule)
    val sortData = Page2VectorDriver.page2Vector(pages, vectorFunction)
    val randomData = sortData.randomSplit(Array[Double](0.01, 0.99), 11l)(0)
    val result = SortingDriver.sort(randomData)
    result
  }

  private def updateWeights(weights:RDD[(Int, String, Double)])={
    
  }
  
  def main(args: Array[String]): Unit = {
    val sc = new SparkContext(args(0), "pageRank_" + System.currentTimeMillis())
    val pages = fetchPage(sc)
    val sortResult = sorting(pages)
    updateWeights(sortResult)
    sortResult.foreach(println _)
  }
}