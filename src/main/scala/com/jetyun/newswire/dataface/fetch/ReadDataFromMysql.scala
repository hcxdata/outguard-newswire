package com.jetyun.newswire.dataface.fetch

import java.sql.DriverManager
import java.sql.ResultSet
import scala.beans.BeanProperty
import org.apache.spark.SparkContext
import org.apache.spark.rdd.JdbcRDD
import org.apache.spark.rdd.RDD
import com.jetyun.newswire.config.DBConfig
import com.jetyun.newswire.sorting.HttpPage
import com.jetyun.newswire.util.JsonTools
import java.util.HashMap
import java.util.regex.Pattern
import com.jetyun.newswire.java.util.JJsonTools
import org.apache.commons.lang.StringUtils

/**
 * @author dell
 */
object ReadDataFromMysql {

  val timePattern = Pattern.compile("")

  def fetchDataFromMysql(sc: SparkContext, sql: String, low: Long, up: Long, partitation: Int): RDD[HttpPage] = {
    val data = new JdbcRDD(sc, createConnection, sql, low, up, partitation, extractValues)
    data
  }

  private def createConnection() = {
    Class.forName(DBConfig.getConfigString("db.driver"))
    DriverManager.getConnection(DBConfig.getConfigString("db.url"), DBConfig.getConfigString("db.user"), DBConfig.getConfigString("db.pass"))
  }

  private def extractValues(rs: ResultSet): HttpPage = {
    val id = rs.getInt("id")
    val publishtime =rs.getString("publish_time")
    val title = rs.getString("title")
    val context = rs.getString("main_body")
    val keywords = rs.getString("keywords")
    val url = rs.getString("url")
    HttpPage(id, id + "", title, context, publishtime, keywords, url)
  }
}