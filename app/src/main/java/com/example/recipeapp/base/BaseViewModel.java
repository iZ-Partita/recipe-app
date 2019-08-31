package com.example.recipeapp.base;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import io.reactivex.disposables.CompositeDisposable;

public abstract class BaseViewModel extends ViewModel {
    protected MutableLiveData<Throwable> mThrowableMutableLiveData = new MutableLiveData<>();
    protected final MutableLiveData<Boolean> mIsLoadingLiveData = new MutableLiveData<>();

    protected CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Override
    protected void onCleared() {
        mCompositeDisposable.clear();
        super.onCleared();
    }

    public MutableLiveData<Throwable> getThrowableMutableLiveData() {
        return mThrowableMutableLiveData;
    }

    public MutableLiveData<Boolean> getIsLoadingLiveData() {
        return mIsLoadingLiveData;
    }
}
