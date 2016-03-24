package com.keyboard.bean;

import java.util.List;

public class EmoticonSetBean {
    private long id;
    /**
     * 表情集名称(必须且唯一)
     */
    private String name;

    /**
     * 表情集描述,可以为中文或者说明等，不建议做唯一标识
     */
    private String description;

    /**
     * 每页行数
     */
    private int line;
    /**
     * 每页列数
     */
    private int row;
    /**
     * 表情集图标路径
     */
    private String iconUri;
    /**
     * 表情集图标名称
     */
    private String iconName;
    /**
     * 是否在每页最后一项显示删除按钮
     */
    private boolean isShowDelBtn;
    /**
     * 表情内间距
     */
    private int itemPadding;
    /**
     * 表情列间距
     */
    private int horizontalSpacing;
    /**
     * 表情行间距
     */
    private int verticalSpacing;
    /**
     * 表情集数据源
     */
    private List<EmoticonBean> emoticonList;

    public EmoticonSetBean() {
    }

    public EmoticonSetBean(String name, int line, int row) {
        this.name = name;
        this.line = line;
        this.row = row;
    }

    public EmoticonSetBean(String name, String description, int line, int row, String iconUri,
                           String iconName, boolean isShowDelBtn, int itemPadding,
                           int horizontalSpacing, int verticalSpacing, List<EmoticonBean> emoticonList) {
        this.name = name;
        this.description = description;
        this.line = line;
        this.row = row;
        this.iconUri = iconUri;
        this.iconName = iconName;
        this.isShowDelBtn = isShowDelBtn;
        this.itemPadding = itemPadding;
        this.horizontalSpacing = horizontalSpacing;
        this.verticalSpacing = verticalSpacing;
        this.emoticonList = emoticonList;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public String getIconUri() {
        return iconUri;
    }

    public void setIconUri(String iconUri) {
        this.iconUri = iconUri;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    public boolean isShowDelBtn() {
        return isShowDelBtn;
    }

    public void setShowDelBtn(boolean isShowDelBtn) {
        this.isShowDelBtn = isShowDelBtn;
    }

    public int getItemPadding() {
        return itemPadding;
    }

    public void setItemPadding(int itemPadding) {
        this.itemPadding = itemPadding;
    }

    public int getHorizontalSpacing() {
        return horizontalSpacing;
    }

    public void setHorizontalSpacing(int horizontalSpacing) {
        this.horizontalSpacing = horizontalSpacing;
    }

    public int getVerticalSpacing() {
        return verticalSpacing;
    }

    public void setVerticalSpacing(int verticalSpacing) {
        this.verticalSpacing = verticalSpacing;
    }

    public List<EmoticonBean> getEmoticonList() {
        return emoticonList;
    }

    public void setEmoticonList(List<EmoticonBean> emoticonList) {
        this.emoticonList = emoticonList;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
