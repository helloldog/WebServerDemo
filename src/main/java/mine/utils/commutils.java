package mine.utils;

import com.alibaba.fastjson.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class commutils
{
    public static String NowTime()
            throws Exception
    {
        String VI = null;
        try {
            Date date = new Date();
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String nowDate = sf.format(date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(sf.parse(nowDate));

            VI = sf.format(cal.getTime());
        }
        catch (Exception localException)
        {
        }

        return VI;
    }

    public static boolean isJson(String content) {
        try {
            JSONObject.parseObject(content);
            return true; } catch (Exception e) {
        }
        return false;
    }
}