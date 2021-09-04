package fathi.shakhes;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;
import shakhes.R;

public class SplashActivity extends AppCompatActivity {
    Handler handler;
    public static  boolean SplashHistory = true ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SplashHistory) {
            getSupportActionBar().hide();
            setContentView(R.layout.activity_splash);
            TextView textView1 = findViewById(R.id.textView1);
            TextView textView2 = findViewById(R.id.textView2);
            TextView textView3 = findViewById(R.id.textView3);

            Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/IRANSans.ttf");
            textView1.setTypeface(typeFace);
            textView2.setTypeface(typeFace);
            textView3.setTypeface(typeFace);
            handler = new Handler() ;
            handler.postDelayed(() -> {
                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }, 1000);
          SplashHistory = false ;
        }
        else {
            Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
            SplashActivity.this.startActivity(mainIntent);
            SplashActivity.this.finish();
        }
    }

}