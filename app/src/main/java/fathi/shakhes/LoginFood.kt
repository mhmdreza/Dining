package fathi.shakhes

import androidx.appcompat.app.AppCompatActivity
import android.widget.CheckBox
import android.graphics.Typeface
import android.os.Bundle
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject
import com.sdsmdg.tastytoast.TastyToast
import com.android.volley.VolleyError
import android.content.Intent
import org.json.JSONException
import android.view.WindowManager
import shakhes.R
import android.widget.EditText
import fathi.shakhes.helpers.Internet
import android.os.Build
import android.os.Vibrator
import android.os.VibrationEffect
import android.view.View
import android.widget.Button
import com.android.volley.Request
import kotlinx.android.synthetic.main.login_food.*
import java.lang.Exception

class LoginFood : AppCompatActivity() {
    var checkBox: CheckBox? = null
    var url_food_login = "https://dining.sharif.ir/api/login?"
    var url_food_profile = "https://dining.sharif.ir/api/profile?access_token="
    var typeFace: Typeface? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui()
        CreatingLogin()
    }

    fun foodLogin(username: String, pass: String) {
        val jsonObjReq = JsonObjectRequest(
            Request.Method.POST,
            url_food_login + "username=" + username + "&password=" + pass, null,
            { response: JSONObject ->
                try {
                    if (response.getString("status").matches(Regex("success"))) {
                        AppSharedPreferences.createFoodLoginSession(
                            username,
                            response.getString("access_token")
                        )
                        getFoodData(response.getString("access_token"), username)
                        if (AppSharedPreferences.getcheckboxstate_food()) {
                            AppSharedPreferences.saveuserfood(username)
                            AppSharedPreferences.savepassfood(pass)
                        }
                        if (!checkBox!!.isChecked) {
                            AppSharedPreferences.saveuserfood("")
                            AppSharedPreferences.savepassfood("")
                        }
                    } else {
                        TastyToast
                            .makeText(
                                applicationContext,
                                "نام کاربری یا گذرواژه اشتباه است !",
                                TastyToast.LENGTH_SHORT,
                                TastyToast.ERROR
                            )
                            .setMargin(0.015f, 0f)
                    }
                } catch (e: Exception) {
                    TastyToast
                        .makeText(
                            applicationContext,
                            "نام کاربری یا گذرواژه اشتباه است !",
                            TastyToast.LENGTH_SHORT,
                            TastyToast.ERROR
                        )
                        .setMargin(0.015f, 0f)
                }
            }) { error: VolleyError ->
            error.printStackTrace()
            TastyToast
                .makeText(
                    applicationContext,
                    error.message!!.toLowerCase(),
                    TastyToast.LENGTH_SHORT,
                    TastyToast.ERROR
                )
                .setMargin(0.015f, 0f)
        }

// Adding request to request queue
        MainApplication.getInstance().addToRequestQueue(jsonObjReq)
    }

    private fun getFoodData(acc: String, stunum: String) {
        val jsonObjReq = JsonObjectRequest(
            Request.Method.POST,
            url_food_profile + acc, null,
            { response: JSONObject ->
                try {
                    if (!response.getString("success").matches(Regex("true"))) {
                        TastyToast
                            .makeText(
                                applicationContext,
                                "اشکالی رخ داده است",
                                TastyToast.LENGTH_SHORT,
                                TastyToast.ERROR
                            )
                            .setMargin(0.015f, 0f)
                    } else {
                        AppSharedPreferences.insertFoodProfileData(
                            response.getString("name"), response.getString("family"),
                            stunum, response.getInt("balance").toString()
                        )
                        TastyToast
                            .makeText(
                                applicationContext,
                                "شما وارد شدید!",
                                TastyToast.LENGTH_SHORT,
                                TastyToast.SUCCESS
                            )
                            .setMargin(0.015f, 0f)
                        val FoodActivityIntent1 = Intent(this@LoginFood, FoodActivity::class.java)
                        startActivity(FoodActivityIntent1)
                        finish()
                    }
                } catch (e: Exception) {
                    try {
                        AppSharedPreferences.insertFoodProfileData(
                            response.getString("name"), response.getString("family"),
                            stunum, response.getInt("balance").toString()
                        )
                    } catch (e1: JSONException) {
                        e1.printStackTrace()
                    }
                    TastyToast
                        .makeText(
                            applicationContext,
                            "شما وارد شدید!",
                            TastyToast.LENGTH_SHORT,
                            TastyToast.SUCCESS
                        )
                        .setMargin(0.015f, 0f)
                    val FoodActivityIntent = Intent(this@LoginFood, FoodActivity::class.java)
                    startActivity(FoodActivityIntent)
                    finish()
                }
            }) { error: VolleyError ->
            error.printStackTrace()
            TastyToast
                .makeText(
                    applicationContext,
                    error.message!!.toLowerCase(),
                    TastyToast.LENGTH_SHORT,
                    TastyToast.ERROR
                )
                .setMargin(0.015f, 0f)
        }


// Adding request to request queue
        MainApplication.getInstance().addToRequestQueue(jsonObjReq)
    }

    private fun ui() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        supportActionBar!!.hide()
        setContentView(R.layout.login_food)
        food_login_checkbox.isChecked = AppSharedPreferences.getcheckboxstate_food()
    }

    private fun convertNum(`in`: String): String {
        var ret = `in`
        ret = ret.replace("۰", "0")
        ret = ret.replace("۱", "1")
        ret = ret.replace("۲", "2")
        ret = ret.replace("۳", "3")
        ret = ret.replace("۴", "4")
        ret = ret.replace("۵", "5")
        ret = ret.replace("۶", "6")
        ret = ret.replace("۷", "7")
        ret = ret.replace("۸", "8")
        ret = ret.replace("۹", "9")
        return ret
    }

    private fun CreatingLogin() {
        val btnLogin = findViewById<Button>(R.id.btn_login_food)
        val txtUsername = findViewById<EditText>(R.id.Txt_User_food)
        val txtPassword = findViewById<EditText>(R.id.Txt_Pass_food)
        if (AppSharedPreferences.getuserfood() !== "" || AppSharedPreferences.getpassfood() !== "") {
            txtUsername.setText(AppSharedPreferences.getuserfood())
            txtPassword.setText(AppSharedPreferences.getpassfood())
        }
        txtUsername.setSelection(txtUsername.text.length)
        txtPassword.setSelection(txtPassword.text.length)
        // Attached listener for login GUI button
        btnLogin.setOnClickListener {
            if (!Internet.IsConnectionAvailable(applicationContext)) {
                TastyToast
                    .makeText(
                        applicationContext,
                        "به اینترنت دسترسی ندارید!",
                        TastyToast.LENGTH_SHORT,
                        TastyToast.ERROR
                    )
                    .setMargin(0.015f, 0f)
                if (Build.VERSION.SDK_INT >= 26) {
                    (getSystemService(VIBRATOR_SERVICE) as Vibrator).vibrate(
                        VibrationEffect.createOneShot(
                            150,
                            10
                        )
                    )
                } else {
                    (getSystemService(VIBRATOR_SERVICE) as Vibrator).vibrate(150)
                }
            } else {
                EmptyLoginBox()
                if (txtUsername.text.toString()
                        .trim { it <= ' ' }.isNotEmpty() && txtPassword.text.toString()
                        .trim { it <= ' ' }.isNotEmpty()
                ) {
                    TastyToast
                        .makeText(
                            applicationContext,
                            "در حال ورود...",
                            TastyToast.LENGTH_SHORT,
                            TastyToast.DEFAULT
                        )
                        .setMargin(0.015f, 0f)
                    foodLogin(convertNum(txtUsername.text.toString()), txtPassword.text.toString())
                }
            }
        }
    }

    override fun onBackPressed() {
        val BackToMain = Intent(this@LoginFood, MainActivity::class.java)
        startActivity(BackToMain)
        finish()
    }

    fun EmptyLoginBox() {
        val txtUsername = findViewById<View>(R.id.Txt_User_food) as EditText
        val txtPassword = findViewById<View>(R.id.Txt_Pass_food) as EditText
        val email = txtUsername.text.toString()
        val password = txtPassword.text.toString()
        if (email.isEmpty()) {
            txtUsername.error = "لطفا اطلاعات را کامل کنید"
            if (Build.VERSION.SDK_INT >= 26) {
                (getSystemService(VIBRATOR_SERVICE) as Vibrator).vibrate(
                    VibrationEffect.createOneShot(
                        150,
                        10
                    )
                )
            } else {
                (getSystemService(VIBRATOR_SERVICE) as Vibrator).vibrate(150)
            }
        }
        if (password.isEmpty()) {
            txtPassword.error = "لطفا اطلاعات را کامل کنید"
            if (Build.VERSION.SDK_INT >= 26) {
                (getSystemService(VIBRATOR_SERVICE) as Vibrator).vibrate(
                    VibrationEffect.createOneShot(
                        150,
                        10
                    )
                )
            } else {
                (getSystemService(VIBRATOR_SERVICE) as Vibrator).vibrate(150)
            }
        } else {
            txtUsername.error = null
        }
    }

    fun FoodLoginCheckBox() {
        if (checkBox!!.isChecked) {
            AppSharedPreferences.setcheckboxstate_food(true)
        }
        if (!checkBox!!.isChecked) {
            AppSharedPreferences.setcheckboxstate_food(false)
        }
    }
}