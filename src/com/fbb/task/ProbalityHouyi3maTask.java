package com.fbb.task;

import java.util.ArrayList;

import com.fbb.bean.ForecastHouyi3ma;
import com.fbb.bean.Lottery;
import com.fbb.dao.LotteryDao;

public class ProbalityHouyi3maTask {
	public void start() {
		houyi3ma();
	}
	
	public void houyi3ma() {
		LotteryDao lotteryDao = new LotteryDao();
		ArrayList<Lottery> lotterys = lotteryDao.getByStartDate("2017-01-01", "2017-01-20");
		ArrayList<ForecastHouyi3ma> total1 = new ArrayList<ForecastHouyi3ma>();
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
		
		int count = 0;
		int count2 = 0;
		int count3 = 0;
		for (int i = 0; i < lotterys.size(); i++) {
			Lottery lottery = lotterys.get(i);
			for (int j = 0; j < waitList.size(); j++) {
				ForecastHouyi3ma foreBean = waitList.get(j);
				boolean success = vertifyForecast(foreBean, lottery);
				if (success) {
					successList.add(foreBean);
					printForeSuccessDetail(foreBean);
					if(foreBean.getHitNo() == 2) {//TODO &&waitList2.contains(foreBean);
						successList2.add(foreBean);
					}
					if(foreBean.getHitNo() == 3) {//TODO &&waitList2.contains(foreBean);
						successList3.add(foreBean);
					}
				} else {
					if (foreBean.getHitNo() >= 6)
						failList.add(foreBean);
					if(foreBean.getHitNo() == 1) {
						waitList2.add(foreBean);
						if(!total2.contains(foreBean)){
							total2.add(foreBean);
						}
						count2++;
					}
					if(foreBean.getHitNo() == 2) {
						failList2.add(foreBean);
						
						waitList3.add(foreBean);
						if(!total3.contains(foreBean)){
							total3.add(foreBean);
						}
						count3++;
					}
					if(foreBean.getHitNo() == 3) {
						failList3.add(foreBean);
					}
				}
			}
			waitList.removeAll(successList);
			waitList.removeAll(failList);
			waitList2.removeAll(successList2);
			waitList2.removeAll(failList2);
			waitList3.removeAll(successList3);
			waitList3.removeAll(failList3);

			String[] gethouyi3ma = gethouyi3ma(lottery.getLottery_nums());
			if (gethouyi3ma != null) {
				count++;
				ForecastHouyi3ma bean = new ForecastHouyi3ma(
						lottery.getLottery_code(), lottery.getLottery_nums(),
						lottery.getLottery_date(), gethouyi3ma);
				waitList.add(bean);
				total1.add(bean);
			}
		}
		System.out.println("共" + count + "条");
		System.out.println("命中" + successList.size() + "条");
		System.out.println("失败" + failList.size() + "条");
		
		float successCount1 = 0;
		float successCount2 = 0;
		float successCount3 = 0;
		float successCount4 = 0;
		float successCount5 = 0;
		float successCount6 = 0;
		for (ForecastHouyi3ma foreBean : successList) {
			if (foreBean.isSuccess()) {
				if (foreBean.getHitNo() == 1) {
					successCount1++;
				} else if (foreBean.getHitNo() == 2) {
					successCount2++;
				} else if (foreBean.getHitNo() == 3) {
					successCount3++;
				} else if (foreBean.getHitNo() == 4) {
					successCount4++;
				} else if (foreBean.getHitNo() == 5) {
					successCount5++;
				} else if (foreBean.getHitNo() == 6) {
					successCount6++;
				}
			}
		}
		float den = new Float(count);
		System.out.println("1hit:" + successCount1 + " 占比：" + (100f * successCount1 / den)
				+ "%");
		System.out.println("2hit:" + successCount2 + " 占比：" + (100f * successCount2 / den)
				+ "%");
		System.out.println("3hit:" + successCount3 + " 占比：" + (100f * successCount3 / den)
				+ "%");
		System.out.println("4hit:" + successCount4 + " 占比：" + (100f * successCount4 / den)
				+ "%");
		System.out.println("5hit:" + successCount5 + " 占比：" + (100f * successCount5 / den)
				+ "%");
		System.out.println("6hit:" + successCount6 + " 占比：" + (100f * successCount6 / den)
				+ "%");
		System.out.println("3qi:" + successCount6 + " 占比："
				+ (100f * (successCount1 + successCount2 + successCount3) / den) + "%");
		System.out.println("shibai:" + successCount6 + " 占比："
				+ (100f * (failList.size()) / den) + "%");
		System.out.println("3qishibai:" + successCount6 + " 占比："
				+ (100f * (successCount4 + successCount5 + successCount6 + failList.size()) / den)
				+ "%");
		System.out.println("-------------------------------");
		System.out.println("count1:" + count + " count2："+count2+" count3:"+count3);
		System.out.println("total1:" + total1.size() + " total2："+total2.size()+" total3:"+total3.size());
		System.out.println("1no2yes:" + successList2.size() + " 占比："
				+ (100f * (successList2.size()) / (float)total2.size())
				+ "%");
		System.out.println("12no3yes:" + successList3.size() + " 占比："
				+ (100f * (successList3.size()) / (float)total3.size())
				+ "%");
	}

	private boolean vertifyForecast(ForecastHouyi3ma foreBean, Lottery bean) {
//		if (!foreBean.getStart_date().equalsIgnoreCase(bean.getLottery_date())) {
//			return false;
//		}
		String[] hou3 = getHou3(bean.getLottery_nums());
		String[] houyi3ma = foreBean.getHouyi3ma();
		foreBean.setHitNo(foreBean.getHitNo() + 1);

		for (int i = 0; i < houyi3ma.length; i++) {
			if (houyi3ma[i].equalsIgnoreCase(hou3[2])) {
				foreBean.setHit_code(bean.getLottery_code());
				foreBean.setHit_nums(bean.getLottery_nums());
				foreBean.setHit_date(bean.getLottery_date());
				foreBean.setSuccess(true);
				return true;
			}
		}
		return false;
	}

	private void printForeSuccessDetail(ForecastHouyi3ma foreBean) {
		System.out.println("-------" + foreBean.getStart_code() + "-------");
		System.out.println("开" + foreBean.getStart_nums() + " 后三："
				+ foreBean.getHouyi3ma()[0] + foreBean.getHouyi3ma()[1]
				+ foreBean.getHouyi3ma()[2]);
		System.out.println("---" + foreBean.getHitNo() + "期后"
				+ foreBean.getHit_code() + "期命中 " + foreBean.getHit_nums());
		System.out.println("---------------------");
	}

	private String[] getHou3(String lottery_numbers) {
		if (lottery_numbers == null || lottery_numbers.equalsIgnoreCase("")) {
			return null;
		}
		String[] numbers = lottery_numbers.split(",");
		String[] result = new String[3];
		result[0] = numbers[2];
		result[1] = numbers[3];
		result[2] = numbers[4];
		return result;
	}

	private String[] gethouyi3ma(String lottery_numbers) {
		if (lottery_numbers == null || lottery_numbers.equalsIgnoreCase("")) {
			return null;
		}
		String[] numbers = lottery_numbers.split(",");
		StringBuilder builder = new StringBuilder();
		builder.append(numbers[2]);
		builder.append(numbers[3]);
		builder.append(numbers[4]);
		int number = new Integer(builder.toString());
		float hou1 = number * number / 32f;
		if (hou1 < 100) {
			return null;
		}
		String numberText = hou1 + "";
//		System.out.println(numberText);
		if (numberText.contains(".")) {
			numberText = numberText.substring(0, numberText.indexOf("."));
		}
		if (numberText.length() < 3)
			return null;
		ArrayList<String> resultArray = new ArrayList<String>();
//		String resultString = "";
		for (int i = 0; i < numberText.length(); i++) {
			String sub = numberText.substring(i, i + 1);
			if (!resultArray.contains(sub)) {
				resultArray.add(sub);
//				resultString += sub;
			}
			if (resultArray.size() >= 3)
				break;
		}
		if (resultArray.size() < 3) {
			return null;
		}
		String[] result = new String[3];
		result[0] = resultArray.get(0);
		result[1] = resultArray.get(1);
		result[2] = resultArray.get(2);
		// System.out.println("lottery:"+lottery_numbers+" 后三："+number+" 后三计算："+hou1+" 后一三码："+resultString);
		return result;
	}

	// 1hit:1694.0 占比：30.473106%
	// 2hit:1127.0 占比：20.27343%
	// 3hit:805.0 占比：14.481022%
	// 4hit:564.0 占比：10.14571%
	// 5hit:390.0 占比：7.0156503%
	// 6hit:240.0 占比：4.317323%

	// 1hit:4581.0 占比：30.24561%
	// 2hit:3080.0 占比：20.335402%
	// 3hit:2155.0 占比：14.228179%
	// 4hit:1539.0 占比：10.1610985%
	// 5hit:1051.0 占比：6.939126%
	// 6hit:693.0 占比：4.5754657%
	
	//3qimingzhong 64%  3qifail 36%
}
