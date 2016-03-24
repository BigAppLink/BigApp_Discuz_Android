package com.kit.bottomtabui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.kit.bottomtabui.model.TabItem;
import com.kit.bottomtabui.view.MainBottomTabLayout;
import com.kit.bottomtabui.view.OnTabClickListener;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_tab_ui_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private TestFragmentAdapter mAdapter;
        private ViewPager mPager;
        private MainBottomTabLayout mTabLayout;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_bottom_tab_ui_main, container, false);
            setupViews(rootView);
            return rootView;
        }

        private void setupViews(View view) {
            mAdapter = new TestFragmentAdapter(getFragmentManager());
            mPager = (ViewPager) view.findViewById(R.id.tab_pager);
            mPager.setAdapter(mAdapter);
            mTabLayout = (MainBottomTabLayout) view.findViewById(R.id.main_bottom_tablayout);
            ArrayList<TabItem> tabItems = new ArrayList<TabItem>();

            TabItem tabItem0 = new TabItem("微信", getResources().getDrawable(R.drawable.icon_main_home_normal), getResources().getDrawable(R.drawable.icon_main_home_selected));
            TabItem tabItem1 = new TabItem("通讯录", getResources().getDrawable(R.drawable.icon_main_category_normal), getResources().getDrawable(R.drawable.icon_main_category_selected));
            TabItem tabItem2 = new TabItem("发现", getResources().getDrawable(R.drawable.icon_main_service_normal), getResources().getDrawable(R.drawable.icon_main_service_selected));
            TabItem tabItem3 = new TabItem("我", getResources().getDrawable(R.drawable.icon_main_mine_normal), getResources().getDrawable(R.drawable.icon_main_mine_selected));

            tabItems.add(tabItem0);
            tabItems.add(tabItem1);
            tabItems.add(tabItem2);
            tabItems.add(tabItem3);

            OnTabClickListener.OnItemClickListener onItemClickListener = new OnTabClickListener.OnItemClickListener() {
                @Override
                public boolean onItemClick(View v, int position) {
                    return false;
                }
            };

            mTabLayout.bind(tabItems, mPager, onItemClickListener, null);
        }
    }
}
