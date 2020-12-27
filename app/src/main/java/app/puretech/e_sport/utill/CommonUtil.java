package app.puretech.e_sport.utill;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import app.puretech.e_sport.R;
import app.puretech.e_sport.activity.SplashActivity;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by dinesh on 14-02-2018.
 */

@SuppressWarnings("All")
public class CommonUtil {

    public static Dialog getCircularProgressDialog(Activity activity, int titleText, boolean cancelableFlag) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.circular_progressbar);
        GifDrawable gifFromResource = null;
        try {
            gifFromResource = new GifDrawable(activity.getResources(), R.drawable.new_progress);
            dialog.setCancelable(cancelableFlag);
            GifImageView gifImageView = (GifImageView) dialog.findViewById(R.id.gifView);
            gifImageView.setImageDrawable(gifFromResource);
            TextView title = (TextView) dialog.findViewById(R.id.upgrade_title_textview);
            title.setText(activity.getResources().getString(titleText));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dialog;
    }

    public static boolean isNetworkAvailable(Activity activity) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        // Check for network connections
        if (connectivityManager.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING ||
                connectivityManager.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING ||
                connectivityManager.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED) {
            // if connected with internet
            // Toast.makeText(this, " Connected ", Toast.LENGTH_LONG).show();
            return true;
        } else if (
                connectivityManager.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED ||
                        connectivityManager.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED) {
            //Toast.makeText(this, " Not Connected ", Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }

    public static String getRealPathFromURI(Activity activity, Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        @SuppressWarnings("deprecation")
        android.database.Cursor cursor = activity.managedQuery(contentUri, proj, // Which
                // columns
                // to
                // return
                null, // WHERE clause; which rows to return (all rows)
                null, // WHERE clause selection arguments (none)
                null); // Order-by clause (ascending by name)
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public static void hideKeyboard(@NonNull Activity activity) {
        try {
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Bitmap getBitmap(Activity activity, String path) {
        int IMAGE_MAX_SIZE = 250000;
        File externalFile = new File(path);
        Uri uri = Uri.fromFile(externalFile);
        ContentResolver mconContentResolver = activity.getContentResolver();
        InputStream in = null;
        try {
            in = mconContentResolver.openInputStream(uri);
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, o);
            in.close();
            int scale = 1;
            while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) > IMAGE_MAX_SIZE) {
                scale++;
            }
            Log.d("TAG", "scale = " + scale + ", orig-width: " + o.outWidth
                    + ", orig-height: " + o.outHeight);
            Bitmap b = null;
            in = mconContentResolver.openInputStream(uri);
            if (scale > 1) {
                scale--;
                //scale to max possible inSampleSize that still yields an image
                //larger than target
                o = new BitmapFactory.Options();
                o.inSampleSize = scale;
                b = BitmapFactory.decodeStream(in, null, o);
                //resize to desired dimensions
                int height = b.getHeight();
                int width = b.getWidth();
                Log.d("TAG", "1th scale operation dimenions - width: " + width
                        + ",height: " + height);
                double y = Math.sqrt(IMAGE_MAX_SIZE
                        / (((double) width) / height));
                double x = (y / height) * width;
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, (int) x,
                        (int) y, true);
                b.recycle();
                b = scaledBitmap;
                System.gc();
            } else {
                b = BitmapFactory.decodeStream(in);
            }
            in.close();
//            Log.d("TAG", "bitmap size - width: " + b.getWidth() + ", height: "
//                    + b.getHeight());
            return b;
        } catch (IOException e) {
            Log.e("TAG", e.getMessage(), e);
            return null;
        }
    }

    public static String currentDate(Activity activity) {
        SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat day = new SimpleDateFormat("dd");
        Date myDate = new Date();
        String filename = timeStampFormat.format(myDate);
        int days = Integer.parseInt(day.format(myDate));
        String date = "" + filename;
        // tv_Date.setText("" + filename);
        AppPreferences.init(activity).setString("CurrentDate", "" + filename);
        return date;
    } //current

    public static String getCurrentMonth() {
        SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat day = new SimpleDateFormat("dd");
        Date myDate = new Date();
        String filename = timeStampFormat.format(myDate);
        int days = Integer.parseInt(day.format(myDate));
        String date = "" + filename;
        return date;
    } //current

    public static void noConnectionDialog(final Context context) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("Logout");
        //  dialog.setIcon(R.drawable.crossicone);
        dialog.setMessage("Are you sure you want to logout ?");
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                int Flag = 0;
                if (Flag == 0) {
                    SharedPreferences prefs = context.getSharedPreferences(
                            "mypref",
                            MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.clear();
                    editor.commit();
                    Flag = 1;
                }
                if (Flag == 1) {
                    SharedPreferences prefs = context.getSharedPreferences(
                            "Stock",
                            MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.clear();
                    editor.commit();
                    Flag = 0;
                }
                Intent logout = new Intent(context.getApplicationContext(), SplashActivity.class);
                ((Activity) context).startActivity(logout);
                ((Activity) context).finish();
            }
        });
        dialog.show();
    }

    private void hideKeyboard(View context, Activity activity) {
        InputMethodManager imm = (InputMethodManager)
                activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(context.getWindowToken(), 0);
        Toast.makeText(activity, "hide", Toast.LENGTH_SHORT).show();
    }

    public static void showBar(Activity activity, String str_class_name) {
        Snackbar.make(activity.findViewById(android.R.id.content),
                "No internet connection !",
                Snackbar.LENGTH_INDEFINITE).setAction("Retry",
                new View.OnClickListener() {
                    @SuppressLint("NewApi")
                    @Override
                    public void onClick(View v) {
                        try {
                            activity.startActivity(new Intent(activity, Class.forName("app.puretech.e_sport" + str_class_name)));
                            activity.finish();
                        } catch (ClassNotFoundException e) {
                            // Toast.makeText(activity, s + " does not exist yet", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).show();

    }

    public static String convertDate(String dateStr, String current_formate, String converted_formet) {
        SimpleDateFormat curFormater = new SimpleDateFormat(current_formate);
        SimpleDateFormat postFormater;
        String newDateStr = null;
        Date dateObj = null;
        try {
            dateObj = curFormater.parse(dateStr);
            postFormater = new SimpleDateFormat(converted_formet);
            newDateStr = postFormater.format(dateObj);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return newDateStr;
    }

}