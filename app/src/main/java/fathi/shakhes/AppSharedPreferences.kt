package fathi.shakhes

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import fathi.shakhes.MainApplication.Companion.getAppContext
import java.text.DecimalFormat


private const val PREF_NAME = "ShakhesPrefs"
const val USERNAME = "username"
const val PASSWORD = "password"
const val FULL_NAME = "fullName"
const val ACCESS_TOKEN = "accessToken"
const val BALANCE = "balance"
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
            putString(ACCESS_TOKEN, accessToken)
            if (shouldSaveLoginData) {
                putString(USERNAME, username)
                putString(PASSWORD, password)
            }
        }
    }


    val username
        get() = pref.getString(USERNAME, "")


    val password
        get() = pref.getString(PASSWORD, "")

    val accessToken: String
        get() = pref.getString(ACCESS_TOKEN, "") ?: ""

    var shouldSaveLoginData: Boolean
        get() = true
        set(value) {
            pref.edit {
                putBoolean(SAVE_LOGIN_DATA, value)
            }
        }

    fun insertFoodProfileData(firstName: String, lastName: String, balance: Int) {
        val formatter = DecimalFormat("#,###")
        isAccountNegative = balance < 0
        pref.edit {
            putInt(BALANCE, balance)
            putString(FULL_NAME, "$firstName $lastName")
        }
    }

    val fullName: String
        get() = pref.getString(FULL_NAME, "") ?: ""

    val balance: Int
        get() = pref.getInt(BALANCE, 0)


    fun logoutDining() {
        pref.edit {
            remove(ACCESS_TOKEN)
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