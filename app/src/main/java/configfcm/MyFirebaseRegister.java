package configfcm;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;


import java.util.HashMap;

import Config.ApiParams;
import util.CommonClass;
import util.VJsonRequest;

/**
 * Created by subhashsanghani on 12/21/16.
 */

public class MyFirebaseRegister {
    Activity _context;
    CommonClass common;
    public MyFirebaseRegister(Activity context) {
        this._context = context;
        common = new CommonClass(_context);

    }
    public void RegisterUser(String user_id){
        // [START subscribe_topics]
        String token = FirebaseInstanceId.getInstance().getToken();
        FirebaseMessaging.getInstance().subscribeToTopic("doctappo");
        // [END subscribe_topics]


        HashMap<String,String> params = new HashMap<>();
        params.put("user_id",user_id);
        params.put("token",token);
        params.put("device","android");

        VJsonRequest vJsonRequest = new VJsonRequest(_context, ApiParams.REGISTER_FCM_URL,params,
                new VJsonRequest.VJsonResponce(){
                    @Override
                    public void VResponce(String responce) {


                    }
                    @Override
                    public void VError(String responce) {
                        common.setToastMessage(responce);
                    }
                });
    }
    private final Handler handler = new Handler() {
        public void handleMessage(Message message) {
            if (message.getData().containsKey(ApiParams.PARM_RESPONCE)){
                if (message.getData().getBoolean(ApiParams.PARM_RESPONCE)){
                }else{
                    //common.setToastMessage(message.getData().getString(ApiParams.PARM_ERROR));
                }
            }


        }
    };

}
