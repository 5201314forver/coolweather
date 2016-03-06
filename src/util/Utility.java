package util;

import model.City;
import model.CoolWeatherDB;
import model.County;
import model.Province;
import android.R.bool;
import android.R.integer;
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
}
