package com.miyaar.goldanalyzer;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.webkit.*;
import android.widget.Toast;
public class MainActivity extends Activity {
 private WebView webView; private ValueCallback<Uri[]> fileCallback; private static final int FILE_PICKER=41;
 @Override public void onCreate(Bundle state){super.onCreate(state);getWindow().setStatusBarColor(0xFF0B1120);getWindow().setNavigationBarColor(0xFF0B1120);
 webView=new WebView(this);webView.setBackgroundColor(0xFF0F172A);WebSettings s=webView.getSettings();s.setJavaScriptEnabled(true);s.setDomStorageEnabled(true);s.setDatabaseEnabled(true);s.setAllowFileAccess(true);s.setAllowContentAccess(true);s.setDefaultTextEncodingName("UTF-8");s.setSupportZoom(true);s.setBuiltInZoomControls(true);s.setDisplayZoomControls(false);
 webView.setWebChromeClient(new WebChromeClient(){@Override public boolean onShowFileChooser(WebView v,ValueCallback<Uri[]> cb,FileChooserParams p){if(fileCallback!=null)fileCallback.onReceiveValue(null);fileCallback=cb;try{startActivityForResult(p.createIntent(),FILE_PICKER);return true;}catch(ActivityNotFoundException e){fileCallback=null;return false;}}});
 webView.setDownloadListener((url,agent,disposition,mime,length)->{try{DownloadManager.Request q=new DownloadManager.Request(Uri.parse(url));q.setMimeType(mime);q.setTitle(URLUtil.guessFileName(url,disposition,mime));q.setDescription("تنزيل نسخة احتياطية");q.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);q.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,URLUtil.guessFileName(url,disposition,mime));((DownloadManager)getSystemService(DOWNLOAD_SERVICE)).enqueue(q);Toast.makeText(this,"بدأ تنزيل الملف",Toast.LENGTH_SHORT).show();}catch(Exception e){Toast.makeText(this,"تعذر تنزيل الملف",Toast.LENGTH_LONG).show();}});
 setContentView(webView);if(state==null)webView.loadUrl("file:///android_asset/index.html");else webView.restoreState(state);}
 @Override protected void onActivityResult(int r,int c,Intent d){super.onActivityResult(r,c,d);if(r==FILE_PICKER&&fileCallback!=null){fileCallback.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(c,d));fileCallback=null;}}
 @Override protected void onSaveInstanceState(Bundle b){webView.saveState(b);super.onSaveInstanceState(b);}
 @Override public void onBackPressed(){if(webView!=null&&webView.canGoBack())webView.goBack();else super.onBackPressed();}
 @Override protected void onDestroy(){if(fileCallback!=null)fileCallback.onReceiveValue(null);if(webView!=null){webView.destroy();webView=null;}super.onDestroy();}
}
