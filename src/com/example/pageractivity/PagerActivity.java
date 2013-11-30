package com.example.pageractivity;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import java.util.ArrayList;

public class PagerActivity extends Activity {
    private static final String TAG = "PagerActivity";

    private ViewPager2 mPager;
    private Resources mResources;
    private SlidePagerAdapter mPagerAdapter;
    private LayoutInflater mInflater;

    private FrameLayout mTabBar;
    private LinearLayout mTabContainer;
    private ImageView mTabIndicator;

    private ObjectAnimator mTabIndicatorAnimator;
    private int mCurrentIndex;
    
    private int UPDATE_PAGER_INDEX_DELAY = 100;
    protected final int UPDATE_PAGER_INDEX = 0;

    protected Handler mPagerHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case UPDATE_PAGER_INDEX: {
                    removeMessages(UPDATE_PAGER_INDEX);
                    View currentTab = mTabContainer.getChildAt(mPager.getCurrentItem());
                    float x = currentTab.getLeft()
                            + (currentTab.getWidth() - mTabIndicator.getMeasuredWidth())
                            / 2;
                    mTabIndicator.setTranslationX(x);
                    mTabIndicator.setVisibility(View.VISIBLE);
                }
                    break;

                default:
                    break;
            }

            super.handleMessage(msg);
        }
    };

    public class SlidePagerAdapter extends FragmentPagerAdapter2 {
        private ArrayList<Fragment> list;

        public SlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public SlidePagerAdapter(FragmentManager fm,
                ArrayList<Fragment> fragments) {
            super(fm);
            list = fragments;
        }

        public void addFragment(Fragment fragment) {
            list.add(fragment);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getItemPosition(Object item) {
            return super.getItemPosition(item);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.pager_activity);
        mResources = getResources();
        mInflater = LayoutInflater.from(this);
        InitViewPager();
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        mPagerHandler.sendEmptyMessageDelayed(UPDATE_PAGER_INDEX, UPDATE_PAGER_INDEX_DELAY);
    }

    private void InitViewPager() {
        mTabBar = (FrameLayout) findViewById(R.id.tab_bar);
        mTabBar.setOnTouchListener(new OnTabTouchListener());

        mTabIndicator = (ImageView) findViewById(R.id.tab_indicator);
        mTabIndicator.setVisibility(View.INVISIBLE);
        mTabContainer = (LinearLayout) findViewById(R.id.tab_container);

        mPager = (ViewPager2) findViewById(R.id.pager);
        mPagerAdapter = new SlidePagerAdapter(getFragmentManager(),
                new ArrayList<Fragment>());
        mPager.setAdapter(mPagerAdapter);
        mPager.setCurrentItem(0);
        mPager.setOnPageChangeListener(new OnSlidePageChangeListener());
    }

    void addFragmentPager(Fragment fragment, String title) {
        FrameLayout tab = (FrameLayout) mInflater.inflate(R.layout.pager_tab, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        params.weight = 1;
        mTabContainer.addView(tab, params);
        mTabContainer.invalidate();
        TextView titleView = (TextView) tab.findViewById(R.id.tab_title);
        titleView.setText(title);

        mPagerAdapter.addFragment(fragment);
        mPagerAdapter.notifyDataSetChanged();

        if (mTabContainer.getChildCount() == 1) {
            mCurrentIndex = 0;
            mPager.setCurrentItem(mCurrentIndex);
        }
    }

    private class OnTabTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // TODO Auto-generated method stub
            final int action = event.getAction();
            switch (action & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_UP: {
                    if (mTabContainer.getChildCount() > 0) {
                        final float x = event.getX();
                        final int tabBarWidth = mTabBar.getMeasuredWidth();
                        final int tabIndex = (int) (x * mTabContainer.getChildCount() / tabBarWidth);
                        mPager.setCurrentItem(tabIndex);
                        View tabView = mTabContainer.getChildAt(tabIndex);
                        performTouchEffect(tabView);
                    }
                }
                    break;
                default:
                    break;
            }
            return true;
        }

    }

    private void performTouchEffect(View v) {
        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        v.playSoundEffect(SoundEffectConstants.CLICK);
    }

    public class OnSlidePageChangeListener implements ViewPager2.OnPageChangeListener {

        @Override
        public void onPageScrolled(
                int position, float positionOffset, int positionOffsetPixels) {
            View tab = mTabContainer.getChildAt(position);
            float x = tab.getLeft()
                    + (tab.getWidth() - mTabIndicator.getMeasuredWidth()) / 2
                    + positionOffset * tab.getWidth();
            mTabIndicator.setTranslationX(x);
        }

        @Override
        public void onPageSelected(int position) {
            int currentIndex = mPager.getCurrentItem();

        }

        @Override
        public void onPageScrollStateChanged(int state) {
            switch (state) {
                case ViewPager2.SCROLL_STATE_IDLE: {
                    break;
                }
                case ViewPager2.SCROLL_STATE_DRAGGING: {
                    break;
                }
                case ViewPager2.SCROLL_STATE_SETTLING: {
                    break;
                }
                default:
                    break;
            }
        }
    }

    void animateIndicatorToTab(int tabIndex)
    {
        animateIndicatorToTab(mTabContainer.getChildAt(tabIndex));
    }

    public void animateIndicatorToTab(View tab)
    {
        if ((tab == null) || (tab.getWidth() <= 0))
            return;
        if (mTabIndicatorAnimator == null)
        {
            mTabIndicatorAnimator = new ObjectAnimator();
            mTabIndicatorAnimator.setTarget(mTabIndicator);
            mTabIndicatorAnimator.setDuration(300L);
            mTabIndicatorAnimator.setPropertyName("TranslationX");
        }
        float f = tab.getLeft()
                + (tab.getWidth() - mTabIndicator.getDrawable().getIntrinsicWidth()) / 2;
        mTabIndicatorAnimator.setFloatValues(new float[] {
            f
        });
        mTabIndicatorAnimator.cancel();
        mTabIndicatorAnimator.start();
    }

    public void animateToTab(int tabIndex)
    {
        animateIndicatorToTab(tabIndex);
    }


}
