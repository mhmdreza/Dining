package fathi.shakhes.base

import android.content.Intent
import androidx.fragment.app.Fragment
import fathi.shakhes.MainActivity

abstract class BaseFragment : Fragment {

    constructor() : super()
    constructor(contentLayoutId: Int) : super(contentLayoutId)
}

fun Fragment.navigateBack() {
    parentFragmentManager.popBackStack()
}

fun Fragment.openFragment(fragment: Fragment, addToBackStack: Boolean) {
    activity?.openFragment(fragment, addToBackStack)
}

fun BaseFragment.runMain() {
    context?.let {
        Intent(activity, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            it.startActivity(this)
        }
    }
}