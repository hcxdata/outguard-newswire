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
    val json = rs.getString("json").replaceAll("-", "")
    var publishtime = "0"
    var title = ""
    var context = ""
    var keywords = ""
    try {
      val map: HashMap[String, Any] = JJsonTools.getObjectFromJson(json, classOf[HashMap[String, Any]]).asInstanceOf[HashMap[String, Any]]
      if (map.containsKey("publish_time")) {
        publishtime = map.get("publish_time").toString()
      }
      if (map.containsKey("dc:title")) {
        title = map.get("dc:title").toString()
      }
//      if (map.containsKey("mainnewscontent")) {
//        context = map.get("mainnewscontent").toString()
//      }
      if (map.containsKey("bodynewscontent")) {
        context = map.get("bodynewscontent").toString()
      }
      if (map.containsKey("keywords")) {
        keywords = map.get("keywords").toString()
      }

    } catch {
      case e: Exception =>
        publishtime = "0"
        println("parse json error id=[" + rs.getInt("id") + "],message=[" + e.getStackTrace.mkString("\n") + "]")
    }
    HttpPage(rs.getInt("id"), rs.getInt("id") + "", title, context, publishtime, keywords, rs.getString("url"))
  }
}