package com.example.zhangqc.androidfunction;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ComponentInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhangqc.androidfunction.nfc.CheckIntentHandlingActivity;
import com.example.zhangqc.androidfunction.nfc.NfcActivity;
import com.example.zhangqc.androidfunction.nfc.NfcForegroundDispatchDemo;
import com.example.zhangqc.androidfunction.nfc.SimcardManager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "zqc";
    private TextView mPreferAppView;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("tag", "check debug");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("tag", "check debug");
    }

    private void initView() {
        findViewById(R.id.button_nfc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNfc();
            }
        });
        findViewById(R.id.button_prefer_app).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                testDefaultActivity();
            }
        });

        findViewById(R.id.button_jump_to_nfc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, NfcActivity.class));
            }
        });

        findViewById(R.id.button_simcard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimcardManager manager = new SimcardManager(mContext);
                ArrayList<String> names = manager.getSimcardName();
                if (names == null || names.isEmpty()) {
                    return;
                }
                for (String name : names
                        ) {
                    Log.d("sim name", name);
                    Toast.makeText(mContext, name, Toast.LENGTH_LONG).show();
                }
            }
        });

        findViewById(R.id.nfc_forground_dispatch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, NfcForegroundDispatchDemo.class));
            }
        });

        findViewById(R.id.nfc_handling_query).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, CheckIntentHandlingActivity.class));
            }
        });

        findViewById(R.id.nfc_moto_setup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent("com.motorola.vzw.settings.extensions.action.VZW_NFC_ALERT_DIALOG");
                startActivity(i);
                if(true){
                    return;
                }

                final String HELP_URL = "http://help.motorola.com/hc/apps/nfc/?os="
                        + android.os.Build.VERSION.RELEASE;
                final int REQUEST_CODE_SHOW_NFC_SETUP = 1001;
                final String ACTION_NFC_SETUP = "com.android.nfc.action.SHOW_NOTIF_ENABLED";
                final String PACKAGE_NFC_SETUP = "com.motorola.nfc";
                final String CLASS_NFC_SETUP = "com.motorola.nfc.setup.NfcSetupActivity";

                Intent intent = new Intent();
                intent.setClassName(PACKAGE_NFC_SETUP, CLASS_NFC_SETUP);
                intent.setAction(ACTION_NFC_SETUP);
                if (intent.resolveActivityInfo(getPackageManager(), 0) != null) {
                    intent.putExtra("vzw_flow", true);
                    startActivityForResult(intent, REQUEST_CODE_SHOW_NFC_SETUP);
                }
            }
        });

        findViewById(R.id.start_app_store).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                final String STORE_PACKAGE_NAME = "com.lenovo.leos.appstore";
                final String STORE_CLASS_NAME = "com.lenovo.leos.appstore.activities.Main";
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setClassName(STORE_PACKAGE_NAME, STORE_CLASS_NAME);
                startActivity(intent);
            }
        });

        findViewById(R.id.start_alipay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isAlipayInstalled = alipayInstallOrNot("com.eg.android.AlipayGphone");
                if(isAlipayInstalled){
                    Intent alipayIntent = getPackageManager().getLaunchIntentForPackage("com.eg.android.AlipayGphone");
                    startActivity(alipayIntent);
                }else{
                    Toast.makeText(getApplicationContext(), "alipay not install ", Toast.LENGTH_LONG).show();
                }
            }
        });

        

        findViewById(R.id.start_pure_acitivty).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Main4Activity.class));
            }
        });

        findViewById(R.id.start_weixin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                        if(activityPackageName.equals("com.tencent.mm")){
                            String activityName = activityInfo.name;
                            Intent i = new Intent(NfcAdapter.ACTION_TECH_DISCOVERED);
                            i.setClassName("com.tencent.mm", activityName);
                            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(i);
                        }
                        String activityName = activityInfo.name;
                        System.out.println("zqc    "+activityPackageName + "/" + activityName);
                    }
                }
            }
        });

        mPreferAppView = (TextView) findViewById(R.id.textView_preferapp);
    }

    private boolean alipayInstallOrNot(String s) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(s,PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 1.
    private void startNfc() {
        startActivity(new Intent("android.settings.NFC_SETTINGS"));
    }

    // 2.
    private void testDefaultActivity() {
        Intent viewIntent = new Intent(Intent.ACTION_VIEW);
        viewIntent.setData(Uri.parse("http://www.baidu.com"));

        PackageManager pm = getPackageManager();
        ResolveInfo matchInfo = pm.resolveActivity(viewIntent, PackageManager.MATCH_ALL);
        ResolveInfo preferInfo = pm.resolveActivity(viewIntent, PackageManager.MATCH_DEFAULT_ONLY);
        mPreferAppView.setText("matchapp:" + matchInfo.resolvePackageName + "\n" + "defaultapp:" + preferInfo.resolvePackageName);

        startActivity(viewIntent);


        isDefault(this.getComponentName());

    }

    /**
     * Returns true if the supplied component name is the preferred activity
     * for any action.
     *
     * @param component The ComponentName of your Activity, e.g.
     *                  Activity#getComponentName().
     */
    boolean isDefault(ComponentName component) {
        ArrayList<ComponentName> components = new ArrayList<ComponentName>();
        ArrayList<IntentFilter> filters = new ArrayList<IntentFilter>();
//        filters.add( new IntentFilter(Intent.ACTION_MAIN));
//        filters.add(new IntentFilter(Intent.ACTION_DEFAULT));

        getPackageManager().getPreferredActivities(filters, components, null); // get all default application.
        for (ComponentName name : components) {
            Log.d(TAG, name.getPackageName());
        }

        return components.contains(component);
    }
}
