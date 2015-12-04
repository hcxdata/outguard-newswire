package com.jetyun.newswire

import java.util.regex.Pattern
import scala.io.Source

/**
 * @author Administrator
 */
object PatternTest {
  def main(args: Array[String]): Unit = {
    val pattern = Pattern.compile("(\"publish_time\":\"\\d{8,20}\")")
    val string = Source.fromFile("E:\\data\\pattern.txt", "utf-8").getLines()
    string.foreach { x => 
      val m=pattern.matcher(x)
      println(m.group())
    }
  }
}