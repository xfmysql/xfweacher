package com.its.xfweacher.entity;

public class Weather {
	private int id;
	private String citycode;	

	private String weatherdate;
	private long addtime;
	private String tempnow;
	private String weatherstr;
	private String pm25;
	private String wind;
	private String windspeed;
	private String temperature;
	private String temperaturef;
	private String wtimg;
	
	public int getId(){
		return id;
	}
	public void setId(int _id){
		id = id;
	}
	public String getCitycode(){
		return citycode;
	}
	public void setCitycode(String _citycode){
		citycode = _citycode;
	}
	public String getWeatherdate(){
		return weatherdate;
	}	
	public void setWeatherdate(String _weatherdate){
		weatherdate = _weatherdate;
	}
	//
	public long getAddtime(){
		return addtime;
	}
	public void setAddtime(long _addtime){
		addtime = _addtime;
	}
	//
	public String getTempnow(){
		return tempnow;
	}	
	public void setTempnow(String _tempnow){
		tempnow = _tempnow;
	}
	//
	public String getPm25(){
		return pm25;
	}	
	public void setPm25(String _pm25){
		pm25 = _pm25;
	}
	//
	public String getWeatherstr(){
		return weatherstr;
	}	
	public void setWeatherstr(String _weatherstr){
		weatherstr = _weatherstr;
	}
	//
	public String getWind(){
		return wind;
	}	
	public void setWind(String _wind){
		wind = _wind;
	}
	//
	public String getWindspeed(){
		return wind;
	}	
	public void setWindspeed(String _windspeed){
		windspeed = _windspeed;
	}
	//
	public String getTemperature(){
		return temperature;
	}	
	public void setTemperature(String _temperature){
		temperature = _temperature;
	}
	//
	public String getTemperaturef(){
		return temperaturef;
	}	
	public void setTemperaturef(String _temperaturef){
		temperaturef = _temperaturef;
	}
	//
	public String getWtimg(){
		return wtimg;
	}	
	public void setWtimg(String _wtimg){
		wtimg = _wtimg;
	}
}
