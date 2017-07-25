package com.example.myapplication.view.fragment;

import android.content.res.Resources;
import android.support.annotation.IdRes;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public abstract class BaseRecyclerViewAdapter extends RecyclerView.Adapter<BaseRecyclerViewAdapter.BaseViewHolder> {

    protected final List<?> mBodyValues;
    protected final List<Object> mHeaderValues = new ArrayList<>();
    protected final List<Object> mFootValues = new ArrayList<>();
    protected Object mEmptyViewData;
    protected final IItemCLickListener mListener;

    private static final int HEADER = 0x11;
    private static final int FOOT = 0x12;
    private static final int EMPTY = 0x13;
    private static final int BODY = 0x14;

    @IntDef({HEADER, FOOT, BODY, EMPTY})
    private @interface VIEW_TYPE {
    }

    public BaseRecyclerViewAdapter(List<?> items, IItemCLickListener listener) {
        mBodyValues = items;
        mListener = listener;
    }

    public void addHeadItem(Object h) {
        if (h == null) return;
        if (!mHeaderValues.contains(h)) {
            mHeaderValues.add(h);
            notifyItemInserted(mHeaderValues.size() - 1);
        }
    }

    public void addFootItem(Object b) {
        if (b == null) return;
        if (!mFootValues.contains(b)) {
            mFootValues.add(b);
            notifyItemInserted(getItemCount() - mFootValues.size());
        }
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(getItemLayoutId(viewType), parent, false);
        return new BaseViewHolder(viewType, view);
    }

    @Override
    public void onBindViewHolder(final BaseViewHolder holder, int position) {
        int headSize = mHeaderValues.size();
        int footSize = mFootValues.size();
        int bodySize = mBodyValues.size();

        switch (getViewType(position)) {
            case EMPTY:
                holder.mItem = mEmptyViewData;
                break;
            case HEADER:
                holder.mItem = mHeaderValues.get(position);
                break;
            case FOOT:
                holder.mItem = mFootValues.get(position - headSize - getFootTopSize(bodySize));
                break;
            case BODY:
                holder.mItem = mBodyValues.get(position - headSize);
                break;
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.click(holder);
                }
            }
        });
        onBindView(holder, position);
    }

    @Override
    public int getItemCount() {
        int count = mBodyValues.size();
        if (count == 0 && mEmptyViewData != null) {
            return 1 + mHeaderValues.size() + mFootValues.size();
        }
        return count + mHeaderValues.size() + mFootValues.size();
    }

    @Override
    public final int getItemViewType(int position) {
        int headSize = mHeaderValues.size();
        int footSize = mFootValues.size();
        int bodySize = mBodyValues.size();
        int viewType = 0;
        switch (getViewType(position)) {
            case EMPTY:
                //emptyView 类型
                viewType = getEmptyItemViewType(mEmptyViewData);
                break;
            case HEADER:
                //header 类型
                viewType = getHeaderItemViewType(position, mHeaderValues.get(position));
                break;
            case FOOT:
                //foot 类型
                int topSize = getFootTopSize(bodySize);
                int size = position - headSize - topSize;
                viewType = getFootItemViewType(size, mFootValues.get(size));
                break;
            case BODY:
                //body 类型
                int size1 = position - headSize;
                viewType = getBodyItemViewType(size1, mBodyValues.get(size1));
                break;
        }
        return viewType;
    }

    //获取foot 上的 bodySize 或者 emptyViewSize
    private int getFootTopSize(int bodySize) {
        return bodySize == 0 ? (mEmptyViewData == null ? 0 : 1) : bodySize;
    }

    private int getViewType(int position) {
        int headSize = mHeaderValues.size();
        int footSize = mFootValues.size();
        int bodySize = mBodyValues.size();
        int count = headSize + footSize + bodySize;

        if (bodySize == 0 && position == headSize && mEmptyViewData != null) {
            return EMPTY;
        } else if (position < headSize) {
            //header 类型
            return HEADER;
        } else if (position >= headSize && position < bodySize + headSize) {
            //body 类型
            return BODY;
        } else {
            int topSize = getFootTopSize(bodySize);
            if (position >= headSize + topSize && position < headSize + footSize + topSize) {
                //foot 类型
                return FOOT;
            }
        }
        return -1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public int getHeaderSize() {
        return mHeaderValues.size();
    }

    public void getFootSize() {
        mFootValues.size();
    }

    protected int getEmptyItemViewType(Object mEmptyViewData) {
        return 0;
    }

    protected int getFootItemViewType(int size, Object footData) {
        return 0;
    }

    protected int getBodyItemViewType(int size, Object bodyData) {
        return 0;
    }

    protected int getHeaderItemViewType(int position, Object headData) {
        return 0;
    }

    public abstract void onBindView(BaseViewHolder holder, int position);


    public abstract
    @LayoutRes
    int getItemLayoutId(int viewType);


    public static class BaseViewHolder extends RecyclerView.ViewHolder {
        public Object mItem;
        public int mViewType;
        private Map<Integer, View> views = new HashMap<>();

        public BaseViewHolder(int viewType, View itemView) {
            super(itemView);

            this.mViewType = viewType;
        }

        public <E extends View> E getView(@IdRes int viewId) {
            E view = (E) views.get(viewId);
            if (view == null) {
                view = (E) itemView.findViewById(viewId);
            }
            if (view == null) {
                throw new Resources.NotFoundException(String.valueOf(viewId).concat("not found"));
            } else {
                views.put(viewId, view);
            }
            return view;
        }
    }

    public interface IItemCLickListener {
        void click(BaseViewHolder baseViewHolder);
    }

}
