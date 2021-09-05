package fathi.shakhes

import android.app.Application
import android.content.Context
import fathi.shakhes.MainApplication
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import android.text.TextUtils
import com.android.volley.Cache
import com.android.volley.Request
import kotlin.jvm.Synchronized

class MainApplication : Application() {
    val TAG = MainApplication::class.java
        .simpleName
    var mRequestQueue: RequestQueue? = null
    var cache: Cache? = null

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    private val requestQueue: RequestQueue
        get() {
            if (mRequestQueue == null) {
                mRequestQueue = Volley.newRequestQueue(applicationContext)
            }
            cache = mRequestQueue!!.cache.apply {
                initialize()
            }
            return mRequestQueue!!
        }

    fun <T> addToRequestQueue(req: Request<T>, tag: String?) {
        // set the default tag if tag is empty
        req.tag = if (TextUtils.isEmpty(tag)) TAG else tag
        requestQueue.add(req)
    }

    fun <T> addToRequestQueue(req: Request<T>) {
        req.tag = TAG
        requestQueue.add(req)
    }

    companion object {


        private lateinit var instance: MainApplication

        fun getAppContext(): Context {
            return instance.applicationContext
        }

        @Synchronized
        fun getInstance(): MainApplication {
            return instance
        }
    }
}