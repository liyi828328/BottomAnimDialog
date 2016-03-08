package perseverance.li;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import perseverance.li.dialog.IMenuItemSelectListener;
import perseverance.li.dialog.MenuDialog;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, IMenuItemSelectListener {

    private ArrayList<String> menus = new ArrayList<>();
    private MenuDialog<String> mMenuDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initView() {
        findViewById(R.id.dialog).setOnClickListener(this);
    }

    private void initData() {
        menus.add("添加到联系人");
        menus.add("添加到现有联系人");
        menus.add("删除联系人");
        menus.add("直接拨打");
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.dialog) {
            showMenuDialog();
        }
    }

    /**
     * 显示dialog
     */
    private void showMenuDialog() {
        if (mMenuDialog != null) {
            mMenuDialog = null;
        }
        mMenuDialog = new MenuDialog<>(this, "MenuDialog", menus, true, false, this);
        mMenuDialog.show();
    }

    /**
     * 销毁dialog
     */
    private void dismissMenuDialog() {
        if (mMenuDialog != null && mMenuDialog.isShowing()) {
            mMenuDialog.dismiss();
            mMenuDialog = null;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_MENU) {
            showMenuDialog();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void menuSelectedItem(int position) {
        Toast.makeText(this, "dialog select position: " + position, Toast.LENGTH_SHORT).show();
    }
}

