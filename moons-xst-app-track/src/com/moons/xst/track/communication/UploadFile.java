package com.moons.xst.track.communication;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;

import com.google.gson.Gson;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.R;
import com.moons.xst.track.bean.ReturnResultInfo;
import com.moons.xst.track.bean.UploadEntity;
import com.moons.xst.track.common.FileUtils;

/**
 * 文件上传
 * 
 * @author LKZ
 * @version 1.0
 * @created 2015-09-02
 */
public class UploadFile {
	String multipart_form_data = "multipart/form-data";
	String twoHyphens = "--";
	String boundary = "****************Moons.XST.Android.LKZ"; // 数据分隔符
	String lineEnd = System.getProperty("line.separator");

	static UploadFile _intance = null;

	public static UploadFile getIntance() {
		if (_intance == null) {
			_intance = new UploadFile();
		}
		return _intance;
	}

	private void addFileContent(List<File> files, DataOutputStream output) {
		for (File file : files) {
			String _fileNameString = file.getName();
			StringBuilder split = new StringBuilder();
			split.append(twoHyphens + boundary + lineEnd);
			split.append("Content-Disposition: form-data; name=\""
					+ _fileNameString + "\"; filename=\"" + _fileNameString
					+ "\"" + lineEnd);
			split.append("Content-Type:multipart/form-data" + lineEnd);
			split.append(lineEnd);
			try {
				output.writeBytes(split.toString());
				FileInputStream fStream = new FileInputStream(file);
				int bytelength = (int) file.length();
				int bufferSize = 256 * 256;
				if (bytelength > 0) {
					if (bytelength < 1024) {
						bufferSize = 128 * 2;
					} else if (bytelength < 1024 * 1024)
						bufferSize = 1024 * 8 * 2;
					else if (bytelength < 1024 * 1024 * 10) {
						bufferSize = 1024 * 16 * 2;
					} else if (bytelength < 1024 * 1024 * 50) {
						bufferSize = 1024 * 16 * 8 * 2;
					} else {
						bufferSize = 1024 * 256 * 2;
					}
				}
				byte[] buffer = new byte[bufferSize];
				int length = -1;
				/* 从文件读取数据至缓冲区 */
				while ((length = fStream.read(buffer)) != -1) {
					/* 将资料写入DataOutputStream中 */
					output.write(buffer, 0, length);
					Thread.sleep(10);
				}
				/* close streams */
				fStream.close();
				output.writeBytes(lineEnd);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}catch (IOException e) {
				e.printStackTrace();
				//throw new RuntimeException(e);
			}
		}
	}

	private void addFileContent(String[] files, DataOutputStream output) {
		for (String file : files) {
			String _fileNameString = new File(file).getName();
			StringBuilder split = new StringBuilder();
			split.append(twoHyphens + boundary + lineEnd);
			split.append("Content-Disposition: form-data; name=\""
					+ _fileNameString + "\"; filename=\"" + _fileNameString
					+ "\"" + lineEnd);
			split.append("Content-Type:multipart/form-data" + lineEnd);
			split.append(lineEnd);
			try {
				output.writeBytes(split.toString());
				FileInputStream fStream = new FileInputStream(file);
				int bytelength = (int) new File(file).length();
				int bufferSize = 256 * 256;
				if (bytelength > 0) {
					if (bytelength < 1024) {
						bufferSize = 128*2;
					} else if (bytelength < 1024 * 1024)
						bufferSize = 1024 * 8*2;
					else if (bytelength < 1024 * 1024 * 10) {
						bufferSize = 1024 * 16*2;
					} else if (bytelength < 1024 * 1024 * 50) {
						bufferSize = 1024 * 16 * 8*2;
					} else {
						bufferSize = 1024 * 256*2;
					}
				}
				byte[] buffer = new byte[bufferSize];
				int length = -1;
				/* 从文件读取数据至缓冲区 */
				while ((length = fStream.read(buffer)) != -1) {
					/* 将资料写入DataOutputStream中 */
					output.write(buffer, 0, length);
					Thread.sleep(10);
				}
				/* close streams */
				fStream.close();
				output.writeBytes(lineEnd);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}catch (IOException e) {
				e.printStackTrace();
				//throw new RuntimeException(e);
			}
		}
	}

	/**
	 * 添加表单内容
	 */
	private void addFormField(Set<Map.Entry<Object, Object>> params,
			DataOutputStream output) {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<Object, Object> param : params) {
			sb.append(twoHyphens + boundary + lineEnd);
			sb.append("Content-Disposition: form-data; name=\""
					+ param.getKey() + "\"" + lineEnd);
			sb.append(lineEnd);
			sb.append(param.getValue() + lineEnd);
		}
		try {
			//String string = URLEncoder.encode(sb.toString(), "utf-8");
			//output.writeBytes(string);// 发送表单字段数据
			output.writeBytes(sb.toString());// 发送表单字段数据
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 直接通过 HTTP 协议提交带附件的表单
	 * 
	 * @param formDataType
	 *            表单数据类型
	 * @param jsonStr
	 *            表单数据
	 * @param files
	 *            表单附件
	 * @return
	 */
	public String post(String formDataType, String jsonStr, List<File> files) {
		HttpURLConnection conn = null;
		DataOutputStream output = null;
		BufferedReader input = null;
		try {

			Hashtable<Object, Object> _params = new Hashtable<Object, Object>();
			_params.put("PDAGUID", AppContext.GetUniqueID());
			_params.put("XJQCD", AppContext.GetxjqCD());
			_params.put("DATATYPE", formDataType);
			_params.put("DATAJSON", jsonStr);
			Set<Map.Entry<Object, Object>> params = _params.entrySet();
			String actionUrl = AppContext.GetWSAddressBasePath()
					+ "UploadFileToServer.ashx";
			URL url = new URL(actionUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(120000);
			conn.setDoInput(true); // 允许输入
			conn.setDoOutput(true); // 允许输出
			conn.setUseCaches(false); // 不使用Cache
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "keep-alive");
			conn.setRequestProperty("Content-Type", multipart_form_data
					+ "; boundary=" + boundary);
			conn.connect();
			output = new DataOutputStream(conn.getOutputStream());
			addFormField(params, output); // 添加表单字段内容
			addFileContent(files, output); // 添加附件
			output.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);// 数据结束标志
			output.flush();
			int code = conn.getResponseCode();
			if (code != 200) {
				throw new RuntimeException("请求‘" + actionUrl + "’失败！");
			}
			input = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			StringBuilder response = new StringBuilder();
			String oneLine;
			while ((oneLine = input.readLine()) != null) {
				response.append(oneLine + lineEnd);
			}
			return response.toString();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			// 统一释放资源
			try {
				if (output != null) {
					output.close();
				}
				if (input != null) {
					input.close();
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

			if (conn != null) {
				conn.disconnect();
			}
		}
	}

	/**
	 * 直接通过 HTTP 协议提交带附件的表单
	 * 
	 * @param formDataType
	 *            表单数据类型
	 * @param jsonStr
	 *            表单数据
	 * @param files
	 *            表单附件
	 * @return
	 */
	public String post(String formDataType, String jsonStr, String[] files) {
		HttpURLConnection conn = null;
		DataOutputStream output = null;
		BufferedReader input = null;
		try {

			Hashtable<Object, Object> _params = new Hashtable<Object, Object>();
			_params.put("PDAGUID", AppContext.GetUniqueID());
			_params.put("XJQCD", AppContext.GetxjqCD());
			_params.put("DATATYPE", formDataType);
			_params.put("DATAJSON", jsonStr);
			Set<Map.Entry<Object, Object>> params = _params.entrySet();
			String actionUrl = AppContext.GetWSAddressBasePath()
					+ "UploadFileToServer.ashx";
			URL url = new URL(actionUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(120000);
			conn.setDoInput(true); // 允许输入
			conn.setDoOutput(true); // 允许输出
			conn.setUseCaches(false); // 不使用Cache
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "keep-alive");
			conn.setRequestProperty("Content-Type", multipart_form_data
					+ "; boundary=" + boundary);
			//conn.setRequestProperty("Charset", "utf-8");//add
			conn.connect();
			output = new DataOutputStream(conn.getOutputStream());
			addFormField(params, output); // 添加表单字段内容
			addFileContent(files, output); // 添加附件
			output.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);// 数据结束标志
			output.flush();
			int code = conn.getResponseCode();
			if (code != 200) {
				throw new RuntimeException("请求‘" + actionUrl + "’失败！");
			}
			input = new BufferedReader(new InputStreamReader(
					conn.getInputStream(), "UTF-8"));
			StringBuilder response = new StringBuilder();
			String oneLine;
			while ((oneLine = input.readLine()) != null) {
				response.append(oneLine + lineEnd);
			}
			return response.toString();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			// 统一释放资源
			try {
				if (output != null) {
					output.close();
				}
				if (input != null) {
					input.close();
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

			if (conn != null) {
				conn.disconnect();
			}
		}
	}
	
	/**
	 * 通过webservice断点续传文件
	 * 
	 * @param path
	 */
	public boolean UploadFileByWeb(String path,String fileName,String fileType) {
		File file = new File(path);
		try {
			AppContext.SetIsUpload(true);//设置正在传输
			Gson gson = new Gson();
			FileInputStream fStream = new FileInputStream(file);
			long bytelength =  file.length();
			int bufferSize = 256 * 256;
			if (bytelength > 0) {
				if (bytelength < 1024) {
					bufferSize = 128*2;
				} else if (bytelength < 1024 * 1024)
					bufferSize = 1024 * 8*2;
				else if (bytelength < 1024 * 1024 * 10) {
					bufferSize = 1024 * 16*2;
				} else if (bytelength < 1024 * 1024 * 50) {
					bufferSize = 1024 * 16 * 8*2;
				} else {
					bufferSize = 1024 * 256*2;
				}
			}
			byte[] buffer = new byte[bufferSize];
			int length = -1;
			UploadEntity beentity = new UploadEntity();
			beentity.setFileType(fileType);
			beentity.setXJQCD(AppContext.GetxjqCD());
			beentity.setXJQGUID(AppContext.GetUniqueID());
			beentity.setTotalCount(bytelength);
			beentity.setFileName(fileName);
			String jsonBeString = gson.toJson(beentity);
			long offset = WebserviceFactory.uploadFilePrepare(jsonBeString);
			if (offset >= bytelength) {
				WebserviceFactory.uploadFile(jsonBeString);
			}
			fStream.skip(offset);
			while ((length = fStream.read(buffer)) != -1) {
				/* 将资料写入DataOutputStream中 */
				UploadEntity entity = new UploadEntity();
				String filedataString = Base64.encodeToString(buffer,
						Base64.DEFAULT);    
				entity.setBuffer(filedataString);
				entity.setFileType(fileType);
				entity.setXJQCD(AppContext.GetxjqCD());
				entity.setXJQGUID(AppContext.GetUniqueID());
				entity.setTotalCount(bytelength);
				entity.setFileName(fileName);
				String jsonString = gson.toJson(entity);
				offset = WebserviceFactory.uploadFile(jsonString);
				if (offset == -1) {
					break;
				}
				//fStream.skip(offset);
				Thread.sleep(20);
			}
			Thread.sleep(1000);
			fStream.close();
			AppContext.SetIsUpload(false);//设置传输完成
			if (offset >=bytelength) {
				return true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			AppContext.SetIsUpload(false);//设置传输完成
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 通过webservice断点续传文件
	 * 
	 * @param path
	 */
	public boolean UploadFileByWeb(Context context, Handler proBarHandler, String path,String fileName,String fileType) {
		File file = new File(path);
		try {
			String lineid = new File(path).getName();
			AppContext.SetIsUpload(true);//设置正在传输
			Gson gson = new Gson();
			FileInputStream fStream = new FileInputStream(file);
			long bytelength =  file.length();
			int bufferSize = 256 * 256;
			if (bytelength > 0) {
				if (bytelength < 1024) {
					bufferSize = 128*2;
				} else if (bytelength < 1024 * 1024)
					bufferSize = 1024 * 8*2;
				else if (bytelength < 1024 * 1024 * 10) {
					bufferSize = 1024 * 16*2;
				} else if (bytelength < 1024 * 1024 * 50) {
					bufferSize = 1024 * 16 * 8*2;
				} else {
					bufferSize = 1024 * 256*2;
				}
			}
			byte[] buffer = new byte[bufferSize];
			int length = -1;
			UploadEntity beentity = new UploadEntity();
			beentity.setFileType(fileType);
			beentity.setXJQCD(AppContext.GetxjqCD());
			beentity.setXJQGUID(AppContext.GetUniqueID());
			beentity.setTotalCount(bytelength);
			beentity.setFileName(fileName);
			String jsonBeString = gson.toJson(beentity);
			long offset = WebserviceFactory.uploadFilePrepare(jsonBeString);
			if (offset >= bytelength) {
				WebserviceFactory.uploadFile(jsonBeString);
			}
			fStream.skip(offset);
			
			int precent = 0;
			long x = 0;
			x = offset;
			while ((length = fStream.read(buffer)) != -1) {
				/* 将资料写入DataOutputStream中 */
				x += length;
				precent=(int)(((double)x/(double)bytelength)*100);
				if (precent > 100)
					precent = 100;
				Message _msg = Message.obtain();
				_msg.what = 1;
				_msg.obj = context.getString(R.string.uploading_file_hint,
						lineid, FileUtils.formatFileSize(x) + "/" + FileUtils.formatFileSize(bytelength));
//				_msg.obj = context.getString(R.string.uploading_file_hint,
//						lineid, String.valueOf((x / 1024)) + "K") + "/" + String.valueOf((bytelength / 1024)) + "K";
				_msg.arg1 = precent;
				_msg.arg2 = 1;
				if (proBarHandler != null)
					proBarHandler.sendMessage(_msg);
				
				UploadEntity entity = new UploadEntity();
				String filedataString = Base64.encodeToString(buffer,
						Base64.DEFAULT);    
				entity.setBuffer(filedataString);
				entity.setFileType(fileType);
				entity.setXJQCD(AppContext.GetxjqCD());
				entity.setXJQGUID(AppContext.GetUniqueID());
				entity.setTotalCount(bytelength);
				entity.setFileName(fileName);
				String jsonString = gson.toJson(entity);
				offset = WebserviceFactory.uploadFile(jsonString);
				if (offset == -1) {
					break;
				}
				//fStream.skip(offset);
				Thread.sleep(20);
			}
			Thread.sleep(1000);
			fStream.close();
			AppContext.SetIsUpload(false);//设置传输完成
			if (offset >=bytelength) {
				return true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			AppContext.SetIsUpload(false);//设置传输完成
			Message _msg = Message.obtain();
			_msg.what = -1;
			_msg.obj = e.getMessage();
			if (proBarHandler != null)
				proBarHandler.sendMessage(_msg);
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 通过webservice断点续传文件
	 * 
	 * @param path
	 */
	public ReturnResultInfo UploadFileByWebNew(Context context, Handler proBarHandler, String path,String fileName,String fileType) {
		File file = new File(path);
		ReturnResultInfo returnInfo = new ReturnResultInfo();
		try {
			String lineid = new File(path).getName();
			AppContext.SetIsUpload(true);//设置正在传输
			Gson gson = new Gson();
			FileInputStream fStream = new FileInputStream(file);
			long bytelength =  file.length();
			int bufferSize = 256 * 256;
			if (bytelength > 0) {
				if (bytelength < 1024) {
					bufferSize = 128*2;
				} else if (bytelength < 1024 * 1024)
					bufferSize = 1024 * 8*2;
				else if (bytelength < 1024 * 1024 * 10) {
					bufferSize = 1024 * 16*2;
				} else if (bytelength < 1024 * 1024 * 50) {
					bufferSize = 1024 * 16 * 8*2;
				} else {
					bufferSize = 1024 * 256*2;
				}
			}
			byte[] buffer = new byte[bufferSize];
			int length = -1;
			UploadEntity beentity = new UploadEntity();
			beentity.setFileType(fileType);
			beentity.setXJQCD(AppContext.GetxjqCD());
			beentity.setXJQGUID(AppContext.GetUniqueID());
			beentity.setTotalCount(bytelength);
			beentity.setFileName(fileName);
			String jsonBeString = gson.toJson(beentity);
			long offset = WebserviceFactory.uploadFilePrepare(jsonBeString);
			if (offset >= bytelength) {
				WebserviceFactory.uploadFile(context, jsonBeString);
			}
			fStream.skip(offset);
			
			int precent = 0;
			long x = 0;
			x = offset;
			while ((length = fStream.read(buffer)) != -1) {
				/* 将资料写入DataOutputStream中 */
				x += length;
				precent=(int)(((double)x/(double)bytelength)*100);
				if (precent > 100)
					precent = 100;
				Message _msg = Message.obtain();
				_msg.what = 1;
				_msg.obj = context.getString(R.string.uploading_file_hint,
						lineid, FileUtils.formatFileSize(x) + "/" + FileUtils.formatFileSize(bytelength));
				_msg.arg1 = precent;
				_msg.arg2 = 1;
				if (proBarHandler != null)
					proBarHandler.sendMessage(_msg);
				
				UploadEntity entity = new UploadEntity();
				String filedataString = Base64.encodeToString(buffer,
						Base64.DEFAULT);    
				entity.setBuffer(filedataString);
				entity.setFileType(fileType);
				entity.setXJQCD(AppContext.GetxjqCD());
				entity.setXJQGUID(AppContext.GetUniqueID());
				entity.setTotalCount(bytelength);
				entity.setFileName(fileName);
				String jsonString = gson.toJson(entity);
				returnInfo = WebserviceFactory.uploadFile(context, jsonString);
				if (!returnInfo.getResult_YN()) {
					break;
				}
				Thread.sleep(20);
			}
			Thread.sleep(1000);
			fStream.close();
			AppContext.SetIsUpload(false);//设置传输完成
			
			return returnInfo;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			AppContext.SetIsUpload(false);//设置传输完成
			
			returnInfo.setResult_YN(false);
			returnInfo.setResult_Content_TX("");
			returnInfo.setError_Message_TX(e.getMessage());
			return returnInfo;
		}
	}
}