/**
 * 
 */
package com.moons.xst.track.pad_ui.fragement;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.moons.xst.track.AppContext;
import com.moons.xst.track.AppException;
import com.moons.xst.track.R;
import com.moons.xst.track.adapter.pad_adapter.Pad_HomeMesAdapter;
import com.moons.xst.track.adapter.pad_adapter.Pad_HomeToDoAdapter;
import com.moons.xst.track.bean.HomeMessageInfo;
import com.moons.xst.track.bean.ToDoThings;
import com.moons.xst.track.common.DateTimeHelper;
import com.moons.xst.track.common.StringUtils;
import com.moons.xst.track.ui.CaptureActivity;
import com.moons.xst.track.widget.LoadingDialog;
import com.moons.xst.track.xstinterface.Pad_MenuChangeListener;

/**
 * @author LKZ
 * 
 */
public class Pad_HomeMainFragment extends Fragment {

	private LoadingDialog loading;
	private Handler mHandler;

	View mainLayout;
	ListView messageLV, toDoLV;
	Pad_HomeMesAdapter mesAdapter;
	Pad_HomeToDoAdapter toDoAdapter;
	ImageView sbdaScanImageView;

	Pad_MenuChangeListener menuChangeListener;

	private final static int REQUEST_CODE = 2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
	}

	/**
	 * 设置MenuChange监听
	 * 
	 * @param listener
	 */
	public void setMenuChangeListener(Pad_MenuChangeListener listener) {
		menuChangeListener = listener;
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO 自动生成的方法存根
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mainLayout = inflater.inflate(R.layout.fragment_home_main, container,
				false);
		initView();
		loadDataToUI();
		return mainLayout;
	}

	@Override
	public void onDestroyView() {
		// TODO 自动生成的方法存根
		super.onDestroyView();
	}

	@Override
	public void onDetach() {
		// TODO 自动生成的方法存根
		super.onDetach();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE) {
			if (data != null) {
				Bundle bundle = data.getExtras();
				String codeReust = bundle.getString("codeResult");
				if (!StringUtils.isEmpty(codeReust)) {
					// TODO 扫码成功后的操作
					getSBDAInfo(codeReust);
				}
			}
		}
	}

	private void initView() {
		sbdaScanImageView = (ImageView) mainLayout
				.findViewById(R.id.pad_home_main_xj);
		sbdaScanImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Intent intent = new Intent();
				intent.setClass(getActivity(), CaptureActivity.class);
				intent.putExtra("ScanType", "EDITTEXT");
				startActivityForResult(intent, REQUEST_CODE);
			}
		});
		messageLV = (ListView) mainLayout
				.findViewById(R.id.home_main_content_right_view1_messagelist);
		toDoLV = (ListView) mainLayout
				.findViewById(R.id.home_main_content_left_view1_todolist);
	}

	/**
	 * 新相关数据
	 */
	private void loadDataToUI() {
		mHandler = new Handler() {
			public void handleMessage(Message msg) {
				if (loading != null)
					loading.dismiss();
				if (msg.what == 1 && msg.obj != null) {
					bindingData();// 获取的数据绑定到列表中
				} else if (msg.obj != null) {
					((AppException) msg.obj).makeToast(getActivity());
				}
			}
		};
		this.loadUserDJLineThread(false);
	}

	private void loadUserDJLineThread(final boolean isRefresh) {
		loading = new LoadingDialog(getActivity());
		loading.show();

		new Thread() {
			public void run() {
				Message msg = Message.obtain();
				try {
					// 重新加载路线
					loadData();
					Thread.sleep(1000);// debug
					msg.what = 1;
					msg.obj = AppContext.DJLineBuffer;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					msg.what = -1;
					msg.obj = e;
					if (loading != null)
						loading.dismiss();
				}
				mHandler.sendMessage(msg);
			}
		}.start();
	}

	private void loadData() {
		AppContext.todoDataBuffer.clear();
		ToDoThings _Things = new ToDoThings();
		_Things.setToDoThingsID(1);
		_Things.setToDoThingsDesc("点检任务：一号机组巡检路线（周期：每天一次）");
		_Things.setTodoThingsTime(DateTimeHelper.getDateTimeNow());
		_Things.setThingsType("SH");
		AppContext.todoDataBuffer.add(_Things);
		_Things = new ToDoThings();
		_Things.setToDoThingsID(2);
		_Things.setToDoThingsDesc("工单编号为：GD-1#GL-CS-QJ-002的工单需审核");
		_Things.setTodoThingsTime(DateTimeHelper.getDateTimeNow());
		_Things.setThingsType("SH");
		AppContext.todoDataBuffer.add(_Things);
		_Things = new ToDoThings();
		_Things.setToDoThingsID(3);
		_Things.setToDoThingsDesc("缺陷编号为：QX-1#GL-CS-QJ-0011的缺陷需处理");
		_Things.setTodoThingsTime(DateTimeHelper.getDateTimeNow());
		_Things.setThingsType("SH");
		AppContext.todoDataBuffer.add(_Things);
		_Things = new ToDoThings();
		_Things.setToDoThingsID(4);
		_Things.setToDoThingsDesc("缺陷编号为：QX-1#GL-CS-QJ-0012的缺陷需处理");
		_Things.setTodoThingsTime(DateTimeHelper.getDateTimeNow());
		_Things.setThingsType("SH");
		AppContext.todoDataBuffer.add(_Things);
		_Things = new ToDoThings();
		_Things.setToDoThingsID(5);
		_Things.setToDoThingsDesc("缺陷编号为：QX-1#GL-CS-QJ-0013的缺陷需处理");
		_Things.setTodoThingsTime(DateTimeHelper.getDateTimeNow());
		_Things.setThingsType("SH");
		AppContext.todoDataBuffer.add(_Things);
		_Things = new ToDoThings();
		_Things.setToDoThingsID(6);
		_Things.setToDoThingsDesc("二号机组巡检路线（周期：每天一次，工作日有效）");
		_Things.setTodoThingsTime(DateTimeHelper.getDateTimeNow());
		_Things.setThingsType("SH");
		AppContext.todoDataBuffer.add(_Things);
		_Things = new ToDoThings();
		_Things.setToDoThingsID(7);
		_Things.setToDoThingsDesc("工单编号为：GD-1#GL-CS-QJ-008的工单需审核");
		_Things.setTodoThingsTime(DateTimeHelper.getDateTimeNow());
		_Things.setThingsType("SH");
		AppContext.todoDataBuffer.add(_Things);
		_Things = new ToDoThings();
		_Things.setToDoThingsID(8);

		AppContext.homeMessageBuffer.clear();
		HomeMessageInfo _message = new HomeMessageInfo();
		_message.setMessageID(1);
		_message.setMessageDesc("鸣志首届研讨会在意大利成功举行");
		_message.setMessageTime(DateTimeHelper.getDateTimeNow());
		_message.setmessageType("SH");
		AppContext.homeMessageBuffer.add(_message);
		_message = new HomeMessageInfo();
		_message.setMessageID(2);
		_message.setMessageDesc("端午节放假通知");
		_message.setMessageTime(DateTimeHelper.getDateTimeNow());
		_message.setmessageType("SH");
		AppContext.homeMessageBuffer.add(_message);
		_message = new HomeMessageInfo();
		_message.setMessageID(3);
		_message.setMessageDesc("电器消防演练通知");
		_message.setMessageTime(DateTimeHelper.getDateTimeNow());
		_message.setmessageType("SH");
		AppContext.homeMessageBuffer.add(_message);
		_message = new HomeMessageInfo();
		_message.setMessageID(4);
		_message.setMessageDesc("小神探新品发布会");
		_message.setMessageTime(DateTimeHelper.getDateTimeNow());
		_message.setmessageType("SH");
		AppContext.homeMessageBuffer.add(_message);
		_message = new HomeMessageInfo();
		_message.setMessageID(5);
		_message.setMessageDesc("鸣志参加华南国际工业展");
		_message.setMessageTime(DateTimeHelper.getDateTimeNow());
		_message.setmessageType("SH");
		AppContext.homeMessageBuffer.add(_message);
		_message = new HomeMessageInfo();
		_message.setMessageID(6);
		_message.setMessageDesc("消息");
		_message.setMessageTime(DateTimeHelper.getDateTimeNow());
		_message.setmessageType("SH");
		AppContext.homeMessageBuffer.add(_message);
		_message = new HomeMessageInfo();
		_message.setMessageID(7);
		_message.setMessageDesc("消息");
		_message.setMessageTime(DateTimeHelper.getDateTimeNow());
		_message.setmessageType("SH");
		AppContext.homeMessageBuffer.add(_message);
		_message = new HomeMessageInfo();
		_message.setMessageID(8);
		_message.setMessageDesc("消息");
		_message.setMessageTime(DateTimeHelper.getDateTimeNow());
		_message.setmessageType("SH");
		AppContext.homeMessageBuffer.add(_message);
		_message = new HomeMessageInfo();
		_message.setMessageID(9);
		_message.setMessageDesc("消息");
		_message.setMessageTime(DateTimeHelper.getDateTimeNow());
		_message.setmessageType("SH");
		AppContext.homeMessageBuffer.add(_message);
		_message = new HomeMessageInfo();
		_message.setMessageID(10);
		_message.setMessageDesc("消息");
		_message.setMessageTime(DateTimeHelper.getDateTimeNow());
		_message.setmessageType("SH");
		AppContext.homeMessageBuffer.add(_message);
		_message = new HomeMessageInfo();
		_message.setMessageID(1);
		_message.setMessageDesc("消息");
		_message.setMessageTime(DateTimeHelper.getDateTimeNow());
		_message.setmessageType("SH");
		AppContext.homeMessageBuffer.add(_message);
	}

	private void bindingData() {
		toDoAdapter = new Pad_HomeToDoAdapter(getActivity(),
				AppContext.todoDataBuffer, R.layout.pad_listitem_todo);
		toDoLV.setAdapter(toDoAdapter);
		toDoLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

			}
		});

		mesAdapter = new Pad_HomeMesAdapter(getActivity(),
				AppContext.homeMessageBuffer, R.layout.pad_listitem_mes);
		messageLV.setAdapter(mesAdapter);
		messageLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

			}
		});
	}

	/**
	 * 根据查询条件获取设备档案信息
	 */
	private void getSBDAInfo(String caseString) {
		if (menuChangeListener != null) {
			menuChangeListener.redirectToSBDA(caseString);
		}
	}
}
