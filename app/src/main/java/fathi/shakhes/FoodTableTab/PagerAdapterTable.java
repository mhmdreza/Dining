package fathi.shakhes.FoodTableTab;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import shakhes.R;


public class PagerAdapterTable extends FragmentPagerAdapter {

    final int PAGE_COUNT = 3;
    final Context context;

    public PagerAdapterTable(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = NextWeek.newInstance();
                break;
            case 1:
                fragment = Week.newInstance();
                break;
            case 2:
                fragment = Reserved.newInstance();
                break;
        }

        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return context.getResources().getString(R.string.FoodTableTab3);
            case 1:
                return context.getResources().getString(R.string.FoodTableTab2);
            case 2:
                return context.getResources().getString(R.string.FoodTableTab1);
        }
        return null;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

}
