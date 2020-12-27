package app.puretech.e_sport.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import app.puretech.e_sport.App;
import app.puretech.e_sport.R;

/* testing commit */
public class AboutUsActivity extends AppCompatActivity {

    private ImageView iv_instagram;
    private ImageView iv_twiter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_about_us);
        doInit();
    }

    private void doInit() {
        App app = (App) getApplication();
        Activity activity = this;
        toolbar = findViewById(R.id.toolbar_home);
        ImageView iv_facebook = findViewById(R.id.iv_facebook);
        iv_instagram = findViewById(R.id.iv_instagram);
        iv_twiter = findViewById(R.id.iv_twitter);
        iv_facebook.setOnClickListener(v -> {
            Intent viewIntent =
                    new Intent("android.intent.action.VIEW",
                            Uri.parse("https://www.facebook.com"));
            startActivity(viewIntent);
        });
        iv_instagram.setOnClickListener(v -> {
            Intent viewIntent =
                    new Intent("android.intent.action.VIEW",
                            Uri.parse("https://www.instagram.com/"));
            startActivity(viewIntent);
        });
        iv_twiter.setOnClickListener(v -> {
            Intent viewIntent =
                    new Intent("android.intent.action.VIEW",
                            Uri.parse("https://twitter.com/"));
            startActivity(viewIntent);
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
