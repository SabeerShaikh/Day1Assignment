package com.day1assignment.Interface;

import com.day1assignment.Model.CardModelClass;

import java.util.List;

public interface GetDataListener {
    void onSuccess(String message, List<CardModelClass> list);

    void onFailure(String message);
}
