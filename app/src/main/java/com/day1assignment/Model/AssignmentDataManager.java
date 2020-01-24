package com.day1assignment.Model;

import java.util.ArrayList;
import java.util.List;

public class AssignmentDataManager {
    private static AssignmentDataManager instance = null;
    private List<CardModelClass> latestData;

    private AssignmentDataManager() {
        latestData = new ArrayList<>();
    }

    public static AssignmentDataManager getInstance() {

        synchronized (AssignmentDataManager.class) {
            if (instance == null) {
                instance = new AssignmentDataManager();
            }
        }

        return instance;
    }

    public List<CardModelClass> getLatestData() {
        return latestData;
    }

    public void setLatestData(List<CardModelClass> latestData) {

        this.latestData = latestData;
    }
}

