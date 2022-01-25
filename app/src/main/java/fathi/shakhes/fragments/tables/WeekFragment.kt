package fathi.shakhes.fragments.tables

import android.content.res.ColorStateList
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.LinearLayout
import androidx.core.widget.CompoundButtonCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import fathi.shakhes.AppSharedPreferences
import fathi.shakhes.Food
import fathi.shakhes.MainApplication.Companion.getInstance
import fathi.shakhes.WeekAdapter
import fathi.shakhes.base.BaseFragment
import kotlinx.android.synthetic.main.content_table_week.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import shakhes.R
import java.text.SimpleDateFormat
import java.util.*

class WeekFragment : BaseFragment(R.layout.content_table_week) {
    var url_food_place = "https://dining.sharif.ir/api/food-places?access_token="
    var url_food_table = "http://dining.sharif.ir/api/reserve-table?access_token="
    var url_food_status = "https://dining.sharif.ir/api/reserve-status?access_token="
    var url_food_reserve = "https://dining.sharif.ir/api/reserve?access_token="
    lateinit var ids: Array<String?>
    lateinit var names: Array<String?>
    lateinit var food_data: Array<String?>
    lateinit var day_name: Array<String?>
    lateinit var day_date: Array<String?>
    lateinit var meal: Array<String?>
    lateinit var food_name_table: Array<String?>
    lateinit var food_id_table: Array<String?>
    lateinit var lx: Array<LinearLayout>


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MyTask().execute("adb")
    }

    private inner class MyTask() : AsyncTask<String?, Int?, String>() {
        override fun doInBackground(vararg params: String?): String {
            table
            return "this string is passed to onPostExecute"
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            spinner.visibility = View.VISIBLE
        }
    }

    private fun checkStatus(
        foods: Array<Array<JSONArray?>>,
        diet_id: String?,
        place_id: String?,
        j: Int,
        k: Int,
        m: Int
    ) {
        val check = JsonObjectRequest(
            Request.Method.POST,
            "$url_food_status${AppSharedPreferences.accessToken}&diet_id=$diet_id&place_id=$place_id",
            null,
            { response ->
                try {
                    val foodStatus = JSONObject(response.toString())
                    if (foodStatus.getBoolean("reserved")) {
                        food_name_table[j] =
                            foods[j][k]?.getJSONObject(m)?.getString("name") + "(رزرو)"
                    }
                    if (foodStatus.getBoolean("eaten")) {
                        food_name_table[j] =
                            foods[j][k]?.getJSONObject(m)?.getString("name") + "(خورده شده)"
                    }
                    if (foodStatus.getBoolean("lavish")) {
                        food_name_table[j] =
                            foods[j][k]?.getJSONObject(m)?.getString("name") + "(حرام شده)"
                    }
                    if (foodStatus.getBoolean("food_ready")) {
                        food_name_table[j] =
                            foods[j][k]?.getJSONObject(m)?.getString("name") + "(آماده دریافت)"
                    }
                    parentLayout.removeAllViews()
                    parentLayout.visibility = View.VISIBLE
                    waitingText.visibility = View.GONE

                    val foods = arrayListOf<Food>()
                    for (i in 0..6) {
                        foods.add(Food(
                            "${day_name[i].toString()}\n${day_date[i]}",
                            food_name_table[i],
                            meal[i],
                        ))
                    }
                    recyclerView.adapter = WeekAdapter().apply {
                        setData(foods)
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            VolleyError::printStackTrace
        )
        getInstance().addToRequestQueue(check)
        food_name_table[j] = ""
    }

    private fun CreateCheckBox(number: Int, nlx: Int) {
        val lay = lx[nlx].layoutParams as LinearLayout.LayoutParams
        lay.gravity = Gravity.CENTER
        for (i in 0 until number) {
            val cb = CheckBox(context)
            lx[nlx].addView(cb)
            cb.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            cb.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            cb.setPadding(0, 2, 0, 2)
            if (Build.VERSION.SDK_INT < 21) {
                CompoundButtonCompat.setButtonTintList(
                    cb,
                    ColorStateList.valueOf(resources.getColor(R.color.food))
                ) //Use android.support.v4.widget.CompoundButtonCompat when necessary else
            } else {
                cb.buttonTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.food)) //setButtonTintList is accessible directly on API>19
            }
            cb.layoutParams = lay
        }
    }

    private fun spinnerUi() {
        val categories = arrayListOf<String?>()

        for (m in ids.indices) {
            when (ids[m]) {
                "35" -> categories.add("مرکزی-سلف دانشجویان خانم")
                "39" -> categories.add("خوابگاه-طرشت2(خواهران)")
                "42" -> categories.add("خوابگاه-شهید شوریده(خواهران)")
                "31" -> categories.add("مرکزی-سلف دانشجویان آقا")
                "37" -> categories.add("خوابگاه-طرشت 3")
                "40" -> categories.add("خوابگاه-شهید احمدی روشن")
                "43" -> categories.add("خوابگاه-شهید وزوایی")
                "44" -> categories.add("خوابگاه-شادمان")
                "45" -> categories.add("خوابگاه-آزادی")
                "46" -> categories.add("خوابگاه-12 واحدی")
                "50" -> categories.add("خوابگاه-ولیعصر")
            }
        }

        // Creating adapter for spinner
        val dataAdapter: ArrayAdapter<String?> = object : ArrayAdapter<String?>(
            requireContext(), android.R.layout.simple_spinner_item, categories
        ) {}

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // attaching data adapter to spinner
        spinner.adapter = dataAdapter


        // Do things like hide the progress bar or change a TextView
        // spinner.setVisibility(View.VISIBLE);
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                getData(ids[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }// data_food_tmp = "\""+ids[i]+"\"" +":"+ data_temp.get(i)+ ","+ data_food_tmp ;

    //ToDo check returning date
    val table: Unit
        get() {
            val jsonObjReq: JsonObjectRequest = object : JsonObjectRequest(Method.POST,
                "$url_food_place${AppSharedPreferences.accessToken}", null,
                Response.Listener { response: JSONObject ->
                    try {
                        //ToDo check returning date
                        ids = arrayOfNulls(response.length())
                        names = arrayOfNulls(response.length())
                        food_data = arrayOfNulls(response.length())
                        var i = 0
                        val iterator: Iterator<*> = response.keys()
                        while (iterator.hasNext()) {
                            ids[i] = iterator.next() as String?
                            names[i] = response.getString(ids[i])
                            i++
                        }
                        spinnerUi()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }, Response.ErrorListener { obj: VolleyError -> obj.printStackTrace() }) {
                override fun getPriority(): Priority {
                    return Priority.LOW
                }
            }
            jsonObjReq.setShouldCache(false)
            getInstance().addToRequestQueue(jsonObjReq)
        }

    fun getData(key: String?) {
        val c = Calendar.getInstance()
        //        c.add(Calendar.DATE,+7);
        val df = SimpleDateFormat("yyyy-MM-dd")
        val formattedDate = df.format(c.time)
        val jsonObjReq: JsonArrayRequest = object : JsonArrayRequest(
            Method.POST,
            "$url_food_table${AppSharedPreferences.accessToken}&place_id=$key&start_date=$formattedDate",
            null,
            Response.Listener { response -> //  data_temp.put(num,response.toString());
                //                        CreateTable(response.toString(),key);
                createTable(response.toString(), key)
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
            }
        ) {
            override fun getPriority(): Priority {
                return Priority.HIGH
            }
        }
        jsonObjReq.setShouldCache(false)
        getInstance().addToRequestQueue(jsonObjReq)
        parentLayout.visibility = View.GONE
        waitingText.visibility = View.VISIBLE
    }

    fun createTable(DataArray: String?, pid: String?) {
        try {
            day_name = arrayOfNulls(7)
            day_date = arrayOfNulls(7)
            meal = arrayOfNulls(7)
            food_name_table = arrayOfNulls(7)
            food_id_table = arrayOfNulls(7)
            val mealFoods = arrayOfNulls<JSONArray>(7)

            val foods: Array<Array<JSONArray?>> = Array(7) { arrayOfNulls(2) }
            val namesJsonArray = JSONArray(DataArray)
            for (j in 0 until namesJsonArray.length()) {
                day_name[j] = namesJsonArray.getJSONObject(j).getString("day_name")
                day_date[j] = namesJsonArray.getJSONObject(j).getString("day_date")
                if (!namesJsonArray.getJSONObject(j).getString("meal_foods")
                        .equals("هیچ غذایی برای این روز تعریف نشده است.", ignoreCase = true)
                ) {
                    mealFoods[j] =
                        JSONArray(namesJsonArray.getJSONObject(j).getString("meal_foods"))

                    meal[j] = ""
                    food_name_table[j] = ""
                    food_id_table[j] = ""
                    for (k in 0 until (mealFoods[j]?.length() ?: 0)) {
                        meal[j] = meal[j].toString() + "\n" +
                                mealFoods[j]?.getJSONObject(k)?.getString("meal")
                        foods[j][k] = JSONArray(mealFoods[j]?.getJSONObject(k)?.getString("foods"))
                        for (m in 0 until (foods[j][k]?.length() ?: 0)) {
                            food_id_table[j] = foods[j][k]?.getJSONObject(m)?.getString("id")
                            //*****************
                            checkStatus(foods, food_id_table[j], pid, j, k, m)
                            //                            food_name_table[j] = food_name_table[j] + "\n" + foods[j][k].getJSONObject(m).getString("name");
                        }
                    }
                } else {
                    meal[j] = " - "
                    food_name_table[j] = "هیچ غذایی وجود ندارد."

                    //no food define
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    fun reserve(diet_id: String, place_id: String) {
        val jsonObjReq: JsonObjectRequest = object : JsonObjectRequest(
            Method.POST,
            "$url_food_reserve${AppSharedPreferences.accessToken}diet_id$diet_id&place_id=$place_id",
            null,
            Response.Listener { response ->
                try {
                    //ToDo check returning date
                    ids = arrayOfNulls(response.length())
                    names = arrayOfNulls(response.length())
                    food_data = arrayOfNulls(response.length())
                    var i = 0
                    val iterator: Iterator<*> = response.keys()
                    while (iterator.hasNext()) {
                        ids[i] = iterator.next() as String?
                        names[i] = response.getString(ids[i])
                        // data_food_tmp = "\""+ids[i]+"\"" +":"+ data_temp.get(i)+ ","+ data_food_tmp ;
                        i++
                    }
                    spinnerUi()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            },
            VolleyError::printStackTrace
        ) {
            override fun getPriority(): Priority {
                return Priority.LOW
            }
        }
        jsonObjReq.setShouldCache(false)
        getInstance().addToRequestQueue(jsonObjReq)
    }

    companion object {
        @JvmStatic
        fun newInstance(): WeekFragment {
            return WeekFragment()
        }
    }
}