package com.jetyun.newswire.util

import org.codehaus.jackson.map.ObjectMapper
import com.jetyun.newswire.sorting.HttpPage
import org.codehaus.jackson.annotate.JsonMethod
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility

/**
 * @author 杨勇
 */
object JsonTools {
   private val mapper = new ObjectMapper
   mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY)
   
   def readObjectFromJson[T](json:String,clazz:Class[T]):Any={
     val rs = mapper.readValue(json, clazz).asInstanceOf[T]
     rs
   }
   
   def writeObjectToJson(obj:Any):String={
     val json = mapper.writeValueAsString(obj)
     json
   }
   
   def main(args: Array[String]): Unit = {
     val page1 = new HttpPage(1l,"title:String","content:String",1l,"keyword:String","url:String")
     val json = JsonTools.writeObjectToJson(page1)
     println(json)
     val page :HttpPage= JsonTools.readObjectFromJson(json, classOf[HttpPage]).asInstanceOf[HttpPage]
     println(page.url)
   }
   
}