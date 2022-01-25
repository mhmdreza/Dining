package fathi.shakhes

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
import androidx.lifecycle.lifecycleScope
import fathi.shakhes.base.openFragment
import fathi.shakhes.fragments.LoginFragment
import fathi.shakhes.fragments.MainFragment
import fathi.shakhes.fragments.SplashFragment
import fathi.shakhes.helpers.showExitAppDialog
import kotlinx.coroutines.delay
import shakhes.R

class MainActivity : AppCompatActivity() {
    init {
        lifecycleScope.launchWhenCreated {
            delay(2000)
            openNextFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        openFragment(SplashFragment(), true)
    }

    private fun openNextFragment() {
        supportFragmentManager.popBackStack(null, POP_BACK_STACK_INCLUSIVE)
        openFragment(
            if (shouldOpenLoginFragment()) LoginFragment() else MainFragment(), false
        )
    }

    private fun shouldOpenLoginFragment(): Boolean = !AppSharedPreferences.shouldSaveLoginData
            || AppSharedPreferences.accessToken.isBlank()

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        openNextFragment()
    }

    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount
        if (count == 0) {
            showExitAppDialog(this)
        } else {
            supportFragmentManager.popBackStack()
        }
    }

}