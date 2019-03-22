package mayn.demo10;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.aidazi.servercenter.EasySCM;


/**
 * author: Y_Qing
 * date: 2019/1/14
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        EasySCM.getInstance().init(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }
}
