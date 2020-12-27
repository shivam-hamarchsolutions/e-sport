package app.puretech.e_sport.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.RequestBody;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import app.puretech.e_sport.App;
import app.puretech.e_sport.BuildConfig;
import app.puretech.e_sport.R;
import app.puretech.e_sport.model.TrainerGallaryUploadModel;
import app.puretech.e_sport.utill.AppPreferences;
import app.puretech.e_sport.utill.Base64;
import app.puretech.e_sport.utill.CommonUtil;
import app.puretech.e_sport.utill.PermissionUtility;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class TrainerUploadGallaryPhotoActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnUploadImage, btnSelectImage;
    public static int REQUEST_CODE_GALLERY_PROFILE_PROFILE = 1002;
    public static int REQUEST_CODE_CAMERA_PROFILE_PROFILE = 1003;
    String userChoosenTask;
    ImageView imgUpload;
    Intent iback;
    App app;
    Activity activity;
    Toolbar toolbar;
    File photoFile = null;
    public String photoPath = null;
    public File imageFilePath;
    public String base64 = null;

    Spinner spnrClass, spnrDiv;
    private ArrayList arrayClass, arrayDivision;
    private Dialog dialog;
    private ArrayAdapter<String> classAdapter, divisionAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);*/
        setContentView(R.layout.activity_trainer_upload_gallary_photo);


        /* find out id's of all widgets*/
        initWidget();
        getClassDivision();

        btnUploadImage.setOnClickListener(this);
        btnSelectImage.setOnClickListener(this);

    }

    private void initWidget() {
        app = (App) getApplication();
        activity = this;
        dialog = CommonUtil.getCircularProgressDialog(activity, R.string.please_wait, false);
        btnSelectImage = findViewById(R.id.btn_select_photo);
        btnUploadImage = findViewById(R.id.btn_upload_gallary);
        imgUpload = findViewById(R.id.img_trainer_gallary_upload);
        iback = new Intent(activity, TrainerGalleryActivity.class);
        toolbar = findViewById(R.id.toolbar_trainer_upload_gallary);
        spnrClass = findViewById(R.id.spinner_upload_gallary_class);
        spnrDiv = findViewById(R.id.spinner_upload_gallary_division);
        arrayClass = new ArrayList();
        arrayDivision = new ArrayList();

        doBack(toolbar);
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_select_photo:
                requestStoragePermission();
                break;

            case R.id.btn_upload_gallary:
                if (base64 != null)
                    uploadGallary();
                else
                    app.showSnackBar(activity, "Please, select image.");
                break;
        }
    }

    /* call api */
    private void uploadGallary() {
        if (CommonUtil.isNetworkAvailable(activity)) {
            dialog.show();
            RequestBody requestBody = new MultipartBuilder()
                    .type(MultipartBuilder.FORM)
                    .addFormDataPart("className", spnrClass.getSelectedItem().toString())
                    .addFormDataPart("division", spnrDiv.getSelectedItem().toString())
                    .addFormDataPart("gallery_img", base64)
                    .build();
            app.getApiService().uploadTrainerGallary(requestBody).enqueue(new Callback<TrainerGallaryUploadModel>() {
                @Override
                public void onResponse(Response<TrainerGallaryUploadModel> response, Retrofit retrofit) {
                    dialog.dismiss();
                    TrainerGallaryUploadModel model = response.body();
                    if (model.getMessage().equals("Succes")) {
                        activity.startActivity(iback);
                        activity.finish();
                    } else {
                        app.showSnackBar(activity, "Something went wrong!");
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    dialog.dismiss();
                    app.showSnackBar(activity, "Something went wrong!");
                }
            });
        } else {
            CommonUtil.showBar(activity, ".activity.TrainerUploadGallaryPhotoActivity");
        }
    }

    private void requestStoragePermission() {
        Dexter.withActivity(TrainerUploadGallaryPhotoActivity.this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            //   openImageChooser();
                            selectImage(REQUEST_CODE_CAMERA_PROFILE_PROFILE, REQUEST_CODE_GALLERY_PROFILE_PROFILE);
                            // Toast.makeText(TrainerUploadGallaryPhotoActivity.this, "All permissions are granted!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }

                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(TrainerUploadGallaryPhotoActivity.this, "Error occurred! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private void selectImage(int request_camera, int request_gallery) {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = PermissionUtility.checkPermission(TrainerUploadGallaryPhotoActivity.this);
                boolean result_camera = PermissionUtility.checkPermissionCamera(TrainerUploadGallaryPhotoActivity.this);
                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result_camera) {
                      /*  Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, request_camera);*/
                        CallCamera(request_camera);
                    }
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);//
                        startActivityForResult(Intent.createChooser(intent, "Select File"), request_gallery);
                    }
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void CallCamera(int store1ImgCamera) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go

            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
                // Error occurred while creating the File
            }
            if (photoFile != null) {
                photoPath = photoFile.getAbsolutePath();

                Uri photoURI = FileProvider.getUriForFile(TrainerUploadGallaryPhotoActivity.this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        photoFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, store1ImgCamera);
            }
        }
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(TrainerUploadGallaryPhotoActivity.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", TrainerUploadGallaryPhotoActivity.this.getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap;
        //======================= for camera Profile ===========================================
        if (requestCode == REQUEST_CODE_CAMERA_PROFILE_PROFILE) {
            Log.d("xxxyy", " " + photoPath);
            try {
                if (photoPath != null) {
                    Log.d("xxxyy", "file not null : ");
                    Bitmap myBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                    Bitmap bm = rotateImage(myBitmap, 90);
                    String b64 = Base64.encodeImage(bm);
                    base64 = "data:image/jpeg;base64," + b64;
                    imgUpload.setImageBitmap(Base64.getBitmap(b64));
                }
            }catch (Exception e){

            }


        } else if (requestCode == REQUEST_CODE_GALLERY_PROFILE_PROFILE) {
            onSelectFromGalleryResult(data, REQUEST_CODE_GALLERY_PROFILE_PROFILE);
        }
    }

    private void onSelectFromGalleryResult(Intent data, int requestCodeGalleryProfileProfile) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                Bitmap bmp = rotateImage(bm, 90);
                String b64 = Base64.encodeImage(bm);
                base64 = "data:image/jpeg;base64," + b64;
                imgUpload.setImageBitmap(Base64.getBitmap(b64));
                Log.d("xxxyy", "onSelectFromGalleryResult: " + base64);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /* create img with name in app*/
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String mFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File mFile = File.createTempFile(mFileName, ".jpg", storageDir);

        imageFilePath = mFile;
        return mFile;
    }

    @Override
    public void onBackPressed() {
        activity.startActivity(iback);
        activity.finish();
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        if (source.getWidth() > source.getHeight()) {
            Matrix matrix = new Matrix();
            matrix.postRotate(angle);
            return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                    matrix, true);
        }
        return source;
    }


    private void getClassDivision() {
        if (CommonUtil.isNetworkAvailable(activity)) {
            arrayDivision.clear();
            arrayClass.clear();
            dialog.show();
            app.getApiService().getClassDivision().enqueue(new Callback<Map<String, Object>>() {
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
                                JSONArray jsonArray = jobj.getJSONArray("class_data");
                                JSONArray jsonArray1 = jobj.getJSONArray("division_data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject c = jsonArray.getJSONObject(i);
                                    arrayClass.add(c.getString("class"));
                                }
                                for (int id = 0; id < jsonArray1.length(); id++) {
                                    JSONObject jsonObject = jsonArray1.getJSONObject(id);
                                    arrayDivision.add(jsonObject.getString("division"));
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
            CommonUtil.showBar(activity, ".activity.TrainerUploadGallaryPhotoActivity");
        }
    }

    private void setAdapter() {
        classAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arrayClass);
        spnrClass.setAdapter(classAdapter);
        divisionAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arrayDivision);
        spnrDiv.setAdapter(divisionAdapter);
    }

}
