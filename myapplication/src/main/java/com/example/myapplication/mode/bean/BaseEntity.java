package com.example.myapplication.mode.bean;

import java.io.Serializable;

public  class BaseEntity<T> implements Serializable {
    private boolean error;
    private T results;

    public BaseEntity(boolean error, T results) {
        this.error = error;
        this.results = results;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public T getResults() {
        return results;
    }

    public void setResults(T results) {
        this.results = results;
    }
}