package fathi.shakhes

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.google.gson.Gson
import fathi.shakhes.base.BaseFragment
import fathi.shakhes.connections.LoginModel
import fathi.shakhes.helpers.Internet
import fathi.shakhes.helpers.asString
import fathi.shakhes.helpers.hasText
import kotlinx.android.synthetic.main.fragment_login.*
import org.json.JSONException
import org.json.JSONObject
import shakhes.R
import java.util.*

class LoginFragment : BaseFragment(R.layout.fragment_login) {
    private var url_food_login = "https://dining.sharif.ir/api/login?"
    private var url_food_profile = "https://dining.sharif.ir/api/profile?access_token="

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginCheckbox.isChecked = AppSharedPreferences.shouldSaveLoginData
        loginCheckbox.setOnClickListener {
            foodLoginCheckBoxClick()
        }
        initUi()
    }

    private fun login(username: String, pass: String) {
        val jsonObjReq = JsonObjectRequest(
            Request.Method.POST,
            url_food_login + "username=" + username + "&password=" + pass, null,
            getSuccessListener(username, pass), errorHandler
        )

        MainApplication.getInstance().addToRequestQueue(jsonObjReq)
    }

    private fun getSuccessListener(
        username: String,
        password: String
    ) = Response.Listener { response: JSONObject ->
        try {
            val model = Gson().fromJson(response.toString(), LoginModel::class.java)

            if (model.status == "success") {
                AppSharedPreferences.createFoodLoginSession(
                    username, password,
                    model.accessToken
                )
                getFoodData(model.accessToken, username)

            } else {
                wrongLoginData()
            }
        } catch (e: Exception) {
            wrongLoginData()
        }

    }

    private fun wrongLoginData() {
        Toast.makeText(
            context,
            "نام کاربری یا گذرواژه اشتباه است !",
            Toast.LENGTH_SHORT,
        ).show()
    }

    private val errorHandler = Response.ErrorListener { error ->
        error.printStackTrace()
        val message = error.message ?: return@ErrorListener
        Toast.makeText(
            context,
            message.toLowerCase(Locale.getDefault()),
            Toast.LENGTH_SHORT,
        ).show()

    }

    private fun getFoodData(acc: String, stunum: String) {
        val jsonObjReq = JsonObjectRequest(
            Request.Method.POST,
            url_food_profile + acc, null,
            { response: JSONObject ->
                try {
                    if (!response.getString("success").matches(Regex("true"))) {
                        Toast.makeText(
                            context,
                            "اشکالی رخ داده است",
                            Toast.LENGTH_SHORT,
                        ).show()
                    } else {
                        AppSharedPreferences.insertFoodProfileData(
                            response.getString("name"), response.getString("family"),
                            stunum, response.getInt("balance").toString()
                        )
                        enterApp()
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
                    enterApp()
                }
            }) { error: VolleyError ->
            error.printStackTrace()
            Toast.makeText(
                context,
                error.message!!.toLowerCase(Locale.ROOT),
                Toast.LENGTH_SHORT,
            ).show()
        }


        MainApplication.getInstance().addToRequestQueue(jsonObjReq)
    }

    private fun enterApp() {
        Toast.makeText(
            context,
            "شما وارد شدید!",
            Toast.LENGTH_SHORT,
        ).show()
        val foodActivityIntent1 =
            Intent(activity, FoodActivity::class.java)
        startActivity(foodActivityIntent1)
    }

    private fun convertNum(ret: String) = ret.replace("۰", "0")
        .replace("۱", "1")
        .replace("۲", "2")
        .replace("۳", "3")
        .replace("۴", "4")
        .replace("۵", "5")
        .replace("۶", "6")
        .replace("۷", "7")
        .replace("۸", "8")
        .replace("۹", "9")

    private fun initUi() {
        usernameInput.setText(AppSharedPreferences.username)
        passwordInput.setText(AppSharedPreferences.password)

        // Attached listener for login GUI button
        loginButton.setOnClickListener {
            handleLogin()
        }
    }

    private fun handleLogin() {
        if (!Internet.IsConnectionAvailable(context)) {
            Toast.makeText(context, R.string.no_internet_access, Toast.LENGTH_SHORT).show()
        } else {
            handleEmptyLoginBox()
            if (usernameInput.hasText && passwordInput.hasText) {
                Toast.makeText(context, R.string.login_loading, Toast.LENGTH_SHORT).show()
                login(convertNum(usernameInput.asString), passwordInput.asString)
            }

        }
    }

    private fun handleEmptyLoginBox() {
        val email = usernameInput.text.toString()
        val password = passwordInput.text.toString()
        if (email.isEmpty()) {
            usernameInput.error = "لطفا اطلاعات را کامل کنید"
        }
        if (password.isEmpty()) {
            passwordInput.error = "لطفا اطلاعات را کامل کنید"
        } else {
            usernameInput.error = null
        }
    }

    private fun foodLoginCheckBoxClick() {
        AppSharedPreferences.shouldSaveLoginData = loginCheckbox.isChecked
    }
}