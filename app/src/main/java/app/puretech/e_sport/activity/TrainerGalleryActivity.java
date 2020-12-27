package app.puretech.e_sport.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import app.puretech.e_sport.App;
import app.puretech.e_sport.R;
import app.puretech.e_sport.adapter.TrainerGalleryAdapter;
import app.puretech.e_sport.model.TrainerGalleryDTO;
import app.puretech.e_sport.utill.CommonUtil;
import app.puretech.e_sport.utill.DataManager;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import static app.puretech.e_sport.utill.DataManager.urlTrainerGallary;

public class TrainerGalleryActivity extends AppCompatActivity {

    App app;
    Activity activity;
    private GridView rv_gallery;
    private List<TrainerGalleryDTO> array_list = new ArrayList<>();
    private TrainerGalleryAdapter trainerGalleryAdapter;
    private LinearLayout ll_hide;
    private SwipeRefreshLayout s_Refresh;
    private FloatingActionButton fab_addImage;
    int PICK_IMAGE_MULTIPLE = 1;
    private String imageEncoded;
    private List<String> imagesEncodedList;
    private Toolbar toolbar;
    Intent iback;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    /*    getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);*/
        setContentView(R.layout.activity_trainer_gallery);
        doInit();

        rv_gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Log.d("cccccc", "onItemClick: " + array_list.get(position).getStr_image());

              /*  Glide.with(TrainerGalleryActivity.this).load(DataManager.urlTrainerGallary + array_list.get(position).getStr_image())
                        .asBitmap()
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(final Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                fullscrndialog(resource);
                            }
                        });*/
                Log.d("cccccc", "onItemClick: " + array_list.get(position).getStr_image());

                AlertDialog.Builder builder = new AlertDialog.Builder(TrainerGalleryActivity.this);
                final AlertDialog dialog = builder.create();
                LayoutInflater inflater = getLayoutInflater();
                View dialogLayout = inflater.inflate(R.layout.fullscreen_img, null);
                dialog.setView(dialogLayout);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                dialog.show();
                ImageView image = (ImageView) dialog.findViewById(R.id.goProDialogImage);
                ImageView imageClose = (ImageView) dialog.findViewById(R.id.close_zoom);

                Picasso.with(activity).load(urlTrainerGallary + array_list.get(position).getStr_image())
                        .error(R.drawable.no_gallery)
                        .placeholder(R.drawable.no_gallery)
                        .into(image);

                imageClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

            }
        });
    }

    private void fullscrndialog(Bitmap bitmap) {
        Log.d("fffffff", "fullscrndialog: call");
        final Dialog dialog = new Dialog(TrainerGalleryActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fullprofimage);
        final Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageView zoomimage = (ImageView) dialog.findViewById(R.id.fullimageView);
        ImageView cancel = (ImageView) dialog.findViewById(R.id.cancel_button);
        zoomimage.setImageBitmap(bitmap);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void doInit() {
        app = (App) getApplication();
        activity = this;
        dialog = CommonUtil.getCircularProgressDialog(activity, R.string.please_wait, false);
        rv_gallery = findViewById(R.id.rv_Trainer_gallery);
        ll_hide = findViewById(R.id.ll_Hide);
        s_Refresh = findViewById(R.id.s_refresh);
        fab_addImage = findViewById(R.id.fab_trainer_gallery);
        toolbar = findViewById(R.id.toolbar_home);
        iback = new Intent(activity, TrainerHomeActivity.class);
        addImage(fab_addImage);
        swipRefresh(s_Refresh);
        doBack(toolbar);
        getTrainerGallery();
    }

    private void getTrainerGallery() {
        if (CommonUtil.isNetworkAvailable(activity)) {
            array_list.clear();
            dialog.show();
            app.getApiService().getTrainerGallery().enqueue(new Callback<Map<String, Object>>() {
                @Override
                public void onResponse(Response<Map<String, Object>> response, Retrofit retrofit) {
                    if (response.body() != null) {
                        app.getLogger().error("success");
                        String status;
                        String message;
                        JSONObject jobj;
                        try {
                            jobj = new JSONObject(response.body());
                            status = jobj.getString("success");
                            message = jobj.getString("message");
                            if (status.equals("0")) {
                                JSONArray jsonArray = jobj.getJSONArray("gallery_data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject c = jsonArray.getJSONObject(i);
                                    TrainerGalleryDTO trainerGalleryDTO = new TrainerGalleryDTO();
                                    trainerGalleryDTO.setStr_image(c.getString("photo"));
                                    array_list.add(trainerGalleryDTO);
                                }
                                setAdapter();
                            } else if (status.equals("1")) {
                                setAdapter();
                                app.showSnackBar(activity, message);
                            } else {
                                app.showSnackBar(activity, message);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        int a = response.code();
                        if (a == 401) {
                            app.showSnackBar(activity, "" + a);
                        } else {
                            app.showSnackBar(activity, "" + a);
                        }
                    }
                    dialog.dismiss();
                }

                @Override
                public void onFailure(Throwable t) {
                    app.getLogger().error("failure");
                    dialog.dismiss();
                }
            });
        } else {
            CommonUtil.showBar(activity, ".activity.TrainerNotificationActivity");
        }
    }

    private void setAdapter() {
        trainerGalleryAdapter = new TrainerGalleryAdapter(array_list, activity);
        rv_gallery.setAdapter(trainerGalleryAdapter);
        if (trainerGalleryAdapter.getCount() == 0) {
            ll_hide.setVisibility(View.VISIBLE);
        } else {
            ll_hide.setVisibility(View.GONE);
        }
    }

    private void swipRefresh(SwipeRefreshLayout view) {
        view.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                s_Refresh.setRefreshing(false);
            }
        });
    }

    private void addImage(FloatingActionButton v) {
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_MULTIPLE);*/

                Intent intent = new Intent(TrainerGalleryActivity.this, TrainerUploadGallaryPhotoActivity.class);
                startActivity(intent);
            }
        });
    }

    @SuppressLint("NewApi")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub

        if (requestCode == PICK_IMAGE_MULTIPLE) {

            if (resultCode == RESULT_OK) {
                //data.getParcelableArrayExtra(name);
                //If Single image selected then it will fetch from Gallery
                if (data.getData() != null) {

                    Uri mImageUri = data.getData();

                } else {
                    if (data.getClipData() != null) {
                        ClipData mClipData = data.getClipData();
                        ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                        for (int i = 0; i < mClipData.getItemCount(); i++) {

                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();
                            mArrayUri.add(uri);

                        }
                        Log.v("LOG_TAG", "Selected Images" + mArrayUri.size());
                    }

                }

            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void doBack(Toolbar toolbar) {
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(iback);
                activity.finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        activity.startActivity(iback);
        activity.finish();
    }
}