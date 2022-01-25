package fathi.shakhes.fragments.tables

import android.os.Bundle
import android.view.View
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import fathi.shakhes.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_tab_table.*
import shakhes.R

class TabTableFragment : BaseFragment(R.layout.fragment_tab_table) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }


    private fun init() {
        setupViewPager()
        tabLayout.setupWithViewPager(viewPager)
    }

    private fun setupViewPager() {
        val viewPagerAdapter = PagerAdapterTable(childFragmentManager, viewPager.context)
        viewPager.adapter = viewPagerAdapter
        viewPager.currentItem = 2
        viewPager.offscreenPageLimit = 3
        viewPager.addOnPageChangeListener(TabLayoutOnPageChangeListener(tabLayout))
    }
}