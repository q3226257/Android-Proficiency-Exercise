package com.example.myapplication.mode.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.myapplication.mode.mode_i.IDataOption;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by cfadmin on 2017/7/19.
 */
public class DataEntity implements Parcelable ,IDataOption<DataEntity> {
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelableArray(results.toArray(new ResultsBean[results.size()]), i);
    }

    public DataEntity(Parcel parcel) {
        Parcelable[] parcelables = parcel.readParcelableArray(ClassLoader.getSystemClassLoader());
        if (parcelables != null) {
            results = Arrays.asList(Arrays.copyOf(parcelables, parcelables.length, ResultsBean[].class));
        }
    }

    public static final Creator<DataEntity> CREATOR = new Creator<DataEntity>() {
        @Override
        public DataEntity createFromParcel(Parcel parcel) {
            return new DataEntity(parcel);
        }

        @Override
        public DataEntity[] newArray(int i) {
            return new DataEntity[i];
        }
    };

    private List<ResultsBean> results;

    public DataEntity() {
        results = new ArrayList<>();
    }
    public void injectData(DataEntity dataEntity){
        results.addAll(dataEntity.getResults());
    }

    @Override
    public void refreshData(DataEntity data) {
        results.clear();
        injectData(data);
    }

    public List<ResultsBean> getResults() {
        return results;
    }

    public void setResults(List<ResultsBean> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "DataEntity{" +
                "results=" + results +
                '}';
    }

    public static class ResultsBean implements Parcelable {
        /**
         * _id : 5953c796421aa90ca3bb6a79
         * createdAt : 2017-06-28T23:13:26.286Z
         * desc : 仿Google Play商店沉侵式样式
         * publishedAt : 2017-07-19T13:23:20.375Z
         * source : chrome
         * type : Android
         * url : https://github.com/matrixxun/ImmersiveDetailSample
         * used : true
         * who : Jason
         * images : ["http://img.gank.io/e52126cf-77e4-4a3b-9eb9-b62753d1d99b"]
         */
        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(_id);
            parcel.writeString(createdAt);
            parcel.writeString(desc);
            parcel.writeString(publishedAt);
            parcel.writeString(source);
            parcel.writeString(type);
            parcel.writeString(url);
            parcel.writeByte((byte) (used ? 1 : 0));
            parcel.writeString(who);
            parcel.writeStringList(images);
        }

        public ResultsBean(Parcel parcel) {
            _id = parcel.readString();
            createdAt = parcel.readString();
            desc = parcel.readString();
            publishedAt = parcel.readString();
            source = parcel.readString();
            type = parcel.readString();
            url = parcel.readString();
            used = (int) parcel.readByte() == 1;
            who = parcel.readString();
            parcel.readStringList(images);
        }

        public static final Creator<ResultsBean> CREATOR = new Creator<ResultsBean>() {
            @Override
            public ResultsBean createFromParcel(Parcel parcel) {
                return new ResultsBean(parcel);
            }

            @Override
            public ResultsBean[] newArray(int i) {
                return new ResultsBean[i];
            }
        };


        private String _id;
        private String createdAt;
        private String desc;
        private String publishedAt;
        private String source;
        private String type;
        private String url;
        private boolean used;
        private String who;
        private List<String> images;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getPublishedAt() {
            return publishedAt;
        }

        public void setPublishedAt(String publishedAt) {
            this.publishedAt = publishedAt;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public boolean isUsed() {
            return used;
        }

        public void setUsed(boolean used) {
            this.used = used;
        }

        public String getWho() {
            return who;
        }

        public void setWho(String who) {
            this.who = who;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }

        @Override
        public String toString() {
            return "ResultsBean{" +
                    "who='" + who + '\'' +
                    ", desc='" + desc + '\'' +
                    ", createdAt='" + createdAt + '\'' +
                    '}';
        }
    }
}
