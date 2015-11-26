package com.jetyun.newswire.sorting.rule

import com.jetyun.newswire.sorting.HttpPage

/**
 * @author Administrator
 */
trait Page2VectorFunction {
  def weight(page:HttpPage):Double
}