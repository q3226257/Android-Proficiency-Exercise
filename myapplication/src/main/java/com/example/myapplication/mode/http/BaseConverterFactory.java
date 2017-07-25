package com.example.myapplication.mode.http;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class BaseConverterFactory extends Converter.Factory {
    public static final BaseConverterFactory INSTANCE = new BaseConverterFactory();

    public static BaseConverterFactory create() {
        return INSTANCE;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new BaseConverter(type);
    }

    public static class BaseConverter<T> implements Converter<ResponseBody, T> {


        private static final String ERROR_TAG = "error";
        private static final String RESULT_TAG = "results";
        private Type mType;
        private Gson mGson;

        public BaseConverter(Type mType) {
            this.mType = mType;
            this.mGson = new Gson();
        }

        @Override
        public T convert(ResponseBody value) throws IOException {

            String string = value.string();
            String returnStr;
            boolean error;
            try {
                JSONObject jb = new JSONObject(string);
                error = jb.getBoolean(ERROR_TAG);
                if (error) {
                    Log.e("error net", jb.toString());
                    return null;
                }
                returnStr = jb.getString(RESULT_TAG);
                returnStr = "{\"results\":" + returnStr + "}";

                return (T) new Gson().fromJson(returnStr, mType);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                value.close();
            }
            return null;
        }
    }


}

