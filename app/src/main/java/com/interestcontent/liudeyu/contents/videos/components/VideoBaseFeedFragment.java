package com.interestcontent.liudeyu.contents.videos.components;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;

import com.blankj.utilcode.util.SizeUtils;
import com.google.gson.Gson;
import com.interestcontent.liudeyu.R;
import com.interestcontent.liudeyu.base.thread.TaskManager;
import com.interestcontent.liudeyu.contents.videos.VideoPlayManager;
import com.interestcontent.liudeyu.contents.videos.beans.VideoBean;
import com.interestcontent.liudeyu.contents.videos.beans.VideoRequest;
import com.interestcontent.liudeyu.contents.videos.cells.VideoCell;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import com.zhouwei.rvadapterlib.base.Cell;
import com.zhouwei.rvadapterlib.fragment.AbsFeedFragment;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by liudeyu on 2018/2/12.
 */

public class VideoBaseFeedFragment extends AbsFeedFragment {
    private static final int WHAT_INT = 18;
    public static final String ORIGIN_URL = "ORIGIN_URL".toLowerCase();
    private String mNextReUrl = "";
    private String mOriginUrl = "";
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.obj instanceof Exception) {
                onDataError();
                return;
            }
            switch (msg.what) {
                case WHAT_INT:
                    VideoRequest request = (VideoRequest) msg.obj;
                    onDataSuccess(request);
                    break;
            }
        }
    };


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mOriginUrl = bundle.getString(ORIGIN_URL);
            mNextReUrl = mOriginUrl;
        }
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser) {
            VideoPlayManager.getInstance().onDestoryVideoPlayView();
        }
    }

    @Override
    protected void onScrollAndStateChange(int firstVisible) {
        super.onScrollAndStateChange(firstVisible);
        VideoPlayManager.getInstance().onJudgeIfStopPlayVideo(firstVisible);
    }

    private void onDataError() {
        hideLoadingState();
    }

    private void hideLoadingState() {
        hideLoadMore();
        setRefreshing(false);
    }

    private void onDataSuccess(VideoRequest request) {
        if(request==null){
            return;
        }
        mNextReUrl = request.getNextPageUrl();
        if (request.getItemList() == null || request.getItemList().isEmpty()) {
            return;
        }
        hideLoadingState();
        mBaseAdapter.addAll(getCells(request.getItemList()));
    }

    @Override
    public void onRecyclerViewInitialized() {
        startRequestData(mNextReUrl, false);
        HorizontalDividerItemDecoration itemDecoration = new HorizontalDividerItemDecoration.Builder(getContext())
                .margin(SizeUtils.dp2px(10))
                .size(SizeUtils.dp2px(8)).colorResId(R.color.md_grey_100).build();
        mRecyclerView.addItemDecoration(itemDecoration);
    }

    private void startRequestData(String url, boolean isFirst) {
        TaskManager.inst().commit(mHandler, new Callable<VideoRequest>() {
            @Override
            public VideoRequest call() throws Exception {
                String tmp = OkHttpUtils.get().url(mNextReUrl).build().execute().body().string();
                Gson gson = new Gson();
                return gson.fromJson(tmp, VideoRequest.class);

            }
        }, WHAT_INT);
        if (isFirst) {
            mBaseAdapter.showLoading();
        } else {
            mBaseAdapter.showLoadMore();
        }
    }

    @Override
    public void onPullRefresh() {
        startRequestData(mNextReUrl = mOriginUrl, true);
    }

    @Override
    public void onLoadMore() {
        startRequestData(mNextReUrl, false);
    }

    @Override
    protected List<Cell> getCells(List list) {
        List<Cell> cellList = new ArrayList<>();
        List<VideoBean> beans = list;
        for (VideoBean videoBean : beans) {
            if ("video".equals(videoBean.getType())) {
                cellList.add(new VideoCell(videoBean, this));
            }
        }
        return cellList;
    }


}
