package com.fbb.qcssc;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

import com.fbb.task.SerialDaxiaoListener;
import com.fbb.task.SerialDaxiaoTrade;
import com.fbb.util.FileUtil;
import com.fbb.util.LogUtil;


public class Main {
	private final static int TaskStartTime = 10;
	
	public static final String historyFilePath = FileUtil.getProjectResPath()+"/history.txt";
	public static void main(String[] args) {
//		new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
////				new GetHistoryDataTask().start();
////				new ProbalityHouyi3maTask().start();
////				new ProbalityDaxiaoDanshuang().start();
//					new SerialDaxiao().start();
////				new SerialDaxiaoListener().start();
////				new SerialDaxiaoTrade().start();
////				SMSUtil.sendSms();
//			}
//		}).start();
		
		
		Calendar cal = Calendar.getInstance();
		int DAY_OF_MONTH = cal.get(Calendar.DAY_OF_MONTH);
		int HOUR_OF_DAY = cal.get(Calendar.HOUR_OF_DAY);
		int MINUTE = cal.get(Calendar.MINUTE);
		LogUtil.d("currentTime:"+DAY_OF_MONTH + "号 " + HOUR_OF_DAY+":" + MINUTE);
		
		
		cal.set(Calendar.HOUR_OF_DAY, TaskStartTime);
        cal.set(Calendar.MINUTE, 5);
        cal.set(Calendar.SECOND, 0);        
		
		
		
		Timer mainTimer = new Timer();
		SerialDaxiaoListener serialDaxiaoListenerTask = new SerialDaxiaoListener();
		Date time = cal.getTime();
		mainTimer.scheduleAtFixedRate(serialDaxiaoListenerTask, time, 1000 * 60 * 60 * 24);
		
	}
	
	
	
	
}
