package com.example.myapplication.view.adapter;

import android.support.annotation.IntDef;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.view.fragment.BaseRecyclerViewAdapter;
import com.example.myapplication.mode.bean.DataEntity;
import com.example.myapplication.util.ImgHelper;
import com.example.myapplication.util.TimeUtil;

import java.util.List;
import java.util.Stack;

/**
 * Created by cfadmin on 2017/7/21.
 */
public class BaseAdapter extends BaseRecyclerViewAdapter {

    public static final int HEADER_TYPE = 1;
    public static final int FOOT_TYPE = 2;
    public static final int BODY_TYPE_TXT = 3;
    public static final int BODY_TYPE_TXT_IMG = 4;
    public static final int BODY_TYPE_TXT_GIF = 5;
    public static final int EMPTY_TYPE = 6;

    @IntDef({HEADER_TYPE, FOOT_TYPE, BODY_TYPE_TXT, BODY_TYPE_TXT_IMG, BODY_TYPE_TXT_GIF, EMPTY_TYPE})
    @interface ViewType {
    }

    public BaseAdapter(List<DataEntity.ResultsBean> items, IItemCLickListener listener) {
        super(items, listener);
        mEmptyViewData = "没有数据";
    }
    public void setHeadItemValuse(int position,Object object){
        if (mHeaderValues.size()<=position) {
            return;
        }
        mHeaderValues.set(position,object);
    }

    public void addFootItem() {
        if (mFootValues.size() < 1) {
            super.addFootItem("加载更多");
            notifyItemInserted(getItemCount());
        }
    }


    public void setFootItemText(String txt) {
        if (mFootValues.size() == 1) {
            mFootValues.set(0, txt);
            notifyItemChanged(getItemCount() - mFootValues.size());
        }
    }
    @SuppressWarnings("unchecked")
    @Override
    public void onBindView(BaseViewHolder holder, int position) {
        switch (holder.mViewType) {
            case HEADER_TYPE:
                if (holder.mItem instanceof List) {
                    ViewPager viewPager = holder.getView(R.id.vp_header);
                    PagerAdapter adapter = viewPager.getAdapter();
                    if (adapter == null) {
                        viewPager.setAdapter(new VPAdapter((List<DataEntity.ResultsBean>) holder.mItem));
                    }else {
                        adapter.notifyDataSetChanged();
                    }
                }
                break;
            case BODY_TYPE_TXT:
                if (holder.mItem instanceof DataEntity.ResultsBean) {
                    DataEntity.ResultsBean mItem = (DataEntity.ResultsBean) holder.mItem;
                    holder.<TextView>getView(R.id.tv_txt_title).setText(mItem.getDesc());
                    holder.<TextView>getView(R.id.tv_txt_name).setText(mItem.getWho());
                    holder.<TextView>getView(R.id.tv_txt_time).setText(TimeUtil.formatTime(mItem.getCreatedAt()));
                }
                break;
            case BODY_TYPE_TXT_GIF:
                break;
            case BODY_TYPE_TXT_IMG:
                break;
            case FOOT_TYPE:
                if (holder.mItem instanceof String) {
                    String mItem = (String) holder.mItem;
                    holder.<TextView>getView(R.id.tv_foot).setText(mItem);
                }
                break;
            case EMPTY_TYPE:
                if (holder.mItem instanceof String) {
                    String mItem = (String) holder.mItem;
                    if (holder.itemView instanceof TextView) {
                        ((TextView) holder.itemView).setText(mItem);
                    }
                }
                break;
        }
    }

    @Override
    public int getItemLayoutId(@ViewType int viewType) {
        switch (viewType) {
            case HEADER_TYPE:
                return R.layout.item_header;
            case BODY_TYPE_TXT:
                return R.layout.item_txt;
            case BODY_TYPE_TXT_GIF:
                return R.layout.item_txt_gif;
            case BODY_TYPE_TXT_IMG:
                return R.layout.item_txt_img;
            case FOOT_TYPE:
                return R.layout.item_foot;
            case EMPTY_TYPE:
                return R.layout.item_empty;
        }
        return 0;
    }

    @Override
    protected
    @ViewType
    int getBodyItemViewType(int size, Object bodyType) {
        return BODY_TYPE_TXT;
    }

    @Override
    protected
    @ViewType
    int getFootItemViewType(int size, Object footType) {
        return FOOT_TYPE;
    }

    @Override
    protected
    @ViewType
    int getHeaderItemViewType(int position, Object headType) {
        return HEADER_TYPE;
    }

    @Override
    protected int getEmptyItemViewType(Object mEmptyViewData) {
        return EMPTY_TYPE;
    }


    static class VPAdapter extends PagerAdapter{
        List<DataEntity.ResultsBean> dataEntities;
        Stack<View> recycleViews = new Stack<View>();
        public VPAdapter(List<DataEntity.ResultsBean> dataEntities) {
            this.dataEntities = dataEntities;
        }

        @Override
        public int getCount() {
            return dataEntities.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = null;
            VH vh = null;
            if(!recycleViews.isEmpty()){
                view = recycleViews.pop();
                vh = (VH) view.getTag();
            }
            if(view == null){
                vh = new VH();
                view = LayoutInflater.from(container.getContext())
                        .inflate(R.layout.item_header_item,container,false);
                vh.tv = (TextView) view.findViewById(R.id.tv_content_header);
                vh.iv = (ImageView) view.findViewById(R.id.iv_icon_head);
                view.setTag(vh);
            }
            container.addView(view);
            DataEntity.ResultsBean dataEntity = dataEntities.get(position);
            vh.tv.setText(dataEntity.getDesc());
            if(dataEntity.getImages().size()>0){
                ImgHelper.showImageIntoView(dataEntity.getImages().get(0),vh.iv);
            }
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            recycleViews.push((View) object);
        }

        static class VH{
            TextView tv;
            ImageView iv;
        }

    }
}
