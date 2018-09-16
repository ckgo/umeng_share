package com.kevin.home.myjob;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.kevin.home.myjob.share.ShareManager;
import com.kevin.home.myjob.share.Tools;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.utils.SocializeUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity implements View.OnClickListener {

    Button btnlogin_qq;
    Button btnlogin_sina;
    Button btnlogin_wx;
    Button btnshare_qq;
    Button btnshare_qzone;
    Button btnshare_sina;
    Button btnshare_wx;
    Button btnshare_pyq;

    private String content = "分享的内容";
    private String imgurl = "https://mobile.umeng.com/images/pic/home/social/img-1.png";

    private List<String> urllist;
    private SHARE_MEDIA share_media;

    private ShareManager shareManager;
    private boolean isShareImg = false;

    private String[] stringItem = {"https://gd2.alicdn.com/imgextra/i1/2259324182/TB2sdjGm0BopuFjSZPcXXc9EpXa_!!2259324182.jpg",
            "http://img4.tbcdn.cn/tfscom/i1/2259324182/TB2ISF_hKtTMeFjSZFOXXaTiVXa_!!2259324182.jpg",
            "http://img2.tbcdn.cn/tfscom/i1/2259324182/TB2NAMmm00opuFjSZFxXXaDNVXa_!!2259324182.jpg"};
    private List<String> stringList = new ArrayList<>();
    private ArrayList<String> fileList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnlogin_qq = (Button) findViewById(R.id.btnlogin_qq);
        btnlogin_sina = (Button) findViewById(R.id.btnlogin_sina);
        btnlogin_wx = (Button) findViewById(R.id.btnlogin_wx);
        btnshare_qq = (Button) findViewById(R.id.btnshare_qq);
        btnshare_qzone = (Button) findViewById(R.id.btnshare_qzone);
        btnshare_sina = (Button) findViewById(R.id.btnshare_sina);
        btnshare_wx = (Button) findViewById(R.id.btnshare_wx);
        btnshare_pyq = (Button) findViewById(R.id.btnshare_pyq);

        btnlogin_qq.setOnClickListener(this);
        btnlogin_sina.setOnClickListener(this);
        btnlogin_wx.setOnClickListener(this);
        btnshare_qq.setOnClickListener(this);
        btnshare_qzone.setOnClickListener(this);
        btnshare_sina.setOnClickListener(this);
        btnshare_wx.setOnClickListener(this);
        btnshare_pyq.setOnClickListener(this);
        dialog = new ProgressDialog(this);

        checkState();

        urllist = new ArrayList<>();
        urllist.add(imgurl);
        urllist.add(imgurl);
        urllist.add(imgurl);
        urllist.add(imgurl);


        if (Build.VERSION.SDK_INT >= 23) {
            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_APN_SETTINGS};
            requestPermissions(mPermissionList, 123);
        }

        for (int i = 0; i < stringItem.length; i++) {
            stringList.add(stringItem[i]);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnlogin_qq:
                boolean isauth_qq = UMShareAPI.get(this).isAuthorize(this, SHARE_MEDIA.QQ);
                if (isauth_qq) {
                    UMShareAPI.get(this).deleteOauth(this, SHARE_MEDIA.QQ, authListener);
                } else {
                    UMShareAPI.get(this).doOauthVerify(this, SHARE_MEDIA.QQ, authListener);
                }
                break;
            case R.id.btnlogin_sina:

                boolean isauth_sina = UMShareAPI.get(this).isAuthorize(this, SHARE_MEDIA.SINA);
                if (isauth_sina) {
                    UMShareAPI.get(this).deleteOauth(this, SHARE_MEDIA.SINA, authListener);
                } else {
                    UMShareAPI.get(this).doOauthVerify(this, SHARE_MEDIA.SINA, authListener);
                }

                break;
            case R.id.btnlogin_wx:

                boolean isauth_wx = UMShareAPI.get(this).isAuthorize(this, SHARE_MEDIA.WEIXIN);
                if (isauth_wx) {
                    UMShareAPI.get(this).deleteOauth(this, SHARE_MEDIA.WEIXIN, authListener);
                } else {
                    UMShareAPI.get(this).doOauthVerify(this, SHARE_MEDIA.WEIXIN, authListener);
                }

                break;
            case R.id.btnshare_qq:
                if (stringList.size() == 1) {
                    share_media = SHARE_MEDIA.QQ;
                    shareImageNet();
                } else if (stringList.size() > 1) {
                    Toast.makeText(MainActivity.this, "请稍后", Toast.LENGTH_SHORT).show();
                    share(0, content, "qq");
                }


                break;
            case R.id.btnshare_qzone:
                if (stringList.size() == 1) {
                    share_media = SHARE_MEDIA.QZONE;
                    shareImageNet();
                } else if (stringList.size() > 1) {
                    Toast.makeText(MainActivity.this, "请稍后", Toast.LENGTH_SHORT).show();
                    share(1, content, "qq_zone");
                }


                break;
            case R.id.btnshare_sina:
                share_media = SHARE_MEDIA.SINA;
                shareImageNet();
                break;
            case R.id.btnshare_wx:
                if (stringList.size() == 1) {
                    share_media = SHARE_MEDIA.WEIXIN;
                    shareImageNet();
                } else if (stringList.size() > 1) {
                    Toast.makeText(MainActivity.this, "请稍后", Toast.LENGTH_SHORT).show();
                    share(0, content, "wchat");
                }

                break;
            case R.id.btnshare_pyq:
                if (stringList.size() == 1) {
                    share_media = SHARE_MEDIA.WEIXIN_CIRCLE;
                    shareImageNet();
                } else if (stringList.size() > 1) {
                    Toast.makeText(MainActivity.this, "请稍后", Toast.LENGTH_SHORT).show();
                    share(1, content, "wchat");
                }
                break;

        }
    }

    public void checkState() {
        boolean isauth = UMShareAPI.get(this).isAuthorize(this, SHARE_MEDIA.QQ);
        if (isauth) {
            btnlogin_qq.setText("删除qq授权");
        } else {
            btnlogin_qq.setText("qq授权");
        }

        isauth = UMShareAPI.get(this).isAuthorize(this, SHARE_MEDIA.SINA);
        if (isauth) {
            btnlogin_sina.setText("删除sina授权");
        } else {
            btnlogin_sina.setText("sina授权");
        }

        isauth = UMShareAPI.get(this).isAuthorize(this, SHARE_MEDIA.WEIXIN);
        if (isauth) {
            btnlogin_wx.setText("删除wx授权");
        } else {
            btnlogin_wx.setText("wx授权");
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        UMShareAPI.get(this).onSaveInstanceState(outState);
    }

    private ProgressDialog dialog;
    UMAuthListener authListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            SocializeUtils.safeShowDialog(dialog);
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            SocializeUtils.safeCloseDialog(dialog);
            Toast.makeText(MainActivity.this, "授权成功了", Toast.LENGTH_LONG).show();
            checkState();
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            SocializeUtils.safeCloseDialog(dialog);
            Toast.makeText(MainActivity.this, "授权失败：" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            SocializeUtils.safeCloseDialog(dialog);
            Toast.makeText(MainActivity.this, "授权取消了", Toast.LENGTH_LONG).show();
        }
    };

    private void share(int i, String s, String mType) {
        isShareImg = true;
        shareManager = new ShareManager(MainActivity.this);
        shareManager.setShareImage(i, stringList, s, mType);
    }

    public void shareImageNet() {
        UMImage imageurl = new UMImage(this, stringList.get(0));
        imageurl.setThumb(new UMImage(this, R.mipmap.ic_launcher));

        UMImage imageurl2 = new UMImage(this, stringList.get(1));
        imageurl2.setThumb(new UMImage(this, R.mipmap.ic_launcher));

        new ShareAction(MainActivity.this)
                .withMedia(imageurl)//.withMedias(imageurl, imageurl2) //多图
                .setPlatform(share_media)
                .setCallback(shareListener).share();
    }

    private UMShareListener shareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            SocializeUtils.safeShowDialog(dialog);
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(MainActivity.this, "分享成功了", Toast.LENGTH_LONG).show();
            SocializeUtils.safeCloseDialog(dialog);
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            SocializeUtils.safeCloseDialog(dialog);
            Toast.makeText(MainActivity.this, "分享失败" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            SocializeUtils.safeCloseDialog(dialog);
            Toast.makeText(MainActivity.this, "分享取消了", Toast.LENGTH_LONG).show();

        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        if (isShareImg) {
            isShareImg = false;
            File file = new File(Environment.getExternalStorageDirectory() + "/shareImg/");
            Tools.deletePic(file);
        }
    }

}
