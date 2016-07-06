package com.jiayusoft.mobile.utils.app.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import com.jiayusoft.mobile.shengli.bingan.R;
import com.jiayusoft.mobile.utils.eventbus.BusProvider;

/**
 * Created by ASUS on 2014/12/16.
 */
public class ChooseFromToDatesDialogFragment extends DialogFragment {

    public static ChooseFromToDatesDialogFragment newInstance(long from, long to) {
        return newInstance(from,to,0);
    }

    public static ChooseFromToDatesDialogFragment newInstance(long from, long to, int maxBeforeDays) {
        ChooseFromToDatesDialogFragment frag = new ChooseFromToDatesDialogFragment();
        Bundle args = new Bundle();
        args.putLong("from", from);
        args.putLong("to", to);
        args.putInt("maxBeforeDays", maxBeforeDays);
        frag.setArguments(args);
        return frag;
    }

    DatePicker begin,end;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        long beginTime = getArguments().getLong("from",-1);
        long endTime = getArguments().getLong("to", -1);
        LinearLayout relativeLayout = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.dialog_pick_time,null);
        begin = (DatePicker) relativeLayout.findViewById(R.id.datePickerBegin);
        end = (DatePicker) relativeLayout.findViewById(R.id.datePickerEnd);
//        int maxDays = getArguments().getInt("maxBeforeDays", 0);
//        Calendar c = Calendar.getInstance();
//        c.add(Calendar.DAY_OF_YEAR,maxDays);
//        begin.setMaxDate(c.getTimeInMillis());
//        end.setMaxDate(c.getTimeInMillis());
        if (beginTime !=-1){
            begin.getCalendarView().setDate(beginTime);
        }
        if (endTime !=-1){
            end.getCalendarView().setDate(endTime);
        }
        return new AlertDialog.Builder(getActivity()).setTitle("选择起始日期")
                .setView(relativeLayout)
                .setNegativeButton("取消", null)
                .setNeutralButton("清空", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BusProvider.getInstance().post(new ChooseFromToDatesEvent(true));
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        BusProvider.getInstance().post(
                                new ChooseFromToDatesEvent(begin.getCalendarView().getDate(), end.getCalendarView().getDate()));
                    }
                })
                .create();
    }

}
