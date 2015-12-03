package com.jetyun.newswire.util

import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility
import org.codehaus.jackson.annotate.JsonMethod
import org.codehaus.jackson.map.ObjectMapper
import com.jetyun.newswire.sorting.HttpPage
import org.codehaus.jackson.JsonParser.Feature
import java.util.HashMap
import org.codehaus.jackson.JsonParser

/**
 * @author 杨勇
 */
object JsonTools {
  private val mapper = new ObjectMapper
  mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY)
  mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
  def readObjectFromJson[T](json: String, clazz: Class[T]): Any = {
    val rs = mapper.readValue(json, clazz).asInstanceOf[T]
    rs
  }

  def writeObjectToJson(obj: Any): String = {
    val json = mapper.writeValueAsString(obj)
    json
  }

  def main(args: Array[String]): Unit = {
    val map = new HashMap[String, Any]
    map.put("f1", "value1")
    map.put("f2", 1)
    println(writeObjectToJson(map))
  }

}