package com.jetyun.newswire.sorting.rule

import com.jetyun.newswire.sorting.HttpPage
import scala.collection.mutable.HashMap
import com.jetyun.newswire.rdbms.DBConnection
import java.util.regex.Pattern

/**
 * @author 杨勇
 */
object UrlWeightRule extends Serializable with Page2VectorFunction {

  private val urlConfig = new HashMap[String, Double]

  private var initFlag = false

  private def initUrlConfig() {
    val con = DBConnection.getConnection
    val stmt = con.createStatement()
    val sql = "select url,weight from url_weight_rule "
    val rs = stmt.executeQuery(sql)
    println("execute sql --[" + sql + "]")
    while (rs.next()) {
      urlConfig.put(rs.getString("url"), rs.getDouble("weight"))
    }
    initFlag = true
  }

  private val p = Pattern.compile("(?<=//|)((\\w)+\\.)+\\w+") //.compile("(?<=http://|\\.)[^.]*?\\.(com|cn|net|org|biz|info|cc|tv)",Pattern.CASE_INSENSITIVE)

  def weight(page: HttpPage): Double = {
    if (!initFlag) {
      initUrlConfig()
    }
    val weight = matchByIterator(page)
    weight
  }

  def matchByIterator(page: HttpPage): Double = {
    val weights = urlConfig.filter(f=>page.url.contains(f._1))
    if(weights==null||weights.size==0){
      0.0
    }else{
      weights .maxBy(f=>f._1.length)._2
    }
  }

  def matchBySubDomain(page: HttpPage): Double = {
    val domain = {
      val m = p.matcher(page.url)
      if (m.find()) {
        m.group()
      } else {
        "Unknow"
      }
    }
    if (urlConfig.contains(domain)) urlConfig.get(domain).get else urlConfig.get("Unknow").get
  }

  def main(args: Array[String]): Unit = {
    val m = (p.matcher("http://news.cnr.cn/comment/latest/201311/t20131118_514159792.shtml"))
    println(m.find())
    println(m.group())
  }
}