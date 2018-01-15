package com.fbb.model;

import java.util.ArrayList;

import com.fbb.bean.Lottery;
import com.fbb.bean.Trade;
import com.fbb.util.LogUtil;

public class TradeModel3 {
	private static final float mInitialBalance = 10000f;
	private float mCurrentBalance = mInitialBalance;
//	private float[] mLadderPrice = {10,30,70,160,350,800};//TODO get best price ladder
	private float[] mLadderPrice = {100,300,700,1600,3500,8000};//TODO get best price ladder
	
//	private int startTradeSerialCount = 5;
//	private int endTradeCount = 8;
	
	private int startTradeSerialCount = 6;
	private int endTradeCount = 9;
	private float rate = 1.92f;
	
	private ArrayList<Trade> mTradeCache = new ArrayList<Trade>();
	
	boolean openTrade = false;
	boolean trading = false;
	int zhishunCount = 0;
	int yingliCount = 0;
	
	float maxBlance = 0;
	float minBlance = mInitialBalance;
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
		maxBlance = maxBlance > mCurrentBalance ? maxBlance:mCurrentBalance;
		minBlance = minBlance < mCurrentBalance ? minBlance:mCurrentBalance;
	}
	
	public void endTrade(int SerialCount, Lottery currentLottery) {//SerialCount连已结束
//		LogUtil.d("openTrade:"+openTrade+" trading:"+trading+" mTradeCache.size():"+mTradeCache.size()+" SerialCount:"+SerialCount);
		if(openTrade) {
			if(trading && mTradeCache.size() > 0) {
				if(SerialCount - startTradeSerialCount + 1 == mTradeCache.size()) {//无止损行为
					Trade trade = mTradeCache.get(mTradeCache.size() - 1);
					trade.setGetProfit(true);
					
					float profit = trade.getTradeAmount() * rate;
					mCurrentBalance += profit;
					LogUtil.d(currentLottery.getLottery_code()+"期收入 +"+profit+" 余额:"+mCurrentBalance);
					yingliCount++;
					LogUtil.d("yingliCount:"+yingliCount);
					LogUtil.d("zhishunCount:"+zhishunCount +" max:"+maxBlance+" min:"+minBlance);
					LogUtil.d("-----------------------------------------------------");
					
					trading = false;
					openTrade = false;
					
				}
			}
		}
		mTradeCache.clear();
		
		computeToWaitCount(SerialCount);
		boolean tag = isOpenTrade(SerialCount);
		if(!openTrade && tag){
			openTrade = true;
		}
	}
	
	int waitCount = -1;
	private boolean isOpenTrade(int SerialCount) {
		if(SerialCount >= startTradeSerialCount && SerialCount < endTradeCount){ 
			if(toWaitCount >= 0) {
				if(waitCount < 0) waitCount = 0;
				if(waitCount == toWaitCount) {
					LogUtil.d("准备开始交易");
					toWaitCount = -1;
					return true;
				} else {
					waitCount ++;
					LogUtil.d("第"+waitCount+"次出现"+endTradeCount+"连以下");
				}
			}
		}
		return false;
	}

	boolean lianxuda5CountTag = false;
	int needWaitCount = -1;
	int toWaitCount = -1;
	private void computeToWaitCount(int SerialCount) {
		if(SerialCount >= endTradeCount) {
			LogUtil.d("出现"+endTradeCount+"连以上");
			if(!lianxuda5CountTag) {
				lianxuda5CountTag = true;
				needWaitCount = 0;
				LogUtil.d("-----------------------------------------------------");
			} else {
				toWaitCount = needWaitCount;
				waitCount = -1;
				LogUtil.d("中间出现"+endTradeCount+"连以下"+needWaitCount+"次");
				needWaitCount = 0;
				lianxuda5CountTag = true;
			}
		} else if(SerialCount >= startTradeSerialCount && SerialCount < endTradeCount){
			needWaitCount ++;
		}
	}
}
