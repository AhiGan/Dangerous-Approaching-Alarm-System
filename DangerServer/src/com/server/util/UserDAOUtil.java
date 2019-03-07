package com.server.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import entity.Contact;
import entity.MyLatLng;
import entity.User;


public class UserDAOUtil {
	public static List<User> ResultSet2UserList(ResultSet rs) throws SQLException {
		List<User> users = new ArrayList<User>();
		while(rs.next())
		{				
			User u = new User();
			u.setNumber(rs.getString("number"));
			u.setPassword(rs.getString("password"));
			u.setRole(rs.getString("role"));
			users.add(u);
		}	
		return users;
	}
	
	public static List<Contact> ResultSet2ContactList(ResultSet rs) throws SQLException {
		List<Contact> contacts = new ArrayList<Contact>();
		while(rs.next())
		{
			Contact contact = new Contact();
			contact.setName(rs.getString("relation_name"));
			contact.setNumber(rs.getString("relation_number"));
			contacts.add(contact);
		}
		return contacts;
	}
	
	public static ArrayList<MyLatLng> ResultSet2DangerList(ResultSet rs) throws SQLException {
		ArrayList<MyLatLng> dangerList = new ArrayList<MyLatLng>();
		while(rs.next())
		{
			MyLatLng allLat = new MyLatLng(rs.getDouble("lat"), rs.getDouble("long"));
			dangerList.add(allLat);
		}
		return dangerList;
	}



}
