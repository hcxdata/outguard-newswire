package com.jetyun.newswire.sorting

import org.apache.spark.rdd.RDD
import com.jetyun.newswire.sorting.rule.UrlWeightRule
import com.jetyun.newswire.sorting.rule.PublishTimeRule
import com.jetyun.newswire.sorting.rule.Page2VectorFunction

/**
 * @author 杨勇
 * 用来将HttpPage转换成向量,初步实现按时间、网页来源(网站和分类，例如新浪体育、网易新闻等等,从域名上来判断)、分类得分三个维度来进行加权排序
 */
object Page2VectorDriver {
  def page2Vector(pages: RDD[HttpPage], array: Array[(HttpPage)=>Double]): RDD[(Int, String, Array[Double])] = {
    val weights: RDD[(Int, String, Array[Double])] = pages.map { page =>
      val vector = array.map(f => f(page))
      (page.id, page.publishTime, vector)
    }
    weights
  }
}