package com.jiayusoft.mobile.shengli.bingan.card;

import com.jiayusoft.mobile.utils.database.Bingan;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutableTriple;

import java.util.List;

/**
 * Created by Administrator on 15-1-4.
 */
public class BinganQuality {
    Bingan bingan;
    int score;
    List<ImmutableTriple<String,String,Integer>> pingjiaList;

    public BinganQuality(Bingan bingan, int score) {
        this.bingan = bingan;
        this.score = score;
//        pingjiaList = new LinkedList<ImmutableTriple<String, String, Integer>>();
    }

//    public void addPingjia(String xiangmu,String neirong,int score){
//        this.score = this.score - score;
//        pingjiaList.add(new ImmutableTriple<String, String, Integer>(xiangmu,neirong,score));
//    }

    public Bingan getBingan() {
        return bingan;
    }

    public void setBingan(Bingan bingan) {
        this.bingan = bingan;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public List<ImmutableTriple<String, String, Integer>> getPingjiaList() {
        return pingjiaList;
    }

    public void setPingjiaList(List<ImmutableTriple<String, String, Integer>> pingjiaList) {
        this.pingjiaList = pingjiaList;
    }

    public String getText() {
        StringBuilder sb = new StringBuilder()
                .append("总分：<font size=\"7\" color=\"blue\">").append(getScore()).append("</font><br>");
        if (this.pingjiaList!=null){
            for (ImmutableTriple<String,String,Integer> temp : pingjiaList){
                sb.append("<br>原因：<font size=\"7\" color=\"red\">").append(StringUtils.isNotEmpty(temp.getMiddle()) ? temp.getMiddle() : "无").append("</font>")
                        .append("<br>扣分：")
                        .append("<font size=\"7\" color=\"blue\">").append(temp.getRight()).append("</font><br>");
            }
        }
        return sb.toString();
    }

}
