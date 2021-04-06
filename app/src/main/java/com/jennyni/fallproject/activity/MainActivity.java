package com.jennyni.fallproject.activity;

import android.annotation.SuppressLint;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.jennyni.fallproject.R;
import com.jennyni.fallproject.adapter.MyFragmentPagerAdapter;
import com.jennyni.fallproject.fragment.FindFragment;
import com.jennyni.fallproject.fragment.HomeFragment;
import com.jennyni.fallproject.fragment.MeFragment;
import com.jennyni.fallproject.service.LocationService;
import com.jennyni.fallproject.utils.ActivityCollectorUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActivityCollectorUtil {

    private ViewPager viewPager;
    private RadioGroup radioGroup;
    private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
    private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addActivity(this);
        initView();         //初始化控件
        if (isNotificationEnabled(this)){
            startNotifyService();
        }else {
            showDialog_notify(this);
        }

       // startNotifyService();
    }


    private void showDialog_notify(Context context) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        }
        context.startActivity(localIntent);
    }

    private void startNotifyService() {
        LocationService.startService(this);
    }

    private void initView() {
//        tv_main_title = findViewById(R.id.tv_main_title);
//        tv_main_title.setText("设备用户列表");
//        rl_title_bar = findViewById(R.id.title_bar);
//        rl_title_bar.setBackgroundColor(getResources().getColor(R.color.rdTextColorPress));
        radioGroup = findViewById(R.id.radioGroup);
        //RadioGroup选中状态改变监听
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_home:
                        //setCurrentItem()方法中第二个参数控制页面切换动画，true:打开，false:关闭
                        viewPager.setCurrentItem(0, false);
                        break;
                    case R.id.rb_find:
                        viewPager.setCurrentItem(1, false);
                        break;
                    case R.id.rb_me:
                        viewPager.setCurrentItem(2, true);
                        break;
                }
            }
        });

        viewPager = findViewById(R.id.viewPager);

        HomeFragment homeFragment = new HomeFragment();
        FindFragment findFragment = new FindFragment();
        MeFragment meFragment = new MeFragment();

        List<Fragment> alFragment = new ArrayList<Fragment>();
        alFragment.add(homeFragment);
        alFragment.add(findFragment);
        alFragment.add(meFragment);
        viewPager.setOffscreenPageLimit(2);     //mFragments.size()-1,三个界面之间来回切换都不会重新加载数据。
        //ViewPager设置适配器
        viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), alFragment));
        viewPager.setCurrentItem(0);        //ViewPager显示第一个Fragment

        //ViewPager页面切换监听
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        radioGroup.check(R.id.rb_home);
//                        tv_main_title.setText("设备用户列表");
//                        rl_title_bar.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        radioGroup.check(R.id.rb_find);
//                        tv_main_title.setText("发现");
//                        rl_title_bar.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        radioGroup.check(R.id.rb_me);
//                        tv_main_title.setText("我的");
//                        rl_title_bar.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    protected long exitTime;    //记录第一次点击时的时间

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(MainActivity.this, "再按一次退出此应用哟~", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                MainActivity.this.finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        LocationService.stopService(this);
        removeActivity(this);
        super.onDestroy();

    }

    @SuppressLint("NewApi")
    public static boolean isNotificationEnabled(Context context) {

        AppOpsManager mAppOps =
                (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);

        ApplicationInfo appInfo = context.getApplicationInfo();
        String pkg = context.getApplicationContext().getPackageName();
        int uid = appInfo.uid;
        Class appOpsClass = null;

        /* Context.APP_OPS_MANAGER */
        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());

            Method checkOpNoThrowMethod =
                    appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE, String.class);

            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);
            int value = (Integer) opPostNotificationValue.get(Integer.class);

            return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) ==
                    AppOpsManager.MODE_ALLOWED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
