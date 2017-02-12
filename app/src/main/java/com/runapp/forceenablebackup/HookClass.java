package com.runapp.forceenablebackup;

import android.content.res.TypedArray;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public class HookClass implements /*IXposedHookZygoteInit,*/IXposedHookLoadPackage {

    //private String packageName;
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        //After testing, only the system process needs to be hooked for Helium to work.
        //However, several apps such as GMS seems to be peeking allowBackup too.
        //So you can choose to hook the whole system by using the initZygote function.
        if(!lpparam.packageName.equals("android"))return;
        //packageName=lpparam.packageName;
        findAndHookMethod(TypedArray.class, "getBoolean",
            int.class, boolean.class,
            new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    if ((int) param.args[0] == 17) { //com.android.internal.R.styleable.AndroidManifestApplication_allowBackups
                        //XposedBridge.log("hooked in pkg "+packageName);
                        param.setResult(true);
                    }
                }
            }
        );
    }

    /*
    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        findAndHookMethod(TypedArray.class, "getBoolean",
            int.class, boolean.class,
            new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    if ((int) param.args[0] == 17) {
                        XposedBridge.log("hooked by initZygote");
                        //By uncommenting the line below, you will not receive any "hooked in pkg" logs.
                        //param.setResult(true);
                    }
                }
            }
        );
    }
    */
}
