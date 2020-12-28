package com.ymx.driver.viewmodel.launch;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;

import com.ymx.driver.BR;
import com.ymx.driver.R;
import com.ymx.driver.base.BaseViewModel;
import com.ymx.driver.base.SingleLiveEvent;
import com.ymx.driver.util.UIUtils;

import me.tatarka.bindingcollectionadapter2.ItemBinding;

public class IntroViewModel extends BaseViewModel {
    public IntroViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        for (int i = 0; i < resIds.length; i++) {
            items.add(new IntroItemViewModel(this, resIds[i], resContentView[i], titles[i], tips[i], resSelectIds[i], titleColors[i]));
        }
    }

    private static final int[] resIds = {R.drawable.icon_huanying_one,
            R.drawable.icon_huanying_two, R.drawable.icon_huanying_one};

    private static final boolean[] titleColors = {true,
            false, false};

//    private static final int resIds[] = {R.drawable.icon_income_bg,
//            R.drawable.icon_income_bg, R.drawable.icon_income_bg};


    private static final int[] resContentView = {R.drawable.icon_content_huanying_one,
            R.drawable.icon_content_huanying_two, R.drawable.icon_content_huanying_one};
    private static final int[] resSelectIds = {R.drawable.icon_income_select_one,
            R.drawable.icon_income_select_two, R.drawable.icon_income_select_one};


    private static final String[] titles = {UIUtils.getString(R.string.intro_title_1),
            UIUtils.getString(R.string.intro_title_2), UIUtils.getString(R.string.intro_title_3)};

    private static final String[] tips = {UIUtils.getString(R.string.intro_tip_1),

            UIUtils.getString(R.string.intro_tip_2), UIUtils.getString(R.string.intro_tip_3)};


    public ObservableList<IntroItemViewModel> items = new ObservableArrayList<>();
    //给ViewPager添加ItemBinding
    public ItemBinding<IntroItemViewModel> itemBinding = ItemBinding.of(com.ymx.driver.BR.viewModel, R.layout.item_intro);


    public int getPosition(IntroItemViewModel introItemViewModel) {
        return items.indexOf(introItemViewModel);
    }

    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        //名称输入完成
        public SingleLiveEvent<Void> clickEnter = new SingleLiveEvent<>();
    }
}
