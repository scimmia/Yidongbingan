package com.jiayusoft.mobile.shengli.bingan.login;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import com.jiayusoft.mobile.utils.database.DBHelper;
import com.jiayusoft.mobile.utils.database.Office;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by ASUS on 2014/11/19.
 */
public class UserDoctor implements java.io.Serializable {

    // Fields

    private Long id;
    private String name;
    private String idcard;
    private String password;
    private String glksbm;
    private String cxlb;
    private String yyidentiry;
    private String officecode;
    private Long bnjyyxq;
    private Long jyyxq;
    private String logoName;
    private String logoColor;

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    private String orgName;

    // Constructors

    /** default constructor */
    public UserDoctor() {
        String temp = "";
        this.name = temp;
        this.idcard = temp;
        this.password = temp;
        this.glksbm = temp;
        this.cxlb = temp;
        this.yyidentiry = temp;
        this.officecode = temp;
    }

    // Property accessors

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        if (StringUtils.isNotEmpty(name))
            this.name = name;
    }

    public String getIdcard() {
        return this.idcard;
    }

    public void setIdcard(String idcard) {
        if (StringUtils.isNotEmpty(idcard))
            this.idcard = idcard;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        if (StringUtils.isNotEmpty(password))
            this.password = password;
    }

    public String getGlksbm() {
        return this.glksbm;
    }

    public void setGlksbm(String glksbm) {
        if (StringUtils.isNotEmpty(glksbm))
            this.glksbm = glksbm;
    }

    public String getCxlb() {
        return cxlb;
    }

    public void setCxlb(String cxlb) {
        if (StringUtils.isNotEmpty(cxlb))
            this.cxlb = cxlb;
    }

    public String getYyidentiry() {
        return yyidentiry;
    }

    public void setYyidentiry(String yyidentiry) {
        if (StringUtils.isNotEmpty(yyidentiry))
            this.yyidentiry = yyidentiry;
    }

    public String getOfficecode() {
        return officecode;
    }

    public void setOfficecode(String officecode) {
        if (StringUtils.isNotEmpty(officecode))
            this.officecode = officecode;
    }

    public Long getBnjyyxq() {
        return bnjyyxq;
    }

    public void setBnjyyxq(Long bnjyyxq) {
        this.bnjyyxq = bnjyyxq;
    }

    public Long getJyyxq() {
        return jyyxq;
    }

    public void setJyyxq(Long jyyxq) {
        this.jyyxq = jyyxq;
    }


    public String getLogoName() {
        return logoName;
    }

    public void setLogoName(String logoName) {
        this.logoName = logoName;
    }

    public String getLogoColor() {
        return logoColor;
    }

    public void setLogoColor(String logoColor) {
        this.logoColor = logoColor;
    }

    public String getText(Context context) {
        DBHelper dbHelper = new DBHelper(context);
        List<Office> guanlikeshis = dbHelper.getOffices(getYyidentiry(),StringUtils.split(getGlksbm(),","));
        StringBuilder s = new StringBuilder("");
        for (Office t:guanlikeshis){
            s.append(t.getName()).append(',');
        }
        String guanlikeshi = StringUtils.removeEnd(s.toString(),",");
        String suoshukeshi = dbHelper.getOfficeName(getYyidentiry(),getOfficecode());
        String suoshujigou = getOrgName();
        StringBuilder sb = new StringBuilder()
                .append("<br><font size=\"5\" color=\"red\">姓名</font>").append("  :  ")
                .append("<font size=\"7\" color=\"blue\">").append(StringUtils.isNotEmpty(getName()) ? getName() : "无").append("</font><br>")
                .append("<br><font size=\"5\" color=\"red\">身份</font>").append("  :  ")
                .append("<font size=\"7\" color=\"blue\">").append(StringUtils.equals(getCxlb(), "1")?"医师":"管理员").append("</font><br>")
                .append("<br><font size=\"5\" color=\"red\">管理科室</font>").append("  :  ")
                .append("<font size=\"7\" color=\"blue\">").append(StringUtils.isNotEmpty(guanlikeshi) ? guanlikeshi : "无").append("</font><br>")
                .append("<br><font size=\"5\" color=\"red\">所属科室</font>").append("  :  ")
                .append("<font size=\"7\" color=\"blue\">").append(StringUtils.isNotEmpty(suoshukeshi) ? suoshukeshi : "无").append("</font><br>")
                .append("<br><font size=\"5\" color=\"red\">所属机构</font>").append("  :  ")
                .append("<font size=\"7\" color=\"blue\">").append(StringUtils.isNotEmpty(suoshujigou) ? suoshujigou : "无").append("</font><br>")
                ;
        return sb.toString();
    }

    static SpannableStringBuilder template;
    static SpannableStringBuilder getTemplate(){
        if (template == null){
            int color = Color.parseColor("#FFFF536F");
            template = new SpannableStringBuilder (
                    "姓名: {xingming}\n身份: {shenfen}\n管理科室: {guanlikeshi}\n所属科室: {suoshukeshi}\n所属机构: {suoshujigou}");
            template.setSpan(new ForegroundColorSpan(color), 4, 14, 0);
            template.setSpan(new ForegroundColorSpan(color), 19, 28, 0);
            template.setSpan(new ForegroundColorSpan(color), 35, 48, 0);
            template.setSpan(new ForegroundColorSpan(color), 55, 68, 0);
            template.setSpan(new ForegroundColorSpan(color), 75, 88, 0);
        }
        return template;
    }
}
