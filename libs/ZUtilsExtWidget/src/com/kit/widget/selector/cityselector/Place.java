package com.kit.widget.selector.cityselector;

import com.kit.utils.StringUtils;

import java.io.Serializable;

public class Place implements Serializable {

    private static final long serialVersionUID = -7259116803824332562L;

    private int id;
    private String code;
    private String name;
    private String pcode;
    private int level;

    public String getName() {
        return name;
    }

    public String getPcode() {
        return pcode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPcode(String pcode) {
        this.pcode = pcode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return id + " " + code + " " + name + " " + pcode;
    }


    /**
     * 获取long型的code
     *
     * @return
     */
    public Long getLongCode() {
        if (StringUtils.isEmptyOrNullOrNullStr(code))
            return null;
        else
            return Long.parseLong(code);

    }

    /**
     * 设置long型的code
     *
     * @param code
     */
    public void setLongCode(long code) {
        this.code = code + "";
    }
}
