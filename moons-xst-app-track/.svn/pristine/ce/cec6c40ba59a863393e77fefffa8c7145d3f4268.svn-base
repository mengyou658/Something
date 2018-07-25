package com.moons.xst.track.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.baidu.navisdk.util.common.StringUtils;
import com.moons.xst.buss.cycle.Cycle;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.R;
import com.moons.xst.track.adapter.SimpleSpinnerAdapter;
import com.moons.xst.track.common.DateTimeHelper;

public class CalcCycle extends Activity {

	private TextView view, resultTextView;
	private EditText datetimeEditText;
	private Spinner spinner;
	private SimpleSpinnerAdapter adapter;
	private Button calacButton;

	Cycle _cycle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calc_cycle);
		datetimeEditText = (EditText) findViewById(R.id.calac_datatime);
		datetimeEditText.setText(DateTimeHelper.getDateTimeNow());
		view = (TextView) findViewById(R.id.spinnerText);
		resultTextView = (TextView) findViewById(R.id.calac_result);
		calacButton = (Button) findViewById(R.id.btn_calac);
		calacButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				if (_cycle != null
						&& StringUtils.isNotEmpty(String
								.valueOf(datetimeEditText.getText())))
					AppContext.calculateCycleService.calculate(_cycle,
							DateTimeHelper.StringToDate(String
									.valueOf(datetimeEditText.getText())),
							false);
				String beginString = "";
				String endString = "";
				String startTimeString = "";
				String endTimeString = "";
				if (_cycle.getTaskBegin() != null)
					beginString = DateTimeHelper.DateToString(_cycle
							.getTaskBegin());
				if (_cycle.getTaskEnd() != null)
					endString = DateTimeHelper.DateToString(_cycle.getTaskEnd());
				if (StringUtils.isNotEmpty(_cycle.getStartTime()))
					startTimeString = _cycle.getStartTime();
				if (StringUtils.isNotEmpty(_cycle.getEndTime()))
					endTimeString = _cycle.getEndTime();

				resultTextView.setText("TaskBegin：" + beginString
						+ "\n TaskEnd" + endString + "\n Start：" + beginString
						+ "\n End:" + endString);
			}
		});
		spinner = (Spinner) findViewById(R.id.Cycle_Spinner);
		List<List<String>> data = new ArrayList<List<String>>();
		for (Cycle _item : AppContext.DJCycleBuffer) {
			List<String> _temList = new ArrayList<String>();
			_temList.add(_item.getDJCycle().getCycle_ID());// ID
			_temList.add(_item.getDJCycle().getName_TX());// Name
			_temList.add(_item.getDJCycle().getBoundType_CD());
			data.add(_temList);
		}
		adapter = new SimpleSpinnerAdapter(CalcCycle.this, data,
				R.layout.listitem_spinner);
		// 将adapter 添加到spinner中
		spinner.setAdapter(adapter);

		// 添加事件Spinner事件监听
		spinner.setOnItemSelectedListener(new SpinnerSelectedListener());

		// 设置默认值
		spinner.setVisibility(View.VISIBLE);

	}

	// 使用数组形式操作
	class SpinnerSelectedListener implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			_cycle = AppContext.DJCycleBuffer.get(arg2);
			view.setText("CycleID:" + _cycle.getDJCycle().getCycle_ID()
					+ ";CycleName:" + _cycle.getDJCycle().getName_TX());
		}

		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}
}
