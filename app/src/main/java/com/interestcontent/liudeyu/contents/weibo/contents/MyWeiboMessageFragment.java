package com.interestcontent.liudeyu.contents.weibo.contents;

import com.interestcontent.liudeyu.base.baseComponent.BrowseFragmentRemoveAd;
import com.interestcontent.liudeyu.contents.weibo.util.WeiboUrlsUtils;

/**
 * Created by liudeyu on 2018/1/26.
 */

public class MyWeiboMessageFragment extends BrowseFragmentRemoveAd {
    @Override
    protected String provideUrl() {
        return WeiboUrlsUtils.getWeiboMessageUrl();
    }

    @Override
    protected boolean isNeedScrollAndFullScreen() {
        return true;
    }
}
