package com.camera.video.view;

import java.io.IOException;

/** 
* @ClassName: VideoPlayerOperation 
* @Description:  视频播放器操作接口
* @author sx
* @date 2015-1-27 下午1:59:38 
*  
*/
public interface VideoPlayerOperation {

	boolean isPlaying();

	int getCurrentPosition();


	void seekPosition(int position);

	void stopPlay();

	void playVideo(String path) throws IllegalArgumentException, SecurityException,
			IllegalStateException, IOException;

	void pausedPlay();
	void resumePlay();

}
