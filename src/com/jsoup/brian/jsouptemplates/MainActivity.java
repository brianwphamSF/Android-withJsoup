package com.jsoup.brian.jsouptemplates;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
	
	private class SearchResult extends AsyncTask<Void, Void, Void> {

		String resultTextFmt;
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mProgressDialog = new ProgressDialog(MainActivity.this);
			
			mProgressDialog.setTitle("WebMD");
			mProgressDialog.setMessage("Loading...");
			mProgressDialog.setIndeterminate(false);
			mProgressDialog.show();
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				org.jsoup.nodes.Document document = Jsoup.connect(URL1).get();
			
				Elements description2 = document.select("div[class=text_fmt]");
			
				Log.v("Data3", description2.toString());
				resultTextFmt = description2.toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			//super.onPostExecute(result);
			textView.setText(Html.fromHtml(resultTextFmt));
			mProgressDialog.dismiss();
		}

	}

	EditText editText;
	Button button;
	String data;
	
	private String URL;
	private String URL1;
	TextView textView;
	
	ProgressDialog mProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		editText = (EditText) findViewById(R.id.editText1);
		button = (Button) findViewById(R.id.button1);
		textView = (TextView) findViewById(R.id.textView1);
		
		textView.setMovementMethod(new ScrollingMovementMethod());
		
		URL = "http://www.webmd.com/search/search_results/default.aspx?query=";
		
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				data = editText.getText().toString();
				URL1 = URL + data;
				Log.v("URL", URL1);
				
				new SearchResult().execute();
			}
		});
	}
	
	public String getServerDataGET(String targetURL)
			throws ClientProtocolException, IOException {
		try {
			HttpClient client = new DefaultHttpClient();
			HttpUriRequest request = new HttpGet(targetURL);
			Log.v("link", targetURL);
			HttpResponse response = client.execute(request);
			String responseBody = "";
			HttpEntity entity = response.getEntity();
			
			if (entity != null) {
				responseBody = EntityUtils.toString(entity);
				Log.v("test", responseBody);
			}
			
			return responseBody;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
