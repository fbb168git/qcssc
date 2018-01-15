package com.fbb.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import com.fbb.bean.Lottery;
import com.fbb.dao.LotteryDao;

public class SerialDaxiaoStatistic {
	public void start() {
		LotteryDao lotteryDao = new LotteryDao();
		ArrayList<Lottery> lotterys = lotteryDao.getByStartDate("2017-01-01", "2017-4-18");
		
		
		for (int i = 0; i < lotterys.size(); i++) {
			Lottery lottery = lotterys.get(i);
			operate(lottery);
		}
		
		printStatistics();
	}
	
	ArrayList<Lottery> cache = new ArrayList<Lottery>();
	int cacheStatus = 0;//1 小 2 大
	String currentDate = null;
	private void operate(Lottery lottery) {
		if(!lottery.getLottery_date().equalsIgnoreCase(currentDate)) {
			cache.clear();
			cacheStatus = 0;
			currentDate = lottery.getLottery_date();
		}
		int lotteryStatus = getHou2Status(lottery);
		if(lotteryStatus == cacheStatus) {
			cache.add(lottery);
			notifyListener();
		} else {
			//TODO 统计
			notifyStatistics();
			cache.clear();
			cache.add(lottery);
			cacheStatus = lotteryStatus;
		}
	}
	
	//监控
	private void notifyListener() {
		if(cache.size() >= 3) {
			System.out.println(cache.get(cache.size() - 1).getLottery_code()+"期出现"+cache.size()+"连"+(cacheStatus == 1 ? "小":"大")+""+cache.get(cache.size() - 1));
			for(Lottery lottery : cache){
				System.out.println("---"+lottery.getLottery_code()+"期"+lottery.getLottery_nums());
			}
			
		}
	}
	
	HashMap<Integer,Integer> serailStatistics = new HashMap<Integer,Integer>();
	//统计
	private void notifyStatistics() {
		if(cache.size() >= 3) {
			 if(serailStatistics.get(cache.size()) == null) {
				 serailStatistics.put(cache.size(), 1);
			 } else {
				 serailStatistics.put(cache.size(), serailStatistics.get(cache.size())+1);
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
			System.out.println(key+"连出现"+value+"次");
		}
		
	}
	
	private int getHou2Status(Lottery lottery){
		if(lottery.getHou2() < 50 ){
			return 1;
		} else {
			return 2;
		}
		
	}
	

}
