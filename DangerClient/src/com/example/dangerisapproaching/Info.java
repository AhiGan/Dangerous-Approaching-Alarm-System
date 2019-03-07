package com.example.dangerisapproaching;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Info implements Serializable
{
	private static final long serialVersionUID = -1010711775392052966L;
	private double latitude;
	private double longitude;
	private int imgId;
	private String name;
	private String nickname;
	private int zan;

	public static List<Info> infos = new ArrayList<Info>();

	static
	{
		infos.add(new Info(31.8929230000, 118.8331480000, R.drawable.gangju, "体育馆",
				"钢铁大菊花", 367));
		infos.add(new Info(31.8941550000, 118.8322060000, R.drawable.jiaobiao, "焦廷标馆",
				"焦标", 386));
		infos.add(new Info(31.8935370000, 118.8300200000, R.drawable.jiaoxuelou, "教学楼",
				"教2", 734));
		infos.add(new Info(31.8924390000, 118.8247760000, R.drawable.jisuanjilou, "计算机楼",
				"计算机学院&软件学院", 325));
		infos.add(new Info(31.8935750000, 118.8256500000, R.drawable.liwenzheng, "李文正图书馆",
				"李文正网吧", 1456));
		infos.add(new Info(31.8887750000, 118.8325800000, R.drawable.sushe, "梅园生活区",
				"梅5~8", 499));
		infos.add(new Info(31.8882270000, 118.8247890000, R.drawable.xingzhenglou, "行政楼",
				"没有昵称辣~", 103));
	}

	public Info(double latitude, double longitude, int imgId, String name,
			String nickname, int zan)
	{
		this.latitude = latitude;
		this.longitude = longitude;
		this.imgId = imgId;
		this.name = name;
		this.nickname = nickname;
		this.zan = zan;
	}

	public double getLatitude()
	{
		return latitude;
	}

	public void setLatitude(double latitude)
	{
		this.latitude = latitude;
	}

	public double getLongitude()
	{
		return longitude;
	}

	public void setLongitude(double longitude)
	{
		this.longitude = longitude;
	}

	public int getImgId()
	{
		return imgId;
	}

	public void setImgId(int imgId)
	{
		this.imgId = imgId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getNickname()
	{
		return nickname;
	}

	public void setNickname(String distance)
	{
		this.nickname = nickname;
	}

	public int getZan()
	{
		return zan;
	}

	public void setZan(int zan)
	{
		this.zan = zan;
	}

}