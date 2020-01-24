package com.day1assignment.View;

import com.day1assignment.Model.CardModelClass;

import java.util.List;

/**
 * Created by Sabeer on 22/01/2020.
 */
public interface IMainView {
    void controlProgressBar(boolean isShowProgressBar);

    void onSuccess(List<CardModelClass> cardModelClassList);
    //void onFaillure(VolleyError error);

    void showErrorMessage(String errorMessage);
}
