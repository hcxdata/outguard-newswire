package com.jetyun.newswire.sorting

import org.apache.spark.rdd.RDD
import org.apache.spark.mllib.regression.LabeledPoint

/**
 * @author 杨勇
 */
object SortingDriver {
  def sort(data: RDD[(Int, String, Array[Double])]): RDD[(Int, String, Double)] = {
    val result = data.map { label =>
      val array = label._3
      var weight = 0.0
      for (i <- 0 until array.length) {
        weight += array(i) * SortDimen.dimen(i)
      }
      (label._1, label._2, weight)
    }.sortBy(f => f._3, false)
    result
  }
}