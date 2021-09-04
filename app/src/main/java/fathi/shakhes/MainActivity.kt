package fathi.shakhes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import fathi.shakhes.MainActivity
import shakhes.R
import android.widget.TextView
import android.graphics.Typeface
import android.content.Intent
import android.os.Handler
import androidx.lifecycle.lifecycleScope
import fathi.shakhes.LoginFood
import fathi.shakhes.base.openFragment
import fathi.shakhes.helpers.SplashFragment
import kotlinx.coroutines.delay

class MainActivity : AppCompatActivity() {
    init {
        lifecycleScope.launchWhenResumed {
            openFragment(SplashFragment(), false)
            delay(3000)
            val mainIntent = Intent(this@MainActivity, LoginFood::class.java)
            this@MainActivity.startActivity(mainIntent)
            finish()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

}