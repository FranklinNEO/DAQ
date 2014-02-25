package com.redinfo.daq.app;


import mexxen.mx5010.barcode.BarcodeEvent;
import mexxen.mx5010.barcode.BarcodeListener;
import mexxen.mx5010.barcode.BarcodeManager;

import com.redinfo.daq.R;
import com.redinfo.daq.util.SoundPlayer;
import com.redinfo.daq.util.SoundPlayer.State;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class ScanTestActivity extends Activity {
    private static final String DATA_STRING_TAG = "com.motorolasolutions.emdk.datawedge.data_string";
    private static String ourIntentAction = "com.redinfo.daq.scantest.RECVR";
    private String barcode = null;
    public Dialog loadingdialog = null;
    private TextView resultTv = null;
    private TextView numTv = null;
    private com.redinfo.daq.util.SoundPlayer sp;
    private BarcodeManager bm = null;
    private BarcodeListener bl = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);
        resultTv = (TextView) findViewById(R.id.Test_result_tv);
        numTv = (TextView) findViewById(R.id.Test_length_tv);
        sp = new SoundPlayer();
        sp.addSound(ScanTestActivity.this, R.raw.alert, State.refreshed);
        sp.addSound(ScanTestActivity.this, R.raw.beep, State.pull);
        loadingdialog = new Dialog(ScanTestActivity.this, R.style.mmdialog);
        loadingdialog.setContentView(R.layout.loading_dialog);
        bm = new BarcodeManager(this);
        bl = new BarcodeListener() {
            // 重写barcodeEvent 方法，获取条码事件
            @Override
            public void barcodeEvent(BarcodeEvent event) {
                // 当条码事件的命令为“SCANNER_READ”时，进行操作
                if (event.getOrder().equals("SCANNER_READ")) {
                    // 调用getBarcode()方法读取条码信息
                    String barcode = bm.getBarcode();
//					if (barcode.length() != 20) {
//						sp.play(State.refreshed);
//						Toast.makeText(ScanTestActivity.this,
//								getString(R.string.not_bcm_code),
//								Toast.LENGTH_SHORT).show();
//					} else {
//						resultTv.setText(barcode);
//					}
                    numTv.setText(getString(R.string.size)+":"+barcode.length());
                    resultTv.setText(barcode);
                }
            }
        };
        bm.addListener(bl);

    }

    // We need to handle any incoming intents, so let override the onNewIntent
    // method
    @Override
    public void onNewIntent(Intent i) {
        // i.addCategory("com.redinfo.daq.app.code");
        // i.addCategory("android.intent.category.DEFAULT");
        handleDecodeData(i);
    }

    private void handleDecodeData(Intent i) {
        if (i.getAction().contentEquals(ourIntentAction)) {
            String data = i.getStringExtra(DATA_STRING_TAG);
            barcode = data;
            if (barcode.length() != 20) {
                sp.play(State.refreshed);
                Toast.makeText(ScanTestActivity.this,
                        getString(R.string.not_bcm_code), Toast.LENGTH_SHORT)
                        .show();
            } else {
                resultTv.setText(barcode);
            }

        }
    }
}
