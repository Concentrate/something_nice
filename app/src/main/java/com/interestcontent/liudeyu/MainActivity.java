package com.interestcontent.liudeyu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;

import com.interestcontent.liudeyu.base.baseComponent.BaseActivity;
import com.interestcontent.liudeyu.base.baseUiKit.AdvanceViewPager;
import com.interestcontent.liudeyu.base.tabs.BasePageAdapter;
import com.interestcontent.liudeyu.base.tabs.ItemTab;
import com.interestcontent.liudeyu.base.thread.TaskManager;
import com.interestcontent.liudeyu.weibo.data.WeiboLoginManager;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AuthInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by liudeyu on 2017/12/23.
 */

public class MainActivity extends BaseActivity {

    @BindView(R.id.viewpager)
    AdvanceViewPager mViewPager;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    private List<Fragment> mFragments = new ArrayList<>();

    @Override
    protected boolean isUseToolBar() {
        return false;
    }

    @Override
    protected boolean isUseFullAScreenAndTransparent() {
        return true;
    }

    @Override
    protected View getResourceLayout() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_main, null);
        return view;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initUiData();
        initOtherData();
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        mViewPager.setCurrentItem(0);
        mViewPager.setCanScroll(false);

    }

    private void initOtherData() {
        AuthInfo authInfo =WeiboLoginManager.getInstance().getAuthInfo();
        WbSdk.install(this, authInfo);
        TaskManager.inst().init(new TaskManager.TaskManagerConfig().setExecutor(Executors.newFixedThreadPool(4)));
    }


    private void initUiData() {
        List<ItemTab> itemTabs = new ArrayList<>();
        itemTabs.add(new ItemTab(ItemTab.TAB_WEIBO, R.drawable.tab_weibo_selector, getString(R.string.tab_weibo)));
        itemTabs.add(new ItemTab(ItemTab.TAB_NEWS, R.drawable.tab_news_selector, getString(R.string.tab_news)));
        itemTabs.add(new ItemTab(ItemTab.TAB_SETTINGS, R.drawable.tab_setting_selector, getString(R.string.tab_settings)));
        mViewPager.setAdapter(new BasePageAdapter(getSupportFragmentManager(), itemTabs));
        mViewPager.setOffscreenPageLimit(3);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabTextColors(getResources().getColor(R.color.md_blue_grey_200), getResources().getColor(R.color
                .md_black_1000));
        mTabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.md_orange_A700));
        for (int i = 0; i < itemTabs.size(); i++) {
            mTabLayout.getTabAt(i).setIcon(itemTabs.get(i).getResourceId());
        }
    }


    /**
     * 这里跟验证登录接入第三方sdk密切相关
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (WeiboLoginManager.getInstance().getSsoHandler() != null) {
            WeiboLoginManager.getInstance().getSsoHandler().authorizeCallBack(requestCode, resultCode, data);
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
    }
}
