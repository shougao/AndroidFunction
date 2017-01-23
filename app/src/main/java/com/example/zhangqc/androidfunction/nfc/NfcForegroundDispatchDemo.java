package com.example.zhangqc.androidfunction.nfc;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.zhangqc.androidfunction.R;

public class NfcForegroundDispatchDemo extends AppCompatActivity {

    NfcAdapter mAdapter;
    PendingIntent mPendingIntent;
    String[][]  techListsArray;
    private IntentFilter[] intentFiltersArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_foreground_dispatch_demo);

        mAdapter = NfcAdapter.getDefaultAdapter(this);
        techListsArray = new String[][]{new String[]{NfcF.class.getName()}, new String[]{NfcA.class.getName()}};

        // android 分发系统在收到nfc tag事件后，优先处理时，执行的intent
        mPendingIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        // 过滤处理的类型
        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndef.addDataType("*/*");    /* Handles all MIME based dispatches.
                                       You should specify only the ones that you need. */
        }
        catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("fail", e);
        }
        intentFiltersArray = new IntentFilter[] {ndef, };
    }

    public void onPause() {
        super.onPause();
        mAdapter.disableForegroundDispatch(this);
    }

    public void onResume() {
        super.onResume();
        mAdapter.enableForegroundDispatch(this, mPendingIntent, intentFiltersArray, techListsArray);
    }

    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d("zqc", "onNewIntent");

        Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        Toast.makeText(this, "got the tag info: such as \n " + tagFromIntent.getTechList()[0].toString(), Toast.LENGTH_SHORT).show();
        //do something with tagFromIntent
    }
}
