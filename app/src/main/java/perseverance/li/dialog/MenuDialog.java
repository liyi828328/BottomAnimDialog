package perseverance.li.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;

import perseverance.li.R;

/**
 * ---------------------------------------------------------------
 * Author: LiYi
 * Create: 16-3-8 14:05
 * ---------------------------------------------------------------
 * Describe:
 * ---------------------------------------------------------------
 * Changes:
 * ---------------------------------------------------------------
 * 16-3-8 14 : Create by LiYi
 * ---------------------------------------------------------------
 */
public class MenuDialog<T> extends BaseBottomAnimDialog {

    private Context mContext;
    /**
     * 标题
     */
    private String mTitle;
    /**
     * menu数据
     */
    private List<T> mData;
    /**
     * 标题view
     */
    private TextView mSpinnerTitle;
    /**
     * 分割线
     */
    private View gapLine;
    /**
     * 是否展示Menu
     */
    private boolean isShowMenu;
    /**
     * menu展示listview
     */
    private ListView mSpinnerListView;
    /**
     * adapter
     */
    private BaseAdapter mBaseAdapter;
    /**
     * menu点击监听
     */
    private IMenuItemSelectListener mMenuItemSelectListener;
    /**
     * 是否第一次加载
     */
    private boolean firstShow = true;

    /**
     * @param context
     * @param title         操作类型提示,如果为空则不显示
     * @param isShowMenu    是否显示menu键（如手机为虚拟键盘，则在虚拟键盘位置显示menu键）
     * @param goneWindowDim 隐藏dialog window 的背景
     * @param listener      选择回调
     */
    private MenuDialog(Context context, Serializable title, boolean isShowMenu,
                       boolean goneWindowDim, IMenuItemSelectListener listener) {
        super(context, goneWindowDim);
        this.mContext = context;
        if (title instanceof Integer) {
            Integer titleId = (Integer) title;
            this.mTitle = mContext.getResources().getString(titleId);
        } else if (title instanceof String) {
            this.mTitle = (String) title;
        }

        this.isShowMenu = isShowMenu;
        this.mMenuItemSelectListener = listener;
    }

    /**
     * @param context
     * @param title         操作类型提示,如果为空则不显示
     * @param data          数据源
     * @param isShowMenu    是否显示menu键（如手机为虚拟键盘，则在虚拟键盘位置显示menu键）
     * @param goneWindowDim 隐藏dialog window 的背景
     * @param listener      选择回调
     */
    public MenuDialog(Context context, Serializable title, List<T> data,
                      boolean isShowMenu, boolean goneWindowDim, IMenuItemSelectListener listener) {
        this(context, title, isShowMenu, goneWindowDim, listener);
        this.mData = data;
    }

    /**
     * @param context
     * @param title         操作类型提示,如果为空则不显示
     * @param adapter       需要加载的adapter
     * @param isShowMenu    是否显示menu键（如手机为虚拟键盘，则在虚拟键盘位置显示menu键）
     * @param goneWindowDim 隐藏dialog window 的背景
     * @param listener      选择回调
     */
    public MenuDialog(Context context, Serializable title, BaseAdapter adapter,
                      boolean isShowMenu, boolean goneWindowDim, IMenuItemSelectListener listener) {
        this(context, title, isShowMenu, goneWindowDim, listener);
        this.mBaseAdapter = adapter;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.menu_dialog_layout;
    }

    @Override
    public void configView(View rootView) {
        if (isShowMenu) {
            //虚拟键盘显示menu按键
            try {
                getWindow().addFlags(
                        WindowManager.LayoutParams.class.getField("FLAG_NEEDS_MENU_KEY").getInt(null));
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            setOnKeyListener(keyListener);
        }
        mSpinnerTitle = (TextView) rootView.findViewById(R.id.spinner_title);
        mSpinnerListView = (ListView) rootView.findViewById(R.id.spinner_listview);
        gapLine = rootView.findViewById(R.id.spinner_gap_line);
        if (TextUtils.isEmpty(mTitle)) {
            mSpinnerTitle.setVisibility(View.GONE);
            gapLine.setVisibility(View.GONE);
        } else {
            mSpinnerTitle.setText(mTitle);
        }

        mSpinnerListView.setOnItemClickListener(itemClickListener);
        //TODO：如果外部通过构造方法传入自定义的adapter，则使用外部传入的adapter，否则创建自己的adapter
        if (mBaseAdapter == null) {
            mBaseAdapter = new ArrayAdapter<T>(mContext,
                    R.layout.menu_item_layout, mData);
        }
        mSpinnerListView.setAdapter(mBaseAdapter);
    }

    /**
     * ListView监听
     * <p/>
     * item被点击后销毁dialog并且回调{@link IMenuItemSelectListener}监听
     */
    private OnItemClickListener itemClickListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (isShowing()) {
                dismiss();
            }
            if (mMenuItemSelectListener != null) {
                mMenuItemSelectListener.menuSelectedItem(position);
            }
        }
    };

    @Override
    public void show() {
        super.show();
    }

    /**
     * Menu按键监听
     */
    private OnKeyListener keyListener = new OnKeyListener() {

        @Override
        public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent event) {
            Log.d("liyi", "onkey event: " + event.toString());
            //这里为何监听？因为dialog展示出来后Activity的监听就失效了。
            //同时这里只监听ACTION_UP是因为Activity中监听了OnKeyDown，UP事件会被传递过来。
            if (keyCode == KeyEvent.KEYCODE_MENU && event.getAction() == KeyEvent.ACTION_UP) {
                if (firstShow) {
                    firstShow = false;
                    return true;
                }
                dismiss();
                return true;
            }
            return false;
        }
    };
}
