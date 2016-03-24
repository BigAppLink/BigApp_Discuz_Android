package com.youzu.clan.thread;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kit.utils.ZogUtils;
import com.kit.widget.wheel.OnWheelChangedListener;
import com.kit.widget.wheel.WheelView;
import com.kit.widget.wheel.adapters.AbstractWheelAdapter;
import com.youzu.android.framework.ViewUtils;
import com.youzu.android.framework.view.annotation.ViewInject;
import com.youzu.android.framework.view.annotation.event.OnClick;
import com.youzu.clan.R;
import com.youzu.clan.base.json.forumnav.NavForum;
import com.youzu.clan.base.widget.ViewHolder;

import java.util.List;

/**
 * Created by tangh on 2015/8/11.
 */
public class ThreadPublishDialogFragment extends DialogFragment {
    @ViewInject(R.id.forum)
    private WheelView forum;
    @ViewInject(R.id.thread)
    private WheelView thread;
    @ViewInject(R.id.subThread)
    private WheelView subThread;
    private Context context;
    private List<NavForum> forums;
    private ThreadWheelAdapter forumAdapter;
    private ThreadWheelAdapter threadAdapter;
    private ThreadWheelAdapter subThreadAdapter;

    public List<NavForum> getForums() {
        return forums;
    }

    public void setForums(List<NavForum> forums) {
        this.forums = forums;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setStyle(STYLE_NO_TITLE, R.style.dialogWindowAnim);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Window window = getDialog().getWindow();
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.dialogWindowAnim);

        View view = inflater.inflate(R.layout.dialog_fragment_thread_publish, null);
        ViewUtils.inject(this, view);

        LinearLayout layout = (LinearLayout) view.findViewById(R.id.layout);
        LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) layout.getLayoutParams();
        param.width = getResources().getDisplayMetrics().widthPixels;

        context = getActivity();
        initForum();
        initThread();
        initSubThread();

        return view;
    }


    private void initForum() {
        forumAdapter = new ThreadWheelAdapter(context);
        forumAdapter.setData(forums);
        forum.setViewAdapter(forumAdapter);
        forum.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                threadAdapter.setData(forums.get(newValue).getForums());

                thread.invalidateWheel(true);
                if(threadAdapter.getForums()!=null&&threadAdapter.getForums().size()>0){
                    thread.setCurrentItem(0);
                    subThreadAdapter.setData(threadAdapter.getForums().get(0).getSubs());
                }else{
                    subThreadAdapter.setData(null);
                }
                subThread.invalidateWheel(true);
                subThread.setCurrentItem(0);
                ZogUtils.printError(ThreadPublishDialogFragment.class, ">>>new=" + newValue);
            }
        });
    }

    private void initThread() {
        threadAdapter = new ThreadWheelAdapter(context);
        if (forums != null && forums.size() > 0) {
            threadAdapter.setData(forums.get(0).getForums());
        }
        thread.setViewAdapter(threadAdapter);
        thread.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                subThreadAdapter.setData(threadAdapter.getForums().get(newValue).getSubs());
                subThread.invalidateWheel(true);
                subThread.setCurrentItem(0);
                ZogUtils.printError(ThreadPublishDialogFragment.class,">>> new="+newValue);
            }
        });
    }

    private void initSubThread() {
        subThreadAdapter = new ThreadWheelAdapter(context);
        subThreadAdapter.setIsSubThread(true);
        if(forums != null && forums.size() > 0){
            List<NavForum> threads=forums.get(0).getForums();
            if(threads!=null&&threads.size()>0){
                subThreadAdapter.setData(threads.get(0).getSubs());
            }
        }
        subThread.setViewAdapter(subThreadAdapter);
    }

    @OnClick(R.id.cancel)
    private void cancel(View view) {
        this.dismiss();
    }

    @OnClick(R.id.confirm)
    private void confirm(View view) {

        if(context instanceof ThreadPublishActivity){
            dismiss();
            ((ThreadPublishActivity)context).onConfirm(forum.getCurrentItem(), thread.getCurrentItem(), subThread.getCurrentItem());
        }
    }

    class ThreadWheelAdapter extends AbstractWheelAdapter {
        private List<NavForum> forums;
        private boolean isSubThread=false;
        public ThreadWheelAdapter(Context context) {

        }

        public void setData(List<NavForum> forums) {
            this.forums = forums;

            ZogUtils.printError(ThreadPublishDialogFragment.class, ">>>forum.size=" + (forums ==null?null:forums.size()));
        }

        public List<NavForum> getForums() {
            return forums;
        }

        public void setIsSubThread(boolean isSubThread) {
            this.isSubThread = isSubThread;
        }

        @Override
        public int getItemsCount() {
            int count=0;
            if (forums != null) {
                count=forums.size();
            }
            if(isSubThread){
                count++;
            }
            return count;
        }

        @Override
        public View getItem(int index, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.item_dialog_fragment_thread, null);
            }
            TextView threadName = ViewHolder.get(convertView, R.id.threadName);
            if(isSubThread&&index==0){
                threadName.setText("");
            }else if(isSubThread){
                index--;
                threadName.setText(forums.get(index).getName());
            }else{
                threadName.setText(forums.get(index).getName());
            }
            return convertView;
        }
    }

}
