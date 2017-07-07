package seop.gyun.recipedia.recipe;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReplyDateFormat {
	
	private long gapTime, temp;
	private String mDate;
	private String result;
	
	public ReplyDateFormat(String date) {
		mDate = date;
	}
	
	public String create() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss", Locale.KOREA);
		
		Date preDate = null;
		try {
			preDate = format.parse(mDate);
			Date curDate = new Date();
			gapTime = curDate.getTime() - preDate.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
			result = "";
		}
		
		if (gapTime < 60L * 1000L) {
			result = "방금 전";
		} else if (gapTime < 60L * 60L * 1000L) {
			temp = gapTime / (60L * 1000L);
			result = String.valueOf(temp) + "분 전";
		} else if (gapTime < 24L * 60L * 60L * 1000L) {
			temp = gapTime / (60L * 60L * 1000L);
			result = String.valueOf(temp) + "시간 전";
		} else if (gapTime < 7L * 24L * 60L * 60L * 1000L) {
			temp = gapTime / (24L * 60L * 60L * 1000L);
			result = String.valueOf(temp) + "일 전";
		} else if (gapTime < 30L * 24L * 60L * 60L * 1000L) {
			temp = gapTime / (7L * 24L * 60L * 60L * 1000L);
			result = String.valueOf(temp) + "주일 전";
		} else if (gapTime < 365L * 24L * 60L * 60L * 1000L) {
			temp = gapTime / (30L * 24L * 60L * 60L * 1000L);
			result = String.valueOf(temp) + "개월 전"; 
		} else {
			temp = gapTime / (365L * 24L * 60L * 60L * 1000L);
			result = String.valueOf(temp) + "년 전";
		}
		
		return result;
	}
	
}
