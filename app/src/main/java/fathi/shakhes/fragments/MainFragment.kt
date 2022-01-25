package fathi.shakhes.fragments

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
import androidx.lifecycle.lifecycleScope
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import fathi.shakhes.AppSharedPreferences
import fathi.shakhes.AppSharedPreferences.isAccountNegative
import fathi.shakhes.AppSharedPreferences.logoutDining
import fathi.shakhes.MainApplication
import fathi.shakhes.base.BaseFragment
import fathi.shakhes.base.openFragment
import fathi.shakhes.base.runMain
import fathi.shakhes.connections.ApiHelper
import fathi.shakhes.connections.PictureModel
import fathi.shakhes.connections.ResultWrapper
import fathi.shakhes.fragments.message.TabMessageFragment
import fathi.shakhes.fragments.tables.TabTableFragment
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import shakhes.R
import java.text.DecimalFormat

class MainFragment : BaseFragment(R.layout.fragment_main) {
    private var urlFoodPicture = "https://dining.sharif.ir/api/picture?access_token="

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showWeekFood.setOnClickListener {
            openFragment(TabTableFragment(), true)
        }
        daysale.setOnClickListener {
            openFragment(DaySaleFragment(), true)
        }
        messages.setOnClickListener {
            openFragment(TabMessageFragment(), true)
        }
        contactus.setOnClickListener {
            openFragment(ContactUsFragment(), true)
        }

        logout.setOnClickListener {
            logoutDining()
            runMain()

        }

        fetchPicture()

        foodprofile_name.text = AppSharedPreferences.fullName
        foodprofile_charge.text = AppSharedPreferences.username
        foodprofile_id.text = DecimalFormat("#,###").format(AppSharedPreferences.balance)


        foodprofile_id.setTextColor(if (isAccountNegative) Color.RED else Color.BLACK)
    }


    private fun fetchPicture() = lifecycleScope.launch {
        when (val result = ApiHelper.getPicture()) {
            is ResultWrapper.Success -> {
                setProfilePicture(result.value)
            }
            else -> {
                val a = 'a'
            }
        }
    }

    private fun setProfilePicture(picture: PictureModel) {
        if (!picture.success) return
        val decodedString = Base64.decode(picture.picture, Base64.DEFAULT)

        BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)?.let {
            image.setImageBitmap(Bitmap.createScaledBitmap(it, it.width, it.height, false))
            image.layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT,WRAP_CONTENT)
        }
    }
}