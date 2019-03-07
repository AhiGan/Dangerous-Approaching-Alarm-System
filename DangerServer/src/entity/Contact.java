package entity;

import java.io.Serializable;

public class Contact implements Serializable{
	private String userNumber;
	private String name;
	private String number;
	
	public Contact()
	{
		this.name = "";
		this.number = "";
		this.userNumber ="";
	}
	
	public Contact(String name, String number)
	{
		this.name = name;
		this.number = number;
		this.userNumber = "";
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setNumber(String number)
	{
		this.number = number;
	}
	
	public String getNumber()
	{
		return number;
	}
	
	public void setUserNumber(String userNumber)
	{
		this.userNumber = userNumber;
	}
	
	public String getUserNumber()
	{
		return userNumber;
	}
}
