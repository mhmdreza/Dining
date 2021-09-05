package fathi.shakhes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
import androidx.lifecycle.lifecycleScope
import fathi.shakhes.base.openFragment
import fathi.shakhes.fragments.SplashFragment
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
        openFragment(LoginFragment(), true)
    }

    fun shouldOpenLoginFragment(): Boolean = !AppSharedPreferences.shouldSaveLoginData
            || AppSharedPreferences.accessToken.isBlank()

}