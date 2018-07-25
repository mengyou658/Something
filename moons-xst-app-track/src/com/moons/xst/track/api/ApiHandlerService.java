package com.moons.xst.track.api;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public  class ApiHandlerService implements Callable<String> {
	
	//命名空间
	private String nameSpace;
	//调用方法名字
	private String methodName;
	//WebServise去除 ?wsdl后
	private String endPoint;
	//Soap Action
	private String soapAction;


	//查询属性
	private ArrayList<String> params;
	//查询参数
	private ArrayList<Object> vals;
	
    
	
	public  ApiHandlerService(String nameSpace,String endPoint,String methodName,String soapAction,
			ArrayList<String> params,ArrayList<Object> vals )
	{
		this.nameSpace = nameSpace;
		this.endPoint = endPoint;
		this.methodName = methodName;
		this.soapAction = soapAction;
		this.params = params;
		this.vals = vals;
		
	}

		@Override
		public String call() throws Exception {
			// TODO Auto-generated method stub
			return getRemoteInfo();
		}  
		
		public String getRemoteInfo() {
			  // 指定WebService的命名空间和调用的方法名
			  SoapObject rpc = new SoapObject(nameSpace, methodName);
			  for(int i = 0;i < params.size();i++)
			  {
				  rpc.addProperty(params.get(i), vals.get(i));
			  }
			  
			  SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
			  envelope.bodyOut = rpc;
			  envelope.dotNet = true;

			  HttpTransportSE transport = new HttpTransportSE(endPoint);
			  try {
				    // 调用WebService
				    transport.call(soapAction, envelope);
				    // 获取返回的数据
				    SoapObject object = (SoapObject) envelope.bodyIn;
				    // 获取返回的结果
				    final String result = object.getProperty(0).toString();
				    return result;
			  } catch (Exception e) {
				  e.printStackTrace();
			    return null;
			  } 
			  
			}
	}
