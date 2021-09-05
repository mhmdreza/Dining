package fathi.shakhes.connections

import com.google.gson.annotations.SerializedName

data class LoginModel(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("status") val status: String,
)
