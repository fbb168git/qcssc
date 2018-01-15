package com.fbb.model;

import java.util.ArrayList;

import com.fbb.bean.Lottery;
import com.fbb.bean.Trade;
import com.fbb.util.LogUtil;

public class TradeModel2 {
	private static final float mInitialBalance = 10000f;
	private float mCurrentBalance = mInitialBalance;
	private float[] mLadderPrice = {100,300,700,1600,3500,8000};//TODO get best price ladder
	
	private int startTradeSerialCount = 5;
	private int endTradeCount = 8;
	private float rate = 1.92f;
	
	private ArrayList<Trade> mTradeCache = new ArrayList<Trade>();
	
	boolean openTrade = false;
	boolean trading = false;
	int zhishunCount = 0;
	int yingliCount = 0;
	public void addTrade(int SerialCount, Lottery currentLottery) {
		if(openTrade) {
			if(SerialCount >= startTradeSerialCount && SerialCount < endTradeCount) {
				LogUtil.d(currentLottery.getLottery_code()+"期出现"+SerialCount+"连");
				String codeText = currentLottery.getLottery_code().substring(8);
				if(120 - (endTradeCount - startTradeSerialCount) < new Integer(codeText) ){
					trading = false;
					openTrade = false;
					LogUtil.d("期数不够 放弃交易， "+" 余额:"+mCurrentBalance);
					LogUtil.d("-----------------------------------------------------");
					return;
				}
				if(!trading) trading = true;
				Trade trade = new Trade();
				trade.setCurrentLottery(currentLottery);
				trade.setTradeAmount(mLadderPrice[mTradeCache.size()]);
				mTradeCache.add(trade);
				
				mCurrentBalance -= trade.getTradeAmount();
				LogUtil.d(currentLottery.getLottery_code()+"期投入 -"+trade.getTradeAmount()+" 余额:"+mCurrentBalance);
			} else if(SerialCount >= endTradeCount) {
				if(trading) {
					trading = false;
					openTrade = false;
					zhishunCount ++;
					LogUtil.d(currentLottery.getLottery_code()+"期出现"+SerialCount+"连");
					LogUtil.d("止损"+" 余额:"+mCurrentBalance);
					LogUtil.d("zhishunCount:"+zhishunCount);
					LogUtil.d("-----------------------------------------------------");
				}
			}
		} else {
			
		}
		
	}
	
	int lianxuda5Count = 0;
	public void endTrade(int SerialCount, Lottery currentLottery) {//SerialCount连已结束
		if(!openTrade) {
			if(SerialCount >= endTradeCount) {
				openTrade = true;
				LogUtil.d("-----------------------------------------------------");
				LogUtil.d(currentLottery.getLottery_code()+"期出现"+SerialCount+"连，等待出现"+startTradeSerialCount+"连");
				return;
			}
		} else {
			if(trading && mTradeCache.size() > 0) {
				if(SerialCount - startTradeSerialCount + 1 == mTradeCache.size()) {//无止损行为
					Trade trade = mTradeCache.get(mTradeCache.size() - 1);
					trade.setGetProfit(true);
					
					float profit = trade.getTradeAmount() * rate;
					mCurrentBalance += profit;
					LogUtil.d(currentLottery.getLottery_code()+"期收入 +"+profit+" 余额:"+mCurrentBalance);
					yingliCount++;
					LogUtil.d("yingliCount:"+yingliCount);
					LogUtil.d("-----------------------------------------------------");
					
					trading = false;
					openTrade = false;
					
				}
			}
		}
		mTradeCache.clear();
	}
	
	private void computeProfit() {
		
	}
	
}
