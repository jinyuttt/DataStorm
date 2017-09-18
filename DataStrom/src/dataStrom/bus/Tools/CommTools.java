/**
 * 
 */
package dataStrom.bus.Tools;



import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import dataStrom.bus.config.TrackConfig;
import dataStrom.bus.net.Client;
import dataStrom.bus.net.NetType;
import dataStrom.bus.net.Server;
import dataStrom.bus.net.tcp.TcpClient;
import dataStrom.bus.net.tcp.TcpServer;
import dataStrom.bus.net.udp.udpClient;
import dataStrom.bus.net.udp.udpServer;

/**
 * @author jinyu
 *
 */
public class CommTools {
    
    /**
     * 提供注解查找
     * @param name
     * @return
     */
public static  Class<?> getClass(String name)
{
    if(name.toLowerCase().equals("tcp_client"))
    {
        return TcpClient.class;
    }
    else if(name.toLowerCase().equals("tcp_server"))
    {
        return TcpServer.class;
    }
    else if(name.toLowerCase().equals("udp_server"))
    {
        return udpServer.class;
    }
    else if(name.toLowerCase().equals("udp_client"))
    {
        return udpClient.class;
    }
    else
    {
        //获取类检查注解
        try {
        List<String>  list=  getClassNameByJar(TrackConfig.netJar, true);
        if(list!=null)
        {
            for(int i=0;i<list.size();i++)
            {
                String clsName=list.get(i);
              Class<?>  cls=  Class.forName(clsName);
             if( (Server.class.isAssignableFrom(cls)||Client.class.isAssignableFrom(cls))&&cls.isAnnotationPresent(NetType.class))
             {
                 //获取接口或者注解，并且同时满足
                 NetType type=    cls.getAnnotation(NetType.class);
                 if(type.value().toLowerCase().equals(name))
                 {
                     return cls;
                 }
             }
              
            }
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    return null;
    
}
//private void getSource(String jarPath)
//{
//    JarFile jar = null;
//    try {
//        jar = new JarFile(jarPath);
//    } catch (IOException e) {
//        e.printStackTrace();
//    } 
//    Enumeration<JarEntry> enumeration = jar.entries(); 
//    while(enumeration.hasMoreElements()){ 
//    System.out.println(enumeration.nextElement()); 
//    }
//}
/**
 * 从jar获取某包下所有类
 * @param jarPath jar文件路径
 * @param childPackage 是否遍历子包
 * @return 类的完整名称
 * @throws Exception 
 */ 
private static List<String> getClassNameByJar(String jarPath, boolean childPackage) throws Exception { 
    List<String> myClassName = new ArrayList<String>(); 
    String[] jarInfo = jarPath.split("!"); 
    String jarFilePath = jarInfo[0].substring(jarInfo[0].indexOf("/")); 
    String packagePath = jarInfo[1].substring(1);
    JarFile jarFile = null;
    try { 
        jarFile = new JarFile(jarFilePath); 
        Enumeration<JarEntry> entrys = jarFile.entries(); 
        while (entrys.hasMoreElements()) { 
            JarEntry jarEntry = entrys.nextElement(); 
            String entryName = jarEntry.getName(); 
            if (entryName.endsWith(".class")) { 
                if (childPackage) { 
                    if (entryName.startsWith(packagePath)) { 
                        entryName = entryName.replace("/", ".").substring(0, entryName.lastIndexOf(".")); 
                        myClassName.add(entryName); 
                    } 
                } else { 
                    int index = entryName.lastIndexOf("/"); 
                    String myPackagePath; 
                    if (index != -1) { 
                        myPackagePath = entryName.substring(0, index); 
                    } else { 
                        myPackagePath = entryName; 
                    } 
                    if (myPackagePath.equals(packagePath)) { 
                        entryName = entryName.replace("/", ".").substring(0, entryName.lastIndexOf(".")); 
                        myClassName.add(entryName); 
                    } 
                } 
            } 
        } 
    } catch (Exception e) { 
        throw e; 
    }finally{
        if(null != jarFile){
            jarFile.close();
        }
    } 
    return myClassName; 
} 

/**
 * 本机IP
 * @return
 */
public static    ArrayList<String> getLocalIp()
{
    ArrayList<String> list=new ArrayList<String>(4);

    Enumeration<NetworkInterface> allNetInterfaces = null;
    try {
        allNetInterfaces = NetworkInterface.getNetworkInterfaces();
    } catch (SocketException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    InetAddress ip;
    while (allNetInterfaces.hasMoreElements())
    {
    NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
    System.out.println(netInterface.getName());
    Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
    while (addresses.hasMoreElements())
    {
    ip = (InetAddress) addresses.nextElement();
    if (ip != null && ip instanceof Inet4Address)
    {
      list.add(ip.getHostAddress());
    } 
    }
}
return list;
}
}
