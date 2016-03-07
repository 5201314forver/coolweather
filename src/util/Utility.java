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
	 * 解析和处理服务器返回的省级数据
	 */
	public synchronized static boolean handleProvincesResponse(
			CoolWeatherDB coolWeatherDB, String response) {

		if (!TextUtils.isEmpty(response)) {
			String[] allProvince = response.split(",");// ,号一个数组
			if (allProvince != null && allProvince.length > 0) {
				for (String p : allProvince) { // 遍历allprovince数组
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
	 * 解析和处理服务器返回市里的数据
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
	 * 解析和处理返回县上的数据
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
	 * 解析服务器返回的JSON数据，并解析出来的数据存储到本地
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
	 * 将服务器返回的所有天气信息存储到SharedPreferences 文件中。
	 */
	public static void saveWeatherInfo(Context context, String cityname,
			String weathercode, String temp1, String temp2, String weatherDesp,
			String publishtime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);//时间的格式
		SharedPreferences.Editor editor = PreferenceManager
				.getDefaultSharedPreferences(context).edit();//创建sharedPreferences对象
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
