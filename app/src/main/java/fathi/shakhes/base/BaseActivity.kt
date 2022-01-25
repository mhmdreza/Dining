package fathi.shakhes.base

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import shakhes.R

abstract class BaseActivity : AppCompatActivity()

fun Activity.openFragment(fragment: Fragment, addToBackStack: Boolean) {
    if (this !is AppCompatActivity || isFinishing) return
    val transaction = supportFragmentManager
        .beginTransaction()
        .replace(R.id.container, fragment)

    if (addToBackStack) {
        transaction.addToBackStack(fragment.javaClass.name)
    }
    transaction.commit()
}