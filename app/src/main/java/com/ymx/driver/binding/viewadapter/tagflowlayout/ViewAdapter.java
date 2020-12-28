package com.ymx.driver.binding.viewadapter.tagflowlayout;

import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.ymx.driver.binding.command.BindingCommand;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;

public final class ViewAdapter {
    @BindingAdapter(value = {"tagItems", "tagItemBinding"}, requireAll = false)
    public static <T> void setTagFlowLayoutAdapter(TagFlowLayout tagFlowLayout, List<T> tagItems, ItemBinding<T> tagItemBinding) {
        tagFlowLayout.setAdapter(new TagAdapter<T>(tagItems) {

            @Override
            public View getView(FlowLayout parent, int position, Object obj) {
                ViewDataBinding inflate = DataBindingUtil.inflate(LayoutInflater.from(tagFlowLayout.getContext()),
                        tagItemBinding.getLayoutRes(), null, false);
                inflate.setVariable(tagItemBinding.getVariableId(), obj);
                return inflate.getRoot();
            }
        });
    }

    @BindingAdapter(value = {"tagClick"}, requireAll = false)
    public static void setOnTagClickListener(TagFlowLayout tagFlowLayout, final BindingCommand<Integer> tagClick) {
        tagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                if (tagClick != null) {
                    tagClick.execute(position);
                }
                return false;
            }
        });
    }

    public static class ItemBinding<T> {
        private int variableId;
        @LayoutRes
        private int layoutRes;

        public int getVariableId() {
            return variableId;
        }

        public void setVariableId(int variableId) {
            this.variableId = variableId;
        }

        public int getLayoutRes() {
            return layoutRes;
        }

        public void setLayoutRes(int layoutRes) {
            this.layoutRes = layoutRes;
        }

        public ItemBinding(int variableId, int layoutRes) {
            this.variableId = variableId;
            this.layoutRes = layoutRes;
        }
    }
}

