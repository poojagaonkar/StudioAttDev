package Utility;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.zeven.attini.R;
import com.zevenpooja.attini.HomeFragment;
import com.zevenpooja.attini.News;

import org.jsoup.Jsoup;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by PoojaG on 25-11-2015.
 */
public class HomeLandAdapter extends  RecyclerView.Adapter
{

    private List<News> itemList;
    private Context context;
    private String myColor;
    private String views;
    private String comms;
    private String likes;
    private String myTitle;
    private String mydescription;
    private String bitmapUrl;
    private String myDate;
    private ImageLoader mLoader;
    private HomeFragment myfragment;
    private MediaPlayer playSound;



    public HomeLandAdapter(Context context, List<News> itemList, HomeFragment homeFragment) {
        this.itemList = itemList;
        this.context = context;
        this.mLoader = new ImageLoader(context);
        this.myfragment = homeFragment;

    }
    @Override
    public LandViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_staggered, null);
        HomeLandAdapter.LandViewHolder rcv = new HomeLandAdapter.LandViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        HomeLandAdapter.LandViewHolder vh = (LandViewHolder) holder;

        News song = itemList.get(position);

        myColor = song.getColor();


	     /*  myColor = song.getColor();
  			if(myColor=="#7959BC")
  			{
  				myColor = "#62934D";
  			}
  			else if(myColor =="#62934D")
  			{
  				myColor="#7959BC";
  			}*/
        vh.viewdate.setBackgroundColor(Color.parseColor(myColor));


        views = song.getNewsViews();
        comms = song.getNewsComments();
        likes = song.getNewsLikes();


        vh.txtComms.setText(comms);
        vh.txtLikes.setText(likes);
        vh.txtViews.setText(views);
        vh.txtComms.setTextColor(Color.GRAY);
        vh.txtLikes.setTextColor(Color.GRAY);
        vh.txtViews.setTextColor(Color.GRAY);
        // Setting all values in listview
        //Set title
        myTitle =song.getTitle();
        StringBuilder sb = new StringBuilder(myTitle);

        int i = 0;
        while ((i = sb.indexOf(" ", i + 40)) != -1) {
            sb.replace(i, i + 1, "\n");
        }

        vh.title.setText(sb.toString());

        //Set description
        mydescription =  Jsoup.parse(song.getBody()).text();

        vh.description.setMaxLines(3);
        vh.description.setEllipsize(TextUtils.TruncateAt.END);
        vh.description.setText(mydescription);

        // Set image
        bitmapUrl = song.getNewsBigImage();

        //Set date
        myDate = song.getPublishedDate();

        final String OLD_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        final String NEW_FORMAT = "MMM dd, yy";

        String newDate ="";

        SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat myFormat = new SimpleDateFormat("dd MMMM, yy");


        try {
            vh.pubDate.setText(myFormat.format(fromUser.parse(song.getPublishedDate())));
        } catch (ParseException e) {

            vh.pubDate.setText("");
        }


        //convertView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 400));
        mLoader.DisplayImage(bitmapUrl, vh.thumb_image);

    }



    @Override
    public int getItemCount() {
        return itemList.size();
    }
    public class LandViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title;
        TextView description;
        ScaleImageView thumb_image;
        WebView imageWebView;
        TextView pubDate;
        View viewdate;
        TextView txtViews, txtComms, txtLikes;
        FrameLayout layGridLayout;

        public LandViewHolder(View convertView) {
            super(convertView);
            convertView.setOnClickListener(this);
           title = (TextView)convertView.findViewById(R.id.title); // title
           description = (TextView)convertView.findViewById(R.id.description); // artist name
           thumb_image=(ScaleImageView)convertView.findViewById(R.id.imageView1); // thumb image
           pubDate = (TextView)convertView.findViewById(R.id.txtPubDate);
           viewdate = (View)convertView.findViewById(R.id.viewdate);
          // layGridLayout = (FrameLayout)convertView.findViewById(R.id.layoutOfStagGridView);
           txtComms = (TextView)convertView.findViewById(R.id.txtcomm);
           txtViews = (TextView)convertView.findViewById(R.id.txtview);
           txtLikes = (TextView)convertView.findViewById(R.id.txtlike);
        }


        @Override
        public void onClick(View v) {
            myfragment.sendData(getPosition());
            playSound = MediaPlayer.create(context, R.raw.buttonclicksound);
            playSound.start();
        }
    }
}