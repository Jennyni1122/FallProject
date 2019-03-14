package com.jennyni.fallproject.activity.devicelocation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import com.jennyni.fallproject.PermissionsChecker;

/**
 * Created by Yellow on 2017/11/2.
 */

public class PermissionsRequest {

    public static final int PERMISSION_REQUEST_CODE = 0x114; // 系统权限管理页面的参数
    public static final String PACKAGE_URL_SCHEME = "package:"; // 方案
    private Context context;
    private OnPermissionRequestListener onPermissionRequestListener = new OnPermissionRequestListener() {
        @Override
        public void onPermissionRequestSuccess() {

        }

        @Override
        public void onPermissionRequestFail() {

        }
    };
    private boolean isFinish = false;


    private PermissionsChecker mPermissionsChecker; // 权限检测器
    private boolean isRequireCheck = true;
    private String[] permissions;


    public PermissionsRequest(Context context, OnPermissionRequestListener onPermissionRequestListener, boolean isFinish, String[] permissions) {
        this.context = context;
        this.onPermissionRequestListener = onPermissionRequestListener;
        this.isFinish = isFinish;
        this.permissions = permissions;
        mPermissionsChecker=new PermissionsChecker(context);
    }

    public void setRequireCheck(boolean requireCheck) {
        isRequireCheck = requireCheck;
    }



    public void requestPermissions() {
        if (isRequireCheck) {
            String[] permissions = this.permissions;
            if (mPermissionsChecker.lacksPermissions(permissions)) {
                requestPermissions(permissions); // 请求权限
            } else {
                // 全部权限都已获取
                onPermissionRequestListener.onPermissionRequestSuccess();
            }
        }
    }

    // 含有全部的权限
    public  boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    // 请求权限兼容低版本
    private void requestPermissions(String... permissions) {
        ActivityCompat.requestPermissions((Activity) context, permissions, PERMISSION_REQUEST_CODE);
    }

    // 显示缺失权限提示
    public void showMissingPermissionDialog(String tip) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("提示");
        builder.setMessage(tip);
        // 拒绝, 退出应用
        builder.setNegativeButton("退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (isFinish) {
                    ((Activity) context).finish();
                }
            }
        });

        builder.setPositiveButton("去开启", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startAppSettings();
            }
        });

        builder.show();
    }

    // 启动应用的设置
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse(PACKAGE_URL_SCHEME + context.getPackageName()));
        context.startActivity(intent);
    }



//    /**
//     * 用户权限处理,
//     * 如果全部获取, 则直接过.
//     * 如果权限缺失, 则提示Dialog.
//     *
//     * @param requestCode  请求码
//     * @param permissions  权限
//     * @param grantResults 结果
//     */
//    @Override
//public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//    if (requestCode == PermissionsRequest.PERMISSION_REQUEST_CODE && perssionsRequest.hasAllPermissionsGranted(grantResults)) {
//        perssionsRequest.setRequireCheck(true);
//        // 全部权限都已获取
//        onPermissionRequestSuccess();
//    } else {
//        perssionsRequest.setRequireCheck(false);
//        onPermissionRequestFail();
//    }
//}

    public interface OnPermissionRequestListener {

        void onPermissionRequestSuccess();

        void onPermissionRequestFail();

    }

}
