package com.jetyun.newswire.deduplication

import org.apache.spark.rdd.RDD
import org.apache.spark.mllib.regression.LabeledPoint
import com.jetyun.newswire.java.util.StringSimilarity

/**
 * @author 杨勇
 */
object DeduplicationDriver {

  def duplicate(data: RDD[(Int, String)]): RDD[(Int, String)] = {
    val simi = new StringSimilarity
    val map = data.collect().toMap
    val rs = data.map(f => {
      val temp = map.filter(p => simi.getSimilarityRatio(p._2, f._2) > 0.5).map(_._1).toList
      val list = (f._1::temp).sortBy { x => x }
      (list(0),f._2)
    })
    rs
  }
}