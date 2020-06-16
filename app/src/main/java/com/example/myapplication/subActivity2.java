package com.example.myapplication;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;

import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.invoke.VolatileCallSite;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class subActivity2 extends AppCompatActivity {
    TextView mTvBluetoothStatus;

    Button mBtnConnect;

    ImageButton helpBtn;
    Button[] helpNextBtn=new Button[2];

    //LED off, 불 색깔
    ImageButton ledOffBtn;
    ImageButton redBtn;
    ImageButton yellowBtn;
    ImageButton greenBtn;
    ImageButton blueBtn;
    ImageButton whiteBtn;

    //LED 상태
    Boolean isLedOn;
    Boolean isRedOn;
    Boolean isYellowOn;
    Boolean isGreenOn;
    Boolean isBlueOn;
    Boolean isWhiteOn;

    //LED 버튼 이미지
    Drawable dInvisible;
    Drawable dOffline;
    Drawable dOnline;

    LinearLayout mainLayout;
    LinearLayout linearLayout1;
    ConstraintLayout[] helpLayout=new ConstraintLayout[2];

    BluetoothAdapter mBluetoothAdapter;
    Set<BluetoothDevice> mPairedDevices;
    List<String> mListPairedDevices;

    Handler mBluetoothHandler;
    ConnectedBluetoothThread mThreadConnectedBluetooth;
    BluetoothDevice mBluetoothDevice;
    BluetoothSocket mBluetoothSocket;

    final static int BT_REQUEST_ENABLE = 1;
    final static int BT_MESSAGE_READ = 2;
    final static int BT_CONNECTING_STATUS = 3;
    final static UUID BT_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub2);

        mTvBluetoothStatus = (TextView) findViewById(R.id.bluetoothContent);

        linearLayout1=(LinearLayout)findViewById(R.id.lineLayout1);
        mainLayout=(LinearLayout)findViewById(R.id.mainLayout);
        helpLayout[0]=(ConstraintLayout)findViewById(R.id.helpLayout1);
        helpLayout[1]=(ConstraintLayout)findViewById(R.id.helpLayout2);

        helpBtn=(ImageButton)findViewById(R.id.helpButton);
        helpNextBtn[0]=(Button)findViewById(R.id.helpNext1);
        helpNextBtn[1]=(Button)findViewById(R.id.helpNext2);

        ledOffBtn=(ImageButton)findViewById(R.id.ledOff);
        redBtn=(ImageButton)findViewById(R.id.red);
        greenBtn=(ImageButton)findViewById(R.id.green);
        yellowBtn=(ImageButton)findViewById(R.id.yellow);
        blueBtn=(ImageButton)findViewById(R.id.blue);
        whiteBtn=(ImageButton)findViewById(R.id.white);

        dInvisible=getResources().getDrawable(android.R.drawable.presence_invisible);
        dOffline=getResources().getDrawable(android.R.drawable.presence_offline);
        dOnline=getResources().getDrawable(android.R.drawable.presence_online);

        mBtnConnect = (Button) findViewById(R.id.btnConnect);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        setLedState();

        mBtnConnect.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                listPairedDevices();
            }
        });

        //help 버튼 클릭
        helpBtn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                mainLayout.setVisibility(View.GONE);
                helpLayout[1].setVisibility(View.GONE);
                helpLayout[0].setVisibility(View.VISIBLE);
            }
        });
        helpNextBtn[0].setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick (View v){
                helpLayout[0].setVisibility(View.GONE);
                helpLayout[1].setVisibility(View.VISIBLE);
            }
        });
        helpNextBtn[1].setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick (View v){
                helpLayout[1].setVisibility(View.GONE);
                mainLayout.setVisibility(View.VISIBLE);
            }
        });


        redBtn.setOnClickListener(new Button.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                if (mThreadConnectedBluetooth != null) {
                    mThreadConnectedBluetooth.write("1");

                    if(isLedOn) {
                        if (isRedOn) {
                            redBtn.setBackground(dInvisible);
                            isRedOn = false;
                        } else {
                            redBtn.setBackground(dOnline);
                            isRedOn = true;
                        }
                    }

                }
            }
        });
        yellowBtn.setOnClickListener(new Button.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                if (mThreadConnectedBluetooth != null) {
                    mThreadConnectedBluetooth.write("2");

                    if(isLedOn) {
                        if (isYellowOn) {
                            yellowBtn.setBackground(dInvisible);
                            isYellowOn = false;
                        } else {
                            yellowBtn.setBackground(dOnline);
                            isYellowOn = true;
                        }
                    }

                }
            }
        });
        whiteBtn.setOnClickListener(new Button.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                if (mThreadConnectedBluetooth != null) {
                    mThreadConnectedBluetooth.write("3");

                    if(isLedOn) {
                        if (isWhiteOn) {
                            whiteBtn.setBackground(dInvisible);
                            isWhiteOn = false;
                        } else {
                            whiteBtn.setBackground(dOnline);
                            isWhiteOn = true;
                        }
                    }

                }
            }
        });
        blueBtn.setOnClickListener(new Button.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                if (mThreadConnectedBluetooth != null) {
                    mThreadConnectedBluetooth.write("4");

                    if(isLedOn) {
                        if (isBlueOn) {
                            blueBtn.setBackground(dInvisible);
                            isBlueOn = false;
                        } else {
                            blueBtn.setBackground(dOnline);
                            isBlueOn = true;
                        }
                    }

                }
            }
        });
        greenBtn.setOnClickListener(new Button.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                if (mThreadConnectedBluetooth != null) {
                    mThreadConnectedBluetooth.write("5");

                    if(isLedOn) {
                        if (isGreenOn) {
                            greenBtn.setBackground(dInvisible);
                            isGreenOn = false;
                        } else {
                            greenBtn.setBackground(dOnline);
                            isGreenOn = true;
                        }
                    }

                }
            }
        });
        ledOffBtn.setOnClickListener(new Button.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                if (mThreadConnectedBluetooth != null) {
                    mThreadConnectedBluetooth.write("0");
                    if(isLedOn){
                        ledOffBtn.setBackground(dInvisible);
                        redBtn.setBackground(dOffline);
                        greenBtn.setBackground(dOffline);
                        yellowBtn.setBackground(dOffline);
                        whiteBtn.setBackground(dOffline);
                        blueBtn.setBackground(dOffline);
                        setLedStateOff();
                    }
                    else{
                        isLedOn=true;
                        ledOffBtn.setBackground(dOnline);
                        redBtn.setBackground(dInvisible);
                        greenBtn.setBackground(dInvisible);
                        yellowBtn.setBackground(dInvisible);
                        whiteBtn.setBackground(dInvisible);
                        blueBtn.setBackground(dInvisible);
                    }
                }
            }
        });

        mBluetoothHandler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == BT_MESSAGE_READ) {
                    String readMessage = null;
                    try {
                        readMessage = new String((byte[]) msg.obj, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    //mTvReceiveData.setText(readMessage);
                }
            }
        };

        //처음에는 LED 제어 창 안보이게 하기
        linearLayout1.setVisibility(View.INVISIBLE);
        mainLayout.setVisibility(View.VISIBLE);
        helpLayout[0].setVisibility(View.GONE);
        helpLayout[1].setVisibility(View.GONE);
    }

    void bluetoothOn() {
        if (mBluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "블루투스를 지원하지 않는 기기입니다.", Toast.LENGTH_LONG).show();
        } else {
            if (mBluetoothAdapter.isEnabled()) {
                Toast.makeText(getApplicationContext(), "블루투스가 이미 활성화 되어 있습니다.", Toast.LENGTH_LONG).show();
                mTvBluetoothStatus.setText("활성화");
            } else {
                Toast.makeText(getApplicationContext(), "블루투스가 활성화 되어 있지 않습니다.", Toast.LENGTH_LONG).show();
                Intent intentBluetoothEnable = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intentBluetoothEnable, BT_REQUEST_ENABLE);
            }
        }
    }

    void setLedStateOff(){
        //여기 라즈베리에서 블루투스 통신으로 값 받아오는걸로 바꿔야 함
        isLedOn=false;
        isBlueOn=false;
        isGreenOn=false;
        isRedOn=false;
        isWhiteOn=false;
        isYellowOn=false;
    }

    void setLedState(){
        //여기 라즈베리에서 블루투스 통신으로 값 받아오는걸로 바꿔야 함
        isLedOn=false;
        isBlueOn=false;
        isGreenOn=false;
        isRedOn=false;
        isWhiteOn=false;
        isYellowOn=false;
    }

    void bluetoothOff() {
        if (mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.disable();
            Toast.makeText(getApplicationContext(), "블루투스가 비활성화 되었습니다.", Toast.LENGTH_SHORT).show();
            mTvBluetoothStatus.setText("비활성화");
        } else {
            Toast.makeText(getApplicationContext(), "블루투스가 이미 비활성화 되어 있습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case BT_REQUEST_ENABLE:
                if (resultCode == RESULT_OK) { // 블루투스 활성화를 확인을 클릭하였다면
                    Toast.makeText(getApplicationContext(), "블루투스 활성화", Toast.LENGTH_LONG).show();
                    mTvBluetoothStatus.setText("활성화");
                } else if (resultCode == RESULT_CANCELED) { // 블루투스 활성화를 취소를 클릭하였다면
                    Toast.makeText(getApplicationContext(), "취소", Toast.LENGTH_LONG).show();
                    mTvBluetoothStatus.setText("비활성화");
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    void listPairedDevices() {
        if (mBluetoothAdapter.isEnabled()) {
            mPairedDevices = mBluetoothAdapter.getBondedDevices();

            if (mPairedDevices.size() > 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("장치 선택");

                mListPairedDevices = new ArrayList<String>();
                for (BluetoothDevice device : mPairedDevices) {
                    mListPairedDevices.add(device.getName());
                    //mListPairedDevices.add(device.getName() + "\n" + device.getAddress());
                }
                final CharSequence[] items = mListPairedDevices.toArray(new CharSequence[mListPairedDevices.size()]);
                mListPairedDevices.toArray(new CharSequence[mListPairedDevices.size()]);

                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        connectSelectedDevice(items[item].toString());
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                Toast.makeText(getApplicationContext(), "페어링된 장치가 없습니다.", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "블루투스가 비활성화 되어 있습니다.", Toast.LENGTH_SHORT).show();
        }
    }


    @SuppressLint("ResourceAsColor")
    void connectSelectedDevice(String selectedDeviceName) {
        for (BluetoothDevice tempDevice : mPairedDevices) {
            if (selectedDeviceName.equals(tempDevice.getName())) {
                mBluetoothDevice = tempDevice;
                break;
            }
        }
        try {
            mBluetoothSocket=mBluetoothDevice.createInsecureRfcommSocketToServiceRecord(BT_UUID);
            mBluetoothSocket.connect();

            mThreadConnectedBluetooth = new ConnectedBluetoothThread(mBluetoothSocket);
            mThreadConnectedBluetooth.start();
            mBluetoothHandler.obtainMessage(BT_CONNECTING_STATUS, 1, -1).sendToTarget();

            mTvBluetoothStatus.setText("connected device");
            linearLayout1.setVisibility(View.VISIBLE);

        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "블루투스 연결 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
        }
    }




    private class ConnectedBluetoothThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedBluetoothThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "소켓 연결 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }
        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;

            while (true) {
                try {
                    bytes = mmInStream.available();
                    if (bytes != 0) {
                        SystemClock.sleep(100);
                        bytes = mmInStream.available();
                        bytes = mmInStream.read(buffer, 0, bytes);
                        mBluetoothHandler.obtainMessage(BT_MESSAGE_READ, bytes, -1, buffer).sendToTarget();
                    }
                } catch (IOException e) {
                    break;
                }
            }
        }
        public void write(String str) {
            byte[] bytes = str.getBytes();
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "데이터 전송 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
            }
        }
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "소켓 해제 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
