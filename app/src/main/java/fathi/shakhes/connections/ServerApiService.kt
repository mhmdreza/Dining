package fathi.shakhes.connections

import com.google.gson.annotations.SerializedName
import fathi.shakhes.AppSharedPreferences
import okhttp3.ResponseBody
import retrofit2.http.POST
import retrofit2.http.Query

interface ServerApiService {
    @POST("reserve-table")
    suspend fun getReservedTable(
        @Query("place_id") placeId: String,
        @Query("start_date") startDate: String,
        @Query(
            "access_token",
            encoded = true,
        ) accessToken: String = AppSharedPreferences.accessToken,
    ): ArrayList<Reserved>

    @POST("picture")
    suspend fun getPicture(
        @Query(
            "access_token",
            encoded = true,
        ) accessToken: String = AppSharedPreferences.accessToken,
    ): PictureModel

    @POST("food-places")
    suspend fun getFoodPlaces(
        @Query(
            "access_token",
            encoded = true,
        ) accessToken: String = AppSharedPreferences.accessToken,
    ): ResponseBody

    @POST("reserve-status-text")
    suspend fun getTableReserve(
        @Query("food_meal_id") mealId: String,
        @Query("date") date: String,
        @Query(
            "access_token",
            encoded = true,
        ) accessToken: String = AppSharedPreferences.accessToken,
    ): ResponseBody

}

data class PictureModel(
    @SerializedName("success") val success: Boolean,
    @SerializedName("picture") val picture: String,
)
data class Reserved(
    @SerializedName("day_name") val dayName: String,
    @SerializedName("day_date") val dayDate: String,
)
