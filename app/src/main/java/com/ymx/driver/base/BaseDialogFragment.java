package com.ymx.driver.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.amap.api.location.AMapLocation;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle2.components.support.RxDialogFragment;
import com.ymx.driver.util.SoftKeyboardUtil;
import com.ymx.driver.view.LoadingDialog;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;


import io.reactivex.functions.Consumer;

/**
 * Created by wuwei
 * 2020/4/21
 * 佛祖保佑       永无BUG
 */
public abstract class BaseDialogFragment<V extends ViewDataBinding, VM extends BaseViewModel> extends RxDialogFragment implements IBaseView {
    public BaseActivity activity;
    protected V binding;
    protected VM viewModel;
    private int viewModelId;
    private LoadingDialog loadingDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (BaseActivity) getActivity();
        initParam();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dismissLoading();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, initLayoutId(savedInstanceState), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDialog();
        //页面接受的参数方法
        initParam();
        //私有的初始化Databinding和ViewModel方法
        initViewDataBinding();
        //初始化界面
        initView(savedInstanceState);
        //私有的ViewModel与View的契约事件回调逻辑
        registorUIChangeLiveDataCallBack();
        //页面数据初始化方法
        initData();
        //页面事件监听的方法，一般用于ViewModel层转到View层的事件注册
        initViewObservable();
        //注册RxBus
        viewModel.registerEventBus();
    }

//    /**
//     * Message 引发的 DialogFragment 内存泄漏
//     */
//    private static final String SAVED_DIALOG_STATE_TAG = "android:savedDialogState";
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        if (getShowsDialog()) {
//            setShowsDialog(false);
//        }
//        super.onActivityCreated(savedInstanceState);
//        setShowsDialog(true);
//
//        View view = getView();
//        if (view != null) {
//            if (view.getParent() != null) {
//                throw new IllegalStateException(
//                        "DialogFragment can not be attached to a container view");
//            }
//            getDialog().setContentView(view);
//        }
//        final Activity activity = getActivity();
//        if (activity != null) {
//            getDialog().setOwnerActivity(activity);
//        }
//        if (savedInstanceState != null) {
//            Bundle dialogState = savedInstanceState.getBundle(SAVED_DIALOG_STATE_TAG);
//            if (dialogState != null) {
//                getDialog().onRestoreInstanceState(dialogState);
//            }
//        }
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (viewModel != null) {
            viewModel.removeEventBus();
        }
        if (binding != null) {
            binding.unbind();
        }
    }

    /**
     * 注入绑定
     */
    private void initViewDataBinding() {
        viewModelId = initVariableId();
        viewModel = initViewModel();
        if (viewModel == null) {
            Class modelClass;
            Type type = getClass().getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                modelClass = (Class) ((ParameterizedType) type).getActualTypeArguments()[1];
            } else {
                //如果没有指定泛型参数，则默认使用BaseViewModel
                modelClass = BaseViewModel.class;
            }
            viewModel = (VM) createViewModel(this, modelClass);
        }
        binding.setVariable(viewModelId, viewModel);
        //让ViewModel拥有View的生命周期感应
        getLifecycle().addObserver(viewModel);
        //注入RxLifecycle生命周期
        viewModel.injectLifecycleProvider(this);
    }

    /**
     * 初始化一些Dialog信息
     */
    protected void initDialog() {
        try {
            setStyle(DialogFragment.STYLE_NO_TITLE, 0);// 设置Dialog为无标题模式
            if (getDialog() != null) {
                getDialog().setCanceledOnTouchOutside(true);
                getDialog().setCancelable(true);
                getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);// 设置Dialog为无标题模式
                Window window = getDialog().getWindow();
                if (window != null) {
                    window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);// 隐藏软键盘
                    window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));// 设置Dialog背景色为透明
                    //设置dialog在屏幕底部
                    window.setGravity(setGravity());
                    //设置dialog弹出时的动画效果，从屏幕底部向上弹出
                    if (setWindowAnimations() != -1) {
                        window.setWindowAnimations(setWindowAnimations());
                    }
                    window.getDecorView().setPadding(0, 0, 0, 0);
                    //获得window窗口的属性
                    android.view.WindowManager.LayoutParams lp = window.getAttributes();
                    //设置窗口宽度为充满全屏
                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                    //设置窗口高度为包裹内容
                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                    //将设置好的属性set回去
                    window.setAttributes(lp);
                }
            }
        } catch (Exception e) {

        }
    }

    //刷新布局
    public void refreshLayout() {
        if (viewModel != null) {
            binding.setVariable(viewModelId, viewModel);
        }
    }

    public void requestPermission(final int requestCode, final boolean showDialog, String... permissions) {
        try {
            RxPermissions rxPermissions = new RxPermissions(activity);
            rxPermissions
                    .request(permissions)
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean aBoolean) throws Exception {
                            if (aBoolean) {
                                permissionGranted(requestCode);
                            } else {
                                if (showDialog) {
                                    showPermissionDialog(requestCode);
                                } else {
                                    permissionDenied(requestCode);
                                }
                            }
                            permissionGrantedOrDenineCanDo(requestCode);
                        }
                    });
        } catch (Exception e) {

        }
    }

    /**
     * 启动应用的设置
     *
     * @since 2.5.0
     */
    public void startAppSettings() {
        if (activity == null || activity.isDestroyed()) {
            return;
        }
        try {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("package:" + activity.getPackageName()));
            startActivity(intent);
        } catch (Exception e) {

        }
    }

    /**
     * =====================================================================
     **/
    //注册ViewModel与View的契约UI回调事件
    protected void registorUIChangeLiveDataCallBack() {
        //加载对话框显示
        viewModel.getUC().getShowDialogEvent().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String title) {
                showLoading();
            }
        });
        //加载对话框消失
        viewModel.getUC().getDismissDialogEvent().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(@Nullable Void v) {
                dismissLoading();
            }
        });

        viewModel.getUC().getLocateSuccessEvent().observe(this, new Observer<AMapLocation>() {
            @Override
            public void onChanged(AMapLocation aMapLocation) {
                locateSuccess(aMapLocation);
            }
        });

        viewModel.getUC().getHideSoftKeyBoardEvent().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                hideSoftKeyBoard();
            }
        });
    }

    public void showLoading() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(activity);
        }
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!activity.isFinishing()) {
                    loadingDialog.show();
                }
            }
        });
    }

    public void dismissLoading() {
        if (loadingDialog != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!activity.isFinishing()) {
                        loadingDialog.dismiss();
                    }
                }
            });
        }
    }

    public void hideSoftKeyBoard() {
        if (getActivity() != null) {
            View v = getActivity().getCurrentFocus();
            if (v != null) {
                SoftKeyboardUtil.hiddenSoftKeyboard(getActivity(), v.getWindowToken());
            }
        }
    }

    public void showInputMethodManager(EditText editText) {
        try {
            editText.setFocusable(true);
            editText.requestFocus();
            if (getActivity() != null) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        } catch (Exception e) {

        }

    }

    /**
     * =====================================================================
     **/

    /**
     * 初始化根布局
     *
     * @return 布局layout的id
     */
    public abstract int initLayoutId(Bundle savedInstanceState);

    /**
     * 初始化ViewModel的id
     *
     * @return BR的id
     */
    public abstract int initVariableId();

    /**
     * 初始化ViewModel
     *
     * @return 继承BaseViewModel的ViewModel
     */
    public VM initViewModel() {
        return null;
    }

    /**
     * 定位成功
     */
    protected void locateSuccess(AMapLocation aMapLocation) {

    }

    protected int setGravity() {
        return Gravity.CENTER;
    }

    protected int setWindowAnimations() {
        return -1;
    }

    /**
     * 权限未通过需要展示弹窗
     *
     * @param requestCode
     */
    protected void showPermissionDialog(int requestCode) {
        if (activity == null || activity.isDestroyed()) {
            return;
        }

    }

    /**
     * 权限申请成功执行
     *
     * @param requestCode
     */
    protected void permissionGranted(int requestCode) {

    }

    /**
     * 权限申请失败执行
     *
     * @param requestCode
     */
    protected void permissionDenied(int requestCode) {

    }

    /**
     * 权限申请成功或者失败都要执行
     */
    protected void permissionGrantedOrDenineCanDo(int requestCode) {

    }

    /**
     * 创建ViewModel
     *
     * @param cls
     * @param <T>
     * @return
     */
    public <T extends ViewModel> T createViewModel(Fragment fragment, Class<T> cls) {
        return ViewModelProviders.of(fragment).get(cls);
    }


}
