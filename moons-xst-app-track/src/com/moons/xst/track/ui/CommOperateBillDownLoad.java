package com.moons.xst.track.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.moons.xst.track.AppContext;
import com.moons.xst.track.AppException;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.adapter.Comm_OperateBillInfo_ListViewAdapter;
import com.moons.xst.track.bean.CheckPointInfo;
import com.moons.xst.track.bean.OperateBillBaseInfo;
import com.moons.xst.track.common.InputTools;
import com.moons.xst.track.common.NetWorkHelper;
import com.moons.xst.track.common.StringUtils;
import com.moons.xst.track.common.UIHelper;
import com.moons.xst.track.communication.DownLoad;
import com.moons.xst.track.communication.OperateBillDownLoad;
import com.moons.xst.track.communication.WebserviceFactory;
import com.moons.xst.track.widget.LoadingDialog;

public class CommOperateBillDownLoad extends BaseActivity {

	TextView tv_selectedAll;
	ListView lv_operateBillInfo;
	ImageButton returnButton;
	Button btn_download;
	EditText etcontent;

	private LoadingDialog loading;
	private Handler mLoadHandler;
	private Thread loadOperateBillInfoThread;

	Comm_OperateBillInfo_ListViewAdapter mAdapter;
	private boolean downloading = false;
	List<OperateBillBaseInfo> billlist = new ArrayList<OperateBillBaseInfo>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comm_download_operatebill);

		init();
		try {
			loadOperateBillThread();
		} catch (AppException e) {
		}
		loadOperateBillToListViewHandler();
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	protected void onDestroy() {
		if (loading != null)
			loading.dismiss();
		super.onDestroy();
	}

	private void init() {
		etcontent = (EditText) findViewById(R.id.search_content);
		etcontent.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					InputTools.HideKeyboard(etcontent);
				}
				return false;
			}
		});
		etcontent.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}

			public void afterTextChanged(Editable s) {
				mHandler.post(eChanged);
			}
		});
		tv_selectedAll = (TextView) findViewById(R.id.tv_comm_download_operatebill_selectall);
		tv_selectedAll.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				if (tv_selectedAll.getText().equals(
						getResources().getString(
								R.string.commu_download_operatebill_selectall))) {
					tv_selectedAll
							.setText(R.string.commu_download_operatebill_unselectall);
					for (OperateBillBaseInfo info : AppContext.operateBillBaseInfoBuffer) {
						info.setChecked("true");
					}
				} else {
					tv_selectedAll
							.setText(R.string.commu_download_operatebill_selectall);
					for (OperateBillBaseInfo info : AppContext.operateBillBaseInfoBuffer) {
						info.setChecked("false");
					}
				}
				if (mAdapter != null)
					mAdapter.notifyDataSetChanged();
			}
		});

		btn_download = (Button) findViewById(R.id.btn_comm_download_operatebill);
		btn_download.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				if (!NetWorkHelper.isNetworkAvailable(getBaseContext())) {
					UIHelper.ToastMessage(getBaseContext(),
							R.string.network_not_connected);
					return;
				}

				boolean isSelectedAll = true;
				StringBuilder sb = new StringBuilder();
				if (mAdapter != null) {
					List<OperateBillBaseInfo> list = mAdapter.getlist();
					if (list != null && list.size() > 0) {
						for (OperateBillBaseInfo info : list) {
							if (StringUtils.toBool(info.getChecked())) {
								sb.append(info.getOperateBill_Code());
								sb.append(",");
							} else {
								sb.append("");
								isSelectedAll = false;
							}
						}
					}
				}

				if (sb.toString().trim().length() <= 0) {
					UIHelper.ToastMessage(CommOperateBillDownLoad.this,
							R.string.cumm_twobill_chooseczp_promptinfo);
					return;
				} else {
					String operateBillCodes = "";
					// if (!isSelectedAll) {
					operateBillCodes = sb.toString().substring(0,
							sb.toString().length() - 1);
					// }

					showNoticeDialog(operateBillCodes);
				}
			}
		});

		returnButton = (ImageButton) findViewById(R.id.home_head_Rebutton);
		returnButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				if (!downloading)
					AppManager.getAppManager().finishActivity(
							CommOperateBillDownLoad.this);
			}
		});
	}

	static Handler mHandler = new Handler();

	Runnable eChanged = new Runnable() {

		@Override
		public void run() {

			// TODO Auto-generated method stub
			String data = etcontent.getText().toString();
			billlist.clear();
			getmDataSub(billlist, data);
			mAdapter.notifyDataSetChanged();

		}
	};

	private void getmDataSub(List<OperateBillBaseInfo> mDataSubs, String data) {
		for (int i = 0; i < AppContext.operateBillBaseInfoBuffer.size(); ++i) {
			if (AppContext.operateBillBaseInfoBuffer.get(i)
					.getOperateBill_Content().contains(data)) {
				mDataSubs.add(AppContext.operateBillBaseInfoBuffer.get(i));
			}
		}
	}

	/**
	 * 加载可下载的操作票线程
	 * 
	 * @throws AppException
	 */
	private void loadOperateBillThread() throws AppException {
		if (!NetWorkHelper.isNetworkAvailable(getBaseContext())) {
			UIHelper.ToastMessage(getBaseContext(),
					R.string.network_not_connected);
			return;
		}
		loading = new LoadingDialog(this);
		loading.setCanceledOnTouchOutside(false);
		loading.show();
		loadOperateBillInfoThread = new Thread() {
			public void run() {
				Message msg = Message.obtain();
				if (!WebserviceFactory.checkWS()) {
					msg = Message.obtain();
					msg.what = -1;
					msg.obj = getString(R.string.cumm_twobill_webcheckPromptinfo);
					mLoadHandler.sendMessage(msg);
				} else {
					try {
						// 加载数据
						loadOperateBillListData();
						Thread.sleep(1000);
						msg = Message.obtain();
						msg.what = 1;
						msg.obj = AppContext.operateBillBaseInfoBuffer;
						mLoadHandler.sendMessage(msg);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						msg = Message.obtain();
						msg.what = -1;
						msg.obj = e;
						mLoadHandler.sendMessage(msg);
					}
				}

			}
		};
		loadOperateBillInfoThread.start();
	}

	private void loadOperateBillListData() {
		try {
			// 加载数据
			if (Thread.currentThread().isInterrupted())
				return;
			Context context = getBaseContext();
			DownLoad downLoad = new DownLoad(context);
			downLoad.DownLoadOperateBillInfoFromWS(context);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 加载路线Hander初始化
	 */
	private void loadOperateBillToListViewHandler() {
		mLoadHandler = new Handler() {
			public void handleMessage(Message msg) {
				if (loading != null)
					loading.dismiss();
				if (msg.what == 1 && msg.obj != null) {
					bindingOperateBillData();// 获取的数据绑定到列表中
				} else if (msg.what == -1 && msg.obj != null) {
					String msgString = String.valueOf(msg.obj);
					UIHelper.ToastMessage(getBaseContext(), msgString);
				}
			}
		};
	}

	/**
	 * 绑定到列表
	 */
	private void bindingOperateBillData() {
		billlist.clear();
		for (int i = 0; i < AppContext.operateBillBaseInfoBuffer.size(); i++) {
			billlist.add(AppContext.operateBillBaseInfoBuffer.get(i));
		}
		mAdapter = new Comm_OperateBillInfo_ListViewAdapter(this, billlist,
				R.layout.listitem_download_operatebill);
		lv_operateBillInfo = (ListView) findViewById(R.id.lv_comm_download_operatebill);
		lv_operateBillInfo.setAdapter(mAdapter);
	}

	private void showNoticeDialog(final String billCodes) {

		LayoutInflater factory = LayoutInflater.from(this);
		final View view = factory.inflate(R.layout.textview_layout, null);
		new com.moons.xst.track.widget.AlertDialog(this)
				.builder()
				.setTitle(getString(R.string.cumm_twobill_diatitle_warninfo))
				.setView(view)
				.setMsg(getString(R.string.cumm_twobill_diamsg_warninfo))
				.setPositiveButton(getString(R.string.sure),
						new android.view.View.OnClickListener() {
							@Override
							public void onClick(View v) {
								OperateBillDownLoad.getOperateBillDownLoad()
										.encodeAndDownloadOperateBill(
												CommOperateBillDownLoad.this,
												billCodes, true);

							}
						})
				.setNegativeButton(getString(R.string.cancel),
						new android.view.View.OnClickListener() {
							@Override
							public void onClick(View v) {
							}
						}).show();

	}
}