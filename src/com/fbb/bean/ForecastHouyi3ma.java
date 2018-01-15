package com.fbb.bean;

import java.util.ArrayList;


public class ForecastHouyi3ma {
	private String start_code;
	private String start_nums;
	private String start_date;
	
	private int hitNo;//已预测的期数
	private boolean success = false;
	private String hit_code;
	private String hit_nums;
	private String hit_date;
	
	private String[] houyi3ma;
	
	private ArrayList<Lottery> historys = new ArrayList<Lottery>();
	
	
	public ForecastHouyi3ma(String start_code, String start_nums, String start_date) {
		super();
		this.start_code = start_code;
		this.start_nums = start_nums;
		this.start_date = start_date;
	}
	
	public ForecastHouyi3ma(String start_code, String start_nums,
			String start_date, String[] houyi3ma) {
		super();
		this.start_code = start_code;
		this.start_nums = start_nums;
		this.start_date = start_date;
		this.houyi3ma = houyi3ma;
	}
	
	public void addHistory(Lottery bean){
		historys.add(bean);
	}
	
	public void printHistory() {
		System.out.println("第"+start_code+"期开"+start_nums);
		for(Lottery bean : historys) {
			System.out.println("---第"+bean.getLottery_code()+"期开"+bean.getLottery_nums());
		}
	}
	
	public String getStart_code() {
		return start_code;
	}
	public void setStart_code(String start_code) {
		this.start_code = start_code;
	}
	public String getStart_nums() {
		return start_nums;
	}
	public void setStart_nums(String start_nums) {
		this.start_nums = start_nums;
	}
	public String getStart_date() {
		return start_date;
	}
	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}
	
	public String[] getHouyi3ma() {
		return houyi3ma;
	}

	public void setHouyi3ma(String[] houyi3ma) {
		this.houyi3ma = houyi3ma;
	}

	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public int getHitNo() {
		return hitNo;
	}
	public void setHitNo(int hitNo) {
		this.hitNo = hitNo;
	}
	public String getHit_code() {
		return hit_code;
	}
	public void setHit_code(String hit_code) {
		this.hit_code = hit_code;
	}
	public String getHit_nums() {
		return hit_nums;
	}
	public void setHit_nums(String hit_nums) {
		this.hit_nums = hit_nums;
	}
	public String getHit_date() {
		return hit_date;
	}
	public void setHit_date(String hit_date) {
		this.hit_date = hit_date;
	}
	
	
	
}
