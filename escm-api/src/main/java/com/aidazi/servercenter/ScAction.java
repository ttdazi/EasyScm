package com.aidazi.servercenter;

import android.content.Context;
import android.os.Bundle;


/**
 * author: Y_Qing
 * date: 2019/1/16
 */
public interface ScAction {
    void invoke(Context var1, Bundle var2, String var3, ScCallBack var4);
}
