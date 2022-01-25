package fathi.shakhes.fragments

import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import fathi.shakhes.AppSharedPreferences.accessToken
import fathi.shakhes.MainApplication.Companion.getInstance
import fathi.shakhes.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_daysale.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import shakhes.R
import java.text.SimpleDateFormat
import java.util.*

class DaySaleFragment : BaseFragment(R.layout.fragment_daysale) {
    var urlFoodPlace = "https://dining.sharif.ir/api/food-places?access_token="
    var urlFoodTable = "https://dining.sharif.ir/api/reserve-table?access_token="
    var urlFoodStatus = "https://dining.sharif.ir/api/reserve-status?access_token="
    var urlFoodReserve = "https://dining.sharif.ir/api/daysale-buy?access_token="
    var ids = arrayListOf<String>()
    var names = arrayListOf<String>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val toolbar = findViewById<Toolbar>(R.id.toolbar)
//        setSupportActionBar(toolbar)
//        supportActionBar!!.setLogo(R.drawable.shariflogo)
//        supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
//        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
//        val v = LayoutInflater
//            .from(supportActionBar!!.themedContext)
//            .inflate(R.layout.action_bar, null)
//        val params = ActionBar.LayoutParams(
//            ActionBar.LayoutParams.MATCH_PARENT,
//            ActionBar.LayoutParams.MATCH_PARENT
//        )
//        supportActionBar!!.setCustomView(v, params)
//        val myTextView2a = findViewById<View>(R.id.text2) as TextView
//        myTextView2a.text = "روز خرید"

        MyTask().execute("soss")
        doRefreshPage()
    }

    private inner class MyTask() : AsyncTask<String?, Int?, String>() {

        override fun doInBackground(vararg params: String?): String {
            table
            return "this string is passed to onPostExecute"
        }
    }

    //ToDo check returning date
    val table: Unit
        get() {
            val jsonObjReq: JsonObjectRequest = object : JsonObjectRequest(Method.POST,
                urlFoodPlace + accessToken, null,
                Response.Listener { response: JSONObject ->
                    try {
                        daysale_refresh.isRefreshing = false
                        //ToDo check returning date
                        var i = 0
                        val iterator: Iterator<*> = response.keys()
                        while (iterator.hasNext()) {
                            ids[i] = iterator.next() as? String ?: ""
                            names[i] = response.getString(ids[i])
                            when (ids[i]) {
                                "31", "35" -> getData(ids[i])
                            }
                            i++
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }, Response.ErrorListener { obj: VolleyError ->
                    daysale_refresh.isRefreshing = false
                    obj.printStackTrace()
                }) {
                override fun getPriority(): Priority {
                    return Priority.LOW
                }
            }
            jsonObjReq.setShouldCache(false)
            getInstance().addToRequestQueue(jsonObjReq)
        }

    private fun getData(place_id: String?) {
        val c = Calendar.getInstance()
        c.add(Calendar.WEEK_OF_YEAR, -5)
        val df = SimpleDateFormat("yyyy-MM-dd")
        val formattedDate = df.format(c.time)
        val jsonObjReq: JsonArrayRequest = object : JsonArrayRequest(Method.POST,
            "$urlFoodTable$accessToken&place_id=$place_id&start_date=$formattedDate",
            null,
            Response.Listener { response: JSONArray ->
                try {
                    getId(response.toString(), place_id)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { obj: VolleyError -> obj.printStackTrace() }) {
            override fun getHeaders() = hashMapOf(
                "Content-type" to "application/json"
            )
        }
        getInstance().addToRequestQueue(jsonObjReq)
    }

    @Throws(JSONException::class)
    private fun getId(data_food: String, place_id: String?) {
        val tmp = JSONArray(data_food)
        for (j in 0..6) {
            val tempObject = tmp.getJSONObject(j)
            if (tempObject.getString("meal_foods")
                    .equals("هیچ غذایی برای این روز تعریف نشده است.", ignoreCase = true)
            ) {
            } else {
                val mealFoods = JSONArray(tempObject.getString("meal_foods"))
                var mealTmp = ""
                for (i in 0 until mealFoods.length()) {
                    mealTmp = mealFoods.getJSONObject(i).getString("meal") + "\n" + mealTmp
                    val foods = JSONArray(mealFoods.getJSONObject(i).getString("foods"))
                    for (k in 0 until foods.length()) {
                        val foodId = foods.getJSONObject(k).getString("id")
                        val foodName = foods.getJSONObject(k).getString("name")
                        checkDaySale(foodId, foodName, place_id)
                    }
                }
            }
        }
    }

    private fun checkDaySale(diet_id: String?, foodName: String?, placeId: String?) {
        val check = JsonObjectRequest(
            Request.Method.POST,
            "$urlFoodStatus$accessToken&diet_id=$diet_id&place_id=$placeId",
            null,
            { response ->
                try {
                    val foodStatus = JSONObject(response.toString())
                    if (foodStatus.getBoolean("daybuy")) {
                        daysale_food.text = foodName
                        daysale_submit.setOnClickListener {
                            daySaleSubmit(diet_id, placeId)
                        }
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { error: VolleyError ->
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            })
        getInstance().addToRequestQueue(check)
    }

    private fun daySaleSubmit(diet_id: String?, place_id: String?) {
        val check = JsonObjectRequest(
            Request.Method.POST,
            "$urlFoodReserve$accessToken&diet_id=$diet_id&place_id=$place_id",
            null,
            { response ->
                Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show()
            }
        ) { error: VolleyError ->
            Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
        }
        getInstance().addToRequestQueue(check)
    }

    private fun doRefreshPage() {
        daysale_refresh?.let {
            it.setColorSchemeColors(Color.BLUE)
            it.setOnRefreshListener {
                MyTask().execute("soss")
            }
        }
    }
}