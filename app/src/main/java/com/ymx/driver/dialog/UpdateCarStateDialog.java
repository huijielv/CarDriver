package com.ymx.driver.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.ymx.driver.R;

import com.ymx.driver.databinding.DialogUpdateCarStateBinding;
import com.ymx.driver.util.UIUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class UpdateCarStateDialog extends Dialog {
    private DialogUpdateCarStateBinding stateBinding;
    private UpdateCarStateAdapter adapter;
    private Context context;
    private List<String> list = new ArrayList<>();


    public UpdateCarStateDialog(@NonNull Context context) {
        super(context, R.style.Theme_Light_Dialog);
        this.context = context;
        list.add("网约车一口价(接受拼单)");
        list.add("网约车一口价(接受拼单)");
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.getDecorView().setBackgroundColor(UIUtils.getColor(R.color.rx_transparent));
        window.getDecorView().setPadding(0, 0, 0, 0);
        window.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stateBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                R.layout.dialog_update_car_state, null, false);
        setContentView(stateBinding.getRoot());
        adapter = new UpdateCarStateAdapter(list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        stateBinding.recyc.setLayoutManager(layoutManager);
        stateBinding.recyc.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> ada, @NonNull View view, int position) {
                adapter.setSelPos(position);
                adapter.notifyDataSetChanged();
            }
        });


    }


    public class UpdateCarStateAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

        /**
         * 定义选中的下标, 默认-1
         */
        private int selPos = 0;

        public void setSelPos(int selPos) {
            this.selPos = selPos;
        }

        public UpdateCarStateAdapter(List<String> data) {
            super(R.layout.day_update_car_state_rv_item, data);
        }


        @Override
        protected void convert(@NotNull BaseViewHolder help, String item) {
            ImageView ivSelect = help.getView(R.id.cardStateIv);
            ivSelect.setSelected(selPos == help.getAdapterPosition());
            TextView tvSelect = help.getView(R.id.cardStateTv);
            tvSelect.setText(item);
        }


    }


}
