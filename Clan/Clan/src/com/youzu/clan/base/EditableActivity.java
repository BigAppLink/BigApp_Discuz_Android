package com.youzu.clan.base;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.kit.utils.ZogUtils;
import com.youzu.clan.R;
import com.youzu.clan.base.widget.list.OnDataSetChangedObserver;
import com.youzu.clan.base.widget.list.OnEditListener;
import com.youzu.clan.base.widget.list.OnMutipleChoiceListener;
import com.youzu.clan.base.widget.list.RefreshListView;

public abstract class EditableActivity extends BaseActivity implements OnMutipleChoiceListener {

    private MenuItem menuItem;
    private MenuItem editItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected OnDataSetChangedObserver mObserver = new OnDataSetChangedObserver() {
        @Override
        public void onChanged() {

            ZogUtils.printError(EditableActivity.class, "OnDataSetChangedObserver onChanged");

            onEditableChanged(getListView());
        }
    };

    /**
     * 重写获取方法
     *
     * @return
     */
    public abstract RefreshListView getListView();


    public void deleteChoices() {
        RefreshListView listView = getListView();
        if (listView == null) {
            return;
        }
        OnEditListener listener = listView.getOnEditListener();
        if (listener != null) {
            listener.onDelete();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit, menu);
        menuItem = menu.findItem(R.id.menu_delete);
        menuItem.setVisible(false);
        editItem = menu.findItem(R.id.menu_edit);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        RefreshListView listView = getListView();
        if (listView == null) {
            return super.onOptionsItemSelected(item);
        }
        listView.setOnMutipleChoiceListener(this);

        switch (item.getItemId()) {
            case R.id.menu_edit:
                boolean isLastEditable = listView.isEditable();
                setEditable(!isLastEditable);
                return true;
            case R.id.menu_delete:
                deleteChoices();
                return true;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    public void setEditable(boolean isEditable) {
        RefreshListView listView = getListView();
        if (listView != null) {
            listView.setOnMutipleChoiceListener(this);
            listView.setEditable(isEditable);
        }
    }

    @Override
    public void onChoiceChanged(RefreshListView listView, int checkedCount) {
        ZogUtils.printError(EditableActivity.class, "checkedCount:" + checkedCount);
        String title = getString(R.string.delete_with_num, checkedCount);
        menuItem.setTitle(title);
    }


    /**
     * 点击选中之后选中的变化
     *
     * @param listView
     */
    @Override
    public void onEditableChanged(RefreshListView listView) {
        if (listView == null) {
            Log.e("APP", "listView is null!!!");
            return;
        }
        boolean isEmpty = listView.isEmpty();
        if (menuItem != null) {
            menuItem.setVisible(listView.isEditable() && !isEmpty);
            String title = getString(R.string.delete_with_num, listView.getCheckedItemCount());
            menuItem.setTitle(title);
        }
        if (editItem != null) {
            editItem.setTitle(listView.isEditable() && !isEmpty ? R.string.cancel : R.string.edit);
            editItem.setEnabled(!isEmpty);
        }
    }


}
