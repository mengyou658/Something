/**
 *  @author LKZ
 */
package com.moons.xst.track.service;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.moons.xst.track.AppContext;
import com.moons.xst.track.R;
import com.moons.xst.track.common.UIHelper;

/**
 * 封装科大讯飞语音服务（语音播报）
 * 
 * @实现方式：使用科大讯飞在线语音API（免费）和“语音+”组件（免费）；
 * @收费情况：目前组合使用是免费的；
 * @使用条件：离线完成，无需联网；但需正确的K码，K码申请需科大讯飞审批通过，不然每天只能调用500次。
 * @author LKZ
 * @2015-03-21
 */
public class VoiceConvertService extends Service {

	private static final String TAG = "VoiceConvertService";
	private IBinder binder = new VoiceConvertService.LocalBinder();
	boolean mAllowRebind;
	Intent mesIntent;
	/**
	 * 语音阅读对象
	 */
	private SpeechSynthesizer mTts;
	/**
	 * 语音听写对象
	 */
	private SpeechRecognizer mIat;
	/**
	 * 用HashMap存储听写结果
	 * 
	 */
	private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
	/**
	 * 服务引擎（本地/网络）
	 */
	private String mEngineType = SpeechConstant.TYPE_LOCAL;

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		super.onUnbind(intent);
		// All clients have unbound with unbindService()
		return mAllowRebind;
	}

	@Override
	public void onRebind(Intent intent) {
		// A client is binding to the service with bindService(),
		// after onUnbind() has already been called
	}

	@Override
	public void onCreate() {
		super.onCreate();
		// 初始化合成对象
		mTts = SpeechSynthesizer.createSynthesizer(this, mTtsInitListener);
		// 初始化识别对象
		mIat = SpeechRecognizer.createRecognizer(this, mIatInitListener);
		mesIntent = new Intent();
		mesIntent.setAction("com.xst.track.service.voiceservice");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mTts.stopSpeaking();
		// 退出时释放连接
		mTts.destroy();
		mIat.cancel();
		mIat.destroy();
	}

	/**
	 * 定义内容类继承Binder
	 * 
	 * @author LKZ
	 * 
	 */
	public class LocalBinder extends Binder {
		// 返回本地服务
		public VoiceConvertService getService() {
			return VoiceConvertService.this;
		}
	}

	private InitListener mTtsInitListener = new InitListener() {
		@Override
		public void onInit(int code) {
			Log.d(TAG, "InitListener init() code = " + code);
			if (code != ErrorCode.SUCCESS) {
				UIHelper.ToastMessage(getBaseContext(), getString(R.string.xf_listen_init_error) + code);
			} else {
				// 初始化成功，之后可以调用startSpeaking方法
				// 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
				// 正确的做法是将onCreate中的startSpeaking调用移至这里
			}
		}
	};

	/**
	 * 初始化监听器。
	 */
	private InitListener mIatInitListener = new InitListener() {

		@Override
		public void onInit(int code) {
			Log.d(TAG, "SpeechRecognizer init() code = " + code);
			if (code != ErrorCode.SUCCESS) {
				UIHelper.ToastMessage(getBaseContext(), getString(R.string.xf_listen_init_error) + code);
			} else {
				// 初始化成功，之后可以调用startSpeaking方法
				// 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
				// 正确的做法是将onCreate中的startSpeaking调用移至这里
			}
		}
	};

	private SynthesizerListener mTtsListener = new SynthesizerListener() {
		@Override
		public void onSpeakBegin() {
			// UIHelper.ToastMessage(getBaseContext(), "开始播放");
		}

		@Override
		public void onSpeakPaused() {
			// UIHelper.ToastMessage(getBaseContext(), "暂停播放");
		}

		@Override
		public void onSpeakResumed() {
			// UIHelper.ToastMessage(getBaseContext(), "继续播放");
		}

		@Override
		public void onBufferProgress(int percent, int beginPos, int endPos,
				String info) {
			// 合成进度
		}

		@Override
		public void onSpeakProgress(int percent, int beginPos, int endPos) {
			// 播放进度

		}

		@Override
		public void onCompleted(SpeechError error) {
			/*
			 * if (error == null) { UIHelper.ToastMessage(getBaseContext(),
			 * "播放完成"); } else if (error != null) {
			 * UIHelper.ToastMessage(getBaseContext(),
			 * error.getPlainDescription(true)); }
			 */
		}

		@Override
		public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {

		}
	};

	/**
	 * 听写监听器。
	 */
	private RecognizerListener recognizerListener = new RecognizerListener() {

		@Override
		public void onBeginOfSpeech() {
			UIHelper.ToastMessage(getBaseContext(), getString(R.string.xf_listen_begin_desc));
			mesIntent.putExtra("state", "START");
			mesIntent.putExtra("msg", "");
			sendBroadcast(mesIntent);
		}

		@Override
		public void onError(SpeechError error) {
			// Tips：
			// 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
			// 如果使用本地功能（语音+）需要提示用户开启语音+的录音权限。
			mesIntent.putExtra("state", "ERR");
			mesIntent.putExtra("msg", error.getPlainDescription(true));
			sendBroadcast(mesIntent);
		}

		@Override
		public void onEndOfSpeech() {
			UIHelper.ToastMessage(getBaseContext(), getString(R.string.xf_listen_end_desc));
			mesIntent.putExtra("state", "END");
			mesIntent.putExtra("msg", "");
			sendBroadcast(mesIntent);
		}

		@Override
		public void onResult(RecognizerResult results, boolean isLast) {
			Log.d(TAG, results.getResultString());
			printResult(results);

			if (isLast) {
				// TODO 最后的结果
			}
			mesIntent.putExtra("state", "RESULT");
			mesIntent.putExtra("msg", "");
			sendBroadcast(mesIntent);
		}

		@Override
		public void onVolumeChanged(int volume) {
			mesIntent.putExtra("state", "VOLUME");
			mesIntent.putExtra("msg", volume);
			sendBroadcast(mesIntent);
		}

		@Override
		public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
		}
	};

	private void printResult(RecognizerResult results) {
		String text = com.moons.xst.track.common.JsonParser
				.parseIatResult(results.getResultString());

		String sn = null;
		// 读取json结果中的sn字段
		try {
			JSONObject resultJson = new JSONObject(results.getResultString());
			sn = resultJson.optString("sn");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		mIatResults.put(sn, text);
	}

	/**
	 * 播放指定文字
	 * 
	 * @文字阅读配置
	 * @author LKZ
	 */
	public synchronized void Speaking(String wordString) {
		if (!AppContext.getVoice())
			return;
		setParamForSpeaking();
		int code = mTts.startSpeaking(wordString, mTtsListener);
		if (code != ErrorCode.SUCCESS) {
			if (code == ErrorCode.ERROR_COMPONENT_NOT_INSTALLED) {
				// 未安装则跳转到提示安装页面
				UIHelper.ToastMessage(getBaseContext(), getString(R.string.xf_listen_install));
				// mInstaller.install();
			} else {
				UIHelper.ToastMessage(getBaseContext(), getString(R.string.xf_listen_together_error) + code);
			}
		}
	}

	/**
	 * 停止播放
	 * 
	 * @文字阅读配置
	 * @author LKZ
	 */
	public synchronized void StopSpeaking() {
		if (!AppContext.getVoice())
			return;
		mTts.stopSpeaking();
	}

	/**
	 * 开始说话进行语音输入
	 * 
	 * @return
	 */
	public String BeginSpeaking() {
		mIatResults.clear();
		// 设置参数
		setParamForWriting();
		int ret = mIat.startListening(recognizerListener);
		if (ret != ErrorCode.SUCCESS) {
			return getString(R.string.xf_listen_begin_err) + ret;
		} else {
			return getString(R.string.xf_listen_begin_desc);
		}
	}

	/**
	 * 结束说话进行语音识别
	 * 
	 * @return
	 */
	public String EndSpeaking() {
		try {
			mIat.stopListening();
			StringBuffer resultBuffer = new StringBuffer();
			for (String key : mIatResults.keySet()) {
				resultBuffer.append(mIatResults.get(key));
			}
			return resultBuffer.toString();
		} catch (Exception e) {
			return "";
		}

	}

	public void CancelSpeaking() {
		try {
			mIat.cancel();
		} catch (Exception e) {
		}

	}

	private void setParamForSpeaking() {
		// 清空参数
		mTts.setParameter(SpeechConstant.PARAMS, null);
		// 设置合成
		mTts.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
		// 设置发音人 voicer为空默认通过语音+界面指定发音人。
		mTts.setParameter(SpeechConstant.VOICE_NAME, "");
	}

	/**
	 * 参数设置
	 * 
	 * @语音识别配置
	 * @param param
	 * @return
	 */
	private void setParamForWriting() {
		// 清空参数
		mIat.setParameter(SpeechConstant.PARAMS, null);
		// 设置听写引擎
		mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
		// 设置返回结果格式
		mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");
		// 设置语言
		mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
		// 设置语言区域
		mIat.setParameter(SpeechConstant.ACCENT, "mandarin");
		// 设置语音前端点
		mIat.setParameter(SpeechConstant.VAD_BOS, "5000");
		// 设置语音后端点
		mIat.setParameter(SpeechConstant.VAD_EOS, "1800");
		// 设置标点符号
		mIat.setParameter(SpeechConstant.ASR_PTT, "1");
		// 设置音频保存路径
		mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH,
				Environment.getExternalStorageDirectory()
						+ "/iflytek/wavaudio.pcm");
		// 设置听写结果是否结果动态修正，为“1”则在听写过程中动态递增地返回结果，否则只在听写结束之后返回最终结果
		// 注：该参数暂时只对在线听写有效
		mIat.setParameter(SpeechConstant.ASR_DWA, "0");
	}
}
