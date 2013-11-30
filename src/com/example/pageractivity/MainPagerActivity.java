
package com.example.pageractivity;

import android.app.Fragment;
import android.os.Bundle;
import android.view.Menu;

public class MainPagerActivity extends PagerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fragment activityfragment = TestFragment.newInstance("Hello Activity.");
        Fragment groupFragment = TestFragment.newInstance("Hello Group.");
        Fragment friendsFragment = TestFragment.newInstance("Hello Friends.");
        Fragment chatFragment = TestFragment.newInstance("Hello Chat.");
        addFragmentPager(activityfragment, "F1");
        addFragmentPager(groupFragment, "F2");
        addFragmentPager(friendsFragment, "F3");
        addFragmentPager(chatFragment, "F4");

        /*        //Fragment fragment1 = Fragment.instantiate(this, Activity1.class.getName());
                Fragment fragment1 = new Fragment();
                addFragmentPager(fragment1, "F1");
                //Fragment fragment2 = Fragment.instantiate(this, Activity2.class.getName());
                Fragment fragment2 = new Fragment();
                addFragmentPager(fragment2, "F2");*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
