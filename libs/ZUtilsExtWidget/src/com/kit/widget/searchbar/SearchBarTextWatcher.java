package com.kit.widget.searchbar;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

// 文本观察者
public class SearchBarTextWatcher implements TextWatcher {

    public SearchBar searchBar;


    public SearchBarTextWatcher() {
    }

    public SearchBarTextWatcher(SearchBar searchBar) {
        this.searchBar = searchBar;
    }


    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
    }

    // 当文本改变时候的操作
    @Override
    public void onTextChanged(CharSequence s, int start, int before,
                              int count) {
        // 如果编辑框中文本的长度大于0就显示删除按钮否则不显示
        if (searchBar.ivSearchbarDeleteIcon != null) {
            if (s.length() > 0) {
                searchBar.ivSearchbarDeleteIcon.setVisibility(View.VISIBLE);
            } else {
                searchBar.ivSearchbarDeleteIcon.setVisibility(View.GONE);
            }
        }
    }

}