package com.fbb.bean;


public class Lottery {
	private String lottery_code;
	private String lottery_nums;
	private String lottery_date;
	private String lottery_time;
	
	private int hou1;
	private int hou2;
	private int hou3;
	private int qian1;
	private int qian2;
	private int qian3;
	
	public Lottery() {
	}
	
	public Lottery(String lottery_code, String lottery_nums, String lottery_date) {
		this.lottery_code = lottery_code;
		this.lottery_nums = lottery_nums;
		this.lottery_date = lottery_date;
		init();
	}
	
	public Lottery(String lottery_code, String lottery_nums,
			String lottery_date, String lottery_time) {
		super();
		this.lottery_code = lottery_code;
		this.lottery_nums = lottery_nums;
		this.lottery_date = lottery_date;
		this.lottery_time = lottery_time;
		init();
	}
	
	private void init() {
		String[] numbers = lottery_nums.split(",");
		StringBuilder builder = new StringBuilder();
		builder.append(numbers[4]);
		hou1 = new Integer(builder.toString());
		builder.insert(0, numbers[3]);
		hou2 = new Integer(builder.toString());
		builder.insert(0, numbers[2]);
		hou3 = new Integer(builder.toString());
		builder.delete(0, builder.length());
		builder.append(numbers[0]);
		qian1 = new Integer(builder.toString());
		builder.append(numbers[1]);
		qian2 = new Integer(builder.toString());
		builder.append(numbers[2]);
		qian3 = new Integer(builder.toString());
	}

	public int getHou1() {
		return hou1;
	}

	public void setHou1(int hou1) {
		this.hou1 = hou1;
	}

	public int getHou2() {
		return hou2;
	}

	public void setHou2(int hou2) {
		this.hou2 = hou2;
	}

	public int getHou3() {
		return hou3;
	}

	public void setHou3(int hou3) {
		this.hou3 = hou3;
	}

	public int getQian1() {
		return qian1;
	}

	public void setQian1(int qian1) {
		this.qian1 = qian1;
	}

	public int getQian2() {
		return qian2;
	}

	public void setQian2(int qian2) {
		this.qian2 = qian2;
	}

	public int getQian3() {
		return qian3;
	}

	public void setQian3(int qian3) {
		this.qian3 = qian3;
	}

	public String getLottery_date() {
		return lottery_date;
	}

	public void setLottery_date(String lottery_date) {
		this.lottery_date = lottery_date;
	}

	public String getLottery_time() {
		return lottery_time;
	}

	public void setLottery_time(String lottery_time) {
		this.lottery_time = lottery_time;
	}

	public String getLottery_code() {
		return lottery_code;
	}
	public void setLottery_code(String lottery_code) {
		this.lottery_code = lottery_code;
	}
	public String getLottery_nums() {
		return lottery_nums;
	}
	public void setLottery_nums(String lottery_nums) {
		this.lottery_nums = lottery_nums;
	}
	
	
}
