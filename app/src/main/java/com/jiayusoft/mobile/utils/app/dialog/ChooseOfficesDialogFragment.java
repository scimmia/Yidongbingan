package com.jiayusoft.mobile.utils.app.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import com.jiayusoft.mobile.utils.database.Office;
import com.jiayusoft.mobile.utils.eventbus.BusProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASUS on 2014/12/16.
 */
public class ChooseOfficesDialogFragment extends DialogFragment{
    public static ChooseOfficesDialogFragment newInstance(List<Office> offices, ArrayList<Integer> results) {
        ChooseOfficesDialogFragment frag = new ChooseOfficesDialogFragment();
        Bundle args = new Bundle();
        if (offices!=null){
            String[] officeTitles = new String[offices.size()];
            for (int i=0;i<offices.size();i++){
                officeTitles[i] = offices.get(i).getName();
            }
            args.putStringArray("offices", officeTitles);

            boolean[] tempResult = new boolean[offices.size()];
            if (results!=null){
                for (Integer i : results){
                    tempResult[i] = true;
                }
            }
            args.putBooleanArray("results", tempResult);
        }
        frag.setArguments(args);
        return frag;
    }
    private boolean[] tempResult;
    ArrayList<Integer> resultList;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        resultList = new ArrayList<Integer>();
        String[] officeTitles = getArguments().getStringArray("offices");
        tempResult = getArguments().getBooleanArray("results");

        return new AlertDialog.Builder(getActivity()).setTitle("选择出院科室")
                .setMultiChoiceItems(officeTitles, tempResult,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                tempResult[which] = isChecked;
                            }
                        })
                .setNegativeButton("全选", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ArrayList<Integer> resultList = new ArrayList<Integer>();
                        for (int i = 0; i < tempResult.length; i++) {
                            resultList.add(i);
                        }
                        BusProvider.getInstance().post(new ChooseOfficesEvent(resultList));
                    }
                })
                .setNeutralButton("清空", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BusProvider.getInstance().post(new ChooseOfficesEvent(new ArrayList<Integer>()));
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ArrayList<Integer> resultList = new ArrayList<Integer>();
                        for (int i = 0; i < tempResult.length; i++) {
                            if (tempResult[i])
                                resultList.add(i);
                        }
                        BusProvider.getInstance().post(new ChooseOfficesEvent(resultList));                    }
                })
                .create();
    }
}
