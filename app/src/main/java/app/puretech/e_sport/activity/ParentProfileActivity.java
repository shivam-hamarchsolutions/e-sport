package app.puretech.e_sport.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.RequestBody;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.puretech.e_sport.App;
import app.puretech.e_sport.BuildConfig;
import app.puretech.e_sport.R;
import app.puretech.e_sport.model.ParentProfiledModel;
import app.puretech.e_sport.model.TrainerGallaryUploadModel;
import app.puretech.e_sport.utill.AppPreferences;
import app.puretech.e_sport.utill.Base64;
import app.puretech.e_sport.utill.CommonUtil;
import app.puretech.e_sport.utill.DataManager;
import app.puretech.e_sport.utill.ImageUtil;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class ParentProfileActivity extends AppCompatActivity {

    App app;
    Activity activity;
    private CircleImageView civ_User_Profile;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2;
    private Uri uri;
    private Toolbar toolbar;
    Intent iBack;
    private TextView tv_school_name, tv_email, tv_mobile, tv_address, tv_name;
    File photoFile = null;
    public String photoPath = null;
    public File imageFilePath;
    public String base64 = null;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);*/
        setContentView(R.layout.activity_parent_profile);
        doInit();
    }

    private void doInit() {
        app = (App) getApplication();
        activity = this;
        dialog = CommonUtil.getCircularProgressDialog(activity, R.string.please_wait, false);
        civ_User_Profile = findViewById(R.id.civ_Trainer_profile);
        toolbar = findViewById(R.id.toolbar_home);
        iBack = new Intent(activity, ParentHomeActivity.class);
        tv_name = findViewById(R.id.tv_name);
        tv_school_name = findViewById(R.id.tv_school_name);
        tv_mobile = findViewById(R.id.tv_mobile);
        tv_address = findViewById(R.id.tv_address);
        tv_email = findViewById(R.id.tv_email);
        tv_name.setText(AppPreferences.init(activity).getString("name", ""));
        tv_email.setText(AppPreferences.init(activity).getString("email", ""));
        tv_mobile.setText(AppPreferences.init(activity).getString("mobile", ""));
        tv_address.setText(AppPreferences.init(activity).getString("address", ""));

        setProfileImg();

        doBack(toolbar);
        civ_User_Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkAndRequestPermissions()) {
                    selectImage();
                }
            }
        });
    }

    private void setProfileImg() {
    /*    Picasso.with(this)
                .load(DataManager.urlParentPic + AppPreferences.init(activity).getString("parent_image", ""))
                .error(R.drawable.user)
                .into(civ_User_Profile);*/
       ImageUtil.displayImageParent(civ_User_Profile, AppPreferences.init(activity).getString("parent_image", ""), null);
    }

    private boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[0]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

                        // process the normal flow
                        //else any one or both the permissions are not granted
                    } else {
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                            showDialogOK(
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkAndRequestPermissions();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    break;
                                            }
                                        }
                                    });
                        }
                        //permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                        else {
                            Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG)
                                    .show();
                            //                            //proceed with logic by disabling the related features or quit the app.
                        }
                    }
                }
            }
        }

    }

    private void showDialogOK(DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage("Camera and Storage  Permission required for this app")
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }

    // Select image from camera and gallery
    private void selectImage() {
        try {
            PackageManager pm = getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                final CharSequence[] options = {"Take Photo", "Choose From Gallery", "Cancel"};
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(activity);
                builder.setTitle("Select Option");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Take Photo")) {
                            dialog.dismiss();
                          /*  StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                            StrictMode.setVmPolicy(builder.build());
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            File file = new File(Environment.getExternalStorageDirectory(),
                                    "Defect.jpg");
                            uri = Uri.fromFile(file);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                            startActivityForResult(intent, PICK_IMAGE_CAMERA);*/
                            CallCamera(PICK_IMAGE_CAMERA);
                        } else if (options[item].equals("Choose From Gallery")) {
                            dialog.dismiss();
                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);//
                            startActivityForResult(Intent.createChooser(intent, "Select File"), PICK_IMAGE_GALLERY);

                        /*    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);*/
                        } else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            } else
                Toast.makeText(this, "Camera Permission error", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Camera Permission error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void CallCamera(int pick_image_camera) {
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

                Uri photoURI = FileProvider.getUriForFile(ParentProfileActivity.this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        photoFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, pick_image_camera);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String mFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File mFile = File.createTempFile(mFileName, ".jpg", storageDir);

        imageFilePath = mFile;
        return mFile;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_CAMERA) {
         /*   try {
                Bitmap tempBitmap;
                tempBitmap = BitmapFactory.decodeStream(
                        getContentResolver().openInputStream(uri));

                civ_User_Profile.setImageBitmap(tempBitmap);

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }*/
//            displayImage();

            try {
                if (photoPath != null) {
                    Log.d("xxxyy", "file not null : ");
                    Bitmap myBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                    Bitmap bm = rotateImage(myBitmap, 90);
                    String b64 = Base64.encodeImage(bm);
                    base64 = "data:image/jpeg;base64," + b64;
                    updateProfilePhoto(base64);
                    // civ_User_Profile.setImageBitmap(Base64.getBitmap(b64));
                }
            } catch (Exception e) {

            }
        } else if (requestCode == PICK_IMAGE_GALLERY) {
            onSelectFromGalleryResult(data, PICK_IMAGE_GALLERY);

           /* try {
                Uri selectedImage = data.getData();
                Bitmap tempBitmap;
                tempBitmap = BitmapFactory.decodeStream(
                        getContentResolver().openInputStream(selectedImage));
                tempBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                //tempBitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                //Log.e("Activity", "Pick from Gallery::>>> ");

                civ_User_Profile.setImageBitmap(tempBitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }*/
        }
    }

    private void updateProfilePhoto(String base64) {

        if (CommonUtil.isNetworkAvailable(activity)) {
            dialog.show();
            RequestBody requestBody = new MultipartBuilder()
                    .type(MultipartBuilder.FORM)
                    .addFormDataPart("parent_img", base64)
                    .build();
            app.getApiService().uploadParnetProfile(requestBody).enqueue(new Callback<ParentProfiledModel>() {
                @Override
                public void onResponse(Response<ParentProfiledModel> response, Retrofit retrofit) {
                    dialog.dismiss();
                    ParentProfiledModel model = response.body();
                    if (model.getMessage().equals("Succes")) {
                        AppPreferences.init(activity).setString("parent_image", model.getIamge());
                        setProfileImg();
                    } else {
                        app.showSnackBar(activity, "Something went wrong!");
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    dialog.dismiss();
                    Log.d("ffff", "onFailure: "+t.getMessage());
                    app.showSnackBar(activity, "Something went wrong!");
                }
            });
        } else {
            CommonUtil.showBar(activity, ".activity.TrainerUploadGallaryPhotoActivity");
        }
    }

    private void onSelectFromGalleryResult(Intent data, int pick_image_gallery) {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                Bitmap bmp = rotateImage(bm, 90);
                String b64 = Base64.encodeImage(bm);
                base64 = "data:image/jpeg;base64," + b64;
                updateProfilePhoto(base64);
                //   civ_User_Profile.setImageBitmap(Base64.getBitmap(b64));
                Log.d("xxxyy", "onSelectFromGalleryResult: " + base64);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    private void doBack(Toolbar toolbar) {
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(iBack);
                activity.finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        activity.startActivity(iBack);
        activity.finish();
    }
}
