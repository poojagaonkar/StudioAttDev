package com.zevenpooja.attini;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import Attini.DAL.EndPoints;

import Attini.Model.ConnectionDetector;
//import Attini.Model.UserFunctions;
import Utility.DialogHelper;
import Utility.SessionManagement;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zeven.attini.R;

public class Login extends Activity 
{
	//[ Class Variables
	private EditText editUser;
	private ImageButton btnLogin;
	private ImageView imgAttini;
	public static String username, responseData,deviceAuthUrl,hostUrlToken,encodedAccountNameToken,endPointHost ;
	public static String SavedData = "AttiniCommsUserData";
	private String AuthenticateDeviceEndPoint = EndPoints.AuthenticateDevice;
	private SharedPreferences saveEmail;
	private boolean canContinue = false;
	private String checkUsername;
	public static boolean hasLoggedOut = true;
	public static SharedPreferences settings; 
	private SessionManagement sessionManager;
	public ProgressDialog loginDialog;
	private String RegisterDeviceUrl = EndPoints.RegisterDevice;
	public static String deviceId = null;
	public static String deviceName;
	public static Boolean isUserSaved = false;
	boolean hasLoggedout = false;
	public static Context myContext;
    private  boolean isConnected;
	private String name;
	private ConnectionDetector cd;
	
	public static Context getMyContext()
	{
		return myContext;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);

        btnLogin =(ImageButton) findViewById(R.id.button2);
        final EditText editUser = (EditText)findViewById(R.id.editText1);
        final LinearLayout LoginBox = (LinearLayout) findViewById(R.id.LoginBox);





        if (android.os.Build.VERSION.SDK_INT > 9)
		{
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
		
		//Check internet connections

		cd = new ConnectionDetector(Login.this);
		if(cd.isConnectingToInternet()== false)
		{
			DialogHelper.CreateNetworkAlert(Login.this, "Network Error", "Check your internet connection");
		}
		else
			
		canContinue= true;
		myContext = getApplicationContext();

		sessionManager = new SessionManagement(Login.this);
		editUser.setTextColor(Color.BLACK);

		//Animation
        LoginBox.setVisibility(View.GONE);

        File f = new File("/data/data/com.zeven.attini/AttiniCommsUserDetails/.xml");



      if(sessionManager.isLoggedIn()==true)
      {
          //Go directly to main activity
    	  ConnectionDetector cd1 = new ConnectionDetector(Login.this);
			if(cd1.isConnectingToInternet()== false)
			{
                DialogHelper.CreateNetworkAlert(Login.this,"Network Error","Check your internet connection");
			}
			else {
                canContinue = true;
                startMyActivity();
                finish();
            }
      }
      else
      {
        Animation animTranslate  = AnimationUtils.loadAnimation(Login.this, R.anim.translate);
        animTranslate.setAnimationListener(new AnimationListener() 
        {

            @Override
            public void onAnimationStart(Animation arg0) { }

            @Override
            public void onAnimationRepeat(Animation arg0) { }

            @Override
            public void onAnimationEnd(Animation arg0) 
            {
            	
            	
            	LoginBox.setVisibility(View.VISIBLE);
    			Animation animFade  = AnimationUtils.loadAnimation(Login.this, R.anim.fade);
    			LoginBox.startAnimation(animFade);
            	}
            	
        });
        ImageView imgLogo = (ImageView) findViewById(R.id.imageView1);
        imgLogo.startAnimation(animTranslate);
        
       
        
        btnLogin.setOnClickListener(new OnClickListener() 
        {
			
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				  // username, deviceId, deviceName parameters
				
				ConnectionDetector cd = new ConnectionDetector(Login.this);
				if(cd.isConnectingToInternet()== false)
				{
                    DialogHelper.CreateNetworkAlert(Login.this, "Network Error", "Check your internet connection");
				}
				else
					
				canContinue= true;
				boolean didItWork =true;
				username = editUser.getText().toString().trim();
			
				if(username.length()==0)
				{

						DialogHelper.CreateNetworkAlert(Login.this, "Error", "Invalid credentials");

			    }
				else {
					isUserSaved = true;
					//deviceId
					deviceId = GetDeviceId();

					//deviceName
					deviceName = GetMachineName();
					new RegisterDevice(Login.this).execute(RegisterDeviceUrl);
				}
			
			}

			private String GetMachineName()
			{
				// TODO Auto-generated method stubm
				String deviceName = android.os.Build.MODEL;
				return deviceName;
			}

			private String GetDeviceId()
			{
				// TODO Auto-generated method stub
					String androidId = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
					
					String telephoneId = "";
					String teleSim = "";
					TelephonyManager telMan = (TelephonyManager)getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
					if(telMan!=null)
					{
						if(telMan.getDeviceId()!=null)
						{
							telephoneId = telMan.getDeviceId();
						}
						if(telMan.getSimSerialNumber()!=null)
						{
							teleSim = telMan.getSimSerialNumber();
						}
					}
				    UUID deviceUuid = new UUID(androidId.hashCode(), ((long)telephoneId.hashCode()<<32|teleSim.hashCode()));
				    
					return deviceUuid.toString();
			}
		});
      }



	}
	private void startMyActivity()
	{
		Intent in = new Intent(Login.this, Details1.class);
		in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(in);
		finish();
	}

	public class RegisterDevice extends AsyncTask<String, String, String>
	{
		Login login = new Login();
		ProgressDialog dialog;
		Context mContext;
		private boolean didItWork;

		Object register;

		Activity mActivity;

		public RegisterDevice(Login login2)
		{
			// TODO Auto-generated constructor stub
			this.mContext = login2;
			this.mActivity = login2;
		}

		@Override
		protected void onPreExecute()
		{
			// TODO Auto-generated method stub
			super.onPreExecute();

			ConnectionDetector cd = new ConnectionDetector(mContext);
			if(cd.isConnectingToInternet()== false)
			{
				DialogHelper.CreateNetworkAlert(Login.this, "Network Error", "Check your internet connection");
			}
			dialog  = new ProgressDialog(Login.this);
			dialog.setMessage("Authenticating");
			dialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub

			String Url = params[0];
			HttpResponse response =null;
			String resultString = "";
			String responseBody = "" ;
			// Creating HTTP client
			HttpClient httpClient = new DefaultHttpClient();
			// Creating HTTP Post
			HttpPost httpPost = new HttpPost(Url);

			// Building post parameters
			// key and value pair
			List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(3);
			nameValuePair.add(new BasicNameValuePair("DeviceId", Login.deviceId));
			nameValuePair.add(new BasicNameValuePair("Name",Login.deviceName));
			nameValuePair.add(new BasicNameValuePair("EncodedAccountName",Login.username));

			// Url Encoding the POST parameters
			try
			{
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
			}
			catch (UnsupportedEncodingException e)
			{
				// writing error to Log
				e.printStackTrace();
			}

			// Making HTTP Request
			try
			{
				response = httpClient.execute(httpPost);
				if(response.getStatusLine().getStatusCode()==200)
				{
					HttpEntity entity = response.getEntity();


					if(entity!=null)
					{
						responseBody = EntityUtils.toString(entity);
					}

				}
				else
				{
					responseBody = "InvalidCredentials";
				}
				// writing response to log

			}
			catch (ClientProtocolException e)
			{
				// writing exception to log
				e.printStackTrace();
			}
			catch (IOException e)
			{
				// writing exception to log
				e.printStackTrace();

			}

			return resultString = responseBody;
		}

		@Override
		protected void onPostExecute(String result)
		{

			JSONParser parser = new JSONParser();
			super.onPostExecute(result);
			if(dialog!=null && dialog.isShowing())
			{
				try {
					if(result == "" || result.matches("InvalidCredentials"))
					{
						DialogHelper.CreateNetworkAlert(mActivity, "Error", "Invalid credentials");
						dialog.dismiss();

					}
					else {
						didItWork = true;

						register = parser.parse(result);
						org.json.simple.JSONObject registerDevice = (org.json.simple.JSONObject) register;

						//Convert Json objects to strings
						deviceAuthUrl = (String) registerDevice.get("DeviceAuthUrl");
						hostUrlToken = (String) registerDevice.get("HostUrl");
						encodedAccountNameToken = (String) registerDevice.get("EncodedAccountName");
						endPointHost = (String) registerDevice.get("EndPointHost");


						sessionManager.createLoginSession(username, deviceAuthUrl, deviceId, endPointHost, deviceName, name, encodedAccountNameToken, hostUrlToken, true, true);
						dialog.dismiss();
						startMyActivity();

					}
				}
				catch (Exception e)
				{
					dialog.dismiss();
					DialogHelper.CreateNetworkAlert(Login.this, "Error", "Something went wrong");
				}
			}


		}

	}

}
