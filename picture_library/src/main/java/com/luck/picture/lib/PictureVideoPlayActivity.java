package com.luck.picture.lib;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import com.luck.picture.lib.widget.QyVideoPlayer;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;

public class PictureVideoPlayActivity extends PictureBaseActivity{
    private String video_path = "";
    private String thumbImageUrl = "";
    private QyVideoPlayer mVideoView;
    private OrientationUtils orientationUtils;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        //设置window的状态栏不可见
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        //设置状态栏为透明
        window.setStatusBarColor(Color.TRANSPARENT);
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.picture_activity_video_play);
        video_path = getIntent().getStringExtra("video_path");
        mVideoView = (QyVideoPlayer) findViewById(R.id.video_view);
        //设置返回键
        mVideoView.getBackButton().setVisibility(View.VISIBLE);
        mVideoView.setBackgroundColor(Color.BLACK);
        GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_FULL);
        mVideoView.getTitleTextView().setVisibility(View.GONE);
        GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_FULL);
        mVideoView.setUp(video_path, true, "");
        //增加title
        if(TextUtils.isEmpty(thumbImageUrl)){
            Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(video_path, MediaStore.Images.Thumbnails.MINI_KIND);
            mVideoView.loadCoverImage(bitmap);
        }else {
            mVideoView.loadCoverImage(thumbImageUrl,R.drawable.ic_stub);
        }
        //设置旋转
        orientationUtils = new OrientationUtils(this, mVideoView);
        //设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
        mVideoView.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orientationUtils.resolveByClick();
            }
        });
        //是否可以滑动调整
        mVideoView.setIsTouchWiget(true);
        //设置返回按键功能
        mVideoView.getBackButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mVideoView.startPlayLogic();
    }

    @Override
    public boolean isImmersive() {
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVideoView.onVideoPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mVideoView.onVideoResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
        if (orientationUtils != null)
            orientationUtils.releaseListener();
    }

    @Override
    public void onBackPressed() {
        //先返回正常状态
        if (orientationUtils.getScreenType() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            mVideoView.getFullscreenButton().performClick();
            return;
        }
        //释放所有
        mVideoView.setVideoAllCallBack(null);
        super.onBackPressed();
    }
}
