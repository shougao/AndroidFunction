package com.example.zhangqc.androidfunction.nfc;


import android.content.Context;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangqc8 on 2017/1/23.
 */

public class SimcardManager {

    private static final String TAG = "SimcardManager";

    private Context mContext;

    public SimcardManager(Context context){
        mContext = context;
    }




    public ArrayList<String> getSimcardName(){
        List<SubscriptionInfo> subscriptionInfos = SubscriptionManager.from(mContext).getActiveSubscriptionInfoList();
        if(subscriptionInfos == null){
            return null;
        }
        ArrayList<String> result = new ArrayList<>();
        for(int i=0; i<subscriptionInfos.size();i++)
        {
            SubscriptionInfo lsuSubscriptionInfo = subscriptionInfos.get(i);
            Log.d(TAG, "getNumber "+ lsuSubscriptionInfo.getNumber());
            Log.d(TAG, "network name : "+ lsuSubscriptionInfo.getCarrierName());
            Log.d(TAG, "getCountryIso "+ lsuSubscriptionInfo.getCountryIso());
            Log.d(TAG, "getDisplayName "+ lsuSubscriptionInfo.getDisplayName());
            result.add(lsuSubscriptionInfo.getDisplayName().toString());
        }
        return result;
    }


}
