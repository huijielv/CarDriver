package com.ymx.driver.viewmodel.launch;

import androidx.annotation.NonNull;

import com.ymx.driver.base.ItemViewModel;
import com.ymx.driver.binding.command.BindingAction;
import com.ymx.driver.binding.command.BindingCommand;

public class IntroItemViewModel extends ItemViewModel<IntroViewModel> {

    public int resId;
    public String title;
    public String tip;
    public int resContentViewId;
    public int selectId;
    public boolean titleColor;

    public IntroItemViewModel(@NonNull IntroViewModel viewModel, int resId, int resContentViewId, String title, String tip, int selectId, boolean titleColor) {
        super(viewModel);
        this.resId = resId;
        this.resContentViewId = resContentViewId;
        this.title = title;
        this.tip = tip;
        this.selectId = selectId;
        this.titleColor = titleColor;
    }

    public boolean isLast() {
        return viewModel.getPosition(this) == viewModel.items.size() - 1;
    }


    public BindingCommand itemClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            viewModel.uc.clickEnter.call();
        }
    });

}
