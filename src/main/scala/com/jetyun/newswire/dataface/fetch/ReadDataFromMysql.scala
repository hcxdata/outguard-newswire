package com.jetyun.newswire.dataface.fetch

import java.sql.DriverManager
import java.sql.ResultSet

import scala.beans.BeanProperty

import org.apache.spark.SparkContext
import org.apache.spark.rdd.JdbcRDD
import org.apache.spark.rdd.RDD

import com.jetyun.newswire.config.DBConfig
import com.jetyun.newswire.sorting.HttpPage

/**
 * @author dell
 */
object ReadDataFromMysql {
  def fetchDataFromMysql(sc: SparkContext, sql: String, low: Long, up: Long, partitation: Int):RDD[HttpPage]= {
    val data = new JdbcRDD(sc, createConnection, sql, low, up, partitation, extractValues)
    data
  }

  private def createConnection() = {
    Class.forName(DBConfig.getConfigString("db.driver"))
    DriverManager.getConnection(DBConfig.getConfigString("db.url"), DBConfig.getConfigString("db.user"), DBConfig.getConfigString("db.pass"))
  }
  
  private def extractValues(rs: ResultSet):HttpPage = {
    HttpPage(rs.getString("id").hashCode(), rs.getString("id"), rs.getString("title"), rs.getString("text"), rs.getLong("fetchTime"), rs.getString("title"), rs.getString("baseUrl"))
  }
}