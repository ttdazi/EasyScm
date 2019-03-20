
package com.yqing.processor;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic.Kind;

public class SLogger {
    private Messager msg;

    public SLogger(Messager messager) {
        this.msg = messager;
    }

    public void info(CharSequence info) {
        if (info != null) {
            this.msg.printMessage(Kind.NOTE, "SCM :: processors ::" + info);
        }

    }

    public void error(CharSequence error) {
        if (error != null) {
            this.msg.printMessage(Kind.ERROR, "SCM :: processors ::An exception is encountered, [" + error + "]");
        }

    }

    public void error(Throwable error) {
        if (null != error) {
            this.msg.printMessage(Kind.ERROR, "SCM :: processors ::An exception is encountered, [" + error.getMessage() + "]\n" + this.formatStackTrace(error.getStackTrace()));
        }

    }

    public void warning(CharSequence warning) {
        if (warning != null) {
            this.msg.printMessage(Kind.WARNING, "SCM :: processors ::" + warning);
        }

    }

    private String formatStackTrace(StackTraceElement[] stackTrace) {
        StringBuilder sb = new StringBuilder();
        StackTraceElement[] var3 = stackTrace;
        int var4 = stackTrace.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            StackTraceElement element = var3[var5];
            sb.append("    at ").append(element.toString());
            sb.append("\n");
        }

        return sb.toString();
    }
}
