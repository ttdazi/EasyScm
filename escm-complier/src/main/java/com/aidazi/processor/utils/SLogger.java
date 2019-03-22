
package com.aidazi.processor.utils;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic.Kind;

public class SLogger {
    private Messager msg;


    private boolean isLoger;

    public void setLoger(boolean loger) {
        isLoger = loger;
    }

    public SLogger(Messager messager) {
        this.msg = messager;
        this.isLoger = false;
    }

    public void info(CharSequence info) {
        if (isLoger && info != null) {
            this.msg.printMessage(Kind.NOTE, "SCM :: processors ::" + info);
        }

    }

    public void error(CharSequence error) {
        if (isLoger && error != null) {
            this.msg.printMessage(Kind.ERROR, "SCM :: processors ::An exception is encountered, [" + error + "]");
        }

    }

    public void error(Throwable error) {
        if (isLoger && null != error) {
            this.msg.printMessage(Kind.ERROR, "SCM :: processors ::An exception is encountered, [" + error.getMessage() + "]\n" + this.formatStackTrace(error.getStackTrace()));
        }

    }

    public void warning(CharSequence warning) {
        if (isLoger && warning != null) {
            this.msg.printMessage(Kind.WARNING, "SCM :: processors ::" + warning);
        }

    }

    private String formatStackTrace(StackTraceElement[] stackTrace) {
        StringBuilder sb = new StringBuilder();
        StackTraceElement[] var3 = stackTrace;
        int var4 = stackTrace.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            StackTraceElement element = var3[var5];
            sb.append("    at ").append(element.toString());
            sb.append("\n");
        }

        return sb.toString();
    }
}
