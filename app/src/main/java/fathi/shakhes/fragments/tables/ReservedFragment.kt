package fathi.shakhes.fragments.tables

import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import fathi.shakhes.AppSharedPreferences
import fathi.shakhes.MainApplication.Companion.getInstance
import fathi.shakhes.base.BaseFragment
import fathi.shakhes.connections.ApiHelper
import fathi.shakhes.connections.ResultWrapper
import fathi.shakhes.helpers.PersianDateConverter
import kotlinx.android.synthetic.main.content_table_week.*
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import shakhes.R
import java.text.SimpleDateFormat
import java.util.*

class ReservedFragment : BaseFragment(R.layout.content_table_week) {
    private var urlFoodTable = "https://dining.sharif.ir/api/reserve-table?access_token="
    private var urlFoodTableReserve = "https://dining.sharif.ir/api/reserve-status-text?access_token="
    private val daysData = arrayListOf<String>()
    private val foodNameData = arrayListOf<String>()
    private val mealNameData = arrayListOf<String>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getFoodPlaces()
    }


    fun getData(key: String) {
        val c = Calendar.getInstance()
        val df = SimpleDateFormat("yyyy-MM-dd")
        val formattedDate = df.format(c.time)
        getReservedTable(key, formattedDate)
    }

    private fun getReservedTable(key: String, formattedDate: String) = lifecycleScope.launch {
        when (val result = ApiHelper.getReservedTable(key, formattedDate)) {
            is ResultWrapper.Success -> {
                recyclerView.adapter = ReservedAdapter().apply {
                    setData(result.value)
                }
//                result.value.forEachIndexed {index , reserved ->
//                     daysData.add("""
//                    ${reserved.dayName}
//                    ${reserved.dayDate}
//                    """.trimIndent())
//                     val parts = reserved.dayDate.split("/".toRegex()).toTypedArray()
//                     val cc = PersianDateConverter().apply {
//                         setIranianDate(parts[0].toInt(), parts[1].toInt(), parts[2].toInt())
//                     }
//                     cc.gregorianDate
//                     val inputdate = cc.gregorianDate.replace("/", "-")
//                     tableReserve("1", inputdate, index)
//                     tableReserve("2", inputdate, index)
//                 }
            }
            else -> {
                val a = 'a'
            }
        }
    }

    private fun getFoodPlaces() = lifecycleScope.launch {
        when (val result = ApiHelper.getFoodPlaces()) {
            is ResultWrapper.Success -> {
                val res = JSONObject(result.value.string())
                getData(res.keys().next() as String)
            }
            else -> {
                val a = 'a'
            }
        }
    }


    companion object {
        @JvmStatic
        fun newInstance(): ReservedFragment {
            return ReservedFragment()
        }
    }
}