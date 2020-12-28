package com.ymx.driver.ui.main.fragment;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.lifecycle.Observer;

import com.ymx.driver.R;
import com.ymx.driver.base.BaseDialogFragment;
import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.databinding.TodayIncomeSelectTimeFramentBinding;
import com.ymx.driver.entity.app.SelectIncomeTimePickEntity;
import com.ymx.driver.util.DateUtils;
import com.ymx.driver.util.UIUtils;
import com.ymx.driver.view.wheelview.adapter.BaseWheelAdapter;
import com.ymx.driver.view.wheelview.widget.WheelView;
import com.ymx.driver.viewmodel.main.TimePickViewModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TodayIncomeSelectTimeFrament extends BaseDialogFragment<TodayIncomeSelectTimeFramentBinding, TimePickViewModel> {

    private List<SelectIncomeTimePickEntity> timePickEntityList = new ArrayList<>();
    private BaseWheelAdapter monthAdapter;
    private BaseWheelAdapter dayAdapter;
    private int yearPosition;
    private int monthPosition;
    private int dayPosition;

    @Override
    public int initLayoutId(Bundle savedInstanceState) {
        return R.layout.today_income_select_time_frament;
    }

    @Override
    public int initVariableId() {

        return com.ymx.driver.BR.viewModel;
    }

    @Override
    public void initParam() {

    }

    @Override
    protected int setGravity() {
        return Gravity.BOTTOM;
    }

    @Override
    protected int setWindowAnimations() {
        return R.style.FromBottomDialogWindowStyle;
    }


    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public void initData() {
        String currentDate = DateUtils.timeStamp2Date(String.valueOf(System.currentTimeMillis() / 1000),
                "yyyy-MM-dd HH:mm");

        String beforeDate = DateUtils.setDay(currentDate.substring(0, 10), -180);
        int year = Integer.parseInt(currentDate.substring(0, 4));
        int month = Integer.parseInt(currentDate.substring(5, 7));
        int day = Integer.parseInt(currentDate.substring(8, 10));

        int beforeYear = Integer.parseInt(beforeDate.substring(0, 4));
        int beforeMonth = Integer.parseInt(beforeDate.substring(5, 7));
        int beforeDay = Integer.parseInt(beforeDate.substring(8, 10));


        List<SelectIncomeTimePickEntity.MonthDate> monthDateList = new ArrayList<>();

        if (beforeYear == year) {
            for (int i = beforeMonth; i <= month; i++) {
                List<SelectIncomeTimePickEntity.DayDate> dayDateList = new ArrayList<>();
                if (i == beforeMonth) {
                    getDayList(beforeDay, getDayNumber(beforeYear, beforeMonth), dayDateList, monthDateList, i);
                } else if (i == month) {
                    getDayList(1, day, dayDateList, monthDateList, i);
                } else {
                    getDayList(1, getDayNumber(year, i), dayDateList, monthDateList, i);
                }
            }
            SelectIncomeTimePickEntity selectIncomeTimePickEntity = new SelectIncomeTimePickEntity(String.valueOf(year), monthDateList);
            timePickEntityList.add(selectIncomeTimePickEntity);
        }

        if (beforeYear < year) {

            for (int i = beforeMonth; i <= 12; i++) {
                List<SelectIncomeTimePickEntity.DayDate> dayDateList = new ArrayList<>();
                if (i == beforeMonth) {
                    getDayList(beforeDay, getDayNumber(beforeYear, beforeMonth), dayDateList, monthDateList, i);
                } else {
                    getDayList(1, getDayNumber(beforeYear, i), dayDateList, monthDateList, i);
                }
            }
            SelectIncomeTimePickEntity selectIncomeBeforeTimePickEntity = new SelectIncomeTimePickEntity(String.valueOf(beforeYear), monthDateList);
            timePickEntityList.add(selectIncomeBeforeTimePickEntity);


            List<SelectIncomeTimePickEntity.MonthDate> monthNowDateList = new ArrayList<>();
            for (int i = 1; i <= month; i++) {
                List<SelectIncomeTimePickEntity.DayDate> dayDateList = new ArrayList<>();
                if (i == month) {
                    getDayList(1, day, dayDateList, monthNowDateList, i);
                } else {
                    getDayList(1, getDayNumber(beforeYear, i), dayDateList, monthNowDateList, i);
                }
            }
            SelectIncomeTimePickEntity selectIncomeTimePickEntity = new SelectIncomeTimePickEntity(String.valueOf(year), monthNowDateList);
            timePickEntityList.add(selectIncomeTimePickEntity);
        }


        binding.ytd.setWheelAdapter(new YearWheelAdapter());
        binding.ytd.setWheelSize(5);
        binding.ytd.setSelection(0);
        binding.ytd.setSkin(WheelView.Skin.Holo);
        WheelView.WheelViewStyle style = new WheelView.WheelViewStyle();
        style.holoBorderColor = UIUtils.getColor(R.color.rx_e1e1e1);
        style.holoBorderWidth = 1;
        binding.ytd.setStyle(style);

        binding.month.setWheelAdapter(monthAdapter = new MonthWheelAdapter());
        binding.month.setWheelSize(5);
        binding.month.setSelection(0);
        binding.month.setSkin(WheelView.Skin.Holo);
        binding.month.setStyle(style);

        binding.day.setWheelAdapter(dayAdapter = new DayWheelAdapter());
        binding.day.setWheelSize(5);
        binding.day.setSelection(0);
        binding.day.setSkin(WheelView.Skin.Holo);
        binding.day.setStyle(style);

        binding.ytd.setWheelData(timePickEntityList);
        binding.month.setWheelData(timePickEntityList.get(0).getYearDate());
        binding.day.setWheelData(timePickEntityList.get(0).getYearDate().get(0).getMonthDate());


        binding.ytd.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                yearPosition = position;
                monthPosition = monthPosition < timePickEntityList.get(yearPosition).getYearDate().size() ? monthPosition : 0;

                binding.month.setWheelData(timePickEntityList.get(yearPosition).getYearDate());
                binding.day.setWheelData(timePickEntityList.get(yearPosition).getYearDate().get(monthPosition).getMonthDate());

                monthAdapter.notifyDataSetChanged();
                dayAdapter.notifyDataSetChanged();
            }
        });

        binding.month.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                monthPosition = position;
                dayPosition = dayPosition < timePickEntityList.get(yearPosition).getYearDate().get(monthPosition).getMonthDate().size()
                        ? dayPosition : 0;

                binding.day.setWheelData(timePickEntityList.get(yearPosition).getYearDate().get(monthPosition).getMonthDate());

                dayAdapter.notifyDataSetChanged();
            }
        });

        binding.day.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                dayPosition = position;
            }
        });


    }

    @Override
    public void initViewObservable() {
        viewModel.uc.ucClose.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                dismiss();
            }
        });

        viewModel.uc.ucConfirm.observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                dismiss();

                StringBuilder builder = new StringBuilder();
                builder.append(timePickEntityList.get(yearPosition).getYear())
                        .append("-")
                        .append(timePickEntityList.get(yearPosition).getYearDate().get(monthPosition).getMonth())
                        .append("-")
                        .append(timePickEntityList.get(yearPosition).getYearDate().get(monthPosition).getMonthDate().get(dayPosition).getDay());


                EventBus.getDefault().post(new MessageEvent(MessageEvent.MSG_CHARTTER_TODAY_INCOME_CODE, builder.toString()));


            }
        });
    }

    public class YearWheelAdapter extends BaseWheelAdapter<SelectIncomeTimePickEntity> {

        @Override
        protected View bindView(int position, View convertView, ViewGroup parent) {
            String year = mList.get(position).getYear();
            View view = View.inflate(getContext(), R.layout.item_wheel_view, null);
            TextView textView = view.findViewById(R.id.textview);
            textView.setText(year + "年");
            return view;
        }
    }

    public class MonthWheelAdapter extends BaseWheelAdapter<SelectIncomeTimePickEntity.MonthDate> {

        @Override
        protected View bindView(int position, View convertView, ViewGroup parent) {
            String month = mList.get(position).getMonth();
            View view = View.inflate(getContext(), R.layout.item_wheel_view, null);
            TextView textView = view.findViewById(R.id.textview);
            textView.setText(month + "月");
            return view;
        }
    }

    public class DayWheelAdapter extends BaseWheelAdapter<SelectIncomeTimePickEntity.DayDate> {

        @Override
        protected View bindView(int position, View convertView, ViewGroup parent) {
            String day = mList.get(position).getDay();
            View view = View.inflate(getContext(), R.layout.item_wheel_view, null);
            TextView textView = view.findViewById(R.id.textview);
            textView.setText(day + "日");
            return view;
        }
    }

    public void getDayList(int start, int listSize, List<SelectIncomeTimePickEntity.DayDate> dayDateList, List<SelectIncomeTimePickEntity.MonthDate> monthDateList, int month) {
        for (int j = start; j <= listSize; j++) {
            dayDateList.add(new SelectIncomeTimePickEntity.DayDate(String.valueOf(j)));
        }
        monthDateList.add(new SelectIncomeTimePickEntity.MonthDate(String.valueOf(month), dayDateList));
    }


    public int getDayNumber(int year, int month) {
        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
            return 31;
        } else if (month == 4 || month == 6 || month == 9 || month == 11) {

            return 30;
        } else if (month == 2) {

            return isLeapYear(year) ? 29 : 28;


        }
        return 0;
    }


    public boolean isLeapYear(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, 1, 28);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        return calendar.get(Calendar.DAY_OF_MONTH) == 29;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        activity = null;
//        viewModel = null;
    }
}
