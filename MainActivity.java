package com.khanaitrader.app;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.DownloadListener;
import android.widget.Toast;
import android.content.Context;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

public class MainActivity extends Activity {
    private WebView webView;
    private final String backupName = "khan_trader_backup.json";

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webView = new WebView(this);
        setContentView(webView);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_NEVER_ALLOW);
        webView.addJavascriptInterface(new KhanNativeStorage(this), "KhanNativeStorage");
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        webView.setDownloadListener((url, userAgent, contentDisposition, mimeType, contentLength) ->
            Toast.makeText(this, "For backup export, use the in-app backup function when available.", Toast.LENGTH_LONG).show());
        webView.loadUrl("file:///android_asset/index.html");
    }

    @Override public void onBackPressed() {
        if (webView != null && webView.canGoBack()) webView.goBack(); else super.onBackPressed();
    }

    public static class KhanNativeStorage {
        private final Context context;
        KhanNativeStorage(Context context) { this.context = context.getApplicationContext(); }

        @JavascriptInterface public synchronized String loadBackup() {
            File f = new File(context.getFilesDir(), "khan_trader_backup.json");
            if (!f.exists()) return "";
            try (FileInputStream in = new FileInputStream(f)) {
                byte[] bytes = new byte[(int) f.length()];
                int n = in.read(bytes);
                return new String(bytes, 0, Math.max(0, n), StandardCharsets.UTF_8);
            } catch (Exception e) { return ""; }
        }

        @JavascriptInterface public synchronized boolean saveBackup(String json) {
            if (json == null || json.length() > 8_000_000) return false;
            File target = new File(context.getFilesDir(), "khan_trader_backup.json");
            File temp = new File(context.getFilesDir(), "khan_trader_backup.tmp");
            try (FileOutputStream out = new FileOutputStream(temp)) {
                out.write(json.getBytes(StandardCharsets.UTF_8));
                out.getFD().sync();
            } catch (Exception e) { return false; }
            if (target.exists() && !target.delete()) return false;
            return temp.renameTo(target);
        }
    }
}
