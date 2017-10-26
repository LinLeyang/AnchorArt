package com.penta.anchorart;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;


public class Anchor implements TabLayout.OnTabSelectedListener, ObservableScrollView.ScrollViewListener, View.OnTouchListener {

    /**
     * 内部的tab
     */
    private TabLayout mInside_tab;
    /**
     * 外部的tab
     */
    private TabLayout mOutside_tab;

    private ObservableScrollView mScrollView;
    /**
     * 锚点集合
     */
    private List<View> mAimingPointList;

    private Context mContext;
    /**
     * ScrollView 的滑动状态
     */
    private int state = MotionEvent.ACTION_UP;
    /**
     * 覆盖的高度(顶部预留的高度 包括TabLayout 以及自定义的头部)
     */
    private float mCoverHeight;

    boolean forceScroll = false;//手动滚动标识 避免点击Tab时 scrollTo 导致循环改变Tab选择的问题

    /**
     * @param inside_tab      内部Tab
     * @param outside_tab     外部Tab
     * @param scrollView      scrollView
     * @param aimingPointList 定位的锚点集合
     * @param coverHeight     覆盖在scrollView上面的 布局总高度dp值
     */
    public Anchor(TabLayout inside_tab, TabLayout outside_tab, ObservableScrollView scrollView, List<View> aimingPointList, Context context, float coverHeight) {
        this.mInside_tab = inside_tab;
        this.mOutside_tab = outside_tab;
        this.mScrollView = scrollView;
        this.mAimingPointList = aimingPointList;
        this.mContext = context;
        this.mCoverHeight = TabLayoutUtil.dip2px(mContext, coverHeight);

        mOutside_tab.setOnTabSelectedListener(this);
        mInside_tab.setOnTabSelectedListener(this);
        mScrollView.setScrollViewListener(this);
        mScrollView.setOnTouchListener(this);

        mOutside_tab.post(new Runnable() {
            @Override
            public void run() {
                TabLayoutUtil.setIndicator(mContext, mOutside_tab);
            }
        });
        mInside_tab.post(new Runnable() {
            @Override
            public void run() {
                TabLayoutUtil.setIndicator(mContext, mInside_tab);
            }
        });


    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int position = tab.getPosition();
        mOutside_tab.getTabAt(position).select();
        mInside_tab.getTabAt(position).select();
        mOutside_tab.setVisibility(View.VISIBLE);

        //避免滚动的过程中点击tab
        if (state == MotionEvent.ACTION_UP) {
            forceScroll = true;
            mScrollView.smoothScrollTo(0, mAimingPointList.get(position).getTop() - TabLayoutUtil.dip2px(mContext, mCoverHeight));

        }

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }

    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
        /**已经滑动的距离*/
        int scorlly = mScrollView.getScrollY();
        /**导航控制*/
        if (scorlly >= mInside_tab.getTop()) {
            mOutside_tab.setVisibility(View.VISIBLE);
        } else {
            mOutside_tab.setVisibility(View.GONE);
        }
        /**遍历锚点从最下面瞄点开始遍历,如果已经滑动的Y距离+tab layout高度大于锚点的top选中，选中该tab */
        if (!forceScroll) {
            for (int j = 0; j < mAimingPointList.size(); j++) {
                int index = mAimingPointList.size() - j - 1;

                int top = mAimingPointList.get(index).getTop();

                if (scorlly + mCoverHeight >= top) {
                    mInside_tab.getTabAt(index).select();
                    mOutside_tab.getTabAt(index).select();
                    return;
                }

            }
        } else {
            forceScroll = false;
        }
    }

    /**
     * 当前的触摸状态
     */
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        state = motionEvent.getAction();
        return false;
    }


}