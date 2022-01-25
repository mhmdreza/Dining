package fathi.shakhes.fragments.tables

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import shakhes.R

private const val PAGE_COUNT = 3

class PagerAdapterTable(fm: FragmentManager, val context: Context) : FragmentPagerAdapter(
    fm
) {
    override fun getItem(position: Int): Fragment = when (position) {
        0 -> NextWeek.newInstance()
        1 -> WeekFragment.newInstance()
        else -> ReservedFragment.newInstance()
    }

    override fun getPageTitle(position: Int) = when (position) {
        0 -> context.getString(R.string.FoodTableTab3)
        1 -> context.getString(R.string.FoodTableTab2)
        2 -> context.getString(R.string.FoodTableTab1)
        else -> null
    }

    override fun getCount(): Int = PAGE_COUNT
}