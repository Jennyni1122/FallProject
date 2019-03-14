package com.jennyni.fallproject.utils;

import android.animation.FloatArrayEvaluator;
import android.view.View;

/**
 *需要设置AvatarBehavior控件的X轴与Y轴的坐标
 * Created by Jenny on 2019/1/21.
 */

public class AnimHelper {

    private AnimHelper(){
        throw new RuntimeException("AnimHelper cannot be initialized!");
    }
    public static void setViewX(View view,float originalX,float finalX,float percent){
        float calcX = (finalX - originalX)*percent + originalX;
        view.setX(calcX);
    }
    public static void setViewY(View view ,float originalY,float finalY,float percent){
        float calcY = (finalY - originalY)*percent + originalY;
        view.setY(calcY);
    }
    public static void scaleView(View view,float originalSize,float finalSize ,float percent){
        float calcSize = (finalSize - originalSize) * percent * originalSize;
        float calcScale = calcSize / originalSize;
        view.setScaleX(calcScale);
        view.setScaleY(calcScale);
    }
}
