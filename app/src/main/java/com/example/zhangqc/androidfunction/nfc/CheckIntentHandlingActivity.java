package com.example.zhangqc.androidfunction.nfc;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhangqc.androidfunction.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class CheckIntentHandlingActivity extends AppCompatActivity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_intent_handling);
        mTextView = (TextView)findViewById(R.id.textView4);
    }

    @Override
    protected void onResume() {
        super.onResume();

        PackageManager packageManager = getPackageManager();
        Intent intent = new Intent("android.nfc.action.TECH_DISCOVERED");
//        intent.setAction("android.nfc.action.NDEF_DISCOVERED");

        List<ResolveInfo> infos = packageManager.queryIntentActivities(intent, PackageManager.MATCH_ALL);
        if (infos.size() > 0) {

            String activitiesName = "";
            int number = 0;
            for(ResolveInfo info : infos){
                activitiesName = activitiesName + "\n" + info.activityInfo.packageName;
                number++;
            }
            activitiesName = number + activitiesName;
            mTextView.setText(activitiesName);
        } else {
            Toast.makeText(this, "Don'1 find the application", Toast.LENGTH_LONG).show();
        }
    }
}
