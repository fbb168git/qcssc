package com.fbb.model;

import java.util.ArrayList;

import com.fbb.bean.Lottery;
import com.fbb.bean.Trade;
import com.fbb.util.LogUtil;

public class TradeModel {
	private static final float mInitialBalance = 10000f;
	private float mCurrentBalance = mInitialBalance;
	private float[] mLadderPrice = {10,30,70,160,350,800};//TODO get best price ladder
	private int startTradeSerialCount = 2;
	private int endTradeCount = 8;
	private float rate = 1.92f;
	
	private ArrayList<Trade> mTradeCache = new ArrayList<Trade>();
	
	public void addTrade(int SerialCount, Lottery currentLottery) {
		if(SerialCount >= startTradeSerialCount && SerialCount < endTradeCount) {
			Trade trade = new Trade();
			trade.setCurrentLottery(currentLottery);
			trade.setTradeAmount(mLadderPrice[mTradeCache.size()]);
			mTradeCache.add(trade);
			
			mCurrentBalance -= trade.getTradeAmount();
			LogUtil.d(currentLottery.getLottery_code()+"期投入 -"+trade.getTradeAmount()+" 余额:"+mCurrentBalance);
		} else if(SerialCount >= endTradeCount) {
			LogUtil.d("止损"+" 余额:"+mCurrentBalance);
			LogUtil.d("-----------------------------------------------------");
		}
	}
	
	public void endTrade(int SerialCount, Lottery currentLottery) {
		if(mTradeCache.size() > 0) {
			if(SerialCount - startTradeSerialCount + 1 == mTradeCache.size()) {
				Trade trade = mTradeCache.get(mTradeCache.size() - 1);
				trade.setGetProfit(true);
				
				float profit = trade.getTradeAmount() * rate;
				mCurrentBalance += profit;
				LogUtil.d(currentLottery.getLottery_code()+"期收入 +"+profit+" 余额:"+mCurrentBalance);
				LogUtil.d("-----------------------------------------------------");
			}
			computeProfit();
			mTradeCache.clear();
		}
		
	}
	
	private void computeProfit() {
		
	}
	
}
