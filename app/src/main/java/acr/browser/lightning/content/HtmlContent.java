package acr.browser.lightning.content;

import android.app.Activity;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.annotation.TargetApi;
import android.webkit.ValueCallback;
import android.webkit.WebView;

import android.os.Build;

/**
 * Created by jhjiao on 2017/6/30.
 */

public class HtmlContent {
    // not landscape: null
    // landscape: 'true'
    private final static String JsIsLandscape = "(function() { return " +
            "(document.head.querySelector(\"meta[name='screen-orientation']\").content === 'landscape') " +
            "|| (document.head.querySelector(\"meta[name='x5-orientation']\").content === 'landscape')" + "; })();";

    @NonNull private final Activity mActivity;
    @NonNull private String mHtml;

    private HtmlContent(@NonNull Activity activity, @NonNull final String html) {
        mActivity = activity;
        mHtml = html;
    }

    private void setOrientation() {
        if (isLandScape()) {
            if (mActivity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                mActivity.setRequestedOrientation(android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        } else {
            if (mActivity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                mActivity.setRequestedOrientation(android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        }
    }

    private boolean isLandScape() {
        return mHtml.equals("true");
    }

    static public void evaluateJavascript(@NonNull final Activity activity, @NonNull WebView view) {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        // landscape or portrait
        view.evaluateJavascript(
                HtmlContent.JsIsLandscape,
                new ValueCallback<String>() {
                    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
                    @Override
                    public void onReceiveValue(String html) {
                        HtmlContent hc = new HtmlContent(activity, html);
                        hc.setOrientation();
                    }
                });
    }
}
