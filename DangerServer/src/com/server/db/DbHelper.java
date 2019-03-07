package com.server.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.server.util.UserDAOUtil;
import com.baidu.mapapi.model.LatLng;

import entity.Contact;
import entity.MessageType;
import entity.MyLatLng;
import entity.User;

public class DbHelper {
	static String drivername = "com.mysql.jdbc.Driver";
	static String url = "jdbc:mysql://localhost:3306/dangerisapproaching";
	static String username = "root";
	static String password = "lina640";
	//static ArrayList<LatLng> dangerList;
	static
	{
		try
		{
			Class.forName(drivername);//创建驱动
			System.out.println("创建驱动成功！");
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection()
	{
		Connection conn = null;
		try
		{
			conn = DriverManager.getConnection(url, username, password);
			System.out.println("连接数据库成功！");
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return conn;
	}
	
	public static void free(ResultSet rs,Connection conn, Statement stmt)
	{
		try
		{
			if(rs != null)
				rs.close();//关闭结果集
		}
		catch(SQLException e)
		{
			System.out.println("关闭ResultSet失败！");
			e.printStackTrace();
		}
		finally{
			try
			{
				if (conn != null)
					conn.close();//关闭连接
			}
			catch(SQLException e)
			{
				System.out.println("关闭Connection失败！");
				e.printStackTrace();
			}
			finally
			{
				try
				{
					if(stmt != null)
						stmt.close();
				}
				catch(SQLException e)
				{
					System.out.println("关闭Statement失败！");
					e.printStackTrace();
				}
			}
		}
	}
	
	public List<User> sqlQueryUser(String sql,String []paras)
	{
		List<User> users = null;
		User user = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = DbHelper.getConnection();
			pstmt=conn.prepareStatement(sql);
			//给pstmt的问号赋值
			for(int i=0;i<paras.length;i++){
				pstmt.setString(i+1, paras[i]);
			}
			//执行操作
			rs=pstmt.executeQuery();
			users = UserDAOUtil.ResultSet2UserList(rs);			
		} catch (Exception e) {		
			e.printStackTrace();
		}finally{
			//关闭资源
			try {
				if(rs!=null)
					rs.close();
				if(pstmt!=null)
					pstmt.close();
				if(conn!=null)
					conn.close();
				
			} catch (Exception e) {
				
			}
		}
		return users;
	}
	
	public boolean sqlUpdate(String sql, String []paras)
	{
		Connection conn = null;
		PreparedStatement pstmt = null;
		boolean b=true;
		try {
			conn = DbHelper.getConnection();
			pstmt = conn.prepareStatement(sql);
			for(int i=0; i<paras.length; i++)
			{
				pstmt.setString(i+1, paras[i]);
			}
			pstmt.executeUpdate();
			
		} catch (Exception e) {	
			b = false;
			e.printStackTrace();
		}finally{
			//关闭资源
			try {
				if(pstmt!=null)
					pstmt.close();
				if(conn!=null)
					conn.close();
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		return b;
	}
	
	public List<Contact> sqlQueryContacts(String sql, String []paras)
	{
		List<Contact> contacts = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = DbHelper.getConnection();
			pstmt=conn.prepareStatement(sql);
			//给pstmt的问号赋值
			for(int i=0;i<paras.length;i++){
				pstmt.setString(i+1, paras[i]);
			}
			//执行操作
			rs=pstmt.executeQuery();
			contacts = UserDAOUtil.ResultSet2ContactList(rs);			
		} catch (Exception e) {		
			e.printStackTrace();
		}finally{
			//关闭资源
			try {
				if(rs!=null)
					rs.close();
				if(pstmt!=null)
					pstmt.close();
				if(conn!=null)
					conn.close();
				
			} catch (Exception e) {
				
			}
		}
		return contacts;
	}
	
	public ArrayList<MyLatLng> sqlQueryDanger(String sql)
	{
		ArrayList<MyLatLng> dangerList =null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = DbHelper.getConnection();
			pstmt=conn.prepareStatement(sql);
		/*	//给pstmt的问号赋值
			for(int i=0;i<paras.length;i++){
				pstmt.setString(i+1, paras[i]);
			}*/
			//执行操作
			rs=pstmt.executeQuery();
			dangerList = UserDAOUtil.ResultSet2DangerList(rs);			
		} catch (Exception e) {		
			e.printStackTrace();
		}finally{
			//关闭资源
			try {
				if(rs!=null)
					rs.close();
				if(pstmt!=null)
					pstmt.close();
				if(conn!=null)
					conn.close();
				
			} catch (Exception e) {
				
			}
		}
		return dangerList;
	}

	public ArrayList<MyLatLng> sqlQueryDanger(String sql, String[] paras) {
		// TODO Auto-generated method stub
		ArrayList<MyLatLng> dangerList =null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = DbHelper.getConnection();
			pstmt=conn.prepareStatement(sql);
			//给pstmt的问号赋值
			for(int i=0;i<paras.length;i++){
				pstmt.setString(i+1, paras[i]);
			}
			//执行操作
			rs=pstmt.executeQuery();
			//users = UserDAOUtil.ResultSet2List(rs);
			
			
			dangerList = UserDAOUtil.ResultSet2DangerList(rs);	
					
		} catch (Exception e) {		
			e.printStackTrace();
		}finally{
			//关闭资源
			try {
				if(rs!=null)
					rs.close();
				if(pstmt!=null)
					pstmt.close();
				if(conn!=null)
					conn.close();
				
			} catch (Exception e) {
				
			}
		}
		return dangerList;
	}

}
