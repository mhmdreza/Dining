package fathi.shakhes.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

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

fun Fragment.openBottomSheet(bottomSheetDialogFragment: BottomSheetDialogFragment) {
    if (activity == null || activity?.isFinishing == true) return

    bottomSheetDialogFragment.show(parentFragmentManager, "bottomSheet")
}


fun Fragment.setSupportFragmentResult(requestKey: String, result: Bundle = Bundle()) {
    activity?.supportFragmentManager?.setFragmentResult(requestKey, result)
}