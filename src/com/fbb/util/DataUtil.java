package com.fbb.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.fbb.bean.Lottery;

public class DataUtil {
	
	private static SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss");
	private static StringBuilder builder = new StringBuilder();
	public static Lottery requestLatestData() {
		Lottery result = null;
		String date = formatDate.format(new Date());
		String time = formatTime.format(new Date());
		builder.delete(0, builder.length());
		try {
			Document doc = Jsoup.connect("http://www.cqcp.net/game/ssc/").get();
			Element content = doc.getElementById("openlist");
			Elements links = content.getElementsByTag("ul");
			Element element = links.get(1);
			Elements li = element.getElementsByTag("li");
			String lattery_code = li.get(0).text();
			String lattery_numbers = li.get(1).text();
			String[] split = lattery_numbers.split("-");
			for(int i = 0;i < split.length; i++){
				builder.append(split[i]);
				if(i != split.length - 1) builder.append(",");
			}
			lattery_numbers = builder.toString();
			result = new Lottery(lattery_code, lattery_numbers, date, time);
//			System.out.println(lattery_code+" "+lattery_numbers+" "+time);
		} catch (IOException e) {
			e.printStackTrace();
			LogUtil.e("requestLatestData: IOException "+e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.e("requestLatestData: IOException "+e.getMessage());
		}
		return result;
	}
}
