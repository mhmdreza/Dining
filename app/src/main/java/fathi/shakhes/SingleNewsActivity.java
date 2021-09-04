package fathi.shakhes;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.TimeZone;

import fathi.shakhes.helpers.Internet;
import fathi.shakhes.helpers.JalaliCalendar;
import shakhes.R;

public class SingleNewsActivity extends AppCompatActivity {
    Typeface typeFace;
    String id = "",
            url_dining_profile = "http://dining.sharif.ir/api/news/",
            url_dining_webpage = "https://dining.sharif.ir/news/default/view?id=";
    TextView title, time, abst, loading;
    private WebView mywebview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_single_news_dining);
        mywebview = findViewById(R.id.web_dining);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setLogo(R.drawable.shariflogo);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        View v = LayoutInflater
                .from(getSupportActionBar().getThemedContext())
                .inflate(R.layout.action_bar, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT);
        getSupportActionBar().setCustomView(v, params);

        typeFace = Typeface.createFromAsset(getAssets(), "fonts/IRANSans.ttf");
        TextView myTextView2a = findViewById(R.id.text2);

        myTextView2a.setText("اداره امور تغذیه");
        myTextView2a.setTypeface(typeFace);
        id = getIntent().getStringExtra("id");
        title = findViewById(R.id.title);
        time = findViewById(R.id.time);
        abst = findViewById(R.id.abst);
        loading = findViewById(R.id.loading);


        if (!id.equals("")) {
            diningRequest();
        }
        TxtPresentation();


        if (!Internet.IsConnectionAvailable(getApplicationContext())) {
            TastyToast
                    .makeText(getApplicationContext(), "به اینترنت دسترسی ندارید!", TastyToast.LENGTH_SHORT, TastyToast.ERROR)
                    .setMargin(0.015f, 0f);
        }

        DoRefreshPage();
    }

    private void TxtPresentation() {
        title.setTypeface(typeFace);
        time.setTypeface(typeFace);
        abst.setTypeface(typeFace);
        loading.setTypeface(typeFace);
        title.setTextSize(15);
        time.setTextSize(10);
        abst.setTextSize(13);
        loading.setTextSize(18);
    }

    private void diningRequest() {
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.POST,
                url_dining_profile + id, null,
                response -> {
                    System.out.println(response);
                    try {
                        JSONObject tmp = response.getJSONObject(0);
                        title.setText(convertNums(tmp.getString("title")));
                        JalaliCalendar calendar;
                        calendar = new JalaliCalendar(getDateTimeFromTimestamp(tmp.getLong("create_date") * 1000));
                        time.setText(convertNums(calendar.date + " " + calendar.strMonth + " " + calendar.year));
                        abst.setText(convertNums(tmp.getString("summary")));
                        loadweb(url_dining_webpage + id);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, Throwable::printStackTrace);

        MainApplication.Companion.getInstance().addToRequestQueue(jsonObjReq);
    }

    public Date getDateTimeFromTimestamp(Long value) {
        TimeZone timeZone = TimeZone.getDefault();
        long offset = timeZone.getOffset(value);
        if (offset < 0) {
            value -= offset;
        } else {
            value += offset;
        }
        return new Date(value);
    }

    public void loadweb(String WebAddress) {
        mywebview.getSettings().setBuiltInZoomControls(true);
        mywebview.setWebViewClient(new WebViewClient() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                if (url.startsWith("http://") || url.startsWith("https://")) {
                    view.getContext().startActivity(
                            new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    return true;
                } else {
                    return false;
                }
            }


            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                loading.setVisibility(View.GONE);
                mywebview.setVisibility(View.VISIBLE);

            }
        });
        mywebview.getSettings().setDisplayZoomControls(false);
        mywebview.getSettings().setLoadWithOverviewMode(true);
        mywebview.getSettings().setUseWideViewPort(true);
        mywebview.getSettings().setJavaScriptEnabled(true);
        mywebview.loadUrl(WebAddress);
    }

    private String convertNums(String input) {
        String ret = input;
        ret = ret.replaceAll("0", "۰");
        ret = ret.replaceAll("1", "۱");
        ret = ret.replaceAll("2", "۲");
        ret = ret.replaceAll("3", "۳");
        ret = ret.replaceAll("4", "۴");
        ret = ret.replaceAll("5", "۵");
        ret = ret.replaceAll("6", "۶");
        ret = ret.replaceAll("7", "۷");
        ret = ret.replaceAll("8", "۸");
        ret = ret.replaceAll("9", "۹");
        return ret;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void DoRefreshPage() {
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.refresh);
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            startActivity(getIntent());
            finish();
        });
    }

}
