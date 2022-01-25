package fathi.shakhes.fragments.tables

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fathi.shakhes.connections.ApiHelper
import fathi.shakhes.connections.Reserved
import fathi.shakhes.connections.ResultWrapper
import fathi.shakhes.helpers.PersianDateConverter

import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.week_item.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import shakhes.R
import kotlin.collections.ArrayList

class ReservedAdapter() : RecyclerView.Adapter<ReservedAdapter.ViewHolder>() {
    private var reservedList: ArrayList<Reserved> = arrayListOf()

    fun setData(reserved: ArrayList<Reserved>) {
        this.reservedList = reserved
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val listItem = LayoutInflater.from(parent.context)
            .inflate(R.layout.week_item, parent, false)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(roomViewHolder: ViewHolder, position: Int) {
        val res = reservedList[position]
        roomViewHolder.bindData(res)
    }

    override fun getItemCount() = reservedList.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), LayoutContainer {

        fun bindData(reserved: Reserved) {
            itemView.testView.text = "${reserved.dayName} ${reserved.dayDate}"


            val parts = reserved.dayDate.split("/".toRegex()).toTypedArray()
            val cc = PersianDateConverter().apply {
                setIranianDate(parts[0].toInt(), parts[1].toInt(), parts[2].toInt())
            }
            cc.gregorianDate
            val inputdate = cc.gregorianDate.replace("/", "-")
            getTableReserve("1", inputdate)
            getTableReserve("2", inputdate)
        }

        private fun getTableReserve(mealId: String, date: String) = GlobalScope.launch(Dispatchers.Main) {
            when (val result = ApiHelper.getTableReserve(mealId, date)) {
                is ResultWrapper.Success -> {
                    val response = JSONObject(result.value.string())
                    if (response.has("success") and response.getBoolean("success")) {
                        showResult(mealId, response, itemView.testView)
                    }
                }
                else -> {
                    val a = 'a'
                }
            }
        }

        private fun showResult(mealId: String, response: JSONObject, textView: TextView) {
            if (mealId.equals("1", ignoreCase = true)) {
                val mealNameData = Html.fromHtml(response.getString("message")).toString()
                        .trim { it <= ' ' }
                textView.text = "${textView.text} + $mealNameData"
            }
            if (mealId.equals("2", ignoreCase = true)) {
                val foodNameData = Html.fromHtml(response.getString("message")).toString().trim { it <= ' ' }
                textView.text = "${textView.text} * $foodNameData"
            }
        }

        override val containerView: View
            get() = itemView
    }
}

data class Food(
    val day: String,
    val mealName: String?,
    val foodName: String?,
)
