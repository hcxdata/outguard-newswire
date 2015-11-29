package com.jetyun.newswire.config

import java.util.ResourceBundle
import java.util.Locale

/**
 * @author Administrator
 */
object DBConfig {
  private val db = ResourceBundle.getBundle("db", Locale.getDefault)
  
  def getConfigString(key:String) = db.getString(key)
}