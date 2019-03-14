package com.jennyni.fallproject.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import com.jennyni.fallproject.R;


/**
 * 侧滑控件，用于报警界面的Item中
 *
 */
public class SlidingButtonView extends HorizontalScrollView {
    private TextView mTextView_Delete;          //删除按钮
    private int mScrollWidth;                   //横向滚动的范围
    private IonSlidingButtonListener mIonSlidingButtonListener;
    private Boolean isOpen = false;
    private Boolean once = false;               //标记第一次进入获取删除按钮
    public SlidingButtonView(Context context) {
        this(context, null);
    }
    public SlidingButtonView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public SlidingButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setOverScrollMode(OVER_SCROLL_NEVER);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //第一次进入时获取删除按钮控件
        if (!once) {
            mTextView_Delete = (TextView) findViewById(R.id.tv_delete);
            once = true;            //修改标记
        }
    }
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        //默认隐藏删除按钮
        if (changed) {
            this.scrollTo(0, 0);
            //获取水平滚动条可以滑动的范围，即右侧按钮的宽度
            mScrollWidth = mTextView_Delete.getWidth();
        }
    }

    //手势判断
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                mIonSlidingButtonListener.onDownOrMove(this);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                changeScrollx();
                return true;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        mTextView_Delete.setTranslationX(l - mScrollWidth);
    }
    /**
     * 按滚动条被拖动距离判断显示删除按钮
     */
    public void changeScrollx() {
        //触摸滑动的距离大于删除按钮宽度的一半
        if (getScrollX() >= (mScrollWidth / 2)) {
            //显示删除按钮
            this.smoothScrollTo(mScrollWidth, 0);
            isOpen = true;
            mIonSlidingButtonListener.onMenuIsOpen(this);
        } else {
            //隐藏按钮
            this.smoothScrollTo(0, 0);
            isOpen = false;
        }
    }
    /**
     * 打开菜单
     */
    public void openMenu() {
        if (isOpen) {
            return;
        }
        this.smoothScrollTo(mScrollWidth, 0);
        isOpen = true;
        mIonSlidingButtonListener.onMenuIsOpen(this);
    }
    /**
     * 关闭菜单
     */
    public void closeMenu() {
        if (!isOpen) {
            return;
        }
        this.smoothScrollTo(0, 0);
        isOpen = false;
    }
    public void setSlidingButtonListener(IonSlidingButtonListener listener) {
        mIonSlidingButtonListener = listener;
    }
    public interface IonSlidingButtonListener {
        void onMenuIsOpen(View view);
        void onDownOrMove(SlidingButtonView slidingButtonView);
    }
}
