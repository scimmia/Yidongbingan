package com.jiayusoft.mobile.shengli.bingan.card;

/**
 * Created by ASUS on 2014/12/2.
 */
public class CardEvent{
    int position;
    int eventType;

    public CardEvent(int position, int eventType) {
        this.position = position;
        this.eventType = eventType;
    }

    public int getPosition() {
        return position;
    }

    public int getEventType() {
        return eventType;
    }
}
