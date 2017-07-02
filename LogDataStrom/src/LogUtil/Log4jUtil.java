/**    
 * 文件名：Log4jUtil.java    
 *    
 * 版本信息：    
 * 日期：2017年7月2日    
 * Copyright 足下 Corporation 2017     
 * 版权所有    
 *    
 */
package LogUtil;

import java.io.FileInputStream;

import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

import Tools.LogPathTool;

/**    
 *     
 * 项目名称：LogDataStrom    
 * 类名称：Log4jUtil    
 * 类描述：基本日志记录类
 * 创建人：jinyu    
 * 创建时间：2017年7月2日 下午2:33:05    
 * 修改人：jinyu    
 * 修改时间：2017年7月2日 下午2:33:05    
 * 修改备注：    
 * @version     
 *     
 */
public class Log4jUtil {
    private static Log4jUtil log4jUtil;
    private Logger logger;

    public Log4jUtil(String configPath) {
        InputStream in = null;
        try {
            in = new FileInputStream(LogPathTool.getDirPath(this)+configPath);
            ConfigurationSource source = new ConfigurationSource(in);
            Configurator.initialize(null, source);  
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        this.logger = LogManager.getLogger(Log4jUtil.class);
    }

    public static Log4jUtil getLog4jUtil() {
        if (log4jUtil == null) {
            log4jUtil = new Log4jUtil("/config/log.properties");
        }
        return log4jUtil;
    }

    public static void debug(String str) {
        log4jUtil.logger.debug(str);
    }

    public static void info(String str) {
        log4jUtil.logger.info(str);
    }

    public static void warn(String str) {
        log4jUtil.logger.warn(str);
    }

    public static void error(String str) {
        log4jUtil.logger.error(str);
    }

    public static void fatal(String str) {
        log4jUtil.logger.fatal(str);
    }
}
