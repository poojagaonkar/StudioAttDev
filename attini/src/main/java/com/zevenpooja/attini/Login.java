package com.zevenpooja.attini;

import java.io.File;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import Attini.DAL.EndPoints;
import Attini.DAL.RegisterDevice;
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
			new AlertDialog.Builder(this)
		    .setTitle("Network error")
		    .setMessage("Please check your internet connection")
		    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which)
		        {
		            canContinue = false;
		            finish();
		        }
		     })
		    .show();
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
					
				   
					else
					{
					isUserSaved = true;
				
				
			        //deviceId
			         deviceId = GetDeviceId(); 
			      
			        //deviceName
			        deviceName = GetMachineName();
			        
			        //Parse JSON reponse to objects
			        JSONParser parser = new JSONParser();
			        Object register;
                    ProgressDialog dialog = new ProgressDialog(Login.this);
                    dialog.setMessage("Authenticating");
                        dialog.show();
						try
						{


							responseData= new RegisterDevice(Login.this).execute(RegisterDeviceUrl).get();
                            if(responseData == "")
                            {
                                DialogHelper.CreateNetworkAlert(Login.this, "Error", "Something went wrong");
                                dialog.dismiss();
								return;
                            }
                            else

							didItWork = true;

							register= parser.parse(responseData);
							org.json.simple.JSONObject registerDevice = (org.json.simple.JSONObject) register;

							//Convert Json objects to strings
							deviceAuthUrl  = (String) registerDevice.get("DeviceAuthUrl");
							hostUrlToken = (String) registerDevice.get("HostUrl");
							encodedAccountNameToken = (String) registerDevice.get("EncodedAccountName");
							endPointHost = (String) registerDevice.get("EndPointHost");




							//Save user details
						    sessionManager.createLoginSession(username, deviceAuthUrl, deviceId, endPointHost, deviceName, name	, encodedAccountNameToken, hostUrlToken,true, true);
                            dialog.dismiss();
							startMyActivity();
							finish();
							
						}
						catch (Exception e)
						{
                            DialogHelper.CreateNetworkAlert(Login.this, "Error", "Something went wrong");
                            dialog.dismiss();
						}


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
	public void startMyActivity()
	{
		// TODO Auto-generated method stub

		Intent in = new Intent(Login.this, Details1.class);

		in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		
		startActivity(in);
		finish();	

	}

}
