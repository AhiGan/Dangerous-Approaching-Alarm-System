package entity;

import java.io.Serializable;

public class MyLatLng implements Serializable{
	private double longitude;
	private double latitude;
	
	public MyLatLng()
	{
		this.longitude = 0;
		this.latitude = 0;
	}
	
	public MyLatLng(double lat , double longitude)
	{
		this.longitude = longitude;
		this.latitude = lat;
	}
	
	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	
}
