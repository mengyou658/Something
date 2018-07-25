package com.moons.xst.track.api;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.moons.xst.track.AppConst;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.AppException;
import com.moons.xst.track.R;
import com.moons.xst.track.bean.Barcode;
import com.moons.xst.track.bean.Result;
import com.moons.xst.track.bean.URLs;
import com.moons.xst.track.bean.Update;
import com.moons.xst.track.bean.User;
import com.moons.xst.track.common.CyptoUtils;
import com.moons.xst.track.common.FileEncryptAndDecrypt;
import com.moons.xst.track.common.StringUtils;
import com.moons.xst.track.common.UIHelper;
import com.moons.xst.track.communication.WebserviceFactory;
//import android.R;

/**
 * API客户端接口：用于访问网络数据
 * @author gaojun
 * @version 1.0
 * @created 2014-9-21
 */
public class ApiClient {

	public static final String UTF_8 = "UTF-8";
	public static final String DESC = "descend";
	public static final String ASC = "ascend";
	
	private final static int TIMEOUT_CONNECTION = 20000;
	private final static int TIMEOUT_SOCKET = 20000;
	private final static int RETRY_TIME = 3;

	private static String appCookie;
	private static String appUserAgent;

	public static void cleanCookie() {
		appCookie = "";
	}
	
	private static String getCookie(AppContext appContext) {
		if(appCookie == null || appCookie == "") {
			appCookie = appContext.getProperty("cookie");
		}
		return appCookie;
	}
	
	private static String getUserAgent(AppContext appContext) {
		if(appUserAgent == null || appUserAgent == "") {
			StringBuilder ua = new StringBuilder("OSChina.NET");
			ua.append('/'+appContext.getPackageInfo().versionName+'_'+appContext.getPackageInfo().versionCode);//App版本
			ua.append("/Android");//手机系统平台
			ua.append("/"+android.os.Build.VERSION.RELEASE);//手机系统版本
			ua.append("/"+android.os.Build.MODEL); //手机型号
			ua.append("/"+appContext.getAppId());//客户端唯一标识
			appUserAgent = ua.toString();
		}
		return appUserAgent;
	}
	
	private static HttpClient getHttpClient() {        
        HttpClient httpClient = new HttpClient();
		// 设置 HttpClient 接收 Cookie,用与浏览器一样的策略
		httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
        // 设置 默认的超时重试处理策略
		httpClient.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		// 设置 连接超时时间
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(TIMEOUT_CONNECTION);
		// 设置 读数据超时时间 
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(TIMEOUT_SOCKET);
		// 设置 字符集
		httpClient.getParams().setContentCharset(UTF_8);
		return httpClient;
	}	
	
	private static GetMethod getHttpGet(String url, String cookie, String userAgent) {
		GetMethod httpGet = new GetMethod(url);
		// 设置 请求超时时间
		httpGet.getParams().setSoTimeout(TIMEOUT_SOCKET);
		httpGet.setRequestHeader("Host", URLs.HOST);
		httpGet.setRequestHeader("Connection","Keep-Alive");
		httpGet.setRequestHeader("Cookie", cookie);
		httpGet.setRequestHeader("User-Agent", userAgent);
		return httpGet;
	}
	
	private static PostMethod getHttpPost(String url, String cookie, String userAgent) {
		PostMethod httpPost = new PostMethod(url);
		// 设置 请求超时时间
		httpPost.getParams().setSoTimeout(TIMEOUT_SOCKET);
		httpPost.setRequestHeader("Host", URLs.HOST);
		httpPost.setRequestHeader("Connection","Keep-Alive");
		httpPost.setRequestHeader("Cookie", cookie);
		httpPost.setRequestHeader("User-Agent", userAgent);
		return httpPost;
	}
	
	private static String _MakeURL(String p_url, Map<String, Object> params) {
		StringBuilder url = new StringBuilder(p_url);
		if(url.indexOf("?")<0)
			url.append('?');

		for(String name : params.keySet()){
			url.append('&');
			url.append(name);
			url.append('=');
			url.append(String.valueOf(params.get(name)));
			//不做URLEncoder处理
			//url.append(URLEncoder.encode(String.valueOf(params.get(name)), UTF_8));
		}

		return url.toString().replace("?&", "?");
	}
	
	/**
	 * get请求URL
	 * @param url
	 * @throws AppException 
	 */
	private static InputStream http_get(AppContext appContext, String url) throws AppException {	
		String cookie = getCookie(appContext);
		String userAgent = getUserAgent(appContext);
		
		HttpClient httpClient = null;
		GetMethod httpGet = null;

		String responseBody = "";
		int time = 0;
		do{
			try 
			{
				httpClient = getHttpClient();
				httpGet = getHttpGet(url, cookie, userAgent);			
				int statusCode = httpClient.executeMethod(httpGet);
				if (statusCode != HttpStatus.SC_OK) {
					throw AppException.http(statusCode);
				}
				responseBody = httpGet.getResponseBodyAsString();
				break;				
			} catch (HttpException e) {
				time++;
				if(time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {} 
					continue;
				}
				// 发生致命的异常，可能是协议不对或者返回的内容有问题
				e.printStackTrace();
				throw AppException.http(e);
			} catch (IOException e) {
				time++;
				if(time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {} 
					continue;
				}
				// 发生网络异常
				e.printStackTrace();
				throw AppException.network(e);
			} finally {
				// 释放连接
				httpGet.releaseConnection();
				httpClient = null;
			}
		}while(time < RETRY_TIME);
		
		//responseBody = responseBody.replaceAll("\\p{Cntrl}", "\r\n");
		if(responseBody.contains("result") && responseBody.contains("errorCode") && appContext.containsUserProperty("user.uid")){
			try {
				//Result res = Result.parse(new ByteArrayInputStream(responseBody.getBytes()));	
				//if(res.getErrorCode() == 0){
					appContext.Logout();
					appContext.getUnLoginHandler().sendEmptyMessage(1);
				//}
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}
		return new ByteArrayInputStream(responseBody.getBytes());
	}
	
	/**
	 * 公用post方法
	 * @param url
	 * @param params
	 * @param files
	 * @throws AppException
	 */
	private static InputStream _post(AppContext appContext, String url, Map<String, Object> params, Map<String,File> files) throws AppException {
		//System.out.println("post_url==> "+url);
		String cookie = getCookie(appContext);
		String userAgent = getUserAgent(appContext);
		
		HttpClient httpClient = null;
		PostMethod httpPost = null;
		
		//post表单参数处理
		int length = (params == null ? 0 : params.size()) + (files == null ? 0 : files.size());
		Part[] parts = new Part[length];
		int i = 0;
        if(params != null)
        for(String name : params.keySet()){
        	parts[i++] = new StringPart(name, String.valueOf(params.get(name)), UTF_8);
        	//System.out.println("post_key==> "+name+"    value==>"+String.valueOf(params.get(name)));
        }
        if(files != null)
        for(String file : files.keySet()){
        	try {
				parts[i++] = new FilePart(file, files.get(file));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
        	//System.out.println("post_key_file==> "+file);
        }
		
		String responseBody = "";
		int time = 0;
		do{
			try 
			{
				httpClient = getHttpClient();
				httpPost = getHttpPost(url, cookie, userAgent);	        
		        httpPost.setRequestEntity(new MultipartRequestEntity(parts,httpPost.getParams()));		        
		        int statusCode = httpClient.executeMethod(httpPost);
		        if(statusCode != HttpStatus.SC_OK) 
		        {
		        	throw AppException.http(statusCode);
		        }
		        else if(statusCode == HttpStatus.SC_OK) 
		        {
		            Cookie[] cookies = httpClient.getState().getCookies();
		            String tmpcookies = "";
		            for (Cookie ck : cookies) {
		                tmpcookies += ck.toString()+";";
		            }
		            //保存cookie   
	        		if(appContext != null && tmpcookies != ""){
	        			appContext.setProperty("cookie", tmpcookies);
	        			appCookie = tmpcookies;
	        		}
		        }
		     	responseBody = httpPost.getResponseBodyAsString();
		        //System.out.println("XMLDATA=====>"+responseBody);
		     	break;	     	
			} catch (HttpException e) {
				time++;
				if(time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {} 
					continue;
				}
				// 发生致命的异常，可能是协议不对或者返回的内容有问题
				e.printStackTrace();
				throw AppException.http(e);
			} catch (IOException e) {
				time++;
				if(time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {} 
					continue;
				}
				// 发生网络异常
				e.printStackTrace();
				throw AppException.network(e);
			} finally {
				// 释放连接
				httpPost.releaseConnection();
				httpClient = null;
			}
		}while(time < RETRY_TIME);
        
        responseBody = responseBody.replaceAll("\\p{Cntrl}", "");
		if(responseBody.contains("result") && responseBody.contains("errorCode") && appContext.containsUserProperty("user.uid")){
			try {
				//Result res = Result.parse(new ByteArrayInputStream(responseBody.getBytes()));	
				//if(res.getErrorCode() == 0){
					appContext.Logout();
					appContext.getUnLoginHandler().sendEmptyMessage(1);
				//}
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}
        return new ByteArrayInputStream(responseBody.getBytes());
	}
	
	/**
	 * post请求URL
	 * @param url
	 * @param params
	 * @param files
	 * @throws AppException 
	 * @throws IOException 
	 * @throws  
	 */
	private static Result http_post(AppContext appContext, String url, Map<String, Object> params, Map<String,File> files) throws AppException, IOException {
        return new Result();//Result.parse(_post(appContext, url, params, files));  
	}	

	/**
	 * 检查版本更新
	 * @param url
	 * @return
	 */
	public static Update checkVersion(AppContext appContext) throws AppException {
		try{
			String appversionxml = WebserviceFactory.getAppVersionInfo(appContext,AppContext.getModel());
			return Update.parse(appversionxml);		
		}catch(Exception e){
			if(e instanceof AppException)
				throw (AppException)e;
			throw AppException.network(e);
		}
	}

	/**
	 * 登录， 自动处理cookie
	 * @param url
	 * @param userInfo
	 * @param loginType
	 * @return
	 * @throws AppExceptionlineListlineListlineListlineListlineListlineListlineList
	 */	
	public static User login(AppContext appContext, User userInfo, AppContext.LoginType loginType) throws AppException {			
		User user = new User();
		user = userInfo;
		CheckUserPWD(appContext, user, loginType);
		return user;
	}

	/**
     * 验证用户名密码
     * @author LKZ
     */
    private synchronized static void CheckUserPWD(AppContext appContext, User user, AppContext.LoginType loginType)
    {
    	Result result =new Result();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();  
        try  
        {  
            DocumentBuilder db = dbf.newDocumentBuilder();  
            
            String path = AppConst.XSTBasePath()+AppConst.NewUserXmlFile;
            File fi = new File(path);
            if (!fi.exists()) {
            	result.setErrorCode(0);
            	result.setErrorMessage(appContext.getString(R.string.sinalogin_check_nocommunication));
            	return;
            }
			//String temppath = AppConst.XSTBasePath() + AppConst.tempUserXmlFile;
			//FileEncryptAndDecrypt.decrypt(path,temppath);
			//File f = new File(path);
            Document doc = db.parse(fi); 
            doc.normalize();
            NodeList tableList = doc.getElementsByTagName("Z_AppUser"); 
            for (int i = 0; i < tableList.getLength(); i++)  
            {  
                Node table = tableList.item(i);  
                int userID=0;
                String AppAccount_TX ="";
                String AppUser_CD ="";
                String Name_TX ="";
                String XJQPWD_TX ="";
                String UserRFID_TX = "";
                String UserAccess_TX ="";
                String lineListString="";
                ArrayList<String> LineList =new ArrayList<String>();
                for (Node node = table.getFirstChild(); node != null; node = node.getNextSibling())  
                {  
                    if (node.getNodeType() == Node.ELEMENT_NODE)  
                    {  
                    	if(node.getNodeName().toUpperCase().equals("APPUSER_ID")){
                    		userID =Integer.valueOf(node.getTextContent()).intValue();
                    	}
                    	else if(node.getNodeName().toUpperCase().equals("APPACCOUNT_TX")){
                    		AppAccount_TX =node.getTextContent();
                    	}
                    	else if(node.getNodeName().toUpperCase().equals("APPUSER_CD")){
                    		AppUser_CD =node.getTextContent();
                    	}
                    	else if(node.getNodeName().toUpperCase().equals("NAME_TX")){
                    		Name_TX =node.getTextContent();
                    	}
                    	else if(node.getNodeName().toUpperCase().equals("XJQPWD_TX")){
                    		XJQPWD_TX =node.getTextContent();
                    	}  
                    	else if(node.getNodeName().toUpperCase().equals("USERRFID_TX")){
                    		UserRFID_TX = node.getTextContent();
                    	}
                    	else if(node.getNodeName().toUpperCase().equals("USERACCESS_TX")){
                    		UserAccess_TX =node.getTextContent();
                    	}
                    	else if(node.getNodeName().toUpperCase().equals("LINELIST")){
                    		lineListString =node.getTextContent();
                    		if ((!StringUtils.isEmpty(lineListString))
                    			&&lineListString.contains(";")) {
								String[] _LineList =lineListString.split(";");
								LineList.clear();
								for (String _line : _LineList) {
									if(!StringUtils.isEmpty(_line)){
									LineList.add(_line);
									}
								}
							}
                    	} 
                    }  
                }
                
                if (loginType == AppContext.LoginType.Account || loginType == AppContext.LoginType.Scan){
		            if(user.getUserAccount().equals(AppAccount_TX)){
		            	if (StringUtils.isEmpty(XJQPWD_TX) || (CyptoUtils.MD5(user.getUserPwd()).equals(XJQPWD_TX))) {
		            		result.setErrorCode(1);
		                    result.setErrorMessage("");
		                    user.setUserID(userID);
		                    user.setUserCD(AppUser_CD);                        
		                    user.setUserName(Name_TX);
		                    user.setUserRFID(UserRFID_TX);
		                    user.setUserAccess(UserAccess_TX);
		                    user.setUserLineList(LineList);
		                    user.setUserLineListStr(lineListString);
		                    //f.delete();
		                    return;
						}
		            	else {
		                	result.setErrorCode(0);
		                    result.setErrorMessage(appContext.getString(R.string.sinalogin_check_wrongpassword));
						}
		            }
                }
                else if (loginType == AppContext.LoginType.RFID){
                	try {
	                	String tempRFID = StringUtils.leftPad(user.getUserRFID(), "0", 16);
	                	if (user.getUserRFID().equalsIgnoreCase(UserRFID_TX) ||
	                			tempRFID.equalsIgnoreCase(UserRFID_TX) ||
	                			StringUtils.leftPad(String.valueOf(Long.parseLong(tempRFID, 16)), "0", 10).equalsIgnoreCase(UserRFID_TX)) {
		            		result.setErrorCode(1);
		                    result.setErrorMessage("");
		                    user.setAccount(AppAccount_TX);
		                    user.setUserID(userID);
		                    user.setUserCD(AppUser_CD);                        
		                    user.setUserName(Name_TX);
		                    user.setUserRFID(UserRFID_TX);
		                    user.setUserAccess(UserAccess_TX);
		                    user.setUserLineList(LineList);
		                    user.setUserLineListStr(lineListString);
		                    //f.delete();
		                    return;
						}
		            	else {
		                	result.setErrorCode(0);
		                    result.setErrorMessage(appContext.getString(R.string.sinalogin_check_wrongaccount));
						}
                	} catch (Exception e) {
                		result.setErrorCode(0);
	                    result.setErrorMessage(appContext.getString(R.string.sinalogin_check_wrongaccount));
                	}
                }
            }
            if(StringUtils.isEmpty(result.getErrorMessage())){
	            result.setErrorCode(0);
	            result.setErrorMessage(appContext.getString(R.string.sinalogin_check_wrongaccount));
            }
            //f.delete();
        }  
        catch (Exception e)  
        {  
            e.printStackTrace(); 
            result.setErrorCode(0);
            if (e.getMessage().contains("Unexpected token")|| e.getMessage().contains("Unexpected end")) {
            	result.setErrorMessage(appContext.getString(R.string.filedamege_error));
            }else {
            	result.setErrorMessage(e.getMessage());
            }
        } 
        finally
        {
        	user.setValidate(result);
        }
    }

	/**
	 * 获取动态列表
	 * @param uid
	 * @param catalog 1最新动态  2@我  3评论  4我自己 
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 * @throws AppException
	 */
	/*
	public static ActiveList getActiveList(AppContext appContext, final int uid,final int catalog, final int pageIndex, final int pageSize) throws AppException {
		String newUrl = _MakeURL(URLs.ACTIVE_LIST, new HashMap<String, Object>(){{
			put("uid", uid);
			put("catalog", catalog);
			put("pageIndex", pageIndex);
			put("pageSize", pageSize);
		}});
		
		try{
			return ActiveList.parse(http_get(appContext, newUrl));		
		}catch(Exception e){
			if(e instanceof AppException)
				throw (AppException)e;
			throw AppException.network(e);
		}
	}
	*/


	/**
	 * 二维码扫描签到
	 * @param appContext
	 * @param barcode
	 * @return
	 * @throws AppException
	 */
	public static String signIn(AppContext appContext, Barcode barcode) throws AppException {
		try{
			return StringUtils.toConvertString(http_get(appContext, barcode.getUrl()));
		}catch(Exception e){
			if(e instanceof AppException)
				throw (AppException)e;
			throw AppException.network(e);
		}
	} 
}
