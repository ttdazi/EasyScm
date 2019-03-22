package com.aidazi.servercenter;

import android.content.Context;
import android.os.Bundle;

import java.lang.reflect.Field;
import java.util.HashMap;


public class EasySCM {
    private static final EasySCM ourInstance = new EasySCM();
    private HashMap<String, ScAction> actionMap = new HashMap<>();
    private volatile boolean isReady;

    public static EasySCM getInstance() {
        return ourInstance;
    }

    private EasySCM() {
    }

    public void init(Context context) {
        actionMap.clear();
        try {
            Class<?> aClass = Class.forName(EscmConstants.ROUTTABLE_PACKAGE + "." + EscmConstants.ROUT_TABLE);
            Field[] fields = aClass.getFields();
            for (Field field : fields) {
                String name = field.getName();
                String pathAction = (String) field.get(name);
                registerAction(name, (ScAction) Class.forName(pathAction).newInstance());
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
