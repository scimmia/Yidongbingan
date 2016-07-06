package com.jiayusoft.mobile.utils.app.dialog;

/**
 * Created by ASUS on 2014/12/16.
 */
public class ChooseFromToDatesEvent {
    long beginTime;
    long endTime;
    boolean cleanFlag;

    public ChooseFromToDatesEvent(boolean cleanFlag) {
        this.cleanFlag = cleanFlag;
    }

    public boolean isCleanFlag() {
        return cleanFlag;
    }

    public void setCleanFlag(boolean cleanFlag) {
        this.cleanFlag = cleanFlag;
    }

    public ChooseFromToDatesEvent(long beginTime, long endTime) {
        cleanFlag = false;
        if (beginTime > endTime){
            this.beginTime = endTime;
            this.endTime = beginTime;
        }else {
            this.beginTime = beginTime;
            this.endTime = endTime;
        }
    }

    public long getBeginTime() {
        return beginTime;
    }

    public long getEndTime() {
        return endTime;
    }
}
