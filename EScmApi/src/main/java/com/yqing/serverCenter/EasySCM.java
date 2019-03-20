package com.yqing.serverCenter;

import android.content.Context;
import android.os.Bundle;

import com.yqing.utils.ClassUtils;
import com.yqing.utils.StringUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class EasySCM {
    private static final EasySCM ourInstance = new EasySCM();
    private HashMap<String, ScAction> actionMap = new HashMap();
    private volatile boolean isReady;

    public static EasySCM getInstance() {
        return ourInstance;
    }

    private EasySCM() {
    }

    public void init(Context context) {
        try {
            List<String> classFileNames = ClassUtils.getFileNameByPackageName(context, "com.zzy.processor.generated");
            Iterator var3 = classFileNames.iterator();

            while (var3.hasNext()) {
                String className = (String) var3.next();
                String s = Class.forName(className).newInstance().toString();
                Map<String, String> map = StringUtils.mapStringToMap(s);
                Iterator var7 = map.entrySet().iterator();

                while (var7.hasNext()) {
                    Map.Entry<String, String> entry = (Map.Entry) var7.next();
                    Class clazz = Class.forName(entry.getValue());
                    this.registerAction((entry.getKey()).trim(), (ScAction) clazz.newInstance());
                }
            }

            this.isReady = true;
        } catch (Exception var10) {
            var10.printStackTrace();
        }

    }

    public void req(Context context, String action, ScCallBack callback) throws Exception {
        this.req(context, action, (Bundle) null, "", callback);
    }

    public void req(Context context, String action, Bundle param, ScCallBack callback) throws Exception {
        this.req(context, action, param, "", callback);
    }

    public void req(Context context, String action, Bundle param, String tag, ScCallBack callback) throws Exception {
        if (!this.isReady) {
            throw new RuntimeException("EasySCM is not ready! pls wait!");
        } else if (!this.actionMap.containsKey(action)) {
            throw new RuntimeException("EasySCM action not found! name:" + action);
        } else {
            ScAction scAction = this.actionMap.get(action);
            scAction.invoke(context, param, tag, callback);
        }
    }

    private void registerAction(String action, ScAction scAction) throws Exception {
        if (scAction != null && action != null) {
            if (!this.actionMap.containsKey(action)) {
                this.actionMap.put(action, scAction);
            }

        } else {
            throw new Exception("bad input param!");
        }
    }

}
