package com.ymx.driver.ui.chartercar.frament;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.lifecycle.Observer;

import com.ymx.driver.R;
import com.ymx.driver.base.BaseDialogFragment;
import com.ymx.driver.config.MessageEvent;
import com.ymx.driver.databinding.DialogCharterFragmentTimePickBinding;
import com.ymx.driver.entity.app.TimePickEntity;
import com.ymx.driver.util.DateUtils;
import com.ymx.driver.util.UIUtils;
import com.ymx.driver.view.wheelview.adapter.BaseWheelAdapter;
import com.ymx.driver.view.wheelview.widget.WheelView;
import com.ymx.driver.viewmodel.main.TimePickViewModel;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
// 时间弹窗待优化 抽象BaseTimeTimePick 数据加载子类具体实现
public class ChatterDrivingTimePickDialogFragment extends BaseDialogFragment<DialogCharterFragmentTimePickBinding, TimePickViewModel> {

    public static final String TIME = "select_time";
    public static final String NEXT_DAY_TYPE = "next_day_type";
    private List<TimePickEntity> timePickEntityList = new ArrayList<>();
    private BaseWheelAdapter hourAdapter;
    private BaseWheelAdapter minuteAdapter;
    private int datePosition;
    private int hourPosition;
    private int minutePosition;
    private String selectTime;
    private int nextDayType;

    public static ChatterDrivingTimePickDialogFragment newInstance() {
        return newInstance(null);
    }

    public static ChatterDrivingTimePickDialogFragment newInstance(Bundle bundle) {
        ChatterDrivingTimePickDialogFragment fragment = new ChatterDrivingTimePickDialogFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int initLayoutId(Bundle savedInstanceState) {
        return R.layout.dialog_charter_fragment_time_pick;
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

        if (getArguments() != null) {
            Bundle bundle = getArguments();
            selectTime = bundle.getString(TIME);
            nextDayType = bundle.getInt(NEXT_DAY_TYPE);
            if (nextDayType==2){
                binding.timeTv.setText("请选择接驾时间");
            }else {
                binding.timeTv.setText("请确认明天上车时间");
            }

        } else {




        }


        binding.ytd.setWheelAdapter(new YtdWheelAdapter());
        binding.ytd.setWheelSize(5);
        binding.ytd.setSelection(0);
        binding.ytd.setSkin(WheelView.Skin.Holo);
        WheelView.WheelViewStyle style = new WheelView.WheelViewStyle();
        style.holoBorderColor = UIUtils.getColor(R.color.rx_e1e1e1);
        style.holoBorderWidth = 1;
        binding.ytd.setStyle(style);

        binding.hour.setWheelAdapter(hourAdapter = new HourWheelAdapter());
        binding.hour.setWheelSize(5);
        binding.hour.setSelection(0);
        binding.hour.setSkin(WheelView.Skin.Holo);
        binding.hour.setStyle(style);

        binding.minute.setWheelAdapter(minuteAdapter = new MinuteWheelAdapter());
        binding.minute.setWheelSize(5);
        binding.minute.setSelection(0);
        binding.minute.setSkin(WheelView.Skin.Holo);
        binding.minute.setStyle(style);

        /*yyyy-MM-dd HH:mm*/
        // 今天
        String currentDate = selectTime.substring(0, 17);

//        currentDate = addDateMinut(currentDate, 60);


        int hour = Integer.parseInt(currentDate.substring(11, 13));
        int minute = Integer.parseInt(currentDate.substring(14, 16));

        List<TimePickEntity.HourDate> hourList1 = new ArrayList<>();
        for (int j = minute >= 50 ? hour + 1 : hour; j <= 23; j++) {
            List<TimePickEntity.MinuteDate> minuteList = new ArrayList<>();
            for (int k = j > hour || minute >= 50 ? 0 : Integer.parseInt(currentDate.substring(14, 16).substring(0, 1)) + 1; k <= 5; k++) {
                minuteList.add(new TimePickEntity.MinuteDate(((k * 10) < 10 ? "0" + (k * 10) : "" + (k * 10))));
            }

            hourList1.add(new TimePickEntity.HourDate((j < 10 ? "0" + j : "" + j), minuteList));
        }

        TimePickEntity timePickEntity1 = new TimePickEntity(currentDate.substring(0, 10), hourList1);
        timePickEntityList.add(timePickEntity1);

        // 过后的两天
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < (nextDayType == 2 ? 2 : nextDayType == 1 ? 1 : 0); i++) {
            if (nextDayType == 1||nextDayType == 2) {
                c.add(Calendar.DAY_OF_MONTH, 1);
            }

            String date = sim.format(c.getTime());

            List<TimePickEntity.MinuteDate> minuteList = new ArrayList<>();
            for (int j = 0; j <= 5; j++) {
                minuteList.add(new TimePickEntity.MinuteDate(((j * 10) < 10 ? "0" + (j * 10) : "" + (j * 10))));
            }

            List<TimePickEntity.HourDate> hourList = new ArrayList<>();
            for (int j = 0; j <= 23; j++) {
                hourList.add(new TimePickEntity.HourDate((j < 10 ? "0" + j : "" + j), minuteList));
            }

            TimePickEntity timePickEntity = new TimePickEntity(date, hourList);
            timePickEntityList.add(timePickEntity);
        }

        binding.ytd.setWheelData(timePickEntityList);
        binding.hour.setWheelData(timePickEntityList.get(0).getHourDate());
        binding.minute.setWheelData(timePickEntityList.get(0).getHourDate().get(0).getMinuteDate());

        binding.ytd.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                datePosition = position;
                hourPosition = hourPosition < timePickEntityList.get(datePosition).getHourDate().size() ? hourPosition : 0;

                binding.hour.setWheelData(timePickEntityList.get(datePosition).getHourDate());
                binding.minute.setWheelData(timePickEntityList.get(datePosition).getHourDate().get(hourPosition).getMinuteDate());

                hourAdapter.notifyDataSetChanged();
                minuteAdapter.notifyDataSetChanged();
            }
        });

        binding.hour.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                hourPosition = position;
                minutePosition = minutePosition < timePickEntityList.get(datePosition).getHourDate().get(hourPosition).getMinuteDate().size()
                        ? minutePosition : 0;

                binding.minute.setWheelData(timePickEntityList.get(datePosition).getHourDate().get(hourPosition).getMinuteDate());
                minuteAdapter.notifyDataSetChanged();
            }
        });

        binding.minute.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                minutePosition = position;
            }
        });
    }

    public class YtdWheelAdapter extends BaseWheelAdapter<TimePickEntity> {

        @Override
        protected View bindView(int position, View convertView, ViewGroup parent) {
            StringBuilder builder = new StringBuilder();
            String date = mList.get(position).getDate();

            String month = date.substring(5, 7).startsWith("0") ? date.substring(6, 7) : date.substring(5, 7);
            String day = date.substring(8, 10).startsWith("0") ? date.substring(9, 10) : date.substring(8, 10);
            builder.append(month).append(UIUtils.getString(R.string.month))
                    .append(day).append(UIUtils.getString(R.string.day))

                    .append(" ").append(DateUtils.getDayString(DateUtils.getHourDate(date + " 09:00:00")));
            View view = View.inflate(activity, R.layout.item_wheel_view, null);
            TextView textView = view.findViewById(R.id.textview);
            textView.setText(builder.toString());
            return view;
        }
    }

    public class HourWheelAdapter extends BaseWheelAdapter<TimePickEntity.HourDate> {

        @Override
        protected View bindView(int position, View convertView, ViewGroup parent) {
            String hour = mList.get(position).getHour();
            View view = View.inflate(activity, R.layout.item_wheel_view, null);
            TextView textView = view.findViewById(R.id.textview);
            textView.setText(hour + getString(R.string.track_hour));
            return view;
        }
    }

    public class MinuteWheelAdapter extends BaseWheelAdapter<TimePickEntity.MinuteDate> {

        @Override
        protected View bindView(int position, View convertView, ViewGroup parent) {
            String minute = mList.get(position).getMinute();
            View view = View.inflate(activity, R.layout.item_wheel_view, null);
            TextView textView = view.findViewById(R.id.textview);
            textView.setText(minute + getString(R.string.track_minute));
            return view;
        }
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
                try {
                    dismiss();
                    StringBuilder builder = new StringBuilder();
                    builder.append(timePickEntityList.get(datePosition).getDate())
                            .append(" ")
                            .append(timePickEntityList.get(datePosition).getHourDate().get(hourPosition).getHour())
                            .append(":")
                            .append(timePickEntityList.get(datePosition).getHourDate().get(hourPosition).getMinuteDate().get(minutePosition).getMinute()).append(":00");


                    EventBus.getDefault().post(new MessageEvent(MessageEvent.MSG_GOTO_CHARTTER_SELECT_TIME_CODE, builder.toString()));
                }catch (Exception e){

                }


            }
        });
    }


}


