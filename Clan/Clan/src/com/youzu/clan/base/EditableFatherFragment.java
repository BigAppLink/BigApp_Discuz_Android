package com.youzu.clan.base;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.youzu.clan.R;
import com.youzu.clan.base.widget.list.OnDataSetChangedObserver;
import com.youzu.clan.base.widget.list.OnEditListener;
import com.youzu.clan.base.widget.list.OnMutipleChoiceListener;
import com.youzu.clan.base.widget.list.RefreshListView;


/**
 * 罩在EditableFragment上面的fragment
 */
public abstract class EditableFatherFragment extends BaseFragment implements OnMutipleChoiceListener , OnEditListener{
    private MenuItem menuItem;
    private MenuItem editItem;

    protected OnDataSetChangedObserver mObserver = new OnDataSetChangedObserver() {
        @Override
        public void onChanged() {
            if (isAdded() && isVisible()) {
                onEditableChanged(getListView());
            }
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public abstract RefreshListView getListView();

    public void setEditable(boolean isEditable) {
        RefreshListView listView = getListView();
        if (listView != null) {
            listView.setOnMutipleChoiceListener(this);
            listView.setEditable(isEditable);
        }
    }

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
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater = getActivity().getMenuInflater();
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.edit, menu);
        menuItem = menu.findItem(R.id.menu_delete);
        menuItem.setVisible(false);
        editItem = menu.findItem(R.id.menu_edit);
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

    @Override
    public void onChoiceChanged(RefreshListView listView, int checkedCount) {
        String title = getString(R.string.delete_with_num, checkedCount);
        if (menuItem != null)
            menuItem.setTitle(title);
    }


    @Override
    public void onEditableChanged(RefreshListView listView) {
        if (listView == null) {
            Log.e("APP", "listView is null!!!");
            return;
        }
        boolean isEmpty = listView.isEmpty();
        if (menuItem != null) {
            menuItem.setVisible(listView.isEditable() && !isEmpty);
            String title = getString(R.string.delete, listView.getCheckedItemCount());
            menuItem.setTitle(title);
        }
        if (editItem != null) {
            editItem.setTitle(listView.isEditable() && !isEmpty ? R.string.cancel : R.string.edit);
            editItem.setEnabled(!isEmpty);
        }
    }

}
