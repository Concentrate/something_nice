package com.interestcontent.liudeyu.weibo;

import com.blankj.utilcode.util.SizeUtils;
import com.interestcontent.liudeyu.R;
import com.interestcontent.liudeyu.base.constants.Constants;
import com.interestcontent.liudeyu.weibo.data.bean.WeiboRequest;
import com.interestcontent.liudeyu.weibo.feeds.WeiboCell;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import com.zhouwei.rvadapterlib.base.Cell;
import com.zhouwei.rvadapterlib.base.RVBaseCell;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liudeyu on 2017/12/26.
 */

public class WeiboFollowFragment extends WeiboBaseTabFragment {


    private RVBaseCell mCell;

    @Override
    public void onRecyclerViewInitialized() {
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity())
                .color(getActivity().getResources().getColor(R.color.md_white_1000))
                .size(SizeUtils.dp2px(1.0f))
                .build());
        startRequestWeiboFeed(true);
    }

    @Override
    public void onPullRefresh() {
        requestPageData(1);
    }

    @Override
    public void onLoadMore() {
        requestPageData(mCurrentPage + 1);
    }

    @Override
    protected List<Cell> getCells(@NotNull List list) {
        List<Cell> cellList = new ArrayList<>();
        if (mCell == null) {
            mCell = new WeiboCell(list, getActivity());
        }
        mCell.mData = list;
        for (int i = 0; i < list.size(); i++) {
            cellList.add(mCell);
        }
        return cellList;
    }


    @Override
    protected String providedRequestDataUrl() {
        return Constants.HOME_WEIBO_FOLLOW;
    }

    @Override
    protected void getResponseData(List<WeiboRequest.StatusesBean> statuses) {
        mBaseAdapter.addAll(getCells(statuses));
    }

}
