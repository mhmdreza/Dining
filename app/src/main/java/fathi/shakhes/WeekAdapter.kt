package fathi.shakhes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.week_item.*
import kotlinx.android.synthetic.main.week_item.view.*
import shakhes.R
import java.util.*

class WeekAdapter() : RecyclerView.Adapter<WeekAdapter.ViewHolder>() {
    private var foods: ArrayList<Food> = arrayListOf()

    fun setData(foods: ArrayList<Food>) {
        this.foods = foods
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val listItem = LayoutInflater.from(parent.context)
            .inflate(R.layout.week_item, parent, false)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(roomViewHolder: ViewHolder, position: Int) {
        val food = foods[position]
        roomViewHolder.bindData(food)
    }

    override fun getItemCount() = foods.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), LayoutContainer {

        fun bindData(food: Food) {
            itemView.testView.text = "${food.day}---${food.foodName}---${food.mealName}"
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
