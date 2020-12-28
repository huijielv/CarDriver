package com.ymx.driver.base;

import java.util.List;

import androidx.annotation.NonNull;

public class ExpandableStickyItemViewModel<D extends ItemViewModel, VM extends BaseViewModel> extends ItemViewModel<VM> {
    /**
     * 是否展开，默认否
     */
    protected boolean isExpand;
    protected List<D> children;

    public ExpandableStickyItemViewModel(@NonNull VM viewModel) {
        super(viewModel);
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }

    public List<D> getChildren() {
        return children;
    }

    public void setChildren(List<D> children) {
        this.children = children;
    }
}