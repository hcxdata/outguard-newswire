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
import com.jetyun.newswire.dataface.fetch.ReadDataFromMysql

/**
 * @author 杨勇
 */
object BootStrap {

  private def fetchPage(sc: SparkContext,low:Long,up:Long): RDD[HttpPage] = {
    val sql = "select * from webpage_1w where fetchTime>? and fetchTime<?"
    val data = ReadDataFromMysql.fetchDataFromMysql(sc, sql, low, up, 5)
    data
  }

//  private def result2Page(result: Result): HttpPage = {
//    HttpPage(1, "", "", "", 1l, "", "")
//  }
//
//  private def page2Result(page: HttpPage): Put = {
//    val put = new Put("".getBytes)
//    put
//  }

  private def sorting(pages: RDD[HttpPage]): RDD[(Int, String, Double)] = {
    val vectorFunction = Array[Page2VectorFunction](UrlWeightRule, PublishTimeRule, PageLocationRule)
    val sortData = Page2VectorDriver.page2Vector(pages, vectorFunction)
    val randomData = sortData.randomSplit(Array[Double](0.01, 0.99), 11l)(0)
    val result = SortingDriver.sort(randomData)
    result
  }

  private def updateWeights(weights:RDD[(Int, String, Double)])={
    weights.mapPartitions(f, preservesPartitioning)
  }
  
  def main(args: Array[String]): Unit = {
    val sc = new SparkContext(args(0), "pageRank_" + System.currentTimeMillis())
    val low = 1408417323191l
    val up = 1408570903597l
    val pages = fetchPage(sc,low,up)
    val sortResult = sorting(pages)
    updateWeights(sortResult)
    sortResult.foreach(println _)
  }
}