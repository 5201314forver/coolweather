package activity;

import service.AutoUpdateService;
import util.HttpCallbackListener;
import util.HttpUtil;
import util.Utility;

import com.coolweather.app.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WeatherActivity extends Activity implements OnClickListener {

	private LinearLayout weatherInfoLayout;
	/**
	 * ������ʶ������
	 */
	private TextView cityNameText;
	/**
	 * ������ʾ����ʱ��
	 */
	private TextView publishText;
	/**
	 * ������ʾ������������Ϣ
	 */
	private TextView weatherDespText;
	/**
	 * ������ʾ����1
	 */
	private TextView temp1Text;
	/**
	 * ������ʾ����2
	 */
	private TextView temp2Text;

	/**
	 * ������ʾ��ǰ����
	 */
	private TextView currentDateText;

	/**
	 * �л����а�ť
	 */
	private Button switchCity;
	/**
	 * ����������ť
	 */
	private Button refreshWeather;

	
	/**
	 * ��ȡͬ��ʧ�ܵ�weathercode
	 */
	String fweathercode;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		// ��ʼ���ؼ�
		weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout);
		cityNameText = (TextView) findViewById(R.id.city_name);
		publishText = (TextView) findViewById(R.id.publish_text);
		weatherDespText = (TextView) findViewById(R.id.weather_desp);
		temp1Text = (TextView) findViewById(R.id.temp1);
		temp2Text = (TextView) findViewById(R.id.temp2);
		currentDateText = (TextView) findViewById(R.id.current_data);
		// ��ť�¼�
		switchCity = (Button) findViewById(R.id.switch_city);
		refreshWeather = (Button) findViewById(R.id.refresh_weather);
		switchCity.setOnClickListener(this);
		refreshWeather.setOnClickListener(this);

		String countycode = getIntent().getStringExtra("county_code");
		if (!TextUtils.isEmpty(countycode)) {
			// ���ؼ�����ʱ��ȥ��ѯ����
			publishText.setText("ͬ����...");
			weatherInfoLayout.setVisibility(View.INVISIBLE);
			cityNameText.setVisibility(View.INVISIBLE);
			queryWeatherCode(countycode);
		} else {
			// û���ؼ�����ʱ��ֱ����ʾ��������
			showWeather();
		}
	}

	/**
	 * ��Ӧ��ť����¼�
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.switch_city:
			Intent intent = new Intent(this, ChooseAreaActivity.class);
			intent.putExtra("from_weather_activity", true);
			startActivity(intent);
			finish();
			break;
		case R.id.refresh_weather:
			publishText.setText(" ͬ����...");
//			SharedPreferences prefs = PreferenceManager
//					.getDefaultSharedPreferences(this);
//			String weathercode = prefs.getString("weather_code", "");
			
			if (!TextUtils.isEmpty(fweathercode)) {
				queryWeatherInfo(fweathercode);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * ��ѯ�ؼ���������Ӧ���������š�
	 */
	private void queryWeatherCode(String countycode) {
		String address = "http://www.weather.com.cn/data/list3/city"
				+ countycode + ".xml";//��������������190404|101190404�ش���|����������
		queryFromServer(address, "countycode");
	}

	/**
	 * ��ѯ�������Ŷ�Ӧ������
	 */
	private void queryWeatherInfo(String weatherCode) {
		String address = "http://www.weather.com.cn/data/cityinfo/"
				+ weatherCode + ".html";//���ص�������������Ϣ
		queryFromServer(address, "weatherCode");
	}

	/**
	 * ���ݴ���ĵ�ַ������ȥ���������ѯ�������Ż���������Ϣ��
	 */
	private void queryFromServer(final String address, final String type) {
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {

			@Override
			public void onFinish(String response) {
				// TODO Auto-generated method stub
				if ("countycode".equals(type)) {
					if (!TextUtils.isEmpty(response)) {
						String[] array = response.split("\\|");
						if (array != null && array.length == 2) {
							String weatherCode = array[1];
							queryWeatherInfo(weatherCode);
							fweathercode=weatherCode;
						}
					}
				} else if ("weatherCode".equals(type)) {
					Utility.handleWeatherResponse(WeatherActivity.this,
							response);
					// ����������������Ϣ
					runOnUiThread(new Runnable() {
						public void run() {
							showWeather();
						}
					});
				}
			}

			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						publishText.setText("ͬ��ʧ��");
						
					}
				});
			}
		});
	}

	/**
	 * ��SharedPreferences�ļ��ж�ȡ�洢��������Ϣ������ʾ�������ϡ�
	 */
	private void showWeather() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		cityNameText.setText(prefs.getString("city_name", ""));
		temp1Text.setText(prefs.getString("temp2", ""));
		temp2Text.setText(prefs.getString("temp1", ""));
		weatherDespText.setText(prefs.getString("weather_desp", ""));
		publishText.setText("����" + prefs.getString("publish_time", "") + "����");
		currentDateText.setText(prefs.getString("current_date", ""));
		weatherInfoLayout.setVisibility(View.VISIBLE);
		cityNameText.setVisibility(View.VISIBLE);
		
		//������̨����
		Intent intent = new Intent(this, AutoUpdateService.class);
		startService(intent);
		
	}

//	@Override
//	public void onBackPressed() {
//		// TODO Auto-generated method stub
//		
//	}
}