package entity;

import java.io.Serializable;
import java.util.ArrayList;

//import com.baidu.mapapi.model.LatLng;

public class User implements Serializable{
	private String number;
	private String password;
	private String role;	
	private ArrayList<MyLatLng>dangerList=new ArrayList<MyLatLng>();

	public User()
	{
		this.number = "";
		this.password = "";
		this.role = "";
	}
	
	public User(String number, String password)
	{
		this.number = number;
		this.password = password;
		this.role = "";
	}
	
	public User(String number, String password, String role)
	{
		this.number = number;
		this.password = password;
		this.role = role;
	}
	
	public void setNumber(String number)
	{
		this.number = number;
	}
	
	public String getNumber()
	{
		return number;
	}
	
	public void setPassword(String password)
	{
		this.password = password;
	}
	
	public String getPassword()
	{
		return password;
	}
	
	public void setRole(String role)
	{
		this.role = role;
	}
	
	public String getRole()
	{
		return role;
	}
	
	public void setDangerList(ArrayList<MyLatLng> list)
	{
		this.dangerList=list;
	}
	
	public ArrayList<MyLatLng> getDangerLiat()
	{
		return dangerList;
	}
}
