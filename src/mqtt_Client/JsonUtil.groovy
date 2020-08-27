package mqtt_Client

import java.text.SimpleDateFormat

class JsonUtil 
{
	static def convertEpoch_to_date(long timeInMillis)
	{
		//SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Calendar calendar = new GregorianCalendar();
		format.setTimeZone(calendar.getTimeZone());
		return format.format(timeInMillis);
	}
	static def convertEpoch_to_date(long timeInMillis, String formatIn)
	{
		SimpleDateFormat format = new SimpleDateFormat(formatIn);
		Calendar calendar = new GregorianCalendar();
		format.setTimeZone(calendar.getTimeZone());
		return format.format(timeInMillis);
	}
}
