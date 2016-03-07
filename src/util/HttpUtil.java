package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpConnection;
import org.xml.sax.InputSource;

import android.util.Log;

/**
 * �������Ľ���
 * 
 * @author Administrator
 * 
 */
public class HttpUtil {
	public static void sendHttpRequest(final String address,
			final HttpCallbackListener listener) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpURLConnection connection = null;
				try {
					URL url = new URL(address);
					connection = (HttpURLConnection) url.openConnection();// ������������
					connection.setRequestMethod("GET");// �ӷ�������������Ϣ
					connection.setConnectTimeout(8000);
					connection.setReadTimeout(8000);
					InputStream in = connection.getInputStream();// �ӷ����������õķ���������
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(in));// ����������ȡ
					StringBuilder response = new StringBuilder();
					String line;
					while ((line = reader.readLine()) != null) {
						response.append(line);
					}

					if (listener != null) {
						// �ص�onFinish()����
						listener.onFinish(response.toString());
					}
				} catch (IOException e) {

					// �ص�onError()����
					listener.onError(e);
				} finally {
					if (connection != null) {
						connection.disconnect();
					}
				}
			}
		}) {
		}.start();
	}
}
