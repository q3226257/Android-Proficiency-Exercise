package com.example.myapplication.View.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.View.DetailActivity;
import com.example.myapplication.View.adapter.BaseAdapter;
import com.example.myapplication.View.view_i.IBaseF2A;
import com.example.myapplication.View.view_i.IMainView;
import com.example.myapplication.mode.bean.DataEntity;
import com.example.myapplication.present.present_i.IMainPresent;

import java.util.ArrayList;
import java.util.List;

public class BaseDataFragment extends BaseFragment<DataEntity> implements BaseRecyclerViewAdapter.IItemCLickListener
        , SwipeRefreshLayout.OnRefreshListener {

    private static final String KEY_DATA_ = "com.example.myapplication.View.fragment_KEY_DATA_";
    private static final String KEY_TYPE_ = "com.example.myapplication.View.fragment_KEY_TYPE_";
    private IBaseF2A mListener;
    private DataEntity mDataEntity;
    private BaseAdapter mContentAdapter;
    private SwipeRefreshLayout mRefreshLayout;
    private
    @IMainPresent.TYPE
    String mType;

    public BaseDataFragment() {
    }

    public static BaseDataFragment newInstance(DataEntity dataEntity, String title, @IMainPresent.TYPE String type) {
        BaseDataFragment fragment = new BaseDataFragment();
        fragment.mFragmentTitle = title;
        Bundle args = new Bundle();
        args.putParcelable(KEY_DATA_, dataEntity);
        args.putString(KEY_TYPE_, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void initData() {
        Bundle b;
        if ((b = getArguments()) != null) {
            mDataEntity = b.getParcelable(KEY_DATA_);
            checkType(b.getString(KEY_TYPE_));
        }
        if (mDataEntity == null) {
            throw new IllegalStateException("not data");
        }
        mListener.refreshData(mType);
    }

    private void checkType(String type) {
        switch (type) {
            case IMainPresent.ANDROID:
                this.mType = IMainPresent.ANDROID;
                return;
            case IMainPresent.IOS:
                this.mType = IMainPresent.IOS;
                return;
            case IMainPresent.WEB:
                this.mType = IMainPresent.WEB;
                return;
        }
        throw new RuntimeException("must input a available mType as : ANDROID,IOS,前端");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_android, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initData();
        initView(view);
    }

    private void initView(View view) {
        mContentAdapter = new BaseAdapter(mDataEntity.getResults(), new BaseRecyclerViewAdapter.IItemCLickListener() {
            @Override
            public void click(BaseRecyclerViewAdapter.BaseViewHolder viewHolder) {
                DataEntity.ResultsBean data;
                if (viewHolder.mItem instanceof DataEntity.ResultsBean) {
                    data = (DataEntity.ResultsBean) viewHolder.mItem;
                    String url = data.getUrl();
                    DetailActivity.newInstance(url, getContext());
                }
            }
        });
        mContentAdapter.addHeadItem(new Object());

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv_content_list);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext()
                , DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mContentAdapter);
        recyclerView.addOnScrollListener(new ScrollListener());

        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.srl_refresh);
        mRefreshLayout.setOnRefreshListener(this);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IMainView) {
            mListener = (IBaseF2A) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement IMainView");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void click(BaseRecyclerViewAdapter.BaseViewHolder baseViewHolder) {

    }

    public void showLoading(String type) {

    }

    public void showError(String type) {
        if (mRefreshLayout != null && mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(false);
        }
        mContentAdapter.setFootItemText(getContext().getString(R.string.requestError));
    }

    private void loaderMoreData() {
        mContentAdapter.addFootItem();
        mContentAdapter.setFootItemText(getContext().getString(R.string.isLoading));
        mListener.nextPageData(mType);
    }


    @Override
    public void onRefresh() {
        mListener.refreshData(mType);
    }

    @Override
    public void refreshView(DataEntity modifiedData, DataEntity modifyData) {
        mRefreshLayout.setRefreshing(false);
        mContentAdapter.setFootItemText(getContext().getString(R.string.loadComplete));


        List<Object> headerList = new ArrayList<>();
        for (DataEntity.ResultsBean bean : modifiedData.getResults()) {
            if (bean.getImages() != null && bean.getImages().size() > 0) {
                headerList.add(bean);
            }
        }
        mContentAdapter.setHeadItemValuse(0, headerList);

        int nowSize = modifiedData.getResults().size();
        int addSize = modifyData.getResults().size();
        int headSize = mContentAdapter.getHeaderSize();
        if (nowSize == addSize) {
            mContentAdapter.notifyDataSetChanged();
        } else {
            mContentAdapter.notifyItemRangeInserted(nowSize - addSize + headSize, addSize);
        }
    }


    class ScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
            int childCount = layoutManager.getChildCount();
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                if (firstVisibleItemPosition + childCount == layoutManager.getItemCount()
                        && layoutManager.getItemCount() > childCount) {
                    loaderMoreData();
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    }

}
