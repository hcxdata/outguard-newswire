package com.jetyun.newswire.sorting.rule

import com.jetyun.newswire.sorting.HttpPage
import java.util.Date

/**
 * @author 杨勇
 */
object PublishTimeRule extends Serializable with Page2VectorFunction{
  def weight(page:HttpPage):Double={
    1.0
  }
  def main(args: Array[String]): Unit = {
    println(1408555659214l*1.0/1450621790966L)
    println(new Date().getTime+1000*60*60*24*365*10)
  }
}