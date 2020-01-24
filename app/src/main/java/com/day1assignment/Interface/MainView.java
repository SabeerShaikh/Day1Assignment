package com.day1assignment.Interface;

import com.day1assignment.Model.CardModelClass;

import java.util.List;

public interface MainView {
    void onGetDataSuccess(List<CardModelClass> list);

    void onGetDataFailure(String message);

}
