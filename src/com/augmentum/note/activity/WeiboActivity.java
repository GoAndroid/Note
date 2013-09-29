package com.augmentum.note.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.augmentum.note.R;
import com.augmentum.note.constant.WeiboConstant;
import com.weibo.sdk.android.*;
import com.weibo.sdk.android.sso.SsoHandler;
import com.weibo.sdk.android.util.AccessTokenKeeper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WeiboActivity extends Activity {
    private Weibo mWeibo;
    private Oauth2AccessToken mAccessToken;
    private SsoHandler mSsoHandler;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_main);

        mTextView = (TextView) findViewById(R.id.show);

        mWeibo = Weibo.getInstance(WeiboConstant.APP_KEY, WeiboConstant.REDIRECT_URL, WeiboConstant.SCOPE);

        findViewById(R.id.auth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWeibo.anthorize(WeiboActivity.this, new AuthDialogListener());
            }
        });

        findViewById(R.id.sso).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSsoHandler = new SsoHandler(WeiboActivity.this, mWeibo);
                mSsoHandler.authorize(new AuthDialogListener(), null);
            }
        });

        mAccessToken = AccessTokenKeeper.readAccessToken(this);

        if (mAccessToken.isSessionValid()) {
            String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date(mAccessToken.getExpiresTime()));
            mTextView.setText("access_token 仍在有效期内,无需再次登录: \naccess_token:"
                    + mAccessToken.getToken() + "\n有效期：" + date);
            Toast.makeText(WeiboActivity.this, "认证成功", Toast.LENGTH_SHORT).show();
        } else {
            mTextView.setText("使用SSO登录前，请检查手机上是否已经安装新浪微博客户端，"
                    + "目前仅3.0.0及以上微博客户端版本支持SSO；如果未安装，将自动转为Oauth2.0进行认证");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (null != mSsoHandler) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    class AuthDialogListener implements WeiboAuthListener {


        @Override
        public void onComplete(Bundle bundle) {

            Log.v("WeiboActivity", "onComplete");

            String token = bundle.getString("access_token");
            String expiresIn = bundle.getString("expires_in");

            mAccessToken = new Oauth2AccessToken(token, expiresIn);

            if (mAccessToken.isSessionValid()) {
                String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date(mAccessToken.getExpiresTime()));
                mTextView.setText("认证成功: \r\n access_token: " + token + "\r\n" + "expires_in: "
                        + expiresIn + "\r\n有效期： " + date);
                AccessTokenKeeper.keepAccessToken(WeiboActivity.this, mAccessToken);
                Toast.makeText(WeiboActivity.this, "认证成功", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void onWeiboException(WeiboException e) {
            Log.v("WeiboActivity", "onWeiboException");
            Toast.makeText(getApplicationContext(), "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onError(WeiboDialogError e) {
            Log.v("WeiboActivity", "onError");
            Toast.makeText(getApplicationContext(), "Auth error : " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancel() {
            Log.v("WeiboActivity", "onCancel");
            Toast.makeText(getApplicationContext(), "Auth cancel : ", Toast.LENGTH_LONG).show();
        }
    }
}
