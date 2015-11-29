package com.jetyun.newswire

import scala.collection.mutable.ArrayBuffer
import com.jetyun.newswire.sorting.HttpPage
import org.apache.commons.io.FileUtils
import com.jetyun.newswire.util.JsonTools
import java.io.File

/**
 * @author Administrator
 */
object data {
  def main(args: Array[String]): Unit = {
    val conn = DBConnection.connection
    val sql = "select * from webpage_1w where text is not null "
    val stmt = conn.createStatement()
    val rs = stmt.executeQuery(sql)
    val file = "E:\\data\\spark\\httppages\\pages.data"
    while (rs.next()) {
      val page = (HttpPage(rs.getString("id").hashCode(), rs.getString("id"), rs.getString("title"), rs.getString("text"), rs.getLong("fetchTime"), rs.getString("title"), rs.getString("baseUrl")))
      FileUtils.writeStringToFile(new File(file), JsonTools.writeObjectToJson(page)+"\n", "utf-8", true)
    }

  }
}