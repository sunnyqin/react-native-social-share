package com.barefootcoders.android.react.KDSocialShare;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.Callback;

import java.util.List;
import java.io.File;

public class KDSocialShareModule extends ReactContextBaseJavaModule {
  ReactApplicationContext reactContext;

  public KDSocialShareModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return "KDSocialShare";
  }

  @ReactMethod
  public void tweet(ReadableMap options, Callback callback) {
    try {
      String shareText = options.getString("text");
      Intent shareIntent;

      if (doesPackageExist("com.twitter.android")) {
        shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setClassName("com.twitter.android", "com.twitter.android.PostActivity");
        shareIntent.setType("text/*");
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareText);
      } else {
        String tweetUrl = "https://twitter.com/intent/tweet?text=" + shareText;
        Uri uri = Uri.parse(tweetUrl);
        shareIntent = new Intent(Intent.ACTION_VIEW, uri);
      }
      reactContext.startActivity(shareIntent);
    } catch (Exception ex) {
      callback.invoke("error");
    }
  }

  @ReactMethod
  public void shareOnFacebook(ReadableMap options, Callback callback) {
    try {
      Intent shareIntent;

      if (options.hasKey("text") && doesPackageExist("com.facebook.katana")) {
        String shareText = options.getString("text");
        shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
      } else if (options.hasKey("link")) {
        String shareUrl = options.getString("link");
        String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=" + shareUrl;
        shareIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
      } else {
        if (options.hasKey("text") && !doesPackageExist("com.facebook.katana")) {
          callback.invoke("error", "If text is provided to Facebook sharing, the application must be installed");
        } else {
          callback.invoke("error");
        }
        return;
      }

      reactContext.startActivity(shareIntent);
    } catch (Exception ex) {
      callback.invoke("error");
    }
  }

  private boolean doesPackageExist(String targetPackage) {
    PackageManager pm = this.reactContext.getPackageManager();
    try {
      PackageInfo info = pm.getPackageInfo(
        targetPackage,
        PackageManager.GET_META_DATA
      );
     } catch (PackageManager.NameNotFoundException e) {
       return false;
     }
     return true;
  }
}
