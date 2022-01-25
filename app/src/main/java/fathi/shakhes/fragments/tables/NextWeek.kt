package fathi.shakhes.fragments.tables

import android.content.res.ColorStateList
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.CompoundButtonCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import fathi.shakhes.AppSharedPreferences.accessToken
import fathi.shakhes.MainApplication.Companion.getInstance
import fathi.shakhes.base.BaseFragment
import kotlinx.android.synthetic.main.content_table.*
import kotlinx.android.synthetic.main.food_table_child_nextweek.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import shakhes.R
import java.text.SimpleDateFormat
import java.util.*

class NextWeek : BaseFragment(R.layout.content_table) {
    var urlFoodPlace = "https://dining.sharif.ir/api/food-places?access_token="
    var urlFoodTable = "https://dining.sharif.ir/api/reserve-table?access_token="
    var urlFoodStatus = "https://dining.sharif.ir/api/reserve-status?access_token="

    var ids: Array<String?>? = null
    private lateinit var foods: Array<Array<JSONArray?>>
    private lateinit var dayName: Array<String?>
    private lateinit var dayDate: Array<String?>
    private lateinit var meal: Array<String?>
    private lateinit var foodNameTable: Array<String?>
    private val lx: Array<LinearLayout> by lazy {
        arrayOf(
            l1_nextweek,
            l2_nextweek,
            l3_nextweek,
            l4_nextweek,
            l5_nextweek,
            l6_nextweek,
            l7_nextweek,
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MyTask().execute("abcd")
    }

    private inner class MyTask() : AsyncTask<String?, Int?, String>() {
        override fun doInBackground(vararg params: String?): String {
            table
            return "this string is passed to onPostExecute"
        }
    }

    private fun checkStatus(diet_id: String?, place_id: String?, j: Int, k: Int, m: Int) {
        val check = JsonObjectRequest(
            Request.Method.POST,
            "$urlFoodStatus$accessToken&diet_id=$diet_id&place_id=$place_id",
            null,
            { response: JSONObject ->
                try {
                    val foodStatus = JSONObject(response.toString())
                    if (foodStatus.getBoolean("reserve")) {
                        foodNameTable[j] =
                            foods[j][k]?.getJSONObject(m)?.getString("name") + "(قابل رزرو)"
                    }
                    if (foodStatus.getBoolean("reserved")) {
                        foodNameTable[j] =
                            foods[j][k]?.getJSONObject(m)?.getString("name") + "(رزرو)"
                    }
                    if (foodStatus.getBoolean("eaten")) {
                        foodNameTable[j] =
                            foods[j][k]?.getJSONObject(m)?.getString("name") + "(خورده شده)"
                    }
                    if (foodStatus.getBoolean("lavish")) {
                        foodNameTable[j] =
                            foods[j][k]?.getJSONObject(m)?.getString("name") + "(حرام شده)"
                    }
                    if (foodStatus.getBoolean("food_ready")) {
                        foodNameTable[j] =
                            foods[j][k]?.getJSONObject(m)?.getString("name") + "(آماده دریافت)"
                    }
                    parentLayout.removeAllViews()
                    val child = LayoutInflater.from(activity).inflate(
                        R.layout.food_table_child_nextweek, view as? ViewGroup
                    )
                    val days = arrayOf(day_1, day_2, day_3, day_4, day_5, day_6, day_7)

                    val foodName = arrayOf(food_1, food_2, food_3, food_4, food_5, food_6, food_7)
                    val mealName = arrayOf(mill_1, mill_2, mill_3, mill_4, mill_5, mill_6, mill_7)

                    for (i in 0..6) {
                        days[i].text = "${dayName[i]}\n${dayDate[i]}"
                        foodName[i].text = foodNameTable[i]
                        mealName[i].text = meal[i]
                    }
                    parentLayout.addView(child)
                    parentLayout.visibility = View.VISIBLE
                    waitingText.visibility = View.GONE

                    //CreateCheckBox(1,1);
                    //                    mProgressBar.setVisibility(View.GONE);
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            VolleyError::printStackTrace
        )
        getInstance().addToRequestQueue(check)
        foodNameTable[j] = ""
    }

    private fun CreateCheckBox(number: Int, nlx: Int) {
        val lay = lx[nlx].layoutParams as LinearLayout.LayoutParams
        lay.gravity = Gravity.CENTER
        for (i in 0 until number) {
            val cb = CheckBox(activity)
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

        ids?.let {
            for (id: String? in ids!!) {
                when (id) {
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
            val dataAdapter: ArrayAdapter<String?> = object : ArrayAdapter<String?>(
                requireActivity(), android.R.layout.simple_spinner_item, categories
            ) {}

            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = dataAdapter
        }


        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                getData(ids!![position])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }// data_food_tmp = "\""+ids[i]+"\"" +":"+ data_temp.get(i)+ ","+ data_food_tmp ;

    //ToDo check returning date
    val table: Unit
        get() {
            val jsonObjReq: JsonObjectRequest = object : JsonObjectRequest(
                Method.POST,
                "$urlFoodPlace$accessToken",
                null,
                Response.Listener { response ->
                    try {
                        //ToDo check returning date
                        ids = arrayOfNulls(response.length())
                        var i = 0
                        val iterator: Iterator<*> = response.keys()
                        while (iterator.hasNext()) {
                            ids!![i] = iterator.next() as String?
                            // data_food_tmp = "\""+ids[i]+"\"" +":"+ data_temp.get(i)+ ","+ data_food_tmp ;
                            i++
                        }
                        spinnerUi()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { obj: VolleyError ->
                    obj.printStackTrace()
                 }
            ) {
                override fun getPriority(): Priority {
                    return Priority.LOW
                }
            }
            jsonObjReq.setShouldCache(false)
            getInstance().addToRequestQueue(jsonObjReq)
        }

    fun getData(key: String?) {
        val c = Calendar.getInstance()
        c.add(Calendar.DATE, -650)
        val df = SimpleDateFormat("yyyy-MM-dd")
        val formattedDate = df.format(c.time)
        val jsonObjReq: JsonArrayRequest = object : JsonArrayRequest(
            Method.POST,
            "$urlFoodTable$accessToken&place_id=$key&start_date=$formattedDate", null,
            Response.Listener<JSONArray> { response -> //  data_temp.put(num,response.toString());
                //                        CreateTable(response.toString(),key);
                CreateTable(response.toString(), key)
            }, VolleyError::printStackTrace
        ) {
            override fun getPriority() = Priority.HIGH
        }
        jsonObjReq.setShouldCache(false)
        getInstance().addToRequestQueue(jsonObjReq)
        parentLayout.visibility = View.GONE
        waitingText.visibility = View.VISIBLE
    }

    fun CreateTable(DataArray: String?, pid: String?) {
        try {
            dayName = arrayOfNulls(7)
            dayDate = arrayOfNulls(7)
            meal = arrayOfNulls(7)
            val foodNameTable = arrayOfNulls<String>(7)
            foods = Array(7) { arrayOfNulls(2) }
            val namesJsonArray = JSONArray(DataArray)
            for (j in 0 until namesJsonArray.length()) {
                dayName[j] = namesJsonArray.getJSONObject(j).getString("day_name")
                dayDate[j] = namesJsonArray.getJSONObject(j).getString("day_date")
                if (!namesJsonArray.getJSONObject(j).getString("meal_foods")
                        .equals("هیچ غذایی برای این روز تعریف نشده است.", ignoreCase = true)
                ) {
                    val mealFoods =
                        JSONArray(namesJsonArray.getJSONObject(j).getString("meal_foods"))
                    //******************
                    meal[j] = ""
                    foodNameTable[j] = ""
                    for (k in 0 until mealFoods.length()) {
                        meal[j] = meal[j].toString() + "\n" + mealFoods.getJSONObject(k)
                            .getString("meal")
                        meal[j] = meal[j]!!.trim { it <= ' ' }
                        foods[j][k] = JSONArray(mealFoods.getJSONObject(k).getString("foods"))
                        for (m in 0 until foods[j][k]!!.length()) {
                            val foodId = foods[j][k]!!.getJSONObject(m).getString("id")
                            //*****************
                            checkStatus(foodId, pid, j, k, m)
                            //                            food_name_table[j] = food_name_table[j] + "\n" + foods[j][k].getJSONObject(m).getString("name");
                        }
                    }
                } else {
                    meal[j] = " - "
                    foodNameTable[j] = "هیچ غذایی وجود ندارد."

                    //no food define
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    companion object {
        fun newInstance(): NextWeek {
            return NextWeek()
        }
    }
}