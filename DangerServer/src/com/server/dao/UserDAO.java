package com.server.dao;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.server.db.DbHelper;

import entity.Contact;
import entity.MyLatLng;
import entity.User;

public class UserDAO implements java.io.Serializable{
	public static User getUser(User user) {
		// TODO Auto-generated method stub
		String sql = "select * from user where number=? and password=?";
		String[] paras = new String[2];
		paras[0] = user.getNumber();
		paras[1] = user.getPassword();	
		//执行查询操作
		List<User> users = new DbHelper().sqlQueryUser(sql, paras);
		if (users != null && users.size() > 0) {
			return users.get(0);
		} else
			return null;
	}
	
	public static boolean queryUser(String number)
	{
		boolean b;
		String sql = "select * from user where number=?";
		String[] paras = new String[1];
		paras[0] = number;
		List<User> users = new DbHelper().sqlQueryUser(sql, paras);
		if (users != null && users.size() > 0)
			b = true;
		else
			b = false;
		return b;
	}
	
	public static boolean addUser(User user)
	{
		String sql = "insert into user(number,password,role) values (?,?,?)";
		String[] paras = new String[3];
		paras[0] = user.getNumber();
		paras[1] = user.getPassword();
		paras[2] = user.getRole();
		boolean b = new DbHelper().sqlUpdate(sql, paras);
		return b;
	}
	
	public static ArrayList<MyLatLng> readDanger()
	{
		String sql = "select * from pooldata ";
		ArrayList<MyLatLng> dangerList=new DbHelper().sqlQueryDanger(sql);
		if(dangerList != null && dangerList.size() > 0)
			return dangerList;
		else
			return null;
	}
	
	public static boolean insertDanger(User user,MyLatLng position)
	{
		String sql = "insert into dangerlist(user_number,`long`,lat) values (?,?,?)";
		//String sql = "INSERT INTO `dangerisapproaching`.`dangerlist` (`user_number`, `long`, `lat`) VALUES (?, ?, ?)";
		String[] paras = new String[3];
		paras[0] = user.getNumber();
		paras[1] = position.getLongitude()+"";//Double.toString(position.getLongitude());
		paras[2] = position.getLatitude()+"";//Double.toString(position.getLatitude());
		boolean b = new DbHelper().sqlUpdate(sql, paras);
		return b;
	}

	public static boolean deleteDanger(Contact obj) {
		// TODO Auto-generated method stub
		
		String sql = "delete from dangerlist where user_number=?";
		String[] paras = new String[1];
		paras[0] = obj.getNumber();
		boolean b = new DbHelper().sqlUpdate(sql, paras);
		return b;
	}

	public static ArrayList<MyLatLng> monitorDanger(Contact obj) {
		// TODO Auto-generated method stub
		String sql = "select * from dangerlist where user_number=?";
		String[] paras = new String[1];
		paras[0] = obj.getNumber();
		//执行查询操作
		ArrayList<MyLatLng> dangerList =new DbHelper().sqlQueryDanger(sql, paras);
		if(dangerList != null && dangerList.size() > 0)
			return dangerList;
		else
			return null;
	}
}
