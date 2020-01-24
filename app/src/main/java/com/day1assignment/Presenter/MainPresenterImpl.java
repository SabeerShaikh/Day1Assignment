package com.day1assignment.Presenter;


import android.content.Context;

import com.day1assignment.Interface.GetDataListener;
import com.day1assignment.Interface.MainInteractor;
import com.day1assignment.Interface.MainPresenter;
import com.day1assignment.Interface.MainView;

import com.day1assignment.Model.CardModelClass;

import java.util.List;


public class MainPresenterImpl implements MainPresenter, GetDataListener {
    private MainView mMainView;
    private MainInteractor mInteractor;

    public MainPresenterImpl(MainView mMainView) {
        this.mMainView = mMainView;
        this.mInteractor = new MainInteractorImpl(this);
    }

    public MainView getMainView() {
        return mMainView;
    }

    @Override
    public void onSuccess(String message, List<CardModelClass> list) {

        if (mMainView != null) {

            mMainView.onGetDataSuccess(list);
        }
    }

    @Override
    public void onFailure(String message) {
        if (mMainView != null) {

            mMainView.onGetDataFailure(message);
        }
    }

    @Override
    public void getDataForList(Context context) {
        // get this done by the interactor

        mInteractor.provideData(context);

    }


}
