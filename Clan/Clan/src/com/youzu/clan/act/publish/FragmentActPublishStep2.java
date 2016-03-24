package com.youzu.clan.act.publish;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ToggleButton;

import com.keyboard.EmoticonsKeyBoardPopWindow;
import com.keyboard.utils.DefEmoticons;
import com.keyboard.utils.EmoticonsController;
import com.keyboard.view.EmoticonsEditText;
import com.kit.imagelib.ImageSelectAdapter;
import com.kit.imagelib.entity.ImageBean;
import com.kit.imagelib.entity.ImageLibRequestResultCode;
import com.kit.imagelib.photoselector.PicSelectActivity;
import com.kit.imagelib.uitls.ImageLibUitls;
import com.kit.utils.ListUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.youzu.clan.R;
import com.youzu.clan.base.ZBFragment;
import com.youzu.clan.base.util.SmileyUtils;
import com.youzu.clan.base.util.ToastUtils;
import com.youzu.clan.common.images.PicSelectorActivity;

import java.util.ArrayList;
import java.util.List;


public class FragmentActPublishStep2 extends ZBFragment implements View.OnClickListener {
    private GridView mGridView;
    private ImageSelectAdapter mAdapter;
    private EmoticonsEditText mEditText;
    private ImageView mIv_cover, mIv_del;
    private ImageBean mCoverImage;
    private ToggleButton tb_emoticon;

    public EmoticonsKeyBoardPopWindow mKeyBoardPopWindow;

    public static FragmentActPublishStep2 getInstance() {
        return new FragmentActPublishStep2();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_act_publish_step2, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mGridView = (GridView) view.findViewById(R.id.gridView);
        mEditText = (EmoticonsEditText) view.findViewById(R.id.et_content);
        tb_emoticon = (ToggleButton) view.findViewById(R.id.tb_emoticon);
        mIv_cover = (ImageView) view.findViewById(R.id.iv_cover);
        mIv_del = (ImageView) view.findViewById(R.id.iv_del);
        mIv_cover.setOnClickListener(this);
        mIv_del.setOnClickListener(this);
        tb_emoticon.setOnClickListener(this);
        setGridViewContent();
    }

    private void setGridViewContent() {
        mAdapter = new ImageSelectAdapter(getActivity());
        mAdapter.setOnAddPicImageClick2Class(PicSelectorActivity.class);
        mGridView.setAdapter(mAdapter);
    }

    public void showEmoticonKeyBoard() {
        if (mKeyBoardPopWindow == null) {
            EmoticonsController controller = SmileyUtils.getController(getActivity(), null);


            if (controller == null || ListUtils.isNullOrContainEmpty(controller.getEmoticonSetBeanList())) {
                ToastUtils.mkShortTimeToast(getActivity(), getString(R.string.wait_a_moment));
                return;
            }

            mKeyBoardPopWindow = new EmoticonsKeyBoardPopWindow(getActivity());
            mKeyBoardPopWindow.setBuilder(controller);
            mKeyBoardPopWindow.setEditText(mEditText);
            mKeyBoardPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    tb_emoticon.setChecked(false);
                }
            });
        }

        mKeyBoardPopWindow.showPopupWindow(tb_emoticon);
    }

    private final static int COVER_REQUEST = 0x777;

    @Override
    public void onClick(View view) {
        int vId = view.getId();
        if (vId == R.id.iv_cover) {
            Intent intent = new Intent();
            intent.setClass(getActivity(), PicSelectActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("remain", 1);
            intent.putExtras(bundle);
            getActivity().startActivityForResult(intent, COVER_REQUEST);
        } else if (vId == R.id.iv_del) {
            mCoverImage = null;
            mIv_del.setVisibility(View.GONE);
            mIv_cover.setImageResource(R.drawable.z_ic_add_cover);
        } else if (vId == R.id.tb_emoticon) {
            showEmoticonKeyBoard();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        log("onActivityResult resultCode = " + requestCode + ", requestCode = " + requestCode);
        try {
            if (requestCode == ImageLibRequestResultCode.REQUEST_SELECT_PIC && resultCode == Activity.RESULT_OK) {
                Intent intent = data;
                List<ImageBean> images = (List<ImageBean>) intent
                        .getSerializableExtra("images");
                List<ImageBean> imageBeans = mAdapter.getRealData();
                imageBeans.addAll(images);
                mAdapter.setData(imageBeans);
                mAdapter.notifyDataSetChanged();
            } else if (requestCode == ImageLibRequestResultCode.REQUEST_LOOK_PIC && resultCode == Activity.RESULT_OK) {
                Intent intent = data;
                List<ImageBean> images = (List<ImageBean>) intent
                        .getSerializableExtra("M_LIST");
            } else if (requestCode == COVER_REQUEST) {
                Intent intent = data;
                List<ImageBean> images = (List<ImageBean>) intent
                        .getSerializableExtra("images");
                if (images == null || images.size() < 1) {
                    return;
                }
                mCoverImage = images.get(0);
                if (TextUtils.isEmpty(mCoverImage.path)) {
                    mCoverImage = null;
                    return;
                }
                Bitmap bitmap = null;
                DisplayImageOptions options = ImageLibUitls.getDisplayImageOptions(
                        getResources().getDrawable(com.kit.imagelib.R.drawable.no_picture), getResources().getDrawable(com.kit.imagelib.R.drawable.no_picture));
                try {
                    ImageSize targetSize = new ImageSize(80, 80); // result Bitmap will be fit to this size
                    bitmap = ImageLoader.getInstance().loadImageSync("file://" + mCoverImage.path, targetSize, options);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                if (bitmap != null) {
                    mIv_del.setVisibility(View.VISIBLE);
                    mIv_cover.setImageBitmap(bitmap);
                } else {
                    mIv_del.setVisibility(View.GONE);
                    mIv_cover.setImageResource(R.drawable.z_ic_add_cover);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public ActInfo checkInputInfo(ActInfo actInfo) {
        String etStr = mEditText.getText().toString();
        if (TextUtils.isEmpty(etStr)) {
            ToastUtils.show(getActivity(), R.string.z_act_publish_toast_desc_null);
            return null;
        }
        actInfo.desc = DefEmoticons.replaceUnicodeByShortname(getActivity(), etStr).toString();
        actInfo.coverImage = mCoverImage;
        if (mAdapter != null) {
            actInfo.picImages = (ArrayList<ImageBean>) mAdapter.getRealData();
        }
        return actInfo;
    }
}
