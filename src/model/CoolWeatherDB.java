package model;

import java.util.ArrayList;
import java.util.List;

import db.CoolWeatherOpenHelper;
import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CoolWeatherDB {
	/**
	 * ���ݿ���
	 */
	public static final String DB_NAME = "cool_weather";

	/**
	 * ���ݰ汾
	 */
	public static final int VERSION = 1;
	private static CoolWeatherDB coolWeatherDB;
	private SQLiteDatabase db;

	/**
	 * �����ɷ���˽�л�
	 */
	private CoolWeatherDB(Context context) {
		CoolWeatherOpenHelper dbHelper = new CoolWeatherOpenHelper(context,
				DB_NAME, null, VERSION);
		db = dbHelper.getReadableDatabase();
	};

	/**
	 * ��ȡcoolweatherDBʵ��
	 */
	public synchronized static CoolWeatherDB getInstance(Context context) {
		if (coolWeatherDB == null) {
			coolWeatherDB = new CoolWeatherDB(context);
		}
		return coolWeatherDB;
	}

	/**
	 * ��provinceʵ���洢�����ݿ�
	 */
	public void saveProvince(Province province) {
		if (province != null) {
			ContentValues values = new ContentValues();
			values.put("province_name", province.getProvincename());
			values.put("province_code", province.getProvincecode());
			db.insert("Province", null, values);
		}
	};

	/**
	 * �����ݿ��ж�ȡȫ������ʡ�ݵ���Ϣ
	 */
	public List<Province> loadProvinces() {
		List<Province> list = new ArrayList<Province>();
		Cursor cursor = db
				.query("Province", null, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				Province province = new Province();
				province.setId(cursor.getInt(cursor.getColumnIndex("id")));
				province.setProvincecode(cursor.getString(cursor
						.getColumnIndex("province_code")));
				province.setProvincename(cursor.getString(cursor
						.getColumnIndex("province_name")));
				list.add(province);
			} while (cursor.moveToNext());
		}
		return list;
	};

	/**
	 * ��cityʵ���洢�����ݿ�
	 */
	public void saveCity(City city) {
		if (city != null) {
			ContentValues values = new ContentValues();
			values.put("city_name", city.getCityname());
			values.put("city_code", city.getCitycode());
			values.put("province_id", city.getProvinceId());
			db.insert("City", null, values);
		}
	}

	/**
	 * �����ݿ���� ĳʡ�����еĳ�����Ϣ
	 */
	public List<City> loadCities(int provinceId) {
		List<City> list = new ArrayList<City>();

		Cursor cursor = db.query("City", null, "province_id=?",
				new String[] { String.valueOf(provinceId) }, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				City city = new City();
				city.setCitycode(cursor.getString(cursor
						.getColumnIndex("city_code")));
				city.setCityname(cursor.getString(cursor
						.getColumnIndex("city_name")));
				city.setProvinceId(provinceId);
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				list.add(city);
			} while (cursor.moveToNext());
		}
		return list;
	}

	/**
	 * ��county��ʵ���洢�����ݿ�
	 */
	public void savecounty(County county) {
		if (county != null) {
			ContentValues values = new ContentValues();
			values.put("county_name", county.getCountyname());
			values.put("county_code", county.getCountycode());
			values.put("city_id", county.getCityId());
			db.insert("County", null, values);
		}

	}

	/**
	 * �����ݿ��ж�ȡĳ�����������ص���Ϣ
	 */
	public List<County> loadCounties(int cityId) {
		List<County> list = new ArrayList<County>();
		Cursor cursor = db.query("County", null, "city_id=?",
				new String[] { String.valueOf(cityId) }, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				County county = new County();
				county.setCityId(cityId);
				county.setCountycode(cursor.getString(cursor
						.getColumnIndex("county_code")));
				county.setCountyname(cursor.getString(cursor
						.getColumnIndex("county_name")));
				county.setId(cursor.getInt(cursor.getColumnIndex("id")));
				list.add(county);
			} while (cursor.moveToNext());
		}
		return list;
	}
	
}
