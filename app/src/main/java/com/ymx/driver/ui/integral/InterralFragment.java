package com.ymx.driver.ui.integral;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener;
import com.ymx.driver.R;
import com.ymx.driver.base.BaseFragment;
import com.ymx.driver.databinding.FramentInterralBinding;
import com.ymx.driver.entity.app.InterralCommodityListItem;
import com.ymx.driver.view.recyclerview.GridItemDecoration;
import com.ymx.driver.viewmodel.driverinterral.InterralViewModel;

import org.jetbrains.annotations.NotNull;


import java.util.ArrayList;
import java.util.List;


public class InterralFragment extends BaseFragment<FramentInterralBinding, InterralViewModel> {
    private List<InterralCommodityListItem> list;
    private InterralCommodityListAdapter adapter;
    private StaggeredGridLayoutManager layoutManager;

    @Override
    public int initLayoutId(Bundle savedInstanceState) {
        return R.layout.frament_interral;
    }

    @Override
    public int initVariableId() {
        return com.ymx.driver.BR.viewModel;
    }

    @Override
    public void initParam() {

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        list = new ArrayList<>();
        adapter = new InterralCommodityListAdapter(list);
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        binding.recyc.setLayoutManager(layoutManager);
        binding.recyc.setItemAnimator(null);
        binding.recyc.addItemDecoration(new GridItemDecoration(getActivity(), 10));//单位px
        binding.recyc.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                layoutManager.invalidateSpanAssignments(); //防止第一行到顶部有空白区域
            }
        });

        binding.recyc.setAdapter(adapter);
        binding.recyc.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //刷新
                layoutManager.invalidateSpanAssignments();
            }
        });

        binding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                adapter.setList(null);
                viewModel.loadlist.clear();
                viewModel.pagerIndex.set(1);
                viewModel.refresh.set(true);
                viewModel.getInterralCommodityList();

            }
        });

        binding.refreshLayout.setOnMultiPurposeListener(new SimpleMultiPurposeListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                viewModel.refresh.set(false);
                refreshLayout.finishLoadMore(1000);
                viewModel.pagerIndex.set(viewModel.pagerIndex.get() + 1);
                viewModel.getInterralCommodityList();

            }
        });
    }

    @Override
    public void initData() {
        viewModel.getInterralCommodityList();
    }

    @Override
    public void initViewObservable() {
        viewModel.uc.ucCanLoadmore.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                binding.refreshLayout.setEnableLoadMore(aBoolean);
            }
        });
        viewModel.uc.ucGoIntegralDetailList.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {

                nav().navigate(R.id.action_integraldetails_fragment);

            }
        });
        viewModel.uc.ucGoMyOrder.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {

                nav().navigate(R.id.action_integral_order_fragment);

            }
        });

        viewModel.uc.ucShowDialog.observe(this, new Observer<InterralCommodityListItem>() {
            @Override
            public void onChanged(InterralCommodityListItem listItem) {

                Bundle bundle = new Bundle();
                bundle.putSerializable(ConfirmExchangeCommodityDialog.EXCHANGE_INFO, listItem);
                bundle.putString(ConfirmExchangeCommodityDialog.EXCHANGE_NUM, viewModel.integral.get());
                ConfirmExchangeCommodityDialog.newInstance(bundle).show(getChildFragmentManager(), ConfirmExchangeCommodityDialog.class.getSimpleName());

            }
        });

        viewModel.uc.ucRefresh.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {

                binding.refreshLayout.finishRefresh();

            }
        });
        viewModel.uc.ucList.observe(this, new Observer<List<InterralCommodityListItem>>() {
            @Override
            public void onChanged(List<InterralCommodityListItem> commodityListItemList) {

                if (!commodityListItemList.isEmpty()) {
                    adapter.setNewData(commodityListItemList);
                }


            }


        });
    }

    public class InterralCommodityListAdapter extends BaseQuickAdapter<InterralCommodityListItem, BaseViewHolder> {

        public InterralCommodityListAdapter(List<InterralCommodityListItem> data) {
            super(R.layout.intertal_commodity_list_item_2, data);
        }


        @Override
        protected void convert(@NotNull BaseViewHolder help, InterralCommodityListItem item) {
            ImageView iv = help.getView(R.id.iv);
            LinearLayout exchangeLl = help.getView(R.id.exchangeLl);
            exchangeLl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item != null && item.getStocksNumber()>0){
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(ConfirmExchangeCommodityDialog.EXCHANGE_INFO, item);
                        bundle.putString(ConfirmExchangeCommodityDialog.EXCHANGE_NUM, viewModel.integral.get());
                        ConfirmExchangeCommodityDialog.newInstance(bundle).show(getChildFragmentManager(), ConfirmExchangeCommodityDialog.class.getSimpleName());
                    }
                }
            });
            TextView exchangeIntegralTv = help.getView(R.id.exchangeIntegral);
            TextView shopTv = help.getView(R.id.shop);
            Glide.with(iv.getContext()).load(item.getCommodityUrl()).into(iv);
            if (!TextUtils.isEmpty(item.getCommodityName())) {
                help.setText(R.id.commodityName, item.getCommodityName());
            }
            if (!TextUtils.isEmpty(item.getExchangeIntegral())) {
                exchangeIntegralTv.setVisibility(View.VISIBLE);
                exchangeIntegralTv.setText(item.getExchangeIntegral() + getString(R.string.integral_exchange));

            } else {
                exchangeIntegralTv.setVisibility(View.GONE);
            }

            if (item != null && item.getStocksNumber() == 0) {
                shopTv.setVisibility(View.VISIBLE);
            } else {
                shopTv.setVisibility(View.GONE);
            }
        }
    }

}
