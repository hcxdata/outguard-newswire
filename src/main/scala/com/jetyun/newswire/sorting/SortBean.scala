package com.jetyun.newswire.sorting

import java.util.Date
import scala.beans.BeanProperty

/**
 * @author 杨勇
 */
@BeanProperty
case class HttpPage(id:Long,title:String,content:String,publishTime:Long,keyword:String,url:String){
  //定义一个无参的构造函数,方便反射调用
  def this() = this(1l,"","",1l,"","")
}