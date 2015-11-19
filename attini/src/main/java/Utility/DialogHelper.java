package Utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.widget.TextView;

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
}
