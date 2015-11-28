package com.jetyun.newswire.sorting

import com.jetyun.newswire.TestBase
import com.jetyun.newswire.sorting.rule.UrlWeightRule
import com.jetyun.newswire.sorting.rule.PublishTimeRule
import com.jetyun.newswire.sorting.rule.PageLocationRule
import com.jetyun.newswire.util.JsonTools

/**
 * @author Administrator
 */
object SortingTest extends TestBase{
  def main(args: Array[String]): Unit = {
    val path = "E:\\data\\spark\\httppages\\pages.data"
    val pages = sc.textFile(path).map { line => JsonTools.readObjectFromJson(line, classOf[HttpPage]).asInstanceOf[HttpPage] }
    val vectorFunction = Array[(HttpPage)=>Double](UrlWeightRule.weight,PublishTimeRule.weight,PageLocationRule.weight)
    val sortData = Page2VectorDriver.page2Vector(pages, vectorFunction)
    val result = SortingDriver.sort(sortData)
    result.foreach(println _)
  }
}