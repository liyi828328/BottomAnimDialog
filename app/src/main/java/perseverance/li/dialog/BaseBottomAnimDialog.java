package perseverance.li.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import perseverance.li.R;

/**
 * ---------------------------------------------------------------
 * Author: LiYi
 * Create: 16-3-8 13:58
 * ---------------------------------------------------------------
 * Describe:
 * 底部弹出dialog基类
 * ---------------------------------------------------------------
 * Changes:
 * ---------------------------------------------------------------
 * 16-3-8 13 : Create by LiYi
 * ---------------------------------------------------------------
 */
public abstract class BaseBottomAnimDialog extends AlertDialog {

    public Context mContext;
    public LayoutInflater mInflater;
    /**
     * 是否设置dialog背景dimAmount值
     */
    private boolean goneWindowDim = false;

    public BaseBottomAnimDialog(Context context) {
        super(context, R.style.dialogStyle);
        mContext = context;
        init(context);
    }

    public BaseBottomAnimDialog(Context context, boolean goneWindowDim) {
        super(context, R.style.dialogStyle);
        mContext = context;
        this.goneWindowDim = goneWindowDim;
        init(context);
    }

    public BaseBottomAnimDialog(Context context, int theme) {
        super(context, theme);
        mContext = context;
        init(context);
    }

    public BaseBottomAnimDialog(Context context, boolean cancelable,
                                OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mContext = context;
        init(context);
    }

    private void init(Context context) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        setCanceledOnTouchOutside(true);
    }

    /**
     * 设置layout的id
     *
     * @return
     */
    public abstract int getLayoutRes();

    /**
     * 初始化工作
     *
     * @param rootView
     */
    public abstract void configView(View rootView);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = mInflater.inflate(getLayoutRes(), null, false);
        setContentView(rootView);
        setLocation();
        configView(rootView);
    }

    /**
     * 设置window位置为底部弹出
     */
    private void setLocation() {
        WindowManager wm = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = display.getWidth();
        p.gravity = Gravity.BOTTOM;
        if (goneWindowDim) {
            p.dimAmount = 0f;
        }
        getWindow().setAttributes(p);
    }
}
