package fathi.shakhes.FoodMessageTab;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import shakhes.R;


public class PagerAdapterMessage extends FragmentPagerAdapter {

    final int PAGE_COUNT = 2;
    final Context context;

    public PagerAdapterMessage(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = ReadMessage.newInstance();
                break;
            case 1:
                fragment = UnreadMessage.newInstance();
                break;
        }

        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return context.getResources().getString(R.string.FoodMessageTab1);
            case 1:
                return context.getResources().getString(R.string.FoodMessageTab2);
        }
        return null;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

}
