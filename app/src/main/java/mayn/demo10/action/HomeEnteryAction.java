package mayn.demo10.action;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.yqing.annotations.ScActionAnnotation;
import com.yqing.servercenter.ScAction;
import com.yqing.servercenter.ScCallBack;

import mayn.demo10.MainActivity;

/**
 * author: Y_Qing
 * date: 2019/1/14
 */
@ScActionAnnotation(value = ActionConstants.ACTION_ENTRY_HOME_PAGE, modelName = "app")
public class HomeEnteryAction implements ScAction {
    @Override
    public void invoke(Context context, Bundle bundle, String s, ScCallBack scCallback) {
        Intent intent = new Intent();
        intent.setClass(context, MainActivity.class);
        context.startActivity(intent);
        Bundle bundle1 = new Bundle();
        bundle1.putString("start_home", "start_home_okOk");
        scCallback.onCallback(true, bundle1, "");
        Log.i("HomeEnteryAction", "HomeEnteryAction___" + "成功!");
    }
}
