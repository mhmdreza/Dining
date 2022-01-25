package fathi.shakhes.fragments.message

import android.os.Bundle
import android.view.View
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import fathi.shakhes.base.BaseFragment
import kotlinx.android.synthetic.main.tab_activity_message.*
import shakhes.R

class TabMessageFragment : BaseFragment(R.layout.tab_activity_message) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        setupViewPager()
        tabLayout.setupWithViewPager(viewPager)
    }

    private fun setupViewPager() {
        val viewPagerAdapter = PagerAdapterMessage(childFragmentManager, context)
        viewPager.adapter = viewPagerAdapter
        viewPager.currentItem = 1
        viewPager.offscreenPageLimit = 3
        viewPager.addOnPageChangeListener(TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.setTabsFromPagerAdapter(viewPagerAdapter)
    }

}