package com.penta.anchorart;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //内部Tab
    private TabLayout mInside_tab;
    //外部Tab
    private TabLayout mOutside_tab;

    //TabLayout显示的文字
    private String[] pointText = new String[]{"商品描述", "图片", "评价详情"};

    //各个锚点
    private TextView tv_good_detail;
    private TextView tv_pic;
    private TextView tv_evaluate;

    //锚点容器
    private List<View> mAimingPointList;

    //scrollView
    private ObservableScrollView mScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {

        mOutside_tab = (TabLayout) findViewById(R.id.outside_tab);
        mInside_tab = (TabLayout) findViewById(R.id.inside_tab);
        tv_good_detail = (TextView) findViewById(R.id.tv_good_detail);
        tv_pic = (TextView) findViewById(R.id.tv_pic);
        tv_evaluate = (TextView) findViewById(R.id.tv_evaluate);
        mScrollView = (ObservableScrollView) findViewById(R.id.sv_aiming_point);

        /**添加锚点到锚点列表*/
        mAimingPointList = new ArrayList<>();
        mAimingPointList.add(tv_good_detail);
        mAimingPointList.add(tv_pic);
        mAimingPointList.add(tv_evaluate);

        /**初始化tabLayout*/
        for (int i = 0; i < pointText.length; i++) {
            addAimingPoint(mInside_tab, pointText[i]);
            addAimingPoint(mOutside_tab, pointText[i]);
        }

        new Anchor(mInside_tab, mOutside_tab, mScrollView, mAimingPointList, this, 0, 50);

    }

    /**
     * 给TabLayout添加条目
     */
    public void addAimingPoint(TabLayout tabLayout, String text) {
        tabLayout.addTab(tabLayout.newTab().setText(text));
    }


}