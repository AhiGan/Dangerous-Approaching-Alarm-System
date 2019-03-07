package com.server.dao;

import java.util.List;

import com.server.db.DbHelper;

import entity.Contact;

public class ContactDAO {

	public static boolean newContact(Contact contact)
	{
		String sql = "insert into relationship(user_number, relation_name, relation_number) values (?,?,?)";
		String[] paras = new String[3];
		paras[0] = contact.getUserNumber();
		paras[1] = contact.getName();
		paras[2] = contact.getNumber();
		boolean b = new DbHelper().sqlUpdate(sql, paras);
		return b;
	}
	
	public static boolean deleteContact(Contact contact)
	{
		String sql = "delete from relationship where user_number=? and relation_name=? and relation_number=?";
		String[] paras = new String[3];
		paras[0] = contact.getUserNumber();
		paras[1] = contact.getName();
		paras[2] = contact.getNumber();
		boolean b = new DbHelper().sqlUpdate(sql, paras);
		return b;
	}
	
	public static List<Contact> readContacts(String user_number)
	{
		String sql = "select * from relationship where user_number=?";
		String[] paras = new String[1];
		paras[0] = user_number;
		List<Contact> contacts = new DbHelper().sqlQueryContacts(sql, paras);
		if(contacts != null && contacts.size() > 0)
			return contacts;
		else
			return null;
	}
}
