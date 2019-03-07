package com.example.dangerisapproaching.service;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.Callable;

import android.util.Log;

import com.baidu.mapapi.model.LatLng;
import com.example.dangerisapproaching.MainActivity;

import entity.Contact;
import entity.Message;
import entity.MessageType;
import entity.MyLatLng;
import entity.User;

public class MyCallable implements Callable<Object>{
	private Socket s;
	private String operation;
	private Object obj;
	
	public MyCallable(String operation, Object obj)
	{
		this.operation = operation;
		this.obj = obj;
	}
	
	public Object call() throws Exception
	{
		Object result = null;
		try
		{
			s = new Socket("192.168.1.222", 8469);//10.0.2.2
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		if(operation.equals(MessageType.LOGIN))
		{
			result = login((User)obj);
		}
		else if(operation.equals(MessageType.REGISTER))
		{
			result = register((User)obj);
		}
		else if(operation.equals(MessageType.NEWCONTACT))
		{
			result = newContact((Contact)obj);
		}
		else if(operation.equals(MessageType.DELETECONTACT))
		{
			result = deleteContact((Contact)obj);
		}
		else if(operation.equals(MessageType.READCONTACT))
		{
			result = readContacts((User)obj);
		}
		else if(operation.equals(MessageType.READDANGER))
		{
			result = readDanger((User)obj);
		}
		else if(operation.equals(MessageType.INDANGER))
		{
			MyLatLng currentPosition = new MyLatLng(MainActivity.mLastLocationData.latitude,
					MainActivity.mLastLocationData.longitude);
			result = sendInDanger((User)obj,currentPosition);
		}
		else if(operation.equals(MessageType.DELETEDANGER))
		{
			result = deleteDanger((Contact)obj);
		}
		else if(operation.equals(MessageType.DANGERMONITOR)){
			result = monitorDanger((Contact)obj);
		}
		return result;
	}
	



	private void send(Message msg)
	{
		ObjectOutputStream oos;
		try
		{
			oos = new ObjectOutputStream(s.getOutputStream());
			oos.writeObject(msg);
			oos.flush();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private Message receive()
	{
		Message msg = null;
		ObjectInputStream ois;
		try
		{
			ois = new ObjectInputStream(s.getInputStream());
			msg = (Message)ois.readObject();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return msg;
	}
	
	private User login(User user)
	{
		User u;
		Message msg = new Message();
		msg.setSender(user);
		msg.setMsgType(MessageType.LOGIN);
		send(msg);
		msg=receive();
		u = msg.getReceiver();
		return u;
	}
	
	private Object register(User user)
	{
		Message msg = new Message();
		msg.setSender(user);
		msg.setMsgType(MessageType.REGISTER);
		send(msg);
		msg = receive();
		return msg.getMsgType();
	}
	
	private Object newContact(Contact contact)
	{
		Message msg = new Message();
		msg.setMsgType(MessageType.NEWCONTACT);
		msg.setObj(contact);
		send(msg);
		msg = receive();
		return msg.getMsgType();
	}
	
	private Object deleteContact(Contact contact)
	{
		Message msg = new Message();
		msg.setMsgType(MessageType.DELETECONTACT);
		msg.setObj(contact);
		send(msg);
		msg = receive();
		return msg.getMsgType();
	}
	
	private Object readContacts(User user)
	{
		Message msg = new Message();
		msg.setMsgType(MessageType.READCONTACT);
		msg.setSender(user);
		send(msg);
		msg = receive();
		return msg.getObj();
	}
	
	private Object readDanger(User user)
	{
		Message msg = new Message();
		msg.setMsgType(MessageType.READDANGER);
		msg.setSender(user);
		send(msg);
		Log.v("发送","发送成功");
		msg = receive();
		return msg.getObj();
	}
	
	private Object sendInDanger(User user,MyLatLng cuurentPosition)
	{
		Message msg = new Message();
		msg.setMsgType(MessageType.INDANGER);
		msg.setSender(user);
		msg.setObj(cuurentPosition);
		send(msg);
		msg = receive();
		return msg.getObj();
	}
	
	private Object deleteDanger(Contact obj2) {
		// TODO Auto-generated method stub
		Message msg = new Message();
		msg.setMsgType(MessageType.DELETEDANGER);
		msg.setObj(obj2);
		send(msg);
		msg = receive();
		return msg.getMsgType();
	}
	
	private Object monitorDanger(Contact obj2) {
		// TODO Auto-generated method stub
		Message msg = new Message();
		msg.setMsgType(MessageType.DANGERMONITOR);
		msg.setObj(obj2);
		send(msg);
		msg = receive();
		return msg.getObj();
	}

}
