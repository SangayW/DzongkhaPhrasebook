package bt.gov.dzongkha.dzongkhaenglishphrasebook;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thinley on 20/01/2018.
 */

public class tabsPager extends FragmentStatePagerAdapter {

    List<Fragment> fragmentList= new ArrayList<>();
    List<String> fragmentTitleList = new ArrayList<>();

    public tabsPager(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    public void addFragment(Fragment fragment, String title)
    {
        fragmentList.add(fragment);
        fragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitleList.get(position);
    }
}
