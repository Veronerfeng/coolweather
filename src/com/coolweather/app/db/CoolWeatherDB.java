package com.coolweather.app.db;

import java.util.ArrayList;
import java.util.List;

import com.coolweather.app.model.City;
import com.coolweather.app.model.Country;
import com.coolweather.app.model.Province;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 封装常用的一些数据库操作
 */
public class CoolWeatherDB {
	//数据库名
	public static final String DB_NAME= "cool_weather";
	//数据库版本
	public static final int VERSION = 1;
	
	private static CoolWeatherDB coolWeatherDB;
	private SQLiteDatabase db;
	
	/*
	 * 构造方法私有化
	 */
	private CoolWeatherDB(Context context) {
		CoolWeatherOpenHelper dbHelper = new 
				CoolWeatherOpenHelper(context, DB_NAME, null, VERSION);
		db = dbHelper.getWritableDatabase();
	}
	
	/*
	 * 获取CoolWeatherDB实例(单例模式)
	 */
	public synchronized static CoolWeatherDB getInstance(Context context){
		if(coolWeatherDB == null){
			coolWeatherDB  = new CoolWeatherDB(context);
		}
		return coolWeatherDB;
	}
	
	/*
	 * 将province实例存到数据库中
	 */
	public void saveProvince(Province province){
		if(province != null){
			ContentValues values = new ContentValues();
			values.put("province_name", province.getProcinceName());
			values.put("province_code", province.getProvinceCode());
			db.insert("Province", null, values);
		}
	}
	
	/*
	 * 从数据库读取所有省份信息
	 */
	public List<Province> loadProvinces(){
		List<Province> list = new ArrayList<Province>();
		Cursor cursor  = db.query
				("Province", null, null, null, null, null, null);
		if(cursor.moveToFirst()){
			do{
				Province province = new Province();
				province.setId(cursor.getColumnIndex("id"));
				province.setProcinceName(cursor.getString(cursor.getColumnIndex("province_name")));
				province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
				list.add(province);
			}while(cursor.moveToNext());
		}
		
		return list;
	}
	
	
	/*
	 * 将city实例存储到数据库
	 */
	
	public void saveCity(City city){
		if(city != null){
			ContentValues values = new ContentValues();
			values.put("city_name", city.getCityName());
			values.put("city_code", city.getCityCode());
			values.put("province_id", city.getProvinceId());
			db.insert("City", null, values);
		}
	}
	
	/*
	 * 从数据库中读取某个省份下的所有城市信息
	 */
	public List<City> loadCities(int provinceId){
		List<City> list = new ArrayList<City>();
		Cursor cursor = db.query
				("City", null, "province_id=?",
						new String []{String.valueOf(provinceId)},
						null, null, null);
		if(cursor.moveToFirst()){
			do{
				City city = new City();
				city.setId(cursor.getColumnIndex("id"));
				city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
				city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
				city.setProvinceId(provinceId);
				
				list.add(city);
			}while(cursor.moveToNext());
		}
		
		return list;
	}
	
	/*
	 * 将country实例存到数据库中
	 */
	public void saveCountry(Country country){
		if(country != null){
			ContentValues values = new ContentValues();
			values.put("country_name", country.getCountryName());
			values.put("country_code", country.getCountryCode());
			values.put("city_id", country.getCityId());
			db.insert("Country", null, values);
		}
	}
	
	/*
	 * 从数据库中读取某城市下所有县的信息
	 */
	public List<Country> loadCountries(int cityId){
		List<Country> list = new ArrayList<Country>();
		Cursor cursor = db.query
				("Country", null, "city_id =?",
						new String []{String.valueOf(cityId)},
						null, null, null);
		if(cursor.moveToFirst()){
			do{
				Country country = new Country();
				country.setCountryName(cursor.getString(cursor.getColumnIndex("country_name")));
				country.setCountryCode(cursor.getString(cursor.getColumnIndex("country_code")));
				country.setCityId(cityId);
			}while(cursor.moveToNext());
		}
		return list;
	}
}