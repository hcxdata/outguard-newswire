package com.jetyun.newswire

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import com.jetyun.newswire.sorting.HttpPage
import org.apache.commons.io.FileUtils
import com.jetyun.newswire.util.JsonTools


/**
 * @author Administrator
 */
object data {
  def main(args: Array[String]): Unit = {
//    val conn = DBConnection.connection
//    val sql = "select * from webpage_1w where text is not null "
//    val stmt = conn.createStatement()
//    val rs = stmt.executeQuery(sql)
//    val file = "E:\\data\\spark\\httppages\\pages.data"
//    while (rs.next()) {
//      val page = (HttpPage(rs.getString("id").hashCode(), rs.getString("id"), rs.getString("title"), rs.getString("text"), rs.getLong("fetchTime")+"", rs.getString("title"), rs.getString("baseUrl")))
//      //FileUtils.writeStringToFile(new File(file), JsonTools.writeObjectToJson(page)+"\n", "utf-8", true)
//    }
      val list = Array(1,2,3,4,5)
      val real = list.filter(_>3)
      real.foreach(println _)


      val map = new mutable.HashMap[String,Double]()
    val str = "testsfd"
    map.put("t",1.0)
    map.put("te",0.5)
    map.put("tes",2.0)
    map.put("test",5.0)
    val weights = map.filter(f=>str.contains(f._1))
    val value = if(weights==null||weights.size==0){
        0.0
      }else{
        weights .maxBy(f=>f._1.length)._2
      }
    println(value)
  }
}