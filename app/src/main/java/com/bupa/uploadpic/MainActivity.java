package com.bupa.uploadpic;

import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.baoyz.actionsheet.ActionSheet;
import com.bupa.uploadpic.adapter.ImageAdapter;
import com.bupa.uploadpic.util.ImageUtils;
import com.bupa.uploadpic.util.UIUtils;
import com.bupa.uploadpic.view.BadgeDrawable;
import com.bupa.uploadpic.view.MyGridView;
import com.bupa.uploadpic.view.MyToast;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.daimajia.numberprogressbar.OnProgressBarListener;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.carbs.android.library.MDDialog;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

import static com.bupa.uploadpic.util.UIUtils.dp2px;

public class MainActivity extends AppCompatActivity {

    private MyGridView mGvImage;
    private RippleView mRv;
    private LinkedList<String> mDataList;
    private int yetNum;// 上传图片总数量
    private ImageAdapter mAdapter;
    private TextView mTvUpdateimg;
    private NumberProgressBar mProgressBar;
    private Timer mTimer;
    private PopupWindow mPopupWindow;
    private View mImageView;
    private LinearLayout mLayoutShow;
    private BadgeDrawable mDrawable;
    private SpannableString mSpannableString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        listener();
    }

    private void initData() {
        numPic(0);
        mDataList = new LinkedList<>();
        mDataList.addLast("default");// 初始化第一个添加按钮数据
        mAdapter = new ImageAdapter(UIUtils.getContext(), mDataList);
        mGvImage.setAdapter(mAdapter);
    }

    private void listener() {

        mGvImage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, long id) {
                preview(parent, view, position);


            }
        });
        mGvImage.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
                return deleteIma(parent, position);
            }
        });
        // 按钮触发
        mRv.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                mProgressBar.setProgress(0);
                isToast = false;
                upload(rippleView);
            }
        });
        mProgressBar.setOnProgressBarListener(new OnProgressBarListener() {
            @Override
            public void onProgressChange(int current, int max) {
                upload(current, max);
            }
        });
    }

    private void preview(AdapterView<?> parent, View view, int position) {
        if (parent.getItemAtPosition(position) == "default") { // 添加图片
            dialog();
        } else {
            View imageView = View.inflate(UIUtils.getContext(), R.layout.item_pager_img_sel_bupa, null);
            final PopupWindow popupWindow = new PopupWindow(imageView,
                    WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            //设置需要焦点
            popupWindow.setFocusable(true);
            popupWindow.dismiss();
            popupWindow.setBackgroundDrawable(new ColorDrawable());
            //设置进入和出去的动画
            popupWindow.setAnimationStyle(R.style.PopupWindowStyle);
            //在给定的view的下面显示， 后面的两个参数分别对应的是x方向和Y方向的偏移量
            popupWindow.showAsDropDown(view, 60, -view.getHeight() + 10);
            ImageView image = (ImageView) imageView.findViewById(R.id.ivImage);
            View close = imageView.findViewById(R.id.ivPhotoCheaked);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });
            image.setImageBitmap(getBitmap(mDataList.get(position)));
        }
    }

    private void upload(RippleView rippleView) {
        mPopupWindow = new PopupWindow(mImageView,
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        //设置需要焦点
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable());
        //设置进入和出去的动画
        mPopupWindow.setAnimationStyle(R.style.PopupWindowStyle);
        //在给定的view的下面显示， 后面的两个参数分别对应的是x方向和Y方向的偏移量
        mPopupWindow.showAsDropDown(rippleView, 12, -360);
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBar.incrementProgressBy(1);
                    }
                });
            }
        }, 1000, 100);
    }

    boolean isToast;

    private void upload(int current, int max) {
        if (max == current) {
            mPopupWindow.dismiss();
            if (!isToast) {
                MyToast.success("上传成功!");
                mLayoutShow.setBackgroundColor(0xfff);
                isToast = true;
            }
            mProgressBar.setProgress(0);
        }
    }

    private boolean deleteIma(final AdapterView<?> parent, final int position) {
        if (parent.getItemAtPosition(position) != "default") {
            new MDDialog.Builder(this)
                    .setMessages(new String[]{"亲~ 是否删除该图片"})
                    .setIcon(getResources().getDrawable(R.mipmap.over_tb))
                    .setTitle("警告")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mDataList.getLast() == "default") {
                                delete(parent, position);
                            } else {
                                mDataList.addLast("default");
                                delete(parent, position);
                            }
                        }
                    }).show();
        }
        return true;
    }

    private void delete(AdapterView<?> parent, int position) {

        mDataList.remove(parent.getItemAtPosition(position));
        mAdapter.update(mDataList); // 刷新图片
        yetNum = mDataList.size() - 1;
        numPic(yetNum);
    }

    private void numPic(int num) {
        mDrawable = new BadgeDrawable.Builder()
                .type(BadgeDrawable.TYPE_WITH_TWO_TEXT_COMPLEMENTARY)
                .badgeColor(0xffCC9933)
                .text1("可上传10张")
                .text2("已经上传"+String.valueOf(num)+"张")
                .textSize(50)
                .padding(dp2px(10), dp2px(10), dp2px(10), dp2px(10), dp2px(10))
                .strokeWidth((int) dp2px(2))
                .build();
        mSpannableString = new SpannableString(TextUtils.concat(
              mDrawable.toSpannable()
        ));

        mTvUpdateimg.setText(mSpannableString);
    }

    private void dialog() {
        ActionSheet.createBuilder(UIUtils.getContext(), getSupportFragmentManager())
                .setCancelButtonTitle("取消(Cancel)")
                .setOtherButtonTitles("打开相册(Open Gallery)", "拍照(Camera)")
                .setCancelableOnTouchOutside(true)
                .setListener(new ActionSheet.ActionSheetListener() {
                    @Override
                    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {

                    }

                    @Override
                    public void onOtherButtonClick(ActionSheet actionSheet, int code) {

                        switch (code) {
                            // 相册
                            case 0:
                                GalleryFinal.openGalleryMuti(0, getBuild(), mOnHanlderResultCallback);
                                break;
                            // 相机
                            case 1:
                                GalleryFinal.openCamera(0, getBuild(), mOnHanlderResultCallback);
                                break;
                        }
                    }
                })
                .show();
    }

    private FunctionConfig getBuild() {
        return new FunctionConfig.Builder()
                .setEnableCamera(true)
                .setEnableEdit(true)
                .setEnableCrop(true)
                .setEnableRotate(true)
                .setCropSquare(true)
                .setEnablePreview(true)
                .setMutiSelectMaxSize(10 - yetNum)
                .build();
    }

    private void initView() {
        mGvImage = (MyGridView) findViewById(R.id.gv_image);
        mRv = (RippleView) findViewById(R.id.rv);
        mTvUpdateimg = (TextView) findViewById(R.id.tv_updateimg);
        mImageView = View.inflate(UIUtils.getContext(), R.layout.view_loadding, null);
        mProgressBar = (NumberProgressBar) mImageView.findViewById(R.id.myProgressBar);
        mLayoutShow = (LinearLayout) findViewById(R.id.layout_show);
    }

    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            if (resultList != null) {
                for (PhotoInfo path : resultList) {
                    mDataList.addFirst(path.getPhotoPath());
                }
                // 获取已上传的张数
                yetNum = mDataList.size() - 1;
                if (yetNum == 10) {
                    // 已上传10张的时候  删除最后一张
                    mDataList.remove(mDataList.getLast());
                }
                mAdapter.update(mDataList); // 刷新图片
                numPic(yetNum);
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            MyToast.error("上传失败!");
        }
    };

    @Override
    protected void onDestroy() {
        mTimer.cancel();
    }

    private Bitmap getBitmap(String data) {
        return ImageUtils.getImageThumbnail(
                data,
                ImageUtils.getWidth(UIUtils.getContext()) / 3 - 5,
                ImageUtils.getWidth(UIUtils.getContext()) / 3 - 5);
    }
}
