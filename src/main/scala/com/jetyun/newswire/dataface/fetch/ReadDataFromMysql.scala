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

/**
 * @author dell
 */
object ReadDataFromMysql {
  def fetchDataFromMysql(sc: SparkContext, sql: String, low: Long, up: Long, partitation: Int): RDD[HttpPage] = {
    val data = new JdbcRDD(sc, createConnection, sql, low, up, partitation, extractValues)
    data
  }

  private def createConnection() = {
    Class.forName(DBConfig.getConfigString("db.driver"))
    DriverManager.getConnection(DBConfig.getConfigString("db.url"), DBConfig.getConfigString("db.user"), DBConfig.getConfigString("db.pass"))
  }

  private def extractValues(rs: ResultSet): HttpPage = {
//    val json = rs.getString("json").replaceAll("-", "")
//    println("-------------id=="+rs.getInt("id"))
//    val map: HashMap[String, Any] = JsonTools.readObjectFromJson(json, classOf[HashMap[String, Any]]).asInstanceOf[HashMap[String, Any]]
//    var publishtime = "0"
//    if (map.containsKey("publish_time")) {
//      publishtime = map.get("publish_time").toString()
//    }
    HttpPage(rs.getInt("id"), rs.getInt("id") + "", "title", "text", "0", "title", rs.getString("url"))
  }
}