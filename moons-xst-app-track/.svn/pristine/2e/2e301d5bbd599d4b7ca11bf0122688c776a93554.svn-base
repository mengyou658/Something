package com.moons.xst.track.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.AppException;
import com.moons.xst.track.AppManager;
import com.moons.xst.track.R;
import com.moons.xst.track.adapter.SettingConfigAdapter;
import com.moons.xst.track.bean.SettingConfigInfo;
import com.moons.xst.track.common.UIHelper;
import com.moons.xst.track.communication.UploadFile;
import com.moons.xst.track.widget.LoadingDialog;

public class SystemSettingAuthorityAty extends BaseActivity {
	String[] types = { "用户可见设置", "用户不可见设置", "模块设置" };
	List<SettingConfigInfo> myList = new ArrayList<SettingConfigInfo>();
	SettingConfigAdapter mAdapter;
	Handler mHandler;
	ListView lv;
	ImageButton btn;
	// 加载控件
	private LoadingDialog loading, saving;

	List<String[]> list = new ArrayList<String[]>();
	String[] strs;

	String getUrlPath = AppContext.GetWSAddress().replaceAll("wsmemscomm.asmx",
			"Moons/APInfo/SettingConfig.xml");
	String postUrlPath = AppContext.GetWSAddress().replaceAll(
			"wsmemscomm.asmx", "Moons/APInfo/");
	String filepath = AppConst.XSTBasePath() + "TempSettingAuth.xml";
	private TextView saveButton;
	private static final int TIMEOUT = 10000;// 10秒
	private AlertDialog alertDialog;
	private String[] chooses = { "TwoChoose", "ThreeChoose" };
	private String typeToChoose = "TwoChoose";
	private com.moons.xst.track.widget.AlertDialog dialog;
	private int index;
	private String[] twoChooses;
	private String[] threeChooses;
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (saving != null)
				saving.dismiss();
			if (msg.what == 1) {
				UIHelper.ToastMessage(SystemSettingAuthorityAty.this,
						getString(R.string.initcp_message_savesucceed));
			}else if(msg.what==-1){
				UIHelper.ToastMessage(SystemSettingAuthorityAty.this,
						getString(R.string.initcp_message_savedefeated));
			}
		};
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.settingauthority);
		
		File file = new File(filepath);
		if (file.exists()) {
			file.delete();
		}
		
		lv = (ListView) findViewById(R.id.listview_settingauth);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				if (myList.get(position).getInfo()[0].equals("UserLoginType")) {
					typeToChoose = chooses[1];
				} else {
					typeToChoose = chooses[0];
				}
				showsDialog(position, typeToChoose);
			}
		});
		saveButton = (TextView) findViewById(R.id.home_head_savebutton);
		saveButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (saving == null || !saving.isShowing()) {
					saving = new LoadingDialog(SystemSettingAuthorityAty.this);
					saving.setLoadText(getString(R.string.save_ing));
					saving.setCancelable(true);
					saving.show();
				}
				new Thread(new Runnable() {					
					@Override
					public void run() {
						try {
							boolean isFinish = UploadFile.getIntance()
									.UploadFileByWeb(filepath,
											"SettingConfig.xml",
											"SETTINGCONFIG");
							if (isFinish) {
								handler.obtainMessage(1).sendToTarget();
							}else {
								handler.obtainMessage(-1).sendToTarget();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}).start();

			}
		});
		btn = (ImageButton) findViewById(R.id.home_head_Rebutton);
		btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				AppManager.getAppManager().finishActivity(
						SystemSettingAuthorityAty.this);
			}
		});
		getSettingAuthConfig();
	}
	
	@Override
	protected void onDestroy() {
		File file = new File(filepath);
		if (file.exists()) {
			file.delete();
		}
		super.onDestroy();
	}

	private void showsDialog(final int position, final String typeToChoose) {
		int checkitem = 0;
		final List<String> listData = new ArrayList<String>();
		if (typeToChoose.equals(chooses[0])) {
			twoChooses = new String[] { "Yes", "No" };

			listData.add("Yes");
			listData.add("No");
			if (myList.get(position).getInfo()[2].equalsIgnoreCase("Yes")) {
				checkitem = 0;
			} else {
				checkitem = 1;
			}
		} else {
			threeChooses = new String[] { "All", "RFID", "Scan" };

			listData.add("All");
			listData.add("RFID");
			listData.add("Scan");

			if (myList.get(position).getInfo()[2].equalsIgnoreCase("All")) {
				checkitem = 0;
			} else if (myList.get(position).getInfo()[2]
					.equalsIgnoreCase("RFID")) {
				checkitem = 1;
			} else if (myList.get(position).getInfo()[2]
					.equalsIgnoreCase("Scan")) {
				checkitem = 2;
			}
		}
		index = checkitem;

		LayoutInflater factory = LayoutInflater
				.from(SystemSettingAuthorityAty.this);
		final View view = factory.inflate(R.layout.listview_layout, null);
		dialog = new com.moons.xst.track.widget.AlertDialog(
				SystemSettingAuthorityAty.this)
				.builder()
				.setTitle(myList.get(position).getInfo()[1].toString())
				.setView(view)
				.setListViewItems(listData, checkitem,
						new AdapterView.OnItemClickListener() {
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								index = position;
								dialog.refresh(listData, index);
							}
						})
				.setPositiveButton(getString(R.string.sure),
						new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								if (typeToChoose.equals(chooses[0])) {
									if (index == 0) {
										if (!myList.get(position).getInfo()[2]
												.equals("Yes")) {
											saveConfig(position, "Yes");
										}
									} else if (index == 1) {
										if (!myList.get(position).getInfo()[2]
												.equals("No")) {
											saveConfig(position, "No");
										}
									}
								} else {
									if (index == 0) {
										if (!myList.get(position).getInfo()[2]
												.equals("All")) {
											saveConfig(position, "All");
										}
									} else if (index == 1) {
										if (!myList.get(position).getInfo()[2]
												.equals("RFID")) {
											saveConfig(position, "RFID");
										}
									} else if (index == 2) {
										if (!myList.get(position).getInfo()[2]
												.equals("Scan")) {
											saveConfig(position, "Scan");
										}
									}
								}
							}
						})
				.setNegativeButton(getString(R.string.cancel),
						new View.OnClickListener() {

							@Override
							public void onClick(View v) {
							}
						});
		dialog.show();
	}

	private void saveConfig(final int position, String YN) {
		myList.get(position).getInfo()[2] = YN;
		mAdapter.notifyDataSetChanged();
		File file = new File(filepath);
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);
			doc.normalize();
			// 找到根Element
			Element root = doc.getDocumentElement();
			NodeList nodes = root.getElementsByTagName("Setting");

			for (int i = 0; i < nodes.getLength(); i++) {
				Element element = (Element) (nodes.item(i));

				if (myList.get(position).getInfo()[0].equals(element
						.getAttribute("Name"))) {
					element.setAttribute("Using", YN);
					break;
				}
			}
			// 保存xml文件
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			javax.xml.transform.Transformer transformer = transformerFactory
					.newTransformer();
			DOMSource domSource = new DOMSource(doc);
			// 设置编码类型
			transformer.setOutputProperty(OutputKeys.ENCODING, "GB2312");
			FileOutputStream fs = new FileOutputStream(filepath);
			StreamResult result = new StreamResult(fs);
			// 把DOM树转换为xml文件
			transformer.transform(domSource, result);
			fs.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}

	}

	private void getSettingAuthConfig() {
		mHandler = new Handler() {
			public void handleMessage(Message msg) {
				if (loading != null)
					loading.dismiss();
				if (msg.what == 1) {
					// 将数据分类
					lv.setAdapter(mAdapter = new SettingConfigAdapter(
							SystemSettingAuthorityAty.this, myList));
				} else if (msg.obj != null) {
					((AppException) msg.obj)
							.makeToast(SystemSettingAuthorityAty.this);
				}
			}
		};
		getSettingAuthConfigThread();
	}

	private void getSettingAuthConfigThread() {
		if (loading == null || !loading.isShowing()) {
			loading = new LoadingDialog(this);
			loading.setLoadText(getString(R.string.load_ing));
			loading.setCancelable(false);
			loading.show();
		}
		new Thread() {
			public void run() {
				Message msg = Message.obtain();
				try {
					loadConfigData();
					msg.what = 1;
					msg.obj = myList;
				} catch (Exception e) {
					e.printStackTrace();
					msg.what = -1;
					msg.obj = e;
				}
				mHandler.sendMessage(msg);
			}
		}.start();
	}

	private void loadConfigData() {
		try {
			URL url = new URL(getUrlPath);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");// 获得
			conn.setConnectTimeout(10000);// 设置超时时间为5s
			conn.connect();
			if (conn.getResponseCode() == 200)// 检测是否正常返回数据请求 详情参照http协议
			{
				InputStream is = conn.getInputStream();
				File file = new File(filepath);// 新建一个file文件
				if (!file.exists()) {
					file.createNewFile();
				}
				FileOutputStream fos = new FileOutputStream(file);
				byte[] buffer = new byte[1024];// 新建缓存 用来存储 从网络读取数据 再写入文件
				int len = 0;
				while ((len = is.read(buffer)) != -1) {// 当没有读到最后的时候
					fos.write(buffer, 0, len);// 将缓存中的存储的文件流秀娥问file文件
				}
				fos.flush();// 将缓存中的写入file
				is.close();
				fos.close();
				conn.disconnect();

				DocumentBuilderFactory dbf = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document doc = db.parse(file);
				doc.normalize();
				// 找到根Element
				Element root = doc.getDocumentElement();
				NodeList nodes = root.getElementsByTagName("Setting");

				for (int i = 0; i < nodes.getLength(); i++) {
					SettingConfigInfo settingConfig = new SettingConfigInfo();

					Element element = (Element) (nodes.item(i));
					strs = new String[3];
					strs[0] = element.getAttribute("Name");
					strs[1] = element.getAttribute("CNName");
					strs[2] = element.getAttribute("Using");
					if (element.getAttribute("Visible").equals("true")) {
						settingConfig.setLetter(types[0]);
						settingConfig.setInfo(strs);
					} else {
						settingConfig.setLetter(types[1]);
						settingConfig.setInfo(strs);
					}
					myList.add(settingConfig);
					settingConfig = null;

					// list.add(strs);
				}
				for (int i = myList.size() - 1; i > 0; i--) {
					if (myList.get(i).getLetter().equals(types[0])) {
						myList.get(i).setLetter(types[2]);
					} else {
						break;
					}
				}
				// file.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}