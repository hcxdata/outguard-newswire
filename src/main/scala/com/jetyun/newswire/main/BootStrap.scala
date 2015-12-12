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
import com.jetyun.newswire.rdbms.DBConnection
import com.jetyun.newswire.textminer.featurexact.TfidfFeatureExactor
import com.jetyun.newswire.textminer.featurexact.Article
import com.jetyun.newswire.textminer.tfidf.TfidfModel
import org.apache.spark.mllib.regression.LabeledPoint
import com.jetyun.newswire.deduplication.DeduplicationDriver

/**
 * @author 杨勇
 */


object BootStrap {
  private def fetchPage (sc: SparkContext, low: Long, up: Long, limit: Int): RDD[HttpPage] = {
    val sql = "select * from webpage_metadata_parser where 100>? and 100>?  order by updated_at desc limit " + limit
    val data = ReadDataFromMysql.fetchDataFromMysql(sc, sql, low, up, 3)
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
    val vectorFunction = Array[(HttpPage) => Double](UrlWeightRule.weight, PublishTimeRule.weight, PageLocationRule.weight)
    val sortData = Page2VectorDriver.page2Vector(pages, vectorFunction)
    //val randomData = sortData.randomSplit(Array[Double](0.9, 0.1), 11l)(0)
    val result = SortingDriver.sort(sortData)
    result
  }

  private def updateWeights(weights: RDD[(Int, String, Double)]) = {
    weights.mapPartitions(updateMysql, true).collect()
  }

  private def updateMysql(weights: Iterator[(Int, String, Double)]): Iterator[Int] = {
    val conn = DBConnection.getConnection
    val selectSql = "select page_id from page_rank where page_id = ? "
    val stmt = conn.prepareStatement(selectSql)
    val updateSql = "update page_rank set weight = ? where page_id = ?"
    val updatestmt = conn.prepareStatement(updateSql)
    val insertSql = "insert into page_rank(page_id,weight,publishtime) values(?,?,?)"
    val insertstmt = conn.prepareStatement(insertSql)
    val result = weights.map(x => {
      stmt.setInt(1, x._1)
      val rs = stmt.executeQuery()
      if (rs.next()) {
        updatestmt.setDouble(1, x._3)
        updatestmt.setInt(2, x._1)
        updatestmt.executeUpdate()
      } else {
        insertstmt.setInt(1, x._1)
        insertstmt.setDouble(2, x._3)
        insertstmt.setString(3, x._2)
        insertstmt.executeUpdate()
      }
    })
    //    stmt.close()
    //    insertstmt.close()
    //    updatestmt.close()
    //    conn.close()
    result
  }

  private def fetchFeatures(pages: RDD[HttpPage], featureExactor: TfidfFeatureExactor): RDD[LabeledPoint] = {
    val articles = pages.map { page => Article(page.id * 1.0, "", page.title, page.keyword, page.content) }
    val features = featureExactor.exact(articles)
    features
  }

  private def fetchKeyWords(featureExactor: TfidfFeatureExactor, features: RDD[LabeledPoint], topn: Int = 10): RDD[(Int, String)] = {
    val result = features.map { feature =>
      (feature.label.toInt, featureExactor.topFeatures(feature, topn))
    }
    result
  }

  private def saveOrUpdateKeyWords(keywords: Array[(Int, String)]): Array[Int] = {
    val conn = DBConnection.getConnection
    val selectSql = "select page_id from page_keywords where page_id = ? "
    val stmt = conn.prepareStatement(selectSql)
    val updateSql = "update page_keywords set keywords = ? where page_id = ?"
    val updatestmt = conn.prepareStatement(updateSql)
    val insertSql = "insert into page_keywords(page_id,keywords) values(?,?)"
    val insertstmt = conn.prepareStatement(insertSql)
    val result = keywords.map(x => {
      stmt.setInt(1, x._1)
      try {
        val rs = stmt.executeQuery()
        if (rs.next()) {
          updatestmt.setString(1, x._2)
          updatestmt.setInt(2, x._1)
          updatestmt.executeUpdate()
        } else {
          insertstmt.setInt(1, x._1)
          insertstmt.setString(2, x._2)
          insertstmt.executeUpdate()
        }
      } catch {
        case e: Exception =>
          e.printStackTrace()
          1
      }
    })
    //    stmt.close()
    //    insertstmt.close()
    //    updatestmt.close()
    //    conn.close()
    result
  }

  def main(args: Array[String]): Unit = {
    val sc = new SparkContext(args(0), "pageRank_" + System.currentTimeMillis())
    val tfidfModel = new TfidfModel
    tfidfModel.loadModel(args(2))
    val low = 0
    val up = 0
    val pages = fetchPage(sc, low, up, args(1).toInt).cache()
    val sortResult = sorting(pages)
    updateWeights(sortResult)
    val featureExactor = new TfidfFeatureExactor(tfidfModel)
    val features = fetchFeatures(pages, featureExactor)
    val keywords = fetchKeyWords(featureExactor, features, 10)
    val quchongs = DeduplicationDriver.duplicate(keywords).collect()
    saveOrUpdateKeyWords(quchongs)
  }
}