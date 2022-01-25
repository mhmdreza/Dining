package fathi.shakhes.connections

import fathi.shakhes.connections.RetrofitBuilder.serverApiService
import okhttp3.ResponseBody

object ApiHelper {

    suspend fun getReservedTable(placeId: String, startDate: String): ResultWrapper<ArrayList<Reserved>> {
        return safeApiCall {
            serverApiService.getReservedTable(placeId, startDate)
        }
    }
    suspend fun getPicture(): ResultWrapper<PictureModel>{
        return safeApiCall {
            serverApiService.getPicture()
        }
    }
    suspend fun getFoodPlaces(): ResultWrapper<ResponseBody>{
        return safeApiCall {
            serverApiService.getFoodPlaces()
        }
    }

    suspend fun getTableReserve(mealId: String, date: String): ResultWrapper<ResponseBody>{
        return safeApiCall {
            serverApiService.getTableReserve(mealId, date)
        }
    }
}