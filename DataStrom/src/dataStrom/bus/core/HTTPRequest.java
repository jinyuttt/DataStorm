/**
 * 
 */
package dataStrom.bus.core;

import java.util.List;

import com.alibaba.fastjson.serializer.SerializerFeature;

import dataStrom.bus.net.http.MQDataInfo;
import dataStrom.bus.rpc.JsonRpcCodec;

/**
 * @author jinyu
 *
 */
public class HTTPRequest {
private static String encoding="utf-8";

public static String getData(List<MQDataInfo> infos)
{
 return   JsonRpcCodec.toJSON(infos, encoding,
            SerializerFeature.WriteMapNullValue,
            SerializerFeature.WriteClassName);
}
public static String getData(Object obj)
{
 return   JsonRpcCodec.toJSON(obj, encoding,
            SerializerFeature.WriteMapNullValue,
            SerializerFeature.WriteClassName);
}
}
