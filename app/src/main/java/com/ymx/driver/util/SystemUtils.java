package com.ymx.driver.util;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import static android.text.TextUtils.isEmpty;

/**
 * Created by wuwei
 * 2018/1/12
 * 佛祖保佑       永无BUG
 */
public class SystemUtils {

    /**
     * 判断是否是Android Q版本
     *
     * @return
     */
    public static boolean checkedAndroid_Q() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;
    }

    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    /**
     * 获取当前手机系统语言。
     *
     * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”
     */
    public static String getSystemLanguage() {
        return Locale.getDefault().getLanguage();
    }

    /**
     * 获取当前系统上的语言列表(Locale列表)
     *
     * @return 语言列表
     */
    public static Locale[] getSystemLanguageList() {
        return Locale.getAvailableLocales();
    }

    /**
     * 获取当前手机系统版本号
     *
     * @return 系统版本号
     */
    public static String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    public static String getSystemModel() {
        return Build.MODEL;
    }

    /**
     * 获取手机厂商
     *
     * @return 手机厂商
     */
    public static String getDeviceBrand() {
        return Build.BRAND;
    }

    /**
     * 获取版本名称
     *
     * @return 当前应用的版本名称
     */
    public static String getVersion() {
        try {
            PackageManager manager = UIUtils.getContext().getPackageManager();
            PackageInfo info = manager.getPackageInfo(UIUtils.getContext().getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static String getAppVersionCode() {
        long appVersionCode = 0;
        try {
            PackageInfo packageInfo = UIUtils.getContext().getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(UIUtils.getContext().getPackageName(), 0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                appVersionCode = packageInfo.getLongVersionCode();
            } else {
                appVersionCode = packageInfo.versionCode;
            }
        } catch (PackageManager.NameNotFoundException e) {
        }
        return String.valueOf(appVersionCode);
    }

    public static boolean stackResumed(Context context) {
        ActivityManager manager = (ActivityManager) context
                .getApplicationContext().getSystemService(
                        Context.ACTIVITY_SERVICE);
        String packageName = context.getApplicationContext().getPackageName();
        List<ActivityManager.RunningTaskInfo> recentTaskInfos = manager.getRunningTasks(1);
        if (recentTaskInfos != null && recentTaskInfos.size() > 0) {
            ActivityManager.RunningTaskInfo taskInfo = recentTaskInfos.get(0);
            return taskInfo.baseActivity.getPackageName().equals(packageName) && taskInfo.numActivities > 1;
        }

        return false;
    }

    /**
     * 获取包信息
     *
     * @param context
     * @return
     */
    public static PackageInfo getPackageInfo(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            return pm.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
        }
        return new PackageInfo();
    }

    /**
     * 获取电量
     *
     * @param context
     * @return
     */
    public static int getBattery(Context context) {
        BatteryManager batteryManager = (BatteryManager) context.getSystemService(Context.BATTERY_SERVICE);
        return batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
    }

    /**
     * 是否在充电
     *
     * @param context
     * @return
     */
    public static boolean isCharging(Context context) {
        Intent batteryBroadcast = context.registerReceiver(null,
                new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        // 0 means we are discharging, anything else means charging
        boolean isCharging = batteryBroadcast.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1) != 0;
        return isCharging;
    }

    private static long mTotalMemory = -1;

    /**
     * 检测手机是否Rooted
     *
     * @return
     */
    public static boolean isRooted() {
        boolean isSdk = isGoogleSdk();
        Object tags = Build.TAGS;
        if ((!isSdk) && (tags != null)
                && (((String) tags).contains("test-keys"))) {
            return true;
        }
        if (new File("/system/app/Superuser.apk").exists()) {
            return true;
        }
        return (!isSdk) && (new File("/system/xbin/su").exists());
    }

    public static boolean isGoogleSdk() {
        String str = Settings.Secure.getString(UIUtils.getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        return ("sdk".equals(Build.PRODUCT))
                || ("google_sdk".equals(Build.PRODUCT)) || (str == null);
    }

    public static long getAvailMemory() {
        ActivityManager am = (ActivityManager) UIUtils.getContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        return mi.availMem;
    }

    public static String parseFile(File file, String filter) {
        String str = null;
        if (file.exists()) {
            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader(file), 1024);
                String line;
                while ((line = br.readLine()) != null) {
                    Pattern pattern = Pattern.compile("\\s*:\\s*");
                    String[] ret = pattern.split(line, 2);
                    if (ret != null && ret.length > 1 && ret[0].equals(filter)) {
                        str = ret[1];
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return str;
    }

    public static long getSize(String size, String uint, int factor) {
        return Long.parseLong(size.split(uint)[0].trim()) * factor;
    }

    public static synchronized long getTotalMemory() {
        if (mTotalMemory == -1) {
            long total = 0L;
            String str;
            try {
                if (!isEmpty(str = parseFile(
                        new File("/proc/meminfo"), "MemTotal"))) {
                    str = str.toUpperCase(Locale.US);
                    if (str.endsWith("KB")) {
                        total = getSize(str, "KB", 1024);
                    } else if (str.endsWith("MB")) {
                        total = getSize(str, "MB", 1048576);
                    } else if (str.endsWith("GB")) {
                        total = getSize(str, "GB", 1073741824);
                    } else {
                        total = -1;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            mTotalMemory = total;
        }
        return mTotalMemory;
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    public static long[] getSdCardMemory(Context context) {
        long[] sdCardInfo = new long[2];
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            StatFs sf = new StatFs(FileUtil.createDir(context, FileUtil.TYPE_DOWNLOAD).getAbsolutePath());
            if (Build.VERSION.SDK_INT >= 18) {
                long bSize = sf.getBlockSizeLong();
                long bCount = sf.getBlockCountLong();
                long availBlocks = sf.getAvailableBlocksLong();
                sdCardInfo[0] = bSize * bCount;
                sdCardInfo[1] = bSize * availBlocks;
            } else {
                long bSize = sf.getBlockSize();
                long bCount = sf.getBlockCount();
                long availBlocks = sf.getAvailableBlocks();
                sdCardInfo[0] = bSize * bCount;
                sdCardInfo[1] = bSize * availBlocks;
            }
        }
        return sdCardInfo;
    }

    public static String disk(Context context) {
        long[] info = getSdCardMemory(context);
        long total = info[0];
        long avail = info[1];
        if (total <= 0) {
            return "--";
        } else {
            float ratio = (float) ((avail * 100) / total);
            return String.format(Locale.US, "%.01f%% [%s]", ratio, getSizeWithUnit(total));
        }
    }

    public static String ram() {
        long total = getTotalMemory();
        long avail = getAvailMemory();
        if (total <= 0) {
            return "--";
        } else {
            float ratio = (float) ((avail * 100) / total);
            return String.format(Locale.US, "%.01f%% [%s]", ratio, getSizeWithUnit(total));
        }
    }

    public static String getSizeWithUnit(long size) {
        if (size >= 1073741824) {
            float i = (float) (size / 1073741824);
            return String.format(Locale.US, "%.02f GB", i);
        } else if (size >= 1048576) {
            float i = (float) (size / 1048576);
            return String.format(Locale.US, "%.02f MB", i);
        } else {
            float i = (float) (size / 1024);
            return String.format(Locale.US, "%.02f KB", i);
        }
    }

    public static String getSystemProperty(String propName) {
        String line;
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                }
            }
        }
        return line;
    }

    @SuppressLint("MissingPermission")
    public static String getDeviceId() {
        try {
            final TelephonyManager manager = (TelephonyManager) UIUtils.getContext().getSystemService(Context.TELEPHONY_SERVICE);
            if (isEmpty(manager.getDeviceId())) {
                if (Build.VERSION.SDK_INT >= 23) {
                    return manager.getDeviceId(0);
                }
            } else {
                return manager.getDeviceId();
            }
        } catch (Exception e) {

        }
        return "";
    }



    @SuppressLint("MissingPermission")
    public static String getSimSerialNumber() {
        final TelephonyManager manager = (TelephonyManager) UIUtils.getContext().getSystemService(Context.TELEPHONY_SERVICE);
        if(manager.getSimState()==TelephonyManager.SIM_STATE_READY) {
            try {

                return manager.getSimSerialNumber();
            } catch (Exception e) {

            }
        }
        return "";
    }

    public static String getWifiMac(){
        WifiManager wifi = (WifiManager) UIUtils.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        String wifiMac = info.getMacAddress();
        if(!isEmpty(wifiMac)){

            return wifiMac;
        }
        return "";
    }

    public static boolean isWechatInstalled(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 检测是否安装支付宝
     *
     * @param context
     * @return
     */
    public static boolean isAliPayInstalled(Context context) {
        Uri uri = Uri.parse("alipays://platformapi/startApp");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        ComponentName componentName = intent.resolveActivity(context.getPackageManager());
        return componentName != null;
    }



}
