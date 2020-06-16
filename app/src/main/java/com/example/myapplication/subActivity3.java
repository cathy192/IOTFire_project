package com.example.myapplication;
import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.hardware.display.DisplayManager;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;

public class subActivity3 extends AppCompatActivity {
    WebView webView;
  //  EditText editText;
    String url="http://192.168.0.12:8080/stream_simple.html";
    private static final String videoFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/MediaProjection.mp4";
    private static final int REQUEST_CODE_PERMISSIONS = 100;
    private static final int REQUEST_CODE_MediaProjection = 101;

    private MediaProjection mediaProjection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub3);
        checkSelfPermission();
       // Button bt_save=(Button)findViewById(R.id.Savebutton);
      //  editText= (EditText)findViewById(R.id.videoUrl);
        webView=(WebView)findViewById(R.id.wv_stream);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);


        webView.loadUrl(url);
        getPreferences();
        /*bt_save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                savePreferences(editText.getText().toString());
            }

        }); */
    }
 //   @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDestroy() {
        // 녹화중이면 종료하기
        if (mediaProjection != null) {
            mediaProjection.stop();
        }
        super.onDestroy();
    }
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onActivityResult(int requestCode, final int resultCode, @Nullable final Intent data) {
        // 미디어 프로젝션 응답
        if (requestCode == REQUEST_CODE_MediaProjection && resultCode == RESULT_OK) {
            screenRecorder(resultCode, data);
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public boolean checkSelfPermission() {
        String temp = "";
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.RECORD_AUDIO + " ";
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.WRITE_EXTERNAL_STORAGE + " ";
        }

        if (TextUtils.isEmpty(temp) == false) {
            ActivityCompat.requestPermissions(this, temp.trim().split(" "), REQUEST_CODE_PERMISSIONS);
            return false;
        } else {
            initView();
            return true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSIONS: {
                int length = permissions.length;
                for (int i = 0; i < length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        // 퍼미션 동의가 1개라도 부족하면 화면을 초기화 하지 않음
                        return;
                    }
                    initView();
                }
                return;
            }
            default:
                return;
        }
    }
    private void startMediaProjection() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            MediaProjectionManager mediaProjectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
            startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(), REQUEST_CODE_MediaProjection);
        }
    }
    /**
     * 미디어 레코더
     *
     * @return
     */
    private MediaRecorder createRecorder() {
        MediaRecorder mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setOutputFile(videoFile);
        DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
        mediaRecorder.setVideoSize(displayMetrics.widthPixels, displayMetrics.heightPixels);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setVideoEncodingBitRate(512 * 1000);
        mediaRecorder.setVideoFrameRate(30);
        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mediaRecorder;
    }
    private void initView() {
        findViewById(R.id.actionRec).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 미디어 프로젝션 요청
                startMediaProjection();
            }
        });
    }

    /**
     * 화면녹화
     *
     * @param resultCode
     * @param data
     */
  //  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void screenRecorder(int resultCode, @Nullable Intent data) {
        final MediaRecorder screenRecorder = createRecorder();
        MediaProjectionManager mediaProjectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
        mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, data);
        MediaProjection.Callback callback = new MediaProjection.Callback() {
            @Override
            public void onStop(){
                super.onStop();
                if (screenRecorder != null) {
                    screenRecorder.stop();
                    screenRecorder.reset();
                    screenRecorder.release();
                }
                mediaProjection.unregisterCallback(this);
                mediaProjection = null;
            }
        };
        mediaProjection.registerCallback(callback, null);

        DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
        mediaProjection.createVirtualDisplay(
                "sample",
                displayMetrics.widthPixels, displayMetrics.heightPixels, displayMetrics.densityDpi, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                screenRecorder.getSurface(), null, null);
        /**
         * 미디어 프로젝션 요청
         */

        final Button actionRec = findViewById(R.id.actionRec);
        actionRec.setText("STOP REC");
        actionRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionRec.setText("START REC");
                if (mediaProjection != null) {
                    mediaProjection.stop();

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(videoFile), "video/mp4");
                    startActivity(intent);
                }
            }
        });
        screenRecorder.start();
    }
    private void getPreferences(){
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        String sUrl = "http://192.168.0.12:8080/stream_simple.mjpeg";
                //pref.getString("stream", (String) url);//디폴터값이 왜 안되???
   //     editText.setText(sUrl);
    }

    //save
    private void savePreferences(String s){
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("stream", s);
        editor.commit();
        finish();
    }

}
