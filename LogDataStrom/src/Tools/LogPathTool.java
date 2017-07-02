/**    
 * 文件名：PathTool.java    
 *    
 * 版本信息：    
 * 日期：2017年6月26日    
 * Copyright 足下 Corporation 2017     
 * 版权所有    
 *    
 */
package Tools;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;

/**    
 *     
 * 项目名称：Common    
 * 类名称：PathTool    
 * 类描述：    
 * 创建人：jinyu    
 * 创建时间：2017年6月26日 上午1:10:09    
 * 修改人：jinyu    
 * 修改时间：2017年6月26日 上午1:10:09    
 * 修改备注：    
 * @version     
 *     
 */
public class LogPathTool {

/**
 * 获取当前jar路径
 */
public static String getDirPath(Object obj)
{
    URL url = obj.getClass().getProtectionDomain().getCodeSource().getLocation();  
    String filePath = null;  
    try {  
        filePath = URLDecoder.decode(url.getPath(), "utf-8");// 转化为utf-8编码  
    } catch (Exception e) {  
        e.printStackTrace();  
    }  
    if (filePath.endsWith(".jar")) {// 可执行jar包运行的结果里包含".jar"  
        // 截取路径中的jar包名  
        filePath = filePath.substring(0, filePath.lastIndexOf("/") + 1);  
    }  
    File file = new File(filePath);  
    filePath = file.getAbsolutePath();//得到windows下的正确路径  
    return filePath;  
}

/**
 * 获取当前jar路径
 */
public static String getFilePath(Object obj)
{
    URL url = obj.getClass().getProtectionDomain().getCodeSource().getLocation();  
    String filePath = null;  
    try {  
        filePath = URLDecoder.decode(url.getPath(), "utf-8");// 转化为utf-8编码  
    } catch (Exception e) {  
        e.printStackTrace();  
    }  
    File file = new File(filePath);  
    filePath = file.getAbsolutePath();
    return filePath;  
}

}
