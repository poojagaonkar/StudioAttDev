package com.zevenpooja.attini;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Attini.DAL.EndPoints;
//import com.zevenpooja.attini.DrawerActivity.DrawerItemClickListener;
import Attini.Model.AttiniNewsItem;
import Attini.Model.ConnectionDetector;
import Attini.Model.NavDrawerItem;
import Attini.Model.NavDrawerListAdapter;
import Utility.ArticleFilter;
import Utility.CommentsComparator;
import Utility.DateComparator;
import Utility.DialogHelper;
import Utility.ListViewInScrollViewHeight;
import Utility.MyComparator;
import Utility.MyList;
import Utility.PainTitleAdapter;
import Utility.ProgressDialogFragment;
import Utility.SessionManagement;
//import Utility.StorageHelper;
import Utility.ViewsComparator;
import Utility.WaitProgressFragment;
import ch.lambdaj.Lambda;
import ch.lambdaj.collection.LambdaCollections;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.zeven.attini.R;



@SuppressLint("NewApi")
public class Home extends AppCompatActivity implements OnItemClickListener
{
    TextView usersName;
    String encodedAccountName,SPHostUrl,refreshToken, realm,usersname, deviceAuthKey,lastName, fullName;
    ListView newsList;

    public static  String[] titles = null;
    public static   String[] descriptions = null;
    public static   String[] thumbnails = null;
    public static   String[] urls = null;
    //JSON Node Names
    public static final String NEWSITEMS ="NewsItems" ;

    public static final String  ARTICLEGUID ="ArticleGuid";
    public static final String  AUTHORLOGINNAME=" AuthorLoginName";
    public static final String  BODYTEXT ="BodyText";
    public static final String  BODY ="Body" ;
    //JSONObject ColSpan = ColSpan(i),
    public static final String CREATEDDATE = "CreatedDate";
    public static final String  ENTERPRISEKEYWORDS="EnterpriseKeywords";
    public static final String  ISPLACEHOLDERFORTHUMBNAIL="IsPlaceHolderForThumbnail";
    public static final String  NEWSSOURCEID="NewsSourceId";
    public static final String  IMAGEBLOBURLS="ImageBlobUrls";
    public static final String  NEWSSOURCETITLE="NewsSourceTitle";
    public static final String  NUMBEROFCOMMENTS="NumberOfComments";
    public static final String  NUMBEROFVIEWS="NumberOfViews";
    public static final String  PUBLISHEDDATE="PublishedDate";
    //JSONObject RowSpan = RowSpan(i),
    public static final String  THUMBANAILDESCRIPTION="ThumbnailDescription";
    public static final String  THUMBNAILPATH="ThumbnailPath" ;
    public static final String  TITLE = "Title" ;
    public static final String  URL= "Url" ;
    private String isLiked;

    JSONArray newsItems = null;
    public static String avatarUrl = "";

    public static List<News> myNewsList = new ArrayList<News>();

    private  String items;

    private  TextView txtDrawerUserName;

    //URL to get JSON Array
    private WaitProgressFragment refDialog ;

    //  private static final Logout instance = new Logout();


    public ArrayList<AttiniNewsItem> itemsArray;

    public static ProgressDialog myDialog;
    public ProgressDialog newsDialog;
    ImageButton btnNav;
    private View viewIconColor;

    //FOr the navigationMenu
    private DrawerLayout mDrawerLayout;
    private ScrollView mainLinearLayout;
    private ListView mDrawerList, mCatagoryList, moptionsList;
    private android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;
    private MediaPlayer playSound;

    // nav drawer title
    private CharSequence mDrawerTitle;

    // used to store app title
    private CharSequence mTitle;

    // slide menu items
    private String[] navMenuTitles;
    private String[] iconColors;
    private String[] numberofLikesArray;
    private TypedArray navMenuIcons;
    private boolean isRefreshed;

    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;
    public static List<News> myFinalNewsList = new ArrayList<News>();
    public static List<News> companyNewsList = new ArrayList<News>();
    public static ArrayList<String> catagoryArrayList = new ArrayList<String>();

    public static String[] catagoryList = new String[] {"Most Recent","Most Liked", "Most Commented", "Most Viewed","", "About Us"};//,"Logout"};
    public static String[] colorPallete = new String[] {"#1F1A17", "#62934D", "#F9B03F", "#7959BC", "#74B8DE", "#E65641", "#7CC8BB", "#D7CE5D", "#D6BE95", "#B694D1"};
    private String NewsSourceTitle = null;
    ProgressDialogFragment prog;
    private ProgressDialogFragment myprogressDialog;
    private List<News> updatedList;
    private boolean canContinue  = true;
    ArrayList<String> myTitleList = new ArrayList<String>();
    private boolean isSelected =true;
    private  String registerContet;
    private Toolbar toolbar;
    private TextView txtTitle1, txtTitle2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawerlayout);

        ConnectionDetector cd = new ConnectionDetector(Home.this);
        if(cd.isConnectingToInternet()== false)
        {
            DialogHelper.CreateNetworkAlert(Home.this, "Network Error", "Check your internet connection");
        }

        else

            txtDrawerUserName = (TextView)findViewById(R.id.txtUserDrawer);
        viewIconColor = (View)findViewById(R.id.viewIconColor);
        mainLinearLayout = (ScrollView)findViewById(R.id.left_drawer);
        mCatagoryList = (ListView)findViewById(R.id.list_slidermenu2);
        moptionsList = (ListView)findViewById(R.id.list_slidermenu3);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        txtTitle1 = (TextView)findViewById(R.id.txtTitle1);
        txtTitle2 = (TextView)findViewById(R.id.txtTitle2);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        Intent in = getIntent();
        SPHostUrl = in.getStringExtra("SPHostUrl");
        encodedAccountName = in.getStringExtra("EncodedAccountName");
        deviceAuthKey = in.getStringExtra("DeviceAuthKey");
        usersname = in.getStringExtra("UsersName");
        avatarUrl = in.getStringExtra("AvatarUrl");
        lastName = in.getStringExtra("LastName");
        fullName = usersname+" "+ lastName;


        CreateMenu();




    }

    private void CreateMenu(){


        registerContet = EndPoints.FetchNewsItemsUrl + "spHostUrl="+SPHostUrl + "&encodedAccountName="+encodedAccountName+"&deviceAuthKey="+ deviceAuthKey+"&count=50";


        int id = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
        mTitle = mDrawerTitle = getTitle();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        // nav drawer icons from resources
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
        txtDrawerUserName.setText(fullName);
        // enabling action bar app icon and behaving it as toggle button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(Home.this, mDrawerLayout,

                R.string.drawer_open, // nav drawer open - description for accessibility
                R.string.drawer_close // nav drawer close - description for accessibility
        ){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);




        try
        {


            new FetchItems().execute(registerContet);
            //myFinalNewsList = new GetList().execute(items).get();




        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }
    public static List<News> getMyFinalNewsList() {
        return myFinalNewsList;
    }
    public static void setMyFinalNewsList(List<News> myFinalNewsList) {
        Home.myFinalNewsList = myFinalNewsList;
    }
    public static int[] convertIntegers(List<Integer> integers)
    {
        int[] ret = new int[integers.size()];
        Iterator<Integer> iterator = integers.iterator();
        for (int i = 0; i < ret.length; i++)
        {
            ret[i] = iterator.next().intValue();
        }
        return ret;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onResume()
    {
        // TODO Auto-generated method stub
        super.onResume();

    }


    // Get response from the News API
    private class FetchItems extends AsyncTask<String, String, List<News>>
    {
        // TODO Auto-generated method stub
        ProgressDialog myDialog;
        boolean worked;
        @Override
        protected List<News> doInBackground(String... params)
        {
            // TODO Auto-generated method stub
            HttpResponse response =null;
            String resultString = "";
            String myResponseBody = "" ;

            JSONArray jObject;
            // Creating HTTP client
            HttpClient httpClient = new DefaultHttpClient();
            // Creating HTTP Post
            HttpGet request = new HttpGet(params[0]);
            try
            {
                response = httpClient.execute(request);
                switch (response.getStatusLine().getStatusCode())
                {
                    case 200:
                        HttpEntity entity = response.getEntity();
                        if (entity != null)
                        {

                            InputStream inputStream = entity.getContent();
                            myResponseBody = convertToString(inputStream);

                            myNewsList.clear();
                            jObject = new JSONArray(myResponseBody);
                            myTitleList.add("All News");
                            for (int i = 0; i < jObject.length(); i++) {
                                JSONObject menuObject = jObject.getJSONObject(i);

                                String title = menuObject.getString("Title");
                                String description = menuObject.getString("BodyText");
                                String thumbnail = menuObject.getString("ThumbnailPath");
                                String newsUrl = menuObject.getString("Url");
                                String body = menuObject.getString("Body");
                                String newsBigImage = menuObject.getString("ThumbnailPath");
                                String newsComments = menuObject.getString("NumberOfComments");
                                String newsViews = menuObject.getString("NumberOfViews");
                                String publishedDate = menuObject.getString("PublishedDate");
                                String articleGuid = menuObject.getString("ArticleGuid");
                                String newsSourceId = menuObject.getString("NewsSourceId");
                                String newsId = menuObject.getString("Id");
                                String publisherName = menuObject.getString("AuthorDisplayName");
                                String newsSourceTitle = menuObject.getString("NewsSourceTitle");
                                String newsLikes = menuObject.getString("NumberOfLikes");
                                String tags = menuObject.getString("EnterpriseKeywords");
                                try {
                                    isLiked = menuObject.getString("IsLiked");
                                }
                                catch (Exception e)
                                {
                                    isLiked = "Undefined";
                                }

                                myNewsList.add(new News(title, description, thumbnail, newsUrl, body, newsBigImage, newsComments, newsViews, publishedDate, articleGuid, newsSourceId, newsId, publisherName, newsSourceTitle, newsLikes, tags, isLiked));

                                myTitleList.add(newsSourceTitle);
                            }
                            //Assign a color from color pallete to each newsSourceId.
                            Map<String, String> colorHistory = new HashMap<String, String>();
                            int k = -1;
                            for (int i = 0; i < myNewsList.size(); i++)
                            {
                                if (!colorHistory.containsKey(myNewsList.get(i).getNewsSourceId()))
                                {
                                    colorHistory.put(myNewsList.get(i).getNewsSourceId(),colorPallete[++k % colorPallete.length]);
                                }
                                myNewsList.get(i).setColor(colorHistory.get(myNewsList.get(i).getNewsSourceId()));
                            }

                        }
                        break;
                    case 401:
                        myNewsList = Collections.EMPTY_LIST;
                        break;

                }
            }
            catch(Exception e)
            {
                myNewsList = Collections.EMPTY_LIST;
            }




            return myNewsList;
        }

        @Override
        protected void onPostExecute(List<News> result)
        {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            if(result.size() == 0 )
            {
                refDialog.dismiss();
                DialogHelper.CreateLogoutAlert(Home.this,"","Unauthorized. Please login again.");
            }
            else
            {
                myFinalNewsList = result;
                if(myFinalNewsList.size() == 0)
                {
                    DialogHelper.CreateNetworkAlert(Home.this, "Error", "Something went wrong");

                }


                ArrayList<String> isonColors = new ArrayList<String>();
                isonColors.add("#FFFFFF");
                for(int j =0; j < myFinalNewsList.size(); j++)
                {	String colorz = myFinalNewsList.get(j).getColor();
                    isonColors.add(colorz);

                }

                //Remove duplicate titles
                LinkedHashSet<String> titleSet = new LinkedHashSet<>();
                titleSet.addAll(myTitleList);
                myTitleList.clear();
                myTitleList.addAll(titleSet);

                //Remove duplicate colors
                LinkedHashSet<String> colorSet = new LinkedHashSet<>();
                colorSet.addAll(isonColors);
                isonColors.clear();
                isonColors.addAll(colorSet);


                //Add titles and corresponding colors to navigation menu.
                navDrawerItems = new ArrayList<NavDrawerItem>();
                for(int j =0 ;j <myTitleList.size() && j< isonColors.size();j++)
                {

                    navDrawerItems.add(new NavDrawerItem(myTitleList.get(j), isonColors.get(j)));


                }
                // Recycle the typed array
                navMenuIcons.recycle();


                // setting the nav drawer list adapter

                adapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);
                mDrawerList.setAdapter(adapter);

                mDrawerList.setOnItemClickListener(new SlideMenuClickListener());



                displayView(0);

                //Second list

                final View footerView =  ((LayoutInflater)Home.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.logoutfooter, null, false);
                mCatagoryList.addFooterView(footerView);
                footerView.setOnClickListener(new View.OnClickListener()
                {

                    @Override
                    public void onClick(View v)
                    {
                        // TODO Auto-generated method stub
                        // Logout
                        footerView.setBackgroundResource(R.drawable.list_selector1);
                        SessionManagement session = new SessionManagement(getApplicationContext());
                        session.logoutUser();
                        finish();

                    }
                });

                //ArrayAdapter<String> adapter = new ArrayAdapter<String>(Home.this, R.layout.drawer_list_item_2, R.id.txtCatagories, catagoryList);
                PainTitleAdapter adapter = new PainTitleAdapter(Home.this,catagoryList);
                mCatagoryList.setAdapter(adapter);
                mCatagoryList.setOnItemClickListener(Home.this);
                ListViewInScrollViewHeight.setListViewHeightBasedOnChildren(mCatagoryList);
                ListViewInScrollViewHeight.setListViewHeightBasedOnChildren(mDrawerList);

                if(refDialog!=null) {
                    refDialog.dismiss();
                }
                else if(myprogressDialog !=null)
                {
                    myprogressDialog.dismiss();
                }
                Fragment mFragment = new HomeFragment(SPHostUrl,encodedAccountName,deviceAuthKey,usersname,avatarUrl, fullName,getApplicationContext(),result);
            }


        }

        @Override
        protected void onPreExecute()
        {
            // TODO Auto-generated method stub
            super.onPreExecute();

            ConnectionDetector cd = new ConnectionDetector(Home.this);
            if(cd.isConnectingToInternet()== false)
            {
                new AlertDialog.Builder(Home.this)
                        .setTitle("Network error")
                        .setMessage("Please check your internet connection")
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                worked = false;
                                finish();
                            }
                        })
                        .show();
            }
            else
            {
                worked = true;

                if(isRefreshed == true) {
                    if (refDialog != null) {
                        refDialog = null;
                    }
                    refDialog = WaitProgressFragment.newInstance();

                    refDialog.show(getFragmentManager(), "Wait");
                }
                else
                {
                    if (myprogressDialog != null) {
                        myprogressDialog = null;
                    }
                    myprogressDialog = ProgressDialogFragment.newInstance();

                    myprogressDialog.show(getFragmentManager(), "Wait");
                }

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

    @Override
    protected void onRestart()
    {

        // TODO Auto-generated method stub
        super.onRestart();

        if(HomeFragment.myDialog!= null && HomeFragment.myDialog.isShowing())
        {
            HomeFragment.myDialog.dismiss();
        }


    }
    /**
     * Slide menu item click listener
     * */
    private class SlideMenuClickListener implements
            OnItemClickListener
    {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id)
        {
            // display view for selected nav drawer item
            view.setSelected(true);

            displayView(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Fragment fragment = null;
        // toggle nav drawer on selecting action bar app icon/title
        ProgressDialog pg;
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar actions click
        switch (item.getItemId())
        {

            case R.id.action_refresh:

				/*String registerContet = EndPoints.FetchNewsItemsUrl + "spHostUrl="+SPHostUrl + "&encodedAccountName="+encodedAccountName+"&deviceAuthKey="+ deviceAuthKey+"&count=50";
				new FetchItems().execute(registerContet);
*/

                isRefreshed = true;
                CreateMenu();


                break;


            default:
                return super.onOptionsItemSelected(item);
        }
        return  true;
    }


    /***
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mainLinearLayout);
        menu.findItem(R.id.action_refresh).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     * @param myPosition
     * */
    private void displayView(int position)
    {

        Fragment fragment = null;

        //Filter
        if(position>0)
        {
            String myPosition = null;

            myPosition =  navDrawerItems.get(position).getTitle();
            companyNewsList = Lists.newArrayList(Collections2.filter(myFinalNewsList, new ArticleFilter(myPosition)));

            fragment = new HomeFragment(SPHostUrl,encodedAccountName,deviceAuthKey,usersname,avatarUrl, fullName,getApplicationContext(),companyNewsList);
        }
        else if(position==0 && (navDrawerItems.get(position).getTitle()=="All News"))
        {
            companyNewsList = myFinalNewsList;
            fragment = new HomeFragment(SPHostUrl,encodedAccountName,deviceAuthKey,usersname,avatarUrl, fullName,getApplicationContext(),myFinalNewsList);
        }




        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();

            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);

            //setTitle(catagoryList[position]);
            mDrawerLayout.closeDrawer(mainLinearLayout);
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }


    /**
     * To add "Home" as first item in existing array of titles
     */

    @SuppressWarnings("unchecked")
    public static <T> T[] addFirst(T[] current, T value) {

        T[] newone = (T[]) Array.newInstance(current.getClass()
                .getComponentType(), current.length + 1);
        copy(current, newone, 0, 1, current.length);
        newone[0] = value;
        return newone;
    }
    public static <T> T[] copy(T[] from, T[] to, int fromPos, int toPos,
                               int length) {
        System.arraycopy(from, fromPos, to, toPos, length);
        return to;
    }

    @Override
    public void setTitle(CharSequence title)
    {
        mTitle = "Attini Comms";

    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    //Filters
    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
    {
        //companyNewsList = myFinalNewsList;
        // TODO Auto-generated method stub
        Fragment fragment = null;
        switch (position)
        {
            case 0:
                //Most Recent filter
                Collections.sort(companyNewsList, new DateComparator());
                fragment = new HomeFragment(SPHostUrl,encodedAccountName,deviceAuthKey,usersname,avatarUrl, fullName,getApplicationContext(),companyNewsList);

                break;

            case 1://Most Liked

                Collections.sort(companyNewsList, new MyComparator());
                fragment = new HomeFragment(SPHostUrl,encodedAccountName,deviceAuthKey,usersname,avatarUrl, fullName,getApplicationContext(),companyNewsList);

                break;



            case 2:
                //Most commented filter
                Collections.sort(companyNewsList, new CommentsComparator());
                fragment = new HomeFragment(SPHostUrl,encodedAccountName,deviceAuthKey,usersname,avatarUrl, fullName,getApplicationContext(),companyNewsList);

                break;

            case 3:
                // Most Viewed Filter
                Collections.sort(companyNewsList, new ViewsComparator());
                fragment = new HomeFragment(SPHostUrl,encodedAccountName,deviceAuthKey,usersname,avatarUrl, fullName,getApplicationContext(),companyNewsList);

                break;

            case 4:

                final TextView message = new TextView(Home.this);

                final SpannableString s = new SpannableString("To find out more about Attini Comms, and the people behind it, please check out our site at, http://attini.com");
                Linkify.addLinks(s, Linkify.WEB_URLS);
                message.setText(s);
                message.setMovementMethod(LinkMovementMethod.getInstance());
                message.setPadding(12, 12, 12, 12);

                new AlertDialog.Builder(Home.this)
                        .setTitle("About Us")
                        .setCancelable(true)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton("Ok", null)
                        .setView(message)
                        .show();


                break;
        }


        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();

            // update selected item and title, then close the drawer
            mCatagoryList.setItemChecked(position, true);
            mCatagoryList.setSelection(position);
            //setTitle(catagoryList[position]);
            mDrawerLayout.closeDrawer(mainLinearLayout);
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {

            if(resultCode ==  RESULT_OK){

                String registerContet = EndPoints.FetchNewsItemsUrl + "spHostUrl="+SPHostUrl + "&encodedAccountName="+encodedAccountName+"&deviceAuthKey="+ deviceAuthKey+"&count=50";
                new FetchItems().execute(registerContet);
            }
        }

    }
}






