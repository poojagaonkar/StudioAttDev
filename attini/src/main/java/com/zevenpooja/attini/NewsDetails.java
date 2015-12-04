package com.zevenpooja.attini;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import Attini.DAL.EndPoints;
import Attini.Model.ConnectionDetector;
import Utility.CommentsAdapter;
import Utility.DialogHelper;
import Utility.HorizontalListView;
import Utility.HtmHelper;
import Utility.ListViewInScrollViewHeight;
import Utility.WaitProgressFragment;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Picture;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.zeven.attini.R;

public class NewsDetails extends Activity implements  OnClickListener
{

	WebView txtNewsBody;
	TextView txtNewsTitle,txtPubDate,txtPostComments,txtPublisherName;//,txtNewsBody;
	ImageView imgNewsImage;
	String newUrl, newsTitle, newsBody, newsImage, newsViews,newsComments,pubDate,articleGuid, fullName,publisherName,newsSourceTitle,newsLikes;
	HashMap<String, String> data; 
	HashMap<String, String> urls;
	String oldTxtViews,oldTxtComments;
	private TextView txtNewsComments, txtNewsCatagory;
	private View viewDateView, viewNewsCat;
	private String postedComment;
	private ImageView imgThumbnail;
	private TextView txtNewsView,txtNewsView2, txtNewsComment2, txtNewsLikes, txtNewsLikes2, txtMainTitle;
	private ImageButton btnView, btnComments, btnLikes,btnPostLikes;
	private LinearLayout layoutComments;
	private ImageView btnComms;
	String spHostUrl, encodedAccountName, deviceAuthKey, newsSourceId, Id; 
	private ImageButton btnAddComms, btnLike;
	private ScrollView scrollNews;
	private CommentsAdapter comAdapter;
	private ProgressDialog myDialog;
	public static ArrayList<HashMap<String, String>> myCommentsList = new ArrayList<HashMap<String,String>>();
	public HashMap<String, String> map ;
	private ListAdapter itemsAdapter;
	private ListView commentsList;
	private String newsId, mycolor, tags;
	private WaitProgressFragment diaFrag;
	private HorizontalListView ListHorizontal;
	private ImageButton btnBackArticle;
	AlertDialog alert;
	public static final String  AUTHORDISPLAYNAME ="AuthorDisplayName";
	public static final String  DATEUPDATED=" DateUpdated";
	public static final String  TEXT="Text";
	public static final String  BODY ="Body" ;
	private AlertDialog.Builder builder;
	private ImageView imgTag;
	int numberOfViews;
	private Picture p = null;
	private String[] tagsArray = null;
	private ProgressDialog pd;
	private ProgressBar progPostComments;
	private boolean canContinue;
	public String CSS_URI = "";
	private MediaPlayer playSound;
	ArrayList<HashMap<String, String>> getList = new ArrayList<HashMap<String,String>>();
	private String bodyText;
	private SharedPreferences prefs;
	private SharedPreferences.Editor editor;
	private boolean isLiked;


	public NewsDetails(ProgressDialog myDialog) 
	{
		// TODO Auto-generated constructor stub
		this.myDialog = myDialog;
	}


	public NewsDetails()
	{

	}


	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();


		myCommentsList.clear();
		GetComments();


	}





	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.newsdetails);


		/*
		 * Component initialization
		 */

		// TxtViews
		txtNewsTitle = (TextView)findViewById(R.id.txtNewsTitle);
		txtPubDate = (TextView)findViewById(R.id.txtPubDate);
		txtNewsView = (TextView)findViewById(R.id.txtNewsViews);
		txtNewsComments = (TextView)findViewById(R.id.txtNewsComments);
		txtPublisherName = (TextView)findViewById(R.id.txtPublisher);
		txtNewsCatagory = (TextView)findViewById(R.id.txtCatagory);
		txtNewsLikes = (TextView)findViewById(R.id.txtNewsLikes);
		txtNewsLikes2 = (TextView)findViewById(R.id.txtNewsLikes2);
		txtNewsView2 = (TextView)findViewById(R.id.txtNewsViews2);
		txtNewsComment2 = (TextView)findViewById(R.id.txtNewsComments2);
		txtMainTitle = (TextView)findViewById(R.id.txtNewsArticleMainTitle);



		//Buttons and ImageButtons
		btnAddComms = (ImageButton)findViewById(R.id.btnAddComms);
		btnPostLikes = (ImageButton)findViewById(R.id.btnAddLikes);
		btnView = (ImageButton)findViewById(R.id.btnViews);
		btnComments = (ImageButton)findViewById(R.id.btnComments);
		btnComms = (ImageView)findViewById(R.id.btnComms);
		btnLike = (ImageButton)findViewById(R.id.btnLikes);
		btnBackArticle = (ImageButton)findViewById(R.id.btnBackArticle);

		//Others
		txtNewsBody = (WebView)findViewById(R.id.txtNewsBody);
		commentsList = (ListView)findViewById(android.R.id.list);
		ListHorizontal = (HorizontalListView)findViewById(R.id.horizontallistview);
		scrollNews = (ScrollView)findViewById(R.id.scrollNews);
		viewDateView = (View)findViewById(R.id.viewDate);
		viewNewsCat = (View)findViewById(R.id.viewNewsCatagory);




		//Make comments list non clickable
		commentsList.setClickable(false);




		//Dismissing dialog box
		if(myDialog!=null && myDialog.isShowing())
		{
			myDialog.dismiss();
		}

		//Getting HashMap values from HomeFragment
		Intent in = getIntent();
		
		if(in == null)
		{
			ConnectionDetector cd = new ConnectionDetector(NewsDetails.this);
			if(cd.isConnectingToInternet()== false)
			{
				DialogHelper.CreateNetworkAlert(NewsDetails.this, "Network Error", "Check your internet connection");
			}
		}
		else
				
			canContinue= true;
			
		

		newsBody =in.getStringExtra("NewsBody");
		newsTitle = in.getStringExtra("Title");
		newsViews =in.getStringExtra("NewsViews");
		newsComments = in.getStringExtra("NewsComments");
		pubDate = in.getStringExtra("PubDate");
		publisherName =in.getStringExtra("PubName");
		mycolor =in.getStringExtra("Color");
		newsImage =in.getStringExtra("NewsImage");
		newsSourceTitle = in.getStringExtra("NewsSourceTitle");
		newsSourceId = in.getStringExtra("NewsSourceId");
		articleGuid = in.getStringExtra("ArticleGuid");
		newsId = in.getStringExtra("NewsId");
		newsLikes = in.getStringExtra("NewsLikes");
		tags = in.getStringExtra("Tags");
		isLiked = Boolean.parseBoolean(in.getStringExtra("IsLiked"));
		spHostUrl = in.getStringExtra("SPHostUrl");
		encodedAccountName = in.getStringExtra("EncodedAccountName");
		deviceAuthKey = in.getStringExtra("DeviceAuthKey");
		fullName = in.getStringExtra("FullName");

		// Applying date
		SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		SimpleDateFormat myFormat = new SimpleDateFormat("MMM dd, yy");


		try {
			txtPubDate.setText(myFormat.format(fromUser.parse(pubDate)));
		} catch (ParseException e) {

			txtPubDate.setText("");
		}


		//Adding colors
		viewDateView.setBackgroundColor(Color.parseColor(mycolor));
		viewNewsCat.setBackgroundColor(Color.parseColor(mycolor));


		//set up Tags

		List<String> splitArray = Arrays.asList(tags.split(","));
		tagsArray = splitArray.toArray(new String[splitArray.size()]);
		ListHorizontal.setAdapter(mHoriAdapter);
		ListHorizontal.setClickable(false);



		//Add likes
		txtNewsLikes.setText(newsLikes);
		txtNewsLikes2.setText(newsLikes);


		//Add title
		txtNewsCatagory.setText(newsSourceTitle);

		// Component definitions
		btnView.setBackgroundColor(Color.TRANSPARENT);
		btnComments.setBackgroundColor(Color.TRANSPARENT);



		// Add title 
		txtNewsTitle.setText(newsTitle);


		//Add author name

		txtPublisherName.setText(publisherName);


		//Add Body
		WebSettings webSettings =  txtNewsBody.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setDefaultFontSize(16);

		webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);

		bodyText = CSS_URI + "<div>" + newsBody+ "</div>";
		bodyText = HtmHelper.BuildHtml(bodyText);

		txtNewsBody.loadData(bodyText, "text/html; charset=utf-8", "UTF-8");



		if(newsBody.contains(newsImage))
		{
			p =null;
		}
		else
		{ 
			p = txtNewsBody.capturePicture();
		}


		//Get Views and comments


		txtNewsView.setText(newsViews);
		txtNewsView2.setText(newsViews);



		txtNewsComments.setText(newsComments);
		txtNewsComment2.setText(newsComments);




		
	
		btnAddComms.setOnClickListener(this);
		if(isLiked)
		{
			btnPostLikes.setImageResource(R.drawable.like_button_selected);
			btnPostLikes.setPadding(0, 3, 0, 0);
			//btnPostLikes.setEnabled(false);
			btnPostLikes.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
				PostUnlike postUnlike = new PostUnlike();
					postUnlike.execute(EndPoints.UpdateUnlikeUrl);
				}
			});
		}
		else {
			btnPostLikes.setOnClickListener(this);
		}
		btnBackArticle.setOnClickListener(this);
		
		 Typeface typaFace = Typeface.createFromAsset(getAssets(), "RobotoSlab-Bold.ttf");
	      txtMainTitle.setTypeface(typaFace);

	}

	@Override
	protected void onPause() {
		super.onPause();
		try {
			Class.forName("android.webkit.WebView")
					.getMethod("onPause", (Class[]) null)
					.invoke(txtNewsBody, (Object[]) null);

		} catch(Exception cnfe) {

		}
	}

	// For Tags
	private BaseAdapter mHoriAdapter =  new BaseAdapter() {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) 
		{
			// TODO Auto-generated method stub
			View retval = LayoutInflater.from(parent.getContext()).inflate(R.layout.tags_row, null);
			TextView title = (TextView) retval.findViewById(R.id.textTag);
			imgTag = (ImageView)retval.findViewById(R.id.imageTag);

			if(tagsArray.length>1)
			{
				imgTag.setVisibility(View.VISIBLE);
			}
			else
				imgTag.setVisibility(View.GONE);
			title.setText(tagsArray[position]+" ");

			return retval;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return tagsArray.length;
		}

	};
	private void GetComments() 
	{
		// TODO Auto-generated method stub
		scrollNews.scrollTo(0, scrollNews.getBottom());
		
	
		commentsList.setVisibility(View.VISIBLE);

		String items;

		String getRequestForComments = EndPoints.GetCommentsUrl+"?articleId="+newsId+"&deviceAuthKey="+deviceAuthKey+"&encodedAccountName="+encodedAccountName+"&spHostUrl="+spHostUrl;
		new FetchItems().execute(getRequestForComments);


	}
	/*
	 * View Updatation WebAPI call
	 */
	private class updateViews extends AsyncTask<String , String, Void>
	{

		@Override
		protected Void doInBackground(String... params)
		{
			// TODO Auto-generated method stub
			HttpResponse response =null;
			String resultString = "";
			String myResponseBody = "" ;
			// Creating HTTP client
			HttpClient httpClient = new DefaultHttpClient();
			// Creating HTTP Post
			HttpPost request = new HttpPost(params[0]);


			List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(6);
			nameValuePair.add(new BasicNameValuePair("spHostUrl", spHostUrl));
			nameValuePair.add(new BasicNameValuePair("encodedAccountName",encodedAccountName));
			nameValuePair.add(new BasicNameValuePair("deviceAuthKey", deviceAuthKey));
			nameValuePair.add(new BasicNameValuePair("newsSourceId", newsSourceId));
			nameValuePair.add(new BasicNameValuePair("articleGuid", articleGuid));
			nameValuePair.add(new BasicNameValuePair("articleId", newsId));




			try 
			{
				request.setEntity(new UrlEncodedFormEntity(nameValuePair));
				response = httpClient.execute(request);
				if(response.getStatusLine().getStatusCode()== 200)
				{
					HttpEntity entity = response.getEntity();
					if (entity != null)
					{

						InputStream inputStream = entity.getContent();
						myResponseBody = convertToString(inputStream);
					}
				}
			}
			catch(Exception e)
			{
			}
			return null;
		}
		private String convertToString(InputStream inputStream) 
		{
			// TODO Auto-generated method stub
			StringBuffer string = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			String line;
			try 
			{
				while ((line = reader.readLine()) != null) 
				{
					string.append(line + "\n");
				}
			} 
			catch (IOException e) {}
			return string.toString();
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			numberOfViews+=1;
		}
	}
	@Override
	public void onBackPressed() 
	{
		// TODO Auto-generated method stub
		super.onBackPressed();
		if(Home.myDialog!=null && Home.myDialog.isShowing())
		{
			Home.myDialog.dismiss();
		}
		finish();

		String viewUpdateUrl = EndPoints.UpdateViewsUrl;//+"?spHostUrl="+spHostUrl+"&encodedAccountName="+encodedAccountName+"&deviceAuthKey="+deviceAuthKey+"&newsSourceId="+newsSourceId+"&articleGuid="+articleGuid+"&articleId="+newsId;
		new updateViews().execute(viewUpdateUrl);
		
		myCommentsList.clear();
		this.finish();

	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		myCommentsList.clear();
	}
	private Bitmap DownloadImage(String URL) 
	{
		// TODO Auto-generated method stub
		Bitmap bitmap = null;
		InputStream in = null;
		try
		{
			try 
			{
				in = new OpenHttpConnection().execute(URL).get();
				bitmap = BitmapFactory.decodeStream(in);
				in.close();
			} 
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			catch (ExecutionException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} 
		catch (IOException e1) 
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return bitmap;
	}
	private class OpenHttpConnection extends AsyncTask<String, String, InputStream>
	{
		// TODO Auto-generated method stub
		ProgressDialog dialog;
		@Override
		protected InputStream doInBackground(String... params)
		{
			// TODO Auto-generated method stub
			HttpClient httpClient = new DefaultHttpClient();
			// Creating HTTP Post
			HttpGet request = new HttpGet(params[0]);
			InputStream inputStream = null;

			HttpResponse response;
			try 
			{
				response = httpClient.execute(request);
				if(response.getStatusLine().getStatusCode()== 200)
				{
					HttpEntity entity = response.getEntity();
					if (entity != null)
					{
						BufferedHttpEntity bufEntity = new BufferedHttpEntity(entity);
						inputStream =bufEntity.getContent();
					}
				}
			}
			catch(Exception e)
			{

			}
			return inputStream;
		}

		@Override
		protected void onPostExecute(InputStream result) 
		{
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(dialog!=null && dialog.isShowing())
			{
				dialog.dismiss();
			}
		}

		@Override
		protected void onPreExecute()
		{
			// TODO Auto-generated method stub
			super.onPreExecute();

		}

	}

	
	private class FetchItems extends AsyncTask<String,String, Void> 
	{
		// TODO Auto-generated method stub
		

		@Override
		protected Void doInBackground(String... params)
		{
			// TODO Auto-generated method stub
			HttpResponse response =null;
			String resultString = "";
			String myResponseBody = "" ;
			// Creating HTTP client
			HttpClient httpClient = new DefaultHttpClient();
			// Creating HTTP Post
			HttpGet request = new HttpGet(params[0]);


			try 
			{
				response = httpClient.execute(request);
				if(response.getStatusLine().getStatusCode()== 200)
				{
					HttpEntity entity = response.getEntity();
					if (entity != null)
					{

						InputStream inputStream = entity.getContent();
						myResponseBody = convertToString(inputStream);

						try {
							JSONObject jsonResponse = new JSONObject(myResponseBody);
							JSONArray mtUsers = jsonResponse.getJSONArray("ListOfComments");
							for (int i = 0; i < mtUsers.length(); i++)
							{
								JSONObject menuObject = mtUsers.getJSONObject(i);

								String authorName= menuObject.getString("AuthorDisplayName");
								String date= menuObject.getString("DateCreated");
								//String thumbnail= menuObject.getString("ThumbnailPath");
								String commentText = menuObject.getString("Text");


								map = new HashMap<String,String>();
								map.put(AUTHORDISPLAYNAME, authorName);
								map.put(DATEUPDATED, date);
								map.put(TEXT, commentText);

								myCommentsList.add(map);

							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Collections.reverse(myCommentsList);
						getList = myCommentsList;
					}
				}
				else
				{

				}
			}
			catch(Exception e)
			{
			}
			return null;

		}





		@Override
		protected void onPostExecute(Void result) 
		{
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if(pd!=null && pd.isShowing())
				pd.dismiss();

			itemsAdapter = (ListAdapter) new CommentsAdapter(NewsDetails.this, getList);
			//CommentsAdapter mAdapter = new CommentsAdapter(NewsDetails.this, getList);
			//mAdapter.addData(getList);
			commentsList.setAdapter(itemsAdapter);
			ListViewInScrollViewHeight.setListViewHeightBasedOnChildren(commentsList);
			commentsList.invalidate();
			commentsList.refreshDrawableState();
			commentsList.post(new Runnable() 
			{

				@Override
				public void run() 
				{
					// TODO Auto-generated method stub
					commentsList.setSelection(itemsAdapter.getCount()-1);

				}
			});
		}





		@Override
		protected void onPreExecute() 
		{
			// TODO Auto-generated method stub
			super.onPreExecute();
			ConnectionDetector cd = new ConnectionDetector(NewsDetails.this);
			if(cd.isConnectingToInternet()== false)
			{
				new AlertDialog.Builder(NewsDetails.this)
			    .setTitle("Network error")
			    .setMessage("Please check your internet connection")
			    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) 
			        { 
			            finish();
			        }
			     })
			    .show();
			}

		}
		private String convertToString(InputStream inputStream) 
		{
			// TODO Auto-generated method stub
			StringBuffer string = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			String line;
			try 
			{
				while ((line = reader.readLine()) != null) 
				{
					string.append(line + "\n");
				}
			} 
			catch (IOException e) {}
			return string.toString();
		}


	}





	private class PostComment extends AsyncTask<String, String, Integer>
	{

	

		@Override
		protected Integer doInBackground(String... params)
		{
			// TODO Auto-generated method stub
			int responseCode= 0;
			HttpResponse response =null;
			String resultString = "";
			String myResponseBody = "" ;
			// Creating HTTP client
			HttpClient httpClient = new DefaultHttpClient();
			// Creating HTTP Post
			HttpPost request = new HttpPost(params[0]);

			List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(8);
			nameValuePair.add(new BasicNameValuePair("spHostUrl", spHostUrl));
			nameValuePair.add(new BasicNameValuePair("encodedAccountName", encodedAccountName));
			nameValuePair.add(new BasicNameValuePair("deviceAuthKey", deviceAuthKey));
			nameValuePair.add(new BasicNameValuePair("newsSourceId", newsSourceId));
			nameValuePair.add(new BasicNameValuePair("articleGuid", articleGuid));
			nameValuePair.add(new BasicNameValuePair("articleId", newsId));
			nameValuePair.add(new BasicNameValuePair("commentText", postedComment));
			nameValuePair.add(new BasicNameValuePair("authorDisplayName", fullName));



			try 
			{
				request.setEntity(new UrlEncodedFormEntity(nameValuePair));
				response = httpClient.execute(request);
				if(response.getStatusLine().getStatusCode()== 200)
				{
					HttpEntity entity = response.getEntity();
					if (entity != null)
					{

						InputStream inputStream = entity.getContent();
						myResponseBody = convertToString(inputStream);
						responseCode = 0;
					}
				}
				else
				{
					responseCode = 1;
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				responseCode = 1;
			}
			return responseCode;
		}

		private String convertToString(InputStream inputStream) 
		{
			// TODO Auto-generated method stub
			StringBuffer string = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			String line;
			try 
			{
				while ((line = reader.readLine()) != null) 
				{
					string.append(line + "\n");
				}
			} 
			catch (IOException e) {}
			return string.toString();
		}

		@Override
		protected void onPostExecute(Integer code) {
			super.onPostExecute(code);
			switch (code)
			{
				case 0:
					Toast.makeText(NewsDetails.this, "Comment posted",Toast.LENGTH_LONG).show();
					break;
				case 1:
					DialogHelper.CreateNetworkAlert(NewsDetails.this, "Error", "Could not post comment");

					break;
			}
		}
	}
	private class PostLikes extends AsyncTask<String, String, String>
	{
		int flag = 1;
		@Override
		protected String doInBackground(String... params)
		{
			// TODO Auto-generated method stub

			HttpResponse response =null;
			String resultString = "";
			String myResponseBody = "" ;
			// Creating HTTP client
			HttpClient httpClient = new DefaultHttpClient();
			// Creating HTTP Post
			HttpPost request = new HttpPost(params[0]);

			List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(8);
			nameValuePair.add(new BasicNameValuePair("spHostUrl", spHostUrl));
			nameValuePair.add(new BasicNameValuePair("encodedAccountName",encodedAccountName));
			nameValuePair.add(new BasicNameValuePair("deviceAuthKey", deviceAuthKey));
			nameValuePair.add(new BasicNameValuePair("newsSourceId", newsSourceId));
			nameValuePair.add(new BasicNameValuePair("articleGuid", articleGuid));
			nameValuePair.add(new BasicNameValuePair("articleId", newsId));




			try 
			{
				request.setEntity(new UrlEncodedFormEntity(nameValuePair));
				response = httpClient.execute(request);
				if(response.getStatusLine().getStatusCode()== 200)
				{
					HttpEntity entity = response.getEntity();
					if (entity != null)
					{

						InputStream inputStream = entity.getContent();
						resultString = "Success";

					}
				}
				else
				{
					resultString = "Fail";
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			return resultString;
		}

		@Override
		protected void onPostExecute(String s) {
			super.onPostExecute(s);
			if(s.matches("Success")) {

				btnPostLikes.setImageResource(R.drawable.like_button_selected);
				btnPostLikes.setPadding(0, 3, 0, 0);
				btnPostLikes.setEnabled(false);
			}
			else
			{
				Toast.makeText(NewsDetails.this,"Something went wrong",Toast.LENGTH_LONG).show();
			}

		}
	}

	private class PostUnlike extends AsyncTask<String, String, String>
	{
		int flag = 1;
		@Override
		protected String doInBackground(String... params)
		{
			// TODO Auto-generated method stub

			HttpResponse response =null;
			String resultString = "";
			String url = params[0]+"?deviceAuthKey="+deviceAuthKey+"&encodedAccountName="+encodedAccountName+"&spHostUrl="+spHostUrl+"&articleId="+newsId ;


			// Creating HTTP client
			HttpClient httpClient = new DefaultHttpClient();
			// Creating HTTP Post
			HttpDelete request = new HttpDelete(url);

			try
			{
				response = httpClient.execute(request);
				if(response.getStatusLine().getStatusCode()== 200)
				{
					HttpEntity entity = response.getEntity();
					if (entity != null)
					{

						InputStream inputStream = entity.getContent();
						resultString = "Success";

					}
				}
				else
				{
					resultString = "Fail";
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			return resultString;
		}

		@Override
		protected void onPostExecute(String s) {
			super.onPostExecute(s);
			if(s.matches("Success")) {

				btnPostLikes.setImageResource(R.drawable.like_button);
				btnPostLikes.setPadding(0, 3, 0, 0);

			}
			else
			{
				Toast.makeText(NewsDetails.this,"Something went wrong",Toast.LENGTH_LONG).show();
			}

		}
	}


	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		//finish();
		switch (v.getId())
		{
		case R.id.btnAddLikes:
			

			String postLikeUrl  = EndPoints.UpdateLikesUrl;
				int likes = Integer.parseInt(newsLikes);
				likes = likes+1;
				txtNewsLikes.setText(String.valueOf(likes));
				txtNewsLikes2.setText(String.valueOf(likes));

			    new PostLikes().execute(postLikeUrl);

			
			break;
		case R.id.btnAddComms:
		
			scrollNews.fullScroll(v.FOCUS_DOWN);
			btnAddComms.setPressed(true);

			final Dialog dialog = new Dialog(NewsDetails.this);
			dialog.requestWindowFeature(Window.FEATURE_LEFT_ICON);
			dialog.setContentView(R.layout.commentdialog);
			dialog.getWindow().setBackgroundDrawableResource(R.drawable.roundedcorner);
			dialog.setTitle("Post Comment");
			
			final EditText editPostComment = (EditText)dialog.findViewById(R.id.editPostComment);
			
			
			final ImageButton dialogButton = (ImageButton)dialog.findViewById(R.id.dialogButtonOK);
			dialogButton.setOnClickListener(new View.OnClickListener() 
			{
				
				@SuppressLint("ResourceAsColor")
				@Override
				public void onClick(View v)
				{
					
					postedComment =  editPostComment.getText().toString().trim();
					// TODO Auto-generated method stub
					if(postedComment.equals("")|| postedComment.matches("")||postedComment.length()==0)
					{
						Toast.makeText(NewsDetails.this, "Please enter a comment.", Toast.LENGTH_LONG).show();
						editPostComment.findFocus();
					}
					else
					{
						dialog.dismiss();
						pd = new ProgressDialog(NewsDetails.this);
						pd.setMessage("Posting..");
						pd.show();
						pd.setCancelable(false);
						pd.setCanceledOnTouchOutside(false);

						PostComments(postedComment);
					}
				}

				private void PostComments(String postedComment)
				{
					// TODO Auto-generated method stub
					// Post the comment
					String postCommentUrl  = EndPoints.PostCommentsURL;
					new PostComment().execute(postCommentUrl);
					
					//Show the List with the new comment added.
					myCommentsList.clear();
					GetComments();
					
				}
			});
			dialog.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.post_comment_button);
			dialog.show();
			
			break;


		case R.id.btnBackArticle:
			
			finish();
			break;
		}
	}
}
