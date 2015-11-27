package com.jetyun.newswire.sorting.rule

import com.jetyun.newswire.sorting.HttpPage

/**
 * @author 杨勇
 */
trait Page2VectorFunction {
  def weight(page:HttpPage):Double
}