/**
 * 文件状态监控类
 */
package com.moons.xst.track.common;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import android.os.FileObserver;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * 文件状态监控类，封装后可以用于监控指定文件夹下所有文件夹及文件状态
 * 
 * @author LKZ
 * @since 2015.03.07
 */
@SuppressWarnings(value = { "rawtypes", "unchecked" })
public class FileMonitor extends FileObserver {

	/** Only modification events */
	public static int CHANGES_ONLY = CREATE | MODIFY | DELETE | CLOSE_WRITE
			| DELETE_SELF | MOVE_SELF | MOVED_FROM | MOVED_TO;

	private List<SingleFileObserver> mObservers;
	private String mPath;
	private int mMask;
	private Handler _handler;

	public FileMonitor(String path) {
		this(path, ALL_EVENTS);
	}

	public FileMonitor(String path, int mask) {
		super(path, mask);
		mPath = path;
		mMask = mask;
	}

	public FileMonitor(String path, Handler handler) {
		super(path, ALL_EVENTS);
		mPath = path;
		mMask = ALL_EVENTS;
		this._handler = handler;
	}

	@Override
	public void startWatching() {
		if (mObservers != null)
			return;

		mObservers = new ArrayList<SingleFileObserver>();
		Stack<String> stack = new Stack<String>();
		stack.push(mPath);

		while (!stack.isEmpty()) {
			String parent = stack.pop();
			mObservers.add(new SingleFileObserver(parent, mMask));
			File path = new File(parent);
			File[] files = path.listFiles();
			if (null == files)
				continue;
			for (File f : files) {
				if (f.isDirectory() && !f.getName().equals(".")
						&& !f.getName().equals("..")) {
					stack.push(f.getPath());
				}
			}
		}

		for (int i = 0; i < mObservers.size(); i++) {
			SingleFileObserver sfo = mObservers.get(i);
			sfo.startWatching();
		}
	};

	@Override
	public void stopWatching() {
		if (mObservers == null)
			return;

		for (int i = 0; i < mObservers.size(); i++) {
			SingleFileObserver sfo = mObservers.get(i);
			sfo.stopWatching();
		}

		mObservers.clear();
		mObservers = null;
	};

	@Override
	public void onEvent(int event, String path) {
		sendMessage(event, path);
		switch (event) {
		case FileObserver.ACCESS:
			Log.i("FileMonitor", "ACCESS: " + path);
			break;
		case FileObserver.ATTRIB:
			Log.i("FileMonitor", "ATTRIB: " + path);
			break;
		case FileObserver.CLOSE_NOWRITE:
			Log.i("FileMonitor", "CLOSE_NOWRITE: " + path);
			break;
		case FileObserver.CLOSE_WRITE:
			Log.i("FileMonitor", "CLOSE_WRITE: " + path);
			break;
		case FileObserver.CREATE:
			Log.i("FileMonitor", "CREATE: " + path);
			break;
		case FileObserver.DELETE:
			Log.i("FileMonitor", "DELETE: " + path);
			break;
		case FileObserver.DELETE_SELF:
			Log.i("FileMonitor", "DELETE_SELF: " + path);
			break;
		case FileObserver.MODIFY:
			Log.i("FileMonitor", "MODIFY: " + path);
			break;
		case FileObserver.MOVE_SELF:
			Log.i("FileMonitor", "MOVE_SELF: " + path);
			break;
		case FileObserver.MOVED_FROM:
			Log.i("FileMonitor", "MOVED_FROM: " + path);
			break;
		case FileObserver.MOVED_TO:
			Log.i("FileMonitor", "MOVED_TO: " + path);
			break;
		case FileObserver.OPEN:
			Log.i("FileMonitor", "OPEN: " + path);
			break;
		default:
			Log.i("FileMonitor", "DEFAULT(" + event + " : " + path);
			break;
		}
	}

	private void sendMessage(int event, String filePath) {
		if (_handler != null) {
			Message msg = Message.obtain();
			msg.what = event;
			msg.obj = filePath;
			msg.arg1 = 0;
			msg.arg2 = 0;
			_handler.sendMessage(msg);
		}
	}

	/**
	 * Monitor single directory and dispatch all events to its parent, with full
	 * path.
	 */
	class SingleFileObserver extends FileObserver {
		String mPath;

		public SingleFileObserver(String path) {
			this(path, ALL_EVENTS);
			mPath = path;
		}

		public SingleFileObserver(String path, int mask) {
			super(path, mask);
			mPath = path;
		}

		@Override
		public void onEvent(int event, String path) {
			String newPath = mPath + "/" + path;
			FileMonitor.this.onEvent(event, newPath);
		}
	}
}
