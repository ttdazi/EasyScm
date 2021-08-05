package com.aidazi.servercenter;

import android.content.Context;
import android.os.Bundle;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;


public class EasySCM {
    private static final EasySCM ourInstance = new EasySCM();
    private HashMap<String, ScAction> actionMap = new HashMap<>();
    private static final AtomicBoolean initialized = new AtomicBoolean();

    public static EasySCM getInstance() {
        return ourInstance;
    }

    private EasySCM() {
    }

    public void init(Context context) {
        if (!initialized.getAndSet(true)) {
            actionMap.clear();
            try {
                Object o = Class.forName(EscmConstants.ROUTTABLE_PACKAGE + "." + EscmConstants.ROUT_TABLE).newInstance();
                String tableKeyStr = o.toString();
                praseStringKey(tableKeyStr);
            } catch (Exception var10) {
                var10.printStackTrace();
            }
        }
    }

    public void initASM(Context context) {
        if (!initialized.getAndSet(true)) {
            actionMap.clear();
            HashMap<String, String> map = new HashMap<>();
            loadInto(map);
            try {
                for (String key : map.keySet()) {
                    registerAction(key, (ScAction) Class.forName(map.get(key)).newInstance());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 用于动态映射表的生成
     * @param map
     */
    void loadInto(Map<String, String> map) {
        throw new RuntimeException("加载Router映射错误！");
    }

    private void praseStringKey(String tableKeyStr) {
        if (tableKeyStr == null || tableKeyStr.length() == 0) return;
        String[] split = tableKeyStr.split(",");
        try {
            for (int i = 0; i < split.length; i++) {
                String sValue = split[i];
                if (sValue != null && sValue.length() != 0) {
                    String[] splitGroup = sValue.split("=");
                    registerAction(splitGroup[0], (ScAction) Class.forName(splitGroup[1]).newInstance());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void praseFile() {
        Class<?> aClass = null;
        try {
            aClass = Class.forName(EscmConstants.ROUTTABLE_PACKAGE + "." + EscmConstants.ROUT_TABLE);
            Field[] fields = aClass.getFields();
            for (Field field : fields) {
                String name = field.getName();
                String pathAction = (String) field.get(name);
                registerAction(name, (ScAction) Class.forName(pathAction).newInstance());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void req(Context context, String action, ScCallBack callback) throws Exception {
        this.req(context, action, (Bundle) null, "", callback);
    }

    public void req(Context context, String action, Bundle param, ScCallBack callback) throws Exception {
        this.req(context, action, param, "", callback);
    }

    public void req(Context context, String action, Bundle param, String tag, ScCallBack callback) throws Exception {
        if (!initialized.get()) {
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
