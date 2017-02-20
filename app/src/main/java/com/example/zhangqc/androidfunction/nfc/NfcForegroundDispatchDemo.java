package com.example.zhangqc.androidfunction.nfc;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.zhangqc.androidfunction.R;

import java.util.List;

public class NfcForegroundDispatchDemo extends Activity implements View.OnClickListener {

    private static final String ACTION_NAME_KEY = "com.zqc";
    NfcAdapter mAdapter;
    PendingIntent mPendingIntent;
    String[][] techListsArray;
    private IntentFilter[] intentFiltersArray;

    BroadcastReceiver mNfcDispatchReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_nfc_foreground_dispatch_demo);


        findViewById(R.id.button).setOnClickListener(this);
        mAdapter = NfcAdapter.getDefaultAdapter(this);
        techListsArray = new String[][]{new String[]{NfcF.class.getName()}, new String[]{NfcA.class.getName()}};

        registerInternalBroadcastReceiver();

        testNFC();

        initDialog();

        initFesolveFiler();
    }


    /**
     * 两种方法验证NFC的前台处理，目前方法1注释掉了。
     * 1. activity
     * 2. broadcast
     */
    private void testNFC() {
//        方法1
        mNfcDispatchReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("zqc", "received");
            }
        };
//        方法2：
        // android 分发系统在收到nfc tag事件后，优先处理时，执行的intent
        mPendingIntent = PendingIntent.getBroadcast(this, 0, new Intent().setAction(ACTION_NAME_KEY), 0);
                /*PendingIntent.getActivity(
                this, 0, new Intent(this, NfcForgroundActivity.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP).addFlags(PendingIntent.FLAG_UPDATE_CURRENT), 0);
*/
        // 过滤处理的类型
        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndef.addDataType("*/*");    /* Handles all MIME based dispatches.
                                       You should specify only the ones that you need. */
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("fail", e);
        }
        intentFiltersArray = new IntentFilter[]{ndef,};
    }

    private BroadcastReceiver mForegroundDispatchReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle b = intent.getExtras();
            for(String key:b.keySet()){
                Log.d("zqc", "key = " + key + ",  value = " + b.get(key).toString());
            }

            Toast.makeText(NfcForegroundDispatchDemo.this, "receive Nfc", Toast.LENGTH_LONG).show();
            showDialog();
        }
    };
    private IntentFilter filter = new IntentFilter(ACTION_NAME_KEY);
    private void registerInternalBroadcastReceiver(){
        registerReceiver(mForegroundDispatchReceiver, filter);
    }

    public void onPause() {
        super.onPause();
        mAdapter.disableForegroundDispatch(this);
        unregisterReceiver(mNfcDispatchReceiver);
    }

    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter("com.zqc");
        registerReceiver(mNfcDispatchReceiver, filter);
        mAdapter.enableForegroundDispatch(this, mPendingIntent, intentFiltersArray, techListsArray);
    }

    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d("zqc", "onNewIntent");

        Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        Toast.makeText(this, "got the tag info: such as \n " + tagFromIntent.getTechList()[0].toString(), Toast.LENGTH_SHORT).show();
        //do something with tagFromIntent
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button) {
//            showDialog();
        }
    }

    private AlertDialog mDialog;
    Button mButtonInDialog;

    private void initDialog() {
        mButtonInDialog = new Button(this);
        mButtonInDialog.setText("in dialog");

        mDialog = new AlertDialog.Builder(NfcForegroundDispatchDemo.this)
//                .setView(mButtonInDialog)
                .create();


    }


    private void showDialog() {
        if (mDialog != null) {
            mDialog.show();
        }
    }



    private void initFesolveFiler() {
        PackageManager manager = getPackageManager();
        Intent intent = new Intent();
        intent.setAction("android.nfc.action.TECH_DISCOVERED");
//        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        // NOTE: Provide some data to help the Intent resolver
//        intent.setData(Uri.parse("http://www.google.com"));
        // Query for all activities that match my filter and request that the filter used
        //  to match is returned in the ResolveInfo
        List<ResolveInfo> infos = manager.queryIntentActivities (intent,
                PackageManager.GET_RESOLVED_FILTER);
        for (ResolveInfo info : infos) {
            ActivityInfo activityInfo = info.activityInfo;
            IntentFilter filter = info.filter;
            if (filter != null && filter.hasAction("android.nfc.action.TECH_DISCOVERED")) {
                // This activity resolves my Intent with the filter I'm looking for
                String activityPackageName = activityInfo.packageName;
                String activityName = activityInfo.name;
                System.out.println("zqc    "+activityPackageName + "/" + activityName);
            }
        }
    }
}
