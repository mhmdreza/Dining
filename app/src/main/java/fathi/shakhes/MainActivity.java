package fathi.shakhes;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import shakhes.R;

public class MainActivity extends AppCompatActivity {
    boolean BackPress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar4);
        setSupportActionBar(toolbar);
        getSupportActionBar().setLogo(R.drawable.shariflogo);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        View v = LayoutInflater
                .from(getSupportActionBar().getThemedContext())
                .inflate(R.layout.action_bar1, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT);
        getSupportActionBar().setCustomView(v, params);

        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/IRANSans.ttf");
        TextView myTextView1 = findViewById(R.id.text1);
        TextView myTextView2 = findViewById(R.id.text2);
        myTextView1.setTypeface(typeFace);
        myTextView2.setTypeface(typeFace);
    }

    public void food(View view) {
        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent FoodIntent = new Intent(MainActivity.this, LoginFood.class);
            startActivity(FoodIntent);
        }, 270);
    }

    @Override
    public void onBackPressed() {
        if (BackPress) {
            super.onBackPressed();
            overridePendingTransition(0, R.anim.fade_out);
            MainActivity.this.finish();
        }
        this.BackPress = true;
        Toast.makeText(getApplicationContext(), "برای خروج دو بار کلیک کنید", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(() -> BackPress = false, 2000);
    }

}