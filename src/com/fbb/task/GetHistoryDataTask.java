package com.fbb.task;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.fbb.bean.Lottery;
import com.fbb.dao.LotteryDao;

public class GetHistoryDataTask {
	// public static final String url =
	// "http://apis.haoservice.com/lifeservice/HighLottery/query";
	// public static final String key ="9c525e706deb416588477d0b1c76793d";
	// public static final String type = "cqssc";

	public void start() {
//		ArrayList<String> dateStrings = getDateStrings("2010-01-01",
//				"2017-04-19");
		ArrayList<String> dateStrings = getDateStrings("2017-04-19",
				"2017-04-20");
		LotteryDao lotteryDao = new LotteryDao();
		int successCount = 0;
		int failCount = 0;
		for (String dateString : dateStrings) {

			System.out.println("--------" + dateString + "--------");
			ArrayList<Lottery> oneDayData = requestDataFromBaidu(dateString);
			if (oneDayData != null && oneDayData.size() > 0) {
				int successNum = lotteryDao.addOrUpdateBeanList(oneDayData);
				System.out.println("成功插入" + dateString + "的数据" + successNum
						+ "条");
				successCount++;
			} else {
				System.err.println("请求百度数据出错！");
				failCount++;
			}
		}
		System.out.println("有数据的天数：" + successCount + " 无数据的天数：" + failCount);
	}

	private ArrayList<String> getDateStrings(String start, String end) {
		if ((start == null || "".equalsIgnoreCase(start))) {
			return null;
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		if (end == null || "".equalsIgnoreCase(end)) {
			end = format.format(new Date());
		}
		Date startDate = null;
		Date endDate = null;
		try {
			startDate = format.parse(start);
			endDate = format.parse(end);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(startDate);
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(endDate);

		ArrayList<String> result = new ArrayList<String>();
		do {
			String dateText = format.format(startCal.getTime());
			result.add(dateText);
			// System.out.println(dateText);
			int day = startCal.get(Calendar.DAY_OF_MONTH);
			startCal.set(Calendar.DAY_OF_MONTH, day + 1);
		} while (startCal.getTimeInMillis() <= endCal.getTimeInMillis());
		return result;
	}

	private ArrayList<Lottery> requestDataFromBaidu(String date) {
		ArrayList<Lottery> result = new ArrayList<Lottery>();
		try {
			Document doc = Jsoup.connect(
					"http://baidu.lecai.com/lottery/draw/list/200?d=" + date)
					.get();
			Element content = doc.getElementById("draw_list");
			Elements links = content.getElementsByTag("tr");
			for (int i = 1; i < links.size(); i++) {
				Element line = links.get(i);
				Elements lottery_date_elements = line.getElementsByClass("td1");
				Elements lottery_no_elements = line.getElementsByClass("td2");
				Elements numbers_elements = line.getElementsByClass("td3");

				// String date = lottery_date_elements.get(0).text();
				String lottery_no = lottery_no_elements.get(0).text();
				// System.out.println("date:"+date);
				// System.out.println("lottery_no:"+lottery_no);

				Element numbers_element = numbers_elements.get(0);
				Elements ball_elements = numbers_element
						.getElementsByClass("ball_1");
				StringBuilder builder = new StringBuilder();
				for (int j = 0; j < ball_elements.size(); j++) {
					Element ball = ball_elements.get(j);
					String number = ball.text();
					builder.append(number);
					if (j != ball_elements.size() - 1) {
						builder.append(",");
					}
				}
				String numbers = builder.toString();
				result.add(new Lottery(lottery_no, numbers, date));
				// System.out.println("numbers:"+numbers);
				// System.out.println();

			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
