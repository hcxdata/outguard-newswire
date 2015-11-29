package com.jetyun.newswire.rdbms

import com.jetyun.newswire.config.DBConfig
import java.sql.DriverManager
import java.sql.Connection

/**
 * @author Administrator
 */
object DBConnection extends Serializable{
  Class.forName(DBConfig.getConfigString("db.driver"))
  def getConnection:Connection = DriverManager.getConnection(DBConfig.getConfigString("db.url"), DBConfig.getConfigString("db.user"), DBConfig.getConfigString("db.pass"))
  
}