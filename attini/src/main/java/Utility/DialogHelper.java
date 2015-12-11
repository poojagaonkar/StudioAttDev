package Utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.TextView;

import com.zevenpooja.attini.Login;

/**
 * Created by PoojaG on 19-11-2015.
 */
public class DialogHelper {

    private static Dialog dialog;
    private static TextView mProgressText;
    private static  AlertDialog.Builder mAlert;

    public static void CreateNetworkAlert(Activity mActivity, String Title, String Message)
    {
        mAlert = new AlertDialog.Builder(mActivity);
        mAlert.setTitle(Title);
        mAlert.setMessage(Message);
        mAlert.setCancelable(false);
        //mAlert.SetPositiveButton("Ok", delegate { mActivity.OnBackPressed(); });
        mAlert.setNegativeButton("Ok", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {

            }
        });
        mAlert.show();
    }
    public static void CreateLogoutAlert(final Activity mActivity, String Title, String Message)
    {
        mAlert = new AlertDialog.Builder(mActivity);

        mAlert.setTitle(Title);
        mAlert.setMessage(Message);
        mAlert.setCancelable(false);
        mAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                SessionManagement session = new SessionManagement(mActivity);
                session.logoutUser();
                Intent mIntent = new Intent(mActivity, Login.class);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mActivity.startActivity(mIntent);
                mActivity.finish();
            }
        });
        mAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                mActivity.finish();
            }
        });
        mAlert.show();
    }
    public static void FullLogoutAlert(final Activity mActivity, String Title, String Message)
    {
        mAlert = new AlertDialog.Builder(mActivity,android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);

        mAlert.setTitle(Title);
        mAlert.setMessage(Message);
        mAlert.setCancelable(false);
        mAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                SessionManagement session = new SessionManagement(mActivity);
                session.logoutUser();
                Intent mIntent = new Intent(mActivity, Login.class);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mActivity.startActivity(mIntent);
                mActivity.finish();
            }
        });
        mAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                mActivity.finish();
            }
        });
        mAlert.show();

    }
}

