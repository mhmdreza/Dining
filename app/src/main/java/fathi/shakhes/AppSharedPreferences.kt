package fathi.shakhes

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import fathi.shakhes.MainApplication.Companion.getAppContext
import java.text.DecimalFormat
import java.util.HashMap


private const val PREF_NAME = "ShakhesPrefs"
const val KEY_USERNAME = "username"
const val KEY_ACCESS_TOKEN = "access_token"
const val FOOD_USERNAME = "fusername"
const val FOOD_ACC_T = "facc"
const val FOOD_FNAME = "ffname"
const val FOOD_LNAME = "flname"
const val FOOD_ACC = "faccount"
const val FOOD_PASS = "diningpass"
const val SAVE_LOGIN_DATA = "saveLoginData"
const val IS_ACCOUNT_NEGATIVE = "isAccountNegative"

object AppSharedPreferences {
    private val pref: SharedPreferences by lazy {
        getAppContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun createFoodLoginSession(
        username: String,
        password: String,
        accessToken: String
    ) {
        pref.edit {
            putString(FOOD_ACC_T, accessToken)
            if (shouldSaveLoginData) {
                putString(FOOD_USERNAME, username)
                putString(FOOD_PASS, password)
            }
        }
    }


    val username
        get() = pref.getString(FOOD_USERNAME, "")


    val password
        get() = pref.getString(FOOD_PASS, "")

    val accessToken: String
        get() = pref.getString(FOOD_ACC_T, "") ?: ""

    var shouldSaveLoginData: Boolean
        get() = true
        set(value) {
            pref.edit {
                putBoolean(SAVE_LOGIN_DATA, value)
            }
        }

    fun insertFoodProfileData(fname: String?, lname: String?, stunum: String?, account: String?) {
        val formatter = DecimalFormat("#,###")
        val account1 = account?.toFloatOrNull() ?: 0f
        isAccountNegative = account1 < 0
        val accountNumber = formatter.format(account1)
        pref.edit {
            putString(FOOD_ACC, accountNumber)
            putString(FOOD_FNAME, fname)
            putString(FOOD_LNAME, lname)
        }
    }

    val foodProfileData: Array<String?>
        get() {
            val ret = arrayOfNulls<String>(4)
            ret[0] = pref.getString(FOOD_FNAME, "")
            ret[1] = pref.getString(FOOD_LNAME, "")
            ret[2] = pref.getString(FOOD_USERNAME, "")
            ret[3] = pref.getString(FOOD_ACC, "")
            return ret
        }

    val diningData: HashMap<String?, String?>
        get() = hashMapOf(
            KEY_ACCESS_TOKEN to pref.getString(FOOD_ACC_T, null),
        )

    fun logoutDining() {
        pref.edit {
            remove("data_food_json")
            remove("data_food_temp")
        }
    }

    var isAccountNegative: Boolean
        get() = pref.getBoolean(IS_ACCOUNT_NEGATIVE, false)
        set(isNegative) {
            pref.edit {
                putBoolean(IS_ACCOUNT_NEGATIVE, isNegative)
            }
        }
}