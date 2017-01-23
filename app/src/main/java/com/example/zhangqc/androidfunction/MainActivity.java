package com.example.zhangqc.androidfunction;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhangqc.androidfunction.nfc.NfcActivity;
import com.example.zhangqc.androidfunction.nfc.NfcForegroundDispatchDemo;
import com.example.zhangqc.androidfunction.nfc.SimcardManager;

import java.util.ArrayList;
import java.util.Comparator;

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



        mPreferAppView = (TextView) findViewById(R.id.textView_preferapp);
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
