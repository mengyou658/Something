package com.moons.xst.track.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.moons.xst.buss.ComDBHelper;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.adapter.TempTasklistAdapter;
import com.moons.xst.track.bean.GPSXXTempTask;
import com.moons.xst.track.common.UIHelper;

public class TempTaskList extends BaseActivity{

	private TextView titleText;

	private ImageButton returnButton;
	private ListView taskListView;

	private List<GPSXXTempTask> tempTasks = new ArrayList<GPSXXTempTask>();
	private TempTasklistAdapter dataAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.temptasklist);
		init();
		titleText.setText(R.string.temptask_query_title);
		tempTasks = ComDBHelper.GetIntance().loadempTasks(this);
		 if(tempTasks == null )
		 {
			 UIHelper.ToastMessage(this, R.string.query_nullmsg);
			 return;
		 }
		Collections.reverse(tempTasks);  
		dataAdapter = new TempTasklistAdapter(this, tempTasks,
				R.layout.listitem_temptask);
		taskListView.setAdapter(dataAdapter);
		taskListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				TextView aTitle = (TextView) view
						.findViewById(R.id.tt_list_listitem_title);
				if (aTitle == null) {
					return;
				}
				final GPSXXTempTask tempTask = (GPSXXTempTask) aTitle.getTag();

				if (tempTask == null)
					return;
				if (tempTask.getComplete_dt() ==null) {
					AppContext.nowTempTask.clear();
					AppContext.nowTempTask.add(tempTask);
					UIHelper.showTempTask(TempTaskList.this);
				}

			}
		});
		returnButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				AppManager.getAppManager().finishActivity(TempTaskList.this);
			}
		});
	}

	/**
	 * 初始化控件
	 */
	private void init() {
		titleText = (TextView) findViewById(R.id.tt_lista_head_title);
		returnButton = (ImageButton) findViewById(R.id.tt_list_head_Rebutton);
		taskListView = (ListView) findViewById(R.id.tt_list_listview);
	}

}
