package com.jennyni.fallproject.utils;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.Button;

/**
 * Created by Jenny on 2019/2/22.
 */

public class CountDownTimerUtils extends CountDownTimer {

    private Button btn_getSMSCode;

    /**
     * 参数1：倒计时的总时间,单位为毫秒,参数2：计时变化依据的间隔时间,单位为毫秒.
     * @param millisInFuture
     * @param countDownInterval
     */
    public CountDownTimerUtils(Button button,long millisInFuture, long countDownInterval) {       //构造方法
        super(millisInFuture, countDownInterval);
        this.btn_getSMSCode = button;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        btn_getSMSCode.setClickable(false);//设置不可点击
        btn_getSMSCode.setText(millisUntilFinished/1000+"秒");//设置倒计时时间
         SpannableString spannableString=new SpannableString(btn_getSMSCode.getText().toString());//获取按钮上的文字
        ForegroundColorSpan span=new ForegroundColorSpan(Color.GRAY);//设置文字颜色
        btn_getSMSCode.setAllCaps(false);
        spannableString.setSpan(span,0,2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);////将倒计时的时间设置为红色
        btn_getSMSCode.setText(spannableString);
    }

    @Override
    public void onFinish() {
        btn_getSMSCode.setClickable(true);//重新获得点击
        btn_getSMSCode.setText("重新获取");
    }
}
