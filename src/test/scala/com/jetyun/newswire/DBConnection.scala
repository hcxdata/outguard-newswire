package com.jetyun.newswire

import java.sql.DriverManager

/**
 * @author Administrator
 */
object DBConnection {
  Class.forName("com.mysql.jdbc.Driver")
  val connection = DriverManager.getConnection("jdbc:mysql://news.jetyun.com:3306/nutch_crawler?useUnicode=true&amp;characterEncoding=UTF-8", "root", "SgeMeTi7xQw[A1o9")
}