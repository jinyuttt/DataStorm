/**
 * 
 */
package dataStrom.bus.mq;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author jinyu
 *
 */
public class DateTime {
    public String dateFormat="";
    public static String staticFormat="yyyy-MM-dd HH:mm:ss";
public String getDateTime()
{
    if(dateFormat.isEmpty())
    {
        dateFormat=staticFormat;
    }
    SimpleDateFormat dateFm = new SimpleDateFormat(dateFormat); //格式化当前系统日期
    String dateTime = dateFm.format(new java.util.Date());
    return dateTime;
}
public String getDate()
{
    if(dateFormat.isEmpty())
    {
        dateFormat=staticFormat;
    }
    SimpleDateFormat dateFm = new SimpleDateFormat(dateFormat); //格式化当前系统日期
    String dateTime = dateFm.format(new java.util.Date());
    return dateTime;
}
public String getDate(Date date)
{
    if(dateFormat.isEmpty())
    {
        dateFormat=staticFormat;
    }
    SimpleDateFormat dateFm = new SimpleDateFormat(dateFormat); //格式化当前系统日期
    String dateTime = dateFm.format(date);
    return dateTime;
}
public String getDate(long date)
{
    if(dateFormat.isEmpty())
    {
        dateFormat=staticFormat;
    }
    Date tmp=new Date(date);
    SimpleDateFormat dateFm = new SimpleDateFormat(dateFormat); //格式化当前系统日期
    String dateTime = dateFm.format(tmp);
    return dateTime;
}
}
