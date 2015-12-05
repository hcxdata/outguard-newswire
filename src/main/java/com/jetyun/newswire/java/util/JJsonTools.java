package com.jetyun.newswire.java.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.jetyun.newswire.rdbms.DBConnection;


public class JJsonTools {

	private static ObjectMapper mapper;
	
	private static ObjectMapper getMapper(){
		if(mapper == null){
			mapper = new ObjectMapper();
		}
		return mapper;
	}
	
	public static <T> T getObjectFromJson(String json,Class<T> clazz) throws JsonParseException, JsonMappingException, IOException{
		return getMapper().readValue(json, clazz);
	}
	
	public static void main(String[] args) throws IOException, SQLException {
		Connection con = DBConnection.getConnection();
		Statement stmt = con.createStatement();
		String sql = "select * from webpage_metadata_parser where id = 8730";
		ResultSet rs = stmt.executeQuery(sql);
		Map<String,Object> map = null;
		while(rs.next()){
			map = JJsonTools.getObjectFromJson(rs.getString("json"), Map.class);
		}
		
		System.out.println(map.get("publish_time"));
	}
}
