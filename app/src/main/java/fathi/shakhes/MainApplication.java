package fathi.shakhes;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class MainApplication extends Application {

    public static final String TAG = MainApplication.class
            .getSimpleName();

    public static RequestQueue mRequestQueue;
    public static Cache cache ;

    private static MainApplication mInstance;


    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }

    public static synchronized MainApplication getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
       cache =  mRequestQueue.getCache();
        cache.initialize();
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }


}