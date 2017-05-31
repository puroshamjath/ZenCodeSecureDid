package amjath.purosh.zencode.utils;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Techzarinfo on 5/29/2017.
 */

public class Utils extends Application{

    public static String URL = "https://api.github.com/repos/crashlytics/secureudid/issues";

    // For Checking Internet Availability....
    public static boolean isInternetAvailable(Context c) {

        ConnectivityManager connectivity = (ConnectivityManager) c
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivity != null) {

            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }

        return false;
    }

    public static String getDateFromMonth(String task_due_date) {

        //Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        SimpleDateFormat outputFormat = new SimpleDateFormat("MM-dd-yyyy");

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(task_due_date.toString());
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return str;
        // return timestamp.toString();

    }

    public static void showMessageDialog(Context c, String string) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(c);

        alertDialogBuilder.setMessage(string);

        alertDialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        arg0.dismiss();

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
                int count=is.read(bytes, 0, buffer_size);
                if(count==-1)
                    break;
                os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }

}
