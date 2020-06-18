package com.example.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class subActivity5 extends AppCompatActivity{

    private GpsTracker gpsTracker;

    // onRequestPermissionsResult에서 수신된 결과에서
    // ActivityCompat.requestPermissions를 사용한 퍼미션 요청을 구별하기 위해 사용됩니다.
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int SEND_MESSAGE_ENABLE_REQUEST_CODE=2002;
    private static final int CALL_PHONE_REQUEST_CODE=2003;
    private static final int PERMISSIONS_REQUEST_CODE = 100;

    private static final String FIRE_CALL_NUM="01081418141";        //황소현
    private static final String EMERGENCY_CALL_NUM1="01063647136";  //오지영
    private static final String EMERGENCY_CALL_NUM2="01025999183";  //이예나
    private static final String EMERGENCY_CALL_NUM3="";

    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.SEND_SMS,
                                            Manifest.permission.CALL_PHONE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub5);

        final TextView textview_address = (TextView)findViewById(R.id.addressText);

        if (!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting();
        }else {
            checkRunTimePermission();
        }

        gpsTracker = new GpsTracker(subActivity5.this);
        double latitude = gpsTracker.getLatitude();
        double longitude = gpsTracker.getLongitude();
        String address = getCurrentAddress(latitude, longitude);
        textview_address.setText(address);

        final Button ShowLocationButton = (Button) findViewById(R.id.addressBtn);
        ShowLocationButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                gpsTracker = new GpsTracker(subActivity5.this);
                double latitude = gpsTracker.getLatitude();
                double longitude = gpsTracker.getLongitude();
                String address = getCurrentAddress(latitude, longitude);
                textview_address.setText(address);

            }
        });
        final ImageButton fireButton=(ImageButton)findViewById(R.id.fireBtn);
        fireButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                gpsTracker = new GpsTracker(subActivity5.this);
                double latitude = gpsTracker.getLatitude();
                double longitude = gpsTracker.getLongitude();
                String address = getCurrentAddress(latitude, longitude);
                sendMessage(FIRE_CALL_NUM,address+" 에서 화재가 발생하였습니다.");
                callPhone(FIRE_CALL_NUM);
            }
        });
        final ImageButton fireCallButton=(ImageButton)findViewById(R.id.fireCallBtn);
        fireCallButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                callPhone(FIRE_CALL_NUM);
            }
        });
        final ImageButton fireMessageButton=(ImageButton)findViewById(R.id.fireMessageBtn);
        fireMessageButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0) {
                gpsTracker = new GpsTracker(subActivity5.this);
                double latitude = gpsTracker.getLatitude();
                double longitude = gpsTracker.getLongitude();
                String address = getCurrentAddress(latitude, longitude);
                sendMessage(FIRE_CALL_NUM,address+" 에서 화재가 발생하였습니다.");
            }
        });
        final ImageButton eCallButton1=(ImageButton)findViewById(R.id.eCallBtn1);
        eCallButton1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0) {
                callPhone(EMERGENCY_CALL_NUM1);
            }
        });
        final ImageButton eCallButton2=(ImageButton)findViewById(R.id.eCallBtn2);
        eCallButton2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0) {
                callPhone(EMERGENCY_CALL_NUM2);
            }
        });
        final ImageButton eCallButton3=(ImageButton)findViewById(R.id.eCallBtn3);
        eCallButton3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0) {
                callPhone(EMERGENCY_CALL_NUM3);
            }
        });
        final ImageButton eMessageButton1=(ImageButton)findViewById(R.id.eMessage1);
        eMessageButton1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0) {
                gpsTracker = new GpsTracker(subActivity5.this);
                double latitude = gpsTracker.getLatitude();
                double longitude = gpsTracker.getLongitude();
                String address = getCurrentAddress(latitude, longitude);
                sendMessage(EMERGENCY_CALL_NUM1,address+" 에서 화재가 발생하였습니다.");
            }
        });
        final ImageButton eMessageButton2=(ImageButton)findViewById(R.id.eMessageBtn2);
        eMessageButton2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0) {
                gpsTracker = new GpsTracker(subActivity5.this);
                double latitude = gpsTracker.getLatitude();
                double longitude = gpsTracker.getLongitude();
                String address = getCurrentAddress(latitude, longitude);
                sendMessage(EMERGENCY_CALL_NUM2,address+" 에서 화재가 발생하였습니다.");
            }
        });
        final ImageButton eMessageButton3=(ImageButton)findViewById(R.id.eMessageBtn3);
        eMessageButton3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0) {
                gpsTracker = new GpsTracker(subActivity5.this);
                double latitude = gpsTracker.getLatitude();
                double longitude = gpsTracker.getLongitude();
                String address = getCurrentAddress(latitude, longitude);
                sendMessage(EMERGENCY_CALL_NUM3,address+" 에서 화재가 발생하였습니다.");
            }
        });

    }

    public void callPhone(String number){
        String call="tel:"+number;
        try {
            startActivity(new Intent("android.intent.action.CALL", Uri.parse(call)));
        }
        catch (SecurityException e){
            Toast.makeText(subActivity5.this, "접근 권한 에러. 설정에서 전화 퍼미션을 허용해주세요", Toast.LENGTH_LONG).show();
        }
        catch (IllegalArgumentException e){
            Toast.makeText(subActivity5.this,"전화번호 설정이 잘못되었습니다. 등록한 번호를 다시 한번 확인해주세요",Toast.LENGTH_LONG).show();
        }
        catch(Exception e){
            Toast.makeText(subActivity5.this,"전화를 거는 중 오류가 발생하였습니다.",Toast.LENGTH_LONG).show();
        }
    }

    public void sendMessage(String number,String content){
        try {
            //전송
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(number, null, content, null, null);

            Toast.makeText(subActivity5.this, "문자가 발송되었습니다.(화재 발생)", Toast.LENGTH_LONG).show();
        }
        catch (SecurityException e){
            Toast.makeText(subActivity5.this, "접근 권한 에러. 설정에서 메세지 전송 퍼미션을 허용해주세요", Toast.LENGTH_LONG).show();
        }
        catch (IllegalArgumentException e){
            Toast.makeText(subActivity5.this,"전화번호 설정이 잘못되었습니다. 등록한 번호를 다시 한번 확인해주세요",Toast.LENGTH_LONG).show();
        }
        catch(Exception e){
            Toast.makeText(subActivity5.this,"메세지를 보내는 중 오류가 발생하였습니다.",Toast.LENGTH_LONG).show();
        }
    }


     /*
     * ActivityCompat.requestPermissions를 사용한 퍼미션 요청의 결과를 리턴받는 메소드입니다.
     */
    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        if ( permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {
            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면
            boolean check_result = true;

            // 모든 퍼미션을 허용했는지 체크합니다.
            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }

            if ( check_result ) {
                //위치 값을 가져올 수 있음
            }
            else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this,REQUIRED_PERMISSIONS[2])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this,REQUIRED_PERMISSIONS[3])) {
                    //Toast.makeText(subActivity5.this, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
                    finish();
                }else {
                    //Toast.makeText(subActivity5.this, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();
                }
            }

        }
    }

    void checkRunTimePermission(){
        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(subActivity5.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(subActivity5.this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        int hasSendMessagePermission=ContextCompat.checkSelfPermission(subActivity5.this,
                Manifest.permission.SEND_SMS);
        int hasCallPhonePermission= ContextCompat.checkSelfPermission(subActivity5.this,
                Manifest.permission.CALL_PHONE);

        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)
            // 3.  위치 값을 가져올 수 있음
        }
        else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.
            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(subActivity5.this, REQUIRED_PERMISSIONS[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(subActivity5.this,REQUIRED_PERMISSIONS[1])) {
                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(subActivity5.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(subActivity5.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }
            else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(subActivity5.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }
        }

        if(hasCoarseLocationPermission != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(subActivity5.this,REQUIRED_PERMISSIONS[1])){
                Toast.makeText(subActivity5.this,"이 기능을 실행하기 위해서는 위치 접근 권한이 필요합니다.",Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(subActivity5.this,REQUIRED_PERMISSIONS,PERMISSIONS_REQUEST_CODE);
            }
            else{
                ActivityCompat.requestPermissions(subActivity5.this,REQUIRED_PERMISSIONS,PERMISSIONS_REQUEST_CODE);
            }
        }

        if(hasSendMessagePermission!=PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(subActivity5.this,REQUIRED_PERMISSIONS[2])){
                Toast.makeText(subActivity5.this,"이 기능을 실행하기 위해서는 SMS 접근 권한이 필요합니다.",Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(subActivity5.this,REQUIRED_PERMISSIONS,PERMISSIONS_REQUEST_CODE);
            }
            else{
                ActivityCompat.requestPermissions(subActivity5.this,REQUIRED_PERMISSIONS,PERMISSIONS_REQUEST_CODE);
            }
        }

        if(hasCallPhonePermission!=PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(subActivity5.this,REQUIRED_PERMISSIONS[3])){
                Toast.makeText(subActivity5.this,"이 기능을 실행하기 위해서는 통화 접근 권한이 필요합니다.",Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(subActivity5.this,REQUIRED_PERMISSIONS,PERMISSIONS_REQUEST_CODE);
            }
            else{
                ActivityCompat.requestPermissions(subActivity5.this,REQUIRED_PERMISSIONS,PERMISSIONS_REQUEST_CODE);
            }
        }

    }

    @SuppressLint("WrongConstant")
    public String getCurrentAddress(double latitude, double longitude) {
        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    7);
        }
        catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        }
        catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";
        }

        if (addresses == null || addresses.size() == 0) {
            //Toast.makeText(this, "주소 미발견", 1000).show();
            Toast.makeText(this,"주소를 발견하지 못하였습니다. 설정에서 위치 접근 퍼미션을 허용하거나 다시 시도해주세요",Toast.LENGTH_LONG).show();
            return "주소 미발견";
        }

        Address address = addresses.get(0);

        //Toast.makeText(subActivity5.this, "현재 위치가 갱신되었습니다.", Toast.LENGTH_LONG).show();
        return address.getAddressLine(0).toString()+"\n";


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case GPS_ENABLE_REQUEST_CODE:
                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {
                        checkRunTimePermission();
                        return;
                    }
                }
                break;
            case SEND_MESSAGE_ENABLE_REQUEST_CODE:
            case CALL_PHONE_REQUEST_CODE:
                checkRunTimePermission();
                break;
        }
    }

    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {
        AlertDialog.Builder builder = new AlertDialog.Builder(subActivity5.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }


    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

}
