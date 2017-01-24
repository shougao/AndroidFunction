package com.example.zhangqc.androidfunction.nfc;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhangqc.androidfunction.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class CheckIntentHandlingActivity extends AppCompatActivity {

    private TextView mTextView;
    private ListView mListView;
    private ImageView mImageView;
    private List<NfcItem> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_intent_handling);
        mTextView = (TextView) findViewById(R.id.textView4);
        mListView = (ListView) findViewById(R.id.list_view);
        mImageView = (ImageView) findViewById(R.id.imageView2);
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
            for (ResolveInfo info : infos) {
                activitiesName = activitiesName + "\n" + info.activityInfo.packageName;
                info.getIconResource();
                number++;
            }
            activitiesName = number + activitiesName + "\n that is all. \n ";
            mTextView.setText(activitiesName);
        } else {
            Toast.makeText(this, "Don'1 find the application", Toast.LENGTH_LONG).show();
        }
        refreshListView();
    }


    private void refreshListView() {
        initData();
        NfcPackageAdapter adapter = new NfcPackageAdapter(getLayoutInflater(), mData);
        mListView.setAdapter(adapter);
    }

    private void initData() {

        mData = new ArrayList<NfcItem>();
        Intent intent = new Intent("android.nfc.action.TECH_DISCOVERED");
        List<ResolveInfo> resolveInfos = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_ALL);

        for (ResolveInfo info : resolveInfos) {
            NfcItem item = new NfcItem();
            item.setmPackageName(info.activityInfo.packageName);
            item.setmDrawableIcon(info.loadIcon(getPackageManager()));
            mData.add(item);
        }
    }


    public class NfcItem{

        public String getmPackageName() {
            return mPackageName;
        }

        public void setmPackageName(String mPackageName) {
            this.mPackageName = mPackageName;
        }

        public Drawable getmDrawableIcon() {
            return mDrawableIcon;
        }

        public void setmDrawableIcon(Drawable mDrawableIcon) {
            this.mDrawableIcon = mDrawableIcon;
        }

        public String mPackageName;
        public Drawable mDrawableIcon;

    }

    public class NfcPackageAdapter extends BaseAdapter{

        private List<NfcItem> mData;//定义数据。
        private LayoutInflater mInflater;//定义Inflater,加载我们自定义的布局。

        /*
        定义构造器，在Activity创建对象Adapter的时候将数据data和Inflater传入自定义的Adapter中进行处理。
        */
        public NfcPackageAdapter(LayoutInflater inflater, List<NfcItem> data){
            mInflater = inflater;
            mData = data;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertview, ViewGroup viewGroup) {
            //获得ListView中的view
            View viewStudent = mInflater.inflate(R.layout.nfc_app_icon_item,null);
            //获得学生对象
            NfcItem nfcItem = mData.get(position);
            //获得自定义布局中每一个控件的对象。
            ImageView imagePhoto = (ImageView) viewStudent.findViewById(R.id.package_icon);
            TextView name = (TextView) viewStudent.findViewById(R.id.package_name);

            //将数据一一添加到自定义的布局中。
            imagePhoto.setBackground(nfcItem.getmDrawableIcon());
            name.setText(nfcItem.getmPackageName());
            return viewStudent ;
        }
    }
}
