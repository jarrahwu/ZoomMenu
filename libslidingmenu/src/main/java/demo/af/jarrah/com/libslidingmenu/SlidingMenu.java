package demo.af.jarrah.com.libslidingmenu;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.nineoldandroids.view.ViewHelper;


public class SlidingMenu extends HorizontalScrollView {
    /**
     * 屏幕宽度
     */
    private int mScreenWidth;
    /**
     * dp
     */
    private int mMenuRightPadding;

    /**
     * 菜单的宽度
     */
    private int mMenuWidth;
    private int mHalfMenuWidth;

    private boolean isOpen;

    private boolean onece;

    private ViewGroup mMenu;
    private ViewGroup mContent;

    //contains menu content
    private LinearLayout mContainer;

    public SlidingMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public SlidingMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mScreenWidth = ScreenUtils.getScreenWidth(context);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.SlidingMenu, defStyle, 0);
        int n = a.getIndexCount();

        mMenuRightPadding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 150f,
                getResources().getDisplayMetrics());

        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.SlidingMenu_rightPadding) {
                setPadingRight(a, attr);
            }
        }
        a.recycle();

        mContainer = new LinearLayout(getContext());
        mContainer.setOrientation(LinearLayout.HORIZONTAL);
        mContainer.setGravity(Gravity.CENTER_VERTICAL);
        addView(mContainer);
    }

    private void setPadingRight(TypedArray a, int attr) {
        mMenuRightPadding = a.getDimensionPixelSize(attr,
                (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 150f,
                        getResources().getDisplayMetrics()));// 默认为10DP
    }

    public void setup(View menu, View content) {
        mContainer.addView(menu);
        mContainer.addView(content);
    }

    public SlidingMenu(Context context) {
        this(context, null, 0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /**
         * 显示的设置一个宽度
         */
        if (!onece) {
            LinearLayout wrapper = (LinearLayout) getChildAt(0);
            if(wrapper != null) {
                mMenu = (ViewGroup) wrapper.getChildAt(0);
                mContent = (ViewGroup) wrapper.getChildAt(1);

                mMenuWidth = mScreenWidth - mMenuRightPadding;
                mHalfMenuWidth = mMenuWidth / 2;
                mMenu.getLayoutParams().width = mMenuWidth;
                mContent.getLayoutParams().width = mScreenWidth;
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            // 将菜单隐藏
            this.scrollTo(mMenuWidth, 0);
            onece = true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            // Up时，进行判断，如果显示区域大于菜单宽度一半则完全显示，否则隐藏
            case MotionEvent.ACTION_UP:
                int scrollX = getScrollX();
                if (scrollX > mHalfMenuWidth) {
                    this.smoothScrollTo(mMenuWidth, 0);
                    isOpen = false;
                } else {
                    this.smoothScrollTo(0, 0);
                    isOpen = true;
                }
                return true;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 打开菜单
     */
    public void openMenu() {
        if (isOpen)
            return;
        this.smoothScrollTo(0, 0);
        isOpen = true;
    }

    /**
     * 关闭菜单
     */
    public void closeMenu() {
        if (isOpen) {
            this.smoothScrollTo(mMenuWidth, 0);
            isOpen = false;
        }
    }

    /**
     * 切换菜单状态
     */
    public void toggle() {
        if (isOpen) {
            closeMenu();
        } else {
            openMenu();
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        float scale = l * 1.0f / mMenuWidth;
        float leftScale = 1 - 0.3f * scale;
        float rightScale = 0.8f + scale * 0.2f;

		ViewHelper.setScaleX(mMenu, leftScale);
		ViewHelper.setScaleY(mMenu, leftScale);
		ViewHelper.setAlpha(mMenu, 0.6f + 0.4f * (1 - scale));
		ViewHelper.setTranslationX(mMenu, mMenuWidth * scale * 0.7f);

		ViewHelper.setPivotX(mContent, 0);
		ViewHelper.setPivotY(mContent, mContent.getHeight() / 2);
		ViewHelper.setScaleX(mContent, rightScale);
		ViewHelper.setScaleY(mContent, rightScale);

//        ViewCompat.setScaleX();

    }


    public void attachToActivity(Activity activity, View menu, Drawable background) {
        ViewGroup decor = (ViewGroup) activity.getWindow().getDecorView();
        ViewGroup activityViewGroup = (ViewGroup) decor.getChildAt(0);
        decor.removeView(activityViewGroup);
        setup(menu, activityViewGroup);
        if(background != null)
            setBackgroundDrawable(background);
        decor.addView(this);
    }
}
