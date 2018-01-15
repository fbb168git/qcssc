package com.fbb.task;

import java.util.ArrayList;

import com.fbb.bean.ForecastHouyi3ma;
import com.fbb.bean.Lottery;
import com.fbb.dao.LotteryDao;

public class ProbalityDaxiaoDanshuang {
	public void start() {
		houyi3ma();
	}
	
	public void houyi3ma() {
		LotteryDao lotteryDao = new LotteryDao();
		ArrayList<Lottery> lotterys = lotteryDao.getByStartDate("2016-01-01", "2017-3-31");
		
		ArrayList<ForecastHouyi3ma> total = new ArrayList<ForecastHouyi3ma>();
		ArrayList<ForecastHouyi3ma> waitList = new ArrayList<ForecastHouyi3ma>();
		ArrayList<ForecastHouyi3ma> successList = new ArrayList<ForecastHouyi3ma>();
		ArrayList<ForecastHouyi3ma> failList = new ArrayList<ForecastHouyi3ma>();
		
		ArrayList<ForecastHouyi3ma> total2 = new ArrayList<ForecastHouyi3ma>();
		ArrayList<ForecastHouyi3ma> waitList2 = new ArrayList<ForecastHouyi3ma>();
		ArrayList<ForecastHouyi3ma> successList2 = new ArrayList<ForecastHouyi3ma>();
		ArrayList<ForecastHouyi3ma> failList2 = new ArrayList<ForecastHouyi3ma>();
		
		ArrayList<ForecastHouyi3ma> total3 = new ArrayList<ForecastHouyi3ma>();
		ArrayList<ForecastHouyi3ma> waitList3 = new ArrayList<ForecastHouyi3ma>();
		ArrayList<ForecastHouyi3ma> successList3 = new ArrayList<ForecastHouyi3ma>();
		ArrayList<ForecastHouyi3ma> failList3 = new ArrayList<ForecastHouyi3ma>();
		ArrayList<ForecastHouyi3ma> overList3 = new ArrayList<ForecastHouyi3ma>();
		
		for (int i = 0; i < lotterys.size(); i++) {
			Lottery lottery = lotterys.get(i);
			
			for (int j = 0; j < waitList3.size(); j++) {
				ForecastHouyi3ma foreBean = waitList3.get(j);
				if (!foreBean.getStart_date().equalsIgnoreCase(lottery.getLottery_date())) {
					overList3.add(foreBean);
					continue;
				}
				boolean success = vertifyForecast(foreBean, lottery);
				if (success) {
					successList3.add(foreBean);
					printForeSuccessDetail(foreBean);
				} else {
					foreBean.printHistory();
					System.out.println("---------------------");
					if (foreBean.getHitNo() == 3) {
						failList3.add(foreBean);
					}
				}
			}
			waitList3.removeAll(successList3);
			waitList3.removeAll(failList3);
			waitList3.removeAll(overList3);
			//--------------------------------------------
			for (int j = 0; j < waitList2.size(); j++) {
				ForecastHouyi3ma foreBean = waitList2.get(j);
				if (!foreBean.getStart_date().equalsIgnoreCase(lottery.getLottery_date())) {
					failList2.add(foreBean);
					continue;
				}
				boolean success = vertifyForecast(foreBean, lottery);
				if (success) {
					successList2.add(foreBean);
					printForeSuccessDetail(foreBean);
				} else {
					if(foreBean.getHitNo() == 2){
						failList2.add(foreBean);
						
						total3.add(foreBean);
						waitList3.add(foreBean);
					}
				}
			}
			waitList2.removeAll(successList2);
			waitList2.removeAll(failList2);
			//--------------------------------------------
			for (int j = 0; j < waitList.size(); j++) {
				ForecastHouyi3ma foreBean = waitList.get(j);
				if (!foreBean.getStart_date().equalsIgnoreCase(lottery.getLottery_date())) {
					failList.add(foreBean);
					continue;
				}
				boolean success = vertifyForecast(foreBean, lottery);
				if (success) {
					successList.add(foreBean);
					printForeSuccessDetail(foreBean);
				} else {
					if (foreBean.getHitNo() == 1) {
						failList.add(foreBean);
						
						total2.add(foreBean);
						waitList2.add(foreBean);
					}
				}
			}
			waitList.removeAll(successList);
			waitList.removeAll(failList);
			

			
			if (lottery.getHou2() < 50) {
				ForecastHouyi3ma bean = new ForecastHouyi3ma(
						lottery.getLottery_code(), lottery.getLottery_nums(),
						lottery.getLottery_date());
				waitList.add(bean);
				total.add(bean);
			}
		}
		
		System.out.println("共" + total.size() + "条");
		System.out.println("命中" + successList.size() + "条");
		System.out.println("失败" + failList.size() + "条");
		System.out.println("successList2:" + successList2.size()+" failList2:"+failList2.size() + " total2:"+total2.size());
		System.out.println("2016-01-01到2017-3-31");
		System.out.println("两次开小之后，第三次开“大”:" + successList3.size() +" 开小:"+failList3.size() +" 过期:"+overList3.size() + " 共:"+total3.size());
		
		System.out.println("1hit:" + successList.size() + " 占比：" + (100f * (float)successList.size() /(float)total.size())
				+ "%");
		System.out.println("2hit:" + successList2.size() + " 占比：" + (100f * (float)successList2.size() /(float)total2.size())
				+ "%");
		System.out.println("3hit:" + successList3.size() + " 占比：" + (100f * (float)successList3.size() /(float)total3.size())
				+ "%");
		
	}

	private boolean vertifyForecast(ForecastHouyi3ma foreBean, Lottery bean) {
//		if (!foreBean.getStart_date().equalsIgnoreCase(bean.getLottery_date())) {//TODO you bug, fix it
//			return false;
//		}
		foreBean.setHitNo(foreBean.getHitNo() + 1);
		foreBean.addHistory(bean);
		if(bean.getHou2()>=50){
			return true;
		}
		return false;
	}

	private void printForeSuccessDetail(ForecastHouyi3ma foreBean) {
//		System.out.println("-------" + foreBean.getStart_code() + "-------");
//		System.out.println("开" + foreBean.getStart_nums() + " 后三："
//				+ foreBean.getHouyi3ma()[0] + foreBean.getHouyi3ma()[1]
//				+ foreBean.getHouyi3ma()[2]);
//		System.out.println("---" + foreBean.getHitNo() + "期后"
//				+ foreBean.getHit_code() + "期命中 " + foreBean.getHit_nums());
		foreBean.printHistory();
		System.out.println("---------------------");
	}


}
