
package com.example.pageractivity;

import android.os.Bundle;

public class MainSettings extends PagerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TestFragment settings1 = TestFragment.newInstance("P1");
        TestFragment settings2 = TestFragment.newInstance("P2");
        addFragmentPager( settings1, "F1");
        addFragmentPager(settings2, "F2");
    }
}
