package org.me;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * @author Phil Barrett
 * 
 *         Android Rest Client Example UI will show a text field where you can
 *         enter a url that points to a rest service NOTE: if you are working
 *         locally localhost should not be used but your real ip address should
 *         be used
 * 
 */
public class AndroidRestClientActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

	}

	public void RestCall(View view) {
		/**
		 * Invoked from the button click see res/layout/main.xml <Button
		 * android:id="@+id/button1" android:layout_width="wrap_content"
		 * android:layout_height="wrap_content" android:onClick="RestCall"
		 * android:clickable="true" android:text="Rest Call" />
		 */

		new RestCall().execute();

	}

	private class RestCall extends AsyncTask<Void, Void, String> {
		/**
		 * You need an async task as this wont work in the main ui thread
		 */
		// a dialog to show that something is going on
		ProgressDialog progressDialog;

		protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException {
			// get all the content
			InputStream in = entity.getContent();
			StringBuffer out = new StringBuffer();
			int n = 1;
			while (n > 0) {
				byte[] b = new byte[4096];
				n = in.read(b);
				if (n > 0)
					out.append(new String(b, 0, n));
			}
			return out.toString();
		}

		@Override
		protected String doInBackground(Void... params) {
			// The http request
			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			// the url that is typed in
			EditText etURL = (EditText) findViewById(R.id.editText2);
			HttpGet httpGet = new HttpGet(etURL.getText().toString());

			String text = null;
			try {
				HttpResponse response = httpClient.execute(httpGet, localContext);
				HttpEntity entity = response.getEntity();
				text = getASCIIContentFromEntity(entity);
			} catch (Exception e) {
				return e.getLocalizedMessage();
			}
			return text;
		}

		protected void onPostExecute(String results) {
			if (results != null) {
				EditText et = (EditText) findViewById(R.id.editText1);

				String someString = null;
				JSONObject jsonObj = null;
				try {
					jsonObj = new JSONObject(results);
					// if you want to pass the Json you will have to code the
					// parsing...
					// someString =
					// jsonObject.getString("fill your string in here");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// et.setText(results + "JSON String "+ jsonObj.toString() );
			}
			Button b = (Button) findViewById(R.id.button1);
			b.setClickable(true);
			progressDialog.dismiss();
		}

		@Override
		protected void onPreExecute() {
			progressDialog = ProgressDialog.show(
					AndroidRestClientActivity.this,
					"Progress Dialog Title Text", "Process Description Text",
					true);

			// do initialization of required objects objects here
		};
	}

}