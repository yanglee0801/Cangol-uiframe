package mobi.cangol.mobile.navigation;

import mobi.cangol.mobile.R;
import mobi.cangol.mobile.actionbar.ActionBarDrawerToggle;
import mobi.cangol.mobile.base.BaseNavigationFragmentActivity;
import mobi.cangol.mobile.logging.Log;
import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class DrawerNavigationFragmentActivity extends BaseNavigationFragmentActivity {
	
	private boolean mFloatActionBarEnabled;
	public void setFloatActionBarEnabled(boolean floatActionBarEnabled) {
		mFloatActionBarEnabled = floatActionBarEnabled;
	}
	
	public boolean isFloatActionBarEnabled() {
		return mFloatActionBarEnabled;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		this.setNavigationFragmentActivityDelegate(new DrawerMenuNavigationFragmentActivityDelegate(this));
		super.onCreate(savedInstanceState);
	}

	@Override
	abstract public void findViews();

	@Override
	abstract public void initViews(Bundle savedInstanceState);

	@Override
	abstract public void initData(Bundle savedInstanceState);

}

class DrawerMenuNavigationFragmentActivityDelegate extends
		AbstractNavigationFragmentActivityDelegate {
	
	private BaseNavigationFragmentActivity mActivity;
	
	private DrawerMenuLayout mDrawerMenuLayout;

	public DrawerMenuNavigationFragmentActivityDelegate(
			BaseNavigationFragmentActivity activity) {
		mActivity = activity;
	}
	@Override
	public BaseNavigationFragmentActivity getActivity() {
		return mActivity;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		mDrawerMenuLayout = (DrawerMenuLayout) LayoutInflater.from(mActivity).inflate(R.layout.navigation_drawer_main, null);
		mDrawerMenuLayout.setDrawerListener(new ActionBarDrawerToggle(mActivity,
				mDrawerMenuLayout,R.drawable.actionbar_home_indicator) {

			@Override
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				Log.d("onDrawerClosed");
				if (!mDrawerMenuLayout.isDrawerOpen(Gravity.LEFT)) {
					if (mActivity.getCustomFragmentManager().size() <= 1) {
						mDrawerMenuLayout.setDrawerLockMode(Gravity.LEFT,
								DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
					} else {
						mDrawerMenuLayout.setDrawerLockMode(Gravity.LEFT,
								DrawerLayout.LOCK_MODE_UNLOCKED);
						mActivity.getCustomActionBar().displayUpIndicator();
					}
				}
				// 通知menu onClose
				mActivity.notifyMenuOnClose();
			}

			@Override
			public void onDrawerOpened(View view) {
				super.onDrawerOpened(view);
				Log.d("onDrawerOpened");
				// 通知menu onOpen
				mActivity.notifyMenuOnOpen();
			}

		});
	}
	@Override
	public ViewGroup getRootView() {
		return mDrawerMenuLayout;
	}

	@Override
	public ViewGroup getMenuView() {
		return mDrawerMenuLayout.getMenuView();
	}

	@Override
	public ViewGroup getContentView() {
		return mDrawerMenuLayout.getContentView();
	}

	@Override
	public void setContentView(View v) {
		mDrawerMenuLayout.setContentView(v);
	}

	@Override
	public int getMenuFrameId() {
		return mDrawerMenuLayout.getMenuFrameId();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		mDrawerMenuLayout.onConfigurationChanged(newConfig);
	}

	@Override
	public void showMenu(boolean show) {
		mDrawerMenuLayout.showMenu(show);
	}

	@Override
	public boolean isShowMenu() {
		return mDrawerMenuLayout.isShowMenu();
	}
	@Override
	public void setMenuEnable(boolean enable) {
		mDrawerMenuLayout.setMenuEnable(enable);
	}
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& mDrawerMenuLayout.isShowMenu()) {
			mDrawerMenuLayout.showMenu(false);
			return true;
		}
		return false;
	}
	@Override
	public void attachToActivity(Activity activity) {
		// TODO Auto-generated method stub
		mDrawerMenuLayout.attachToActivity(activity,((DrawerNavigationFragmentActivity)activity).isFloatActionBarEnabled());
	}
}