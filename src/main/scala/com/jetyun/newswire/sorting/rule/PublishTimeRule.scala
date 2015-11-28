package com.jetyun.newswire.sorting.rule

import com.jetyun.newswire.sorting.HttpPage

/**
 * @author 杨勇
 */
object PublishTimeRule extends Serializable with Page2VectorFunction{
  def weight(page:HttpPage):Double={
    1.0
  }
}