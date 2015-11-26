package com.jetyun.newswire.sorting.rule

import com.jetyun.newswire.sorting.HttpPage

/**
 * @author Administrator
 */
object UrlWeightRule extends Serializable with Page2VectorFunction{
  def weight(page:HttpPage):Double={
    0.0
  }
}