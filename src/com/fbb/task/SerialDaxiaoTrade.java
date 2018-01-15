package com.fbb.task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimerTask;

import com.fbb.bean.Lottery;
import com.fbb.dao.LotteryDao;
import com.fbb.model.TradeModel;
import com.fbb.model.TradeModel2;
import com.fbb.model.TradeModel3;
import com.fbb.util.DataUtil;
import com.fbb.util.LogUtil;
import com.fbb.util.SMSUtil;

public class SerialDaxiaoTrade extends TimerTask{
	static final int TipsCount = 5;
	static final int TradeStartCount = 2;
	ArrayList<Lottery> cacheQian2 = new ArrayList<Lottery>();
	ArrayList<Lottery> cacheHou2 = new ArrayList<Lottery>();
	int cacheStatusQian2 = 0;//1 小 2 大
	int cacheStatusHou2 = 0;//1 小 2 大
	
	String currentDate = null;
	HashMap<Integer,Integer> serailStatistics = new HashMap<Integer,Integer>();
	
//	TradeModel trade  = new TradeModel();
	TradeModel2 trade  = new TradeModel2();
//	TradeModel3 trade  = new TradeModel3();
	
	private void init() {
		cacheHou2.clear();
		cacheQian2.clear();
		cacheStatusQian2 = 0;
		cacheStatusHou2 = 0;
		currentDate = null;
		serailStatistics.clear();
	}
	
	public void start() {
		LogUtil.d("task start...");
		init();
		LotteryDao lotteryDao = new LotteryDao();
		ArrayList<Lottery> lotterys = lotteryDao.getByStartDate("2016-01-01", "2016-12-31");
		
		boolean tag = true;
		Lottery preLottery = null;
		while(tag) {
//			Lottery latestLottery = DataUtil.requestLatestData();
			if(lotterys.size() <= 0) break;
			Lottery latestLottery = lotterys.get(0);
			lotterys.remove(0);
			if(preLottery == null) {
				preLottery = latestLottery;
//				LogUtil.d(latestLottery.getLottery_time()+" | 第"+latestLottery.getLottery_code()+"期开："+latestLottery.getLottery_nums());
			}
			if(latestLottery != null) {
				if(!latestLottery.getLottery_code().equalsIgnoreCase(preLottery.getLottery_code())) {
//					System.out.println();
//					LogUtil.d(latestLottery.getLottery_time()+" | 第"+latestLottery.getLottery_code()+"期开："+latestLottery.getLottery_nums());
					operate(latestLottery);
					preLottery = latestLottery;
				}
			}
//			System.out.print(".");
//			int HOUR_OF_DAY = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
//			if(HOUR_OF_DAY >= 2 && HOUR_OF_DAY <= 9) {
//				tag = false;
//				LogUtil.d("task stop...");
//				continue;
//			}
//			try {
//				Thread.sleep(10 * 1000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//				LogUtil.e(e.getMessage());
//			}
		}
	}
	private void operate(Lottery lottery) {
		if(!lottery.getLottery_date().equalsIgnoreCase(currentDate)) {//TODO 
			cacheHou2.clear();
			cacheStatusHou2 = 0;
			cacheQian2.clear();
			cacheStatusQian2 = 0;
			currentDate = lottery.getLottery_date();
		}
		
		int lotteryStatus = getHou2Status(lottery);
		if(lotteryStatus == cacheStatusHou2){
			cacheHou2.add(lottery);
			notifyListenerHou2();
			trade.addTrade(cacheHou2.size(), lottery);
		} else {
			trade.endTrade(cacheHou2.size(), lottery);
			cacheHou2.clear();
			cacheHou2.add(lottery);
			cacheStatusHou2 = lotteryStatus;
			
		}
		
		lotteryStatus = getQian2Status(lottery);
		if(lotteryStatus == cacheStatusQian2) {
			cacheQian2.add(lottery);
//			notifyListenerQian2();
		} else {
			cacheQian2.clear();
			cacheQian2.add(lottery);
			cacheStatusQian2 = lotteryStatus;
		}
	}
	
	//监控hou2
	private void notifyListenerHou2() {
		if(cacheHou2.size() >= TradeStartCount) {
			Lottery lottery = cacheHou2.get(cacheHou2.size() - 1);
			String smsMsg = lottery.getLottery_code().substring(2)+"后二出"+cacheHou2.size()+"连"+(cacheStatusHou2 == 1 ? "小":"大");
			String logMsg = smsMsg;
//			LogUtil.d(logMsg);
//			for(Lottery bean : cacheHou2) {
//				LogUtil.d("---"+bean.getLottery_code()+"期"+bean.getLottery_nums());
				
//			}
//			SMSUtil.sendSms(smsMsg, lottery.getLottery_nums(), lottery.getLottery_time());
		}
	}
	
	//监控qian2
	private void notifyListenerQian2() {
		if(cacheQian2.size() >= TipsCount) {
			Lottery lottery = cacheQian2.get(cacheQian2.size() - 1);
			String smsMsg = lottery.getLottery_code().substring(2)+"前二出"+cacheQian2.size()+"连"+(cacheStatusQian2 == 1 ? "小":"大");
			String logMsg = smsMsg+" "+lottery.getLottery_time();
			LogUtil.d(logMsg);
			for(Lottery bean : cacheQian2) {
				LogUtil.d("---"+bean.getLottery_code()+"期"+bean.getLottery_nums());
			}
//			SMSUtil.sendSms(smsMsg, lottery.getLottery_nums(), lottery.getLottery_time());
		}
	}
	
	//统计
	private void notifyStatistics() {
		if(cacheHou2.size() >= 3) {
			 if(serailStatistics.get(cacheHou2.size()) == null) {
				 serailStatistics.put(cacheHou2.size(), 1);
			 } else {
				 serailStatistics.put(cacheHou2.size(), serailStatistics.get(cacheHou2.size())+1);
			 }
		}
	}
	
	private void printStatistics() {
		Set<Entry<Integer,Integer>> entrySet = serailStatistics.entrySet();
		Iterator<Entry<Integer, Integer>> iterator = entrySet.iterator();
		
		while(iterator.hasNext()){
			Entry<Integer, Integer> next = iterator.next();
			Integer key = next.getKey();
			Integer value = next.getValue();
			LogUtil.d(key+"连出现"+value+"次");
		}
		
	}
	
	private int getHou2Status(Lottery lottery){
		if(lottery.getHou2() < 50 ){
			return 1;
		} else {
			return 2;
		}
	}
	
	private int getQian2Status(Lottery lottery){
		if(lottery.getQian2() < 50 ){
			return 1;
		} else {
			return 2;
		}
	}

	@Override
	public void run() {
		start();
	}
	

}
