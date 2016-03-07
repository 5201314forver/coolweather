package util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.WeakHashMap;

import org.json.JSONException;
import org.json.JSONObject;

import model.City;
import model.CoolWeatherDB;
import model.County;
import model.Province;
import android.R.bool;
import android.R.integer;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

public class Utility {
	/**
	 * �����ʹ�����������ص�ʡ������
	 */
	public synchronized static boolean handleProvincesResponse(
			CoolWeatherDB coolWeatherDB, String response) {

		if (!TextUtils.isEmpty(response)) {
			String[] allProvince = response.split(",");// ,��һ������
			if (allProvince != null && allProvince.length > 0) {
				for (String p : allProvince) { // ����allprovince����
					String[] array = p.split("\\|");
					Province province = new Province();
					province.setProvincecode(array[0]);
					province.setProvincename(array[1]);
					coolWeatherDB.saveProvince(province);
				}
				return true;
			}
		}
		return false;
	};

	/**
	 * �����ʹ���������������������
	 */
	public static boolean handleCitiesResponse(CoolWeatherDB coolWeatherDB,
			String response, int provinceId) {
		if (!TextUtils.isEmpty(response)) {
			String[] allCities = response.split(",");
			if (allCities != null && allCities.length > 0) {
				for (String c : allCities) {
					String[] array = c.split("\\|");
					City city = new City();
					city.setCitycode(array[0]);
					city.setCityname(array[1]);
					city.setProvinceId(provinceId);
					coolWeatherDB.saveCity(city);
				}
				return true;
			}
		}
		return false;
	};

	/**
	 * �����ʹ��������ϵ�����
	 */

	public static boolean handleCountiesResponse(CoolWeatherDB coolWeatherDB,
			String response, int cityId) {
		if (!TextUtils.isEmpty(response)) {
			String[] allCounties = response.split(",");
			if (allCounties != null && allCounties.length > 0) {
				for (String c : allCounties) {
					String[] array = c.split("\\|");
					County county = new County();
					county.setCountycode(array[0]);
					county.setCountyname(array[1]);
					county.setCityId(cityId);
					coolWeatherDB.savecounty(county);
				}
				return true;
			}
		}
		return false;
	};

	/**
	 * �������������ص�JSON���ݣ����������������ݴ洢������
	 */
	public static void handleWeatherResponse(Context context, String response) {
		try {
			JSONObject jsonObject = new JSONObject(response);
			JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
			String cityname = weatherInfo.getString("city");
			String weathercode = weatherInfo.getString("cityid");
			String temp1 = weatherInfo.getString("temp1");
			String temp2 = weatherInfo.getString("temp2");
			String weatherDesp = weatherInfo.getString("weather");
			String publishtime = weatherInfo.getString("ptime");
			saveWeatherInfo(context, cityname, weathercode, temp1, temp2,
					weatherDesp, publishtime);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * �����������ص�����������Ϣ�洢��SharedPreferences �ļ��С�
	 */
	public static void saveWeatherInfo(Context context, String cityname,
			String weathercode, String temp1, String temp2, String weatherDesp,
			String publishtime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy��M��d��", Locale.CHINA);//ʱ��ĸ�ʽ
		SharedPreferences.Editor editor = PreferenceManager
				.getDefaultSharedPreferences(context).edit();//����sharedPreferences����
		editor.putBoolean("city_selected", true);
		editor.putString("city_name", cityname);
		editor.putString("weather_code",weathercode );
		editor.putString("temp1", temp1);
		editor.putString("temp2", temp2);
		editor.putString("weather_desp", weatherDesp);
		editor.putString("publish_time", publishtime);
		editor.putString("current_date", sdf.format(new Date()));
		editor.commit();
	}
}
