package com.jiayusoft.mobile.utils.app.dialog;

import java.util.ArrayList;

/**
 * Created by ASUS on 2014/12/16.
 */
public class ChooseOfficesEvent {
    ArrayList<Integer> results;

    public ChooseOfficesEvent(ArrayList<Integer> guanliResults) {
        this.results = guanliResults;
    }

    public ArrayList<Integer> getResults() {
        return results;
    }
}
