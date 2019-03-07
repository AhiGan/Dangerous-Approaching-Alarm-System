package com.server.service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.server.dao.ContactDAO;
import com.server.dao.UserDAO;

import entity.Contact;
import entity.Message;
import entity.MessageType;
import entity.MyLatLng;
import entity.User;

public class Server  {
	@SuppressWarnings("null")
	public Server()
	{
		ServerSocket ss = null;
		try {
			ss = new ServerSocket(8469);
			System.out.println("服务器正在8888监听");
			while(true)
			{
				Socket s = ss.accept();
				ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
				Message msg = (Message)ois.readObject();
				User sender = msg.getSender();
				ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
				
				if(msg.getMsgType().equals(MessageType.LOGIN))
				{
					System.out.println("上线啦！");
					User u = UserDAO.getUser(sender);
					if(u != null)
					{
						msg.setMsgType(MessageType.LOGIN_SUCCESS);
					}
					else
						msg.setMsgType(MessageType.LOGIN_FAILURE);
					msg.setReceiver(u);
				}
				else if(msg.getMsgType().equals(MessageType.REGISTER))
				{
					boolean b = UserDAO.queryUser(sender.getNumber());
					if(b == false)
					{
						UserDAO.addUser(sender);
						msg.setMsgType(MessageType.REGISTER_SUCCESS);
					}
					else
						msg.setMsgType(MessageType.REGISTER_FAILURE);
				}
				else if(msg.getMsgType().equals(MessageType.NEWCONTACT))
				{
					boolean b = UserDAO.queryUser(((Contact)msg.getObj()).getUserNumber());
					if(b == true)
					{
						b = ContactDAO.newContact((Contact)msg.getObj());
						if(b == true)
							msg.setMsgType(MessageType.NEWCONTACT_SUCCESS);
					}
					if(b == false)
						msg.setMsgType(MessageType.NEWCONTACT_FAILURE);
				}
				else if(msg.getMsgType().equals(MessageType.DELETECONTACT))
				{
					boolean b = ContactDAO.deleteContact((Contact)msg.getObj());
					if(b == true)
						msg.setMsgType(MessageType.DELETECONTACT_SUCCESS);
					else
						msg.setMsgType(MessageType.DELETECONTACT_FAILURE);
				}
				else if(msg.getMsgType().equals(MessageType.READCONTACT))
				{
					List<Contact> contacts = ContactDAO.readContacts(sender.getNumber());
					if(contacts != null)
						msg.setObj(contacts);
				}
				else if(msg.getMsgType().equals(MessageType.READDANGER))
				{
					ArrayList<MyLatLng> dangerList = UserDAO.readDanger();
					if(dangerList != null)
						msg.setObj(dangerList);
				}
				else if(msg.getMsgType().equals(MessageType.INDANGER))
				{
					boolean b = UserDAO.insertDanger((User)msg.getSender(), (MyLatLng) msg.getObj());
					if(b == true)
						msg.setMsgType(MessageType.INSERT_SUCCESS);
					else
						msg.setMsgType(MessageType.INSERT_FAILURE);
				}
				else if(msg.getMsgType().equals(MessageType.DELETEDANGER))
				{
					boolean b = UserDAO.deleteDanger((Contact)msg.getObj());
					if(b == true){
						//System.out.println("test true");
						msg.setMsgType(MessageType.DELETEDANGER_SUCCESS);
					}
					else{
						//System.out.println("test false");
						msg.setMsgType(MessageType.DELETEDANGER_FAILURE);
					}					
				}
				else if(msg.getMsgType().equals(MessageType.DANGERMONITOR))
				{
					System.out.println("test");
					ArrayList<MyLatLng> dangerList = UserDAO.monitorDanger((Contact)msg.getObj());
					if(dangerList != null){
						
						msg.setMsgType(MessageType.CHILDINDANGER);
					}
					else{  //孩子安全
						System.out.println("test false");
						ArrayList<MyLatLng> dangerListTemp = new ArrayList<MyLatLng>();
						MyLatLng flag = new MyLatLng(0.0,0.0);
						dangerListTemp.add(flag);
						dangerList = dangerListTemp;
						msg.setMsgType(MessageType.CHILDOUTOFDANGER);
					}	
					msg.setObj(dangerList);	
				}
				oos.writeObject(msg);
				oos.flush();
				ois.close();
				oos.close();
				s.close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
