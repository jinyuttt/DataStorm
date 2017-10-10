/**
 * 
 */
package dataStrom.bus.proxyFactory;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;

import dataStrom.bus.config.BrokerConfig;
import dataStrom.bus.mq.MqMode;

/**
 * @author jinyu
 *
 */
public class ProxyObjProducer {
    ProxyProducer p=null;
 public   ProxyObjProducer(BrokerConfig brokerconfig,MqMode model) 
 {
     p=new ProxyProducer(brokerconfig,model);
 }
public void publish(String name,Object obj)
{
    byte[]data=toJSONBytes(obj,"UTF-8", SerializerFeature.WriteMapNullValue,SerializerFeature.WriteClassName);
    p.publish(name, data);
}
private static final byte[] toJSONBytes(Object object, String charsetName,
        SerializerFeature... features) {
    SerializeWriter out = new SerializeWriter();

    try {
        JSONSerializer serializer = new JSONSerializer(out);
        for (SerializerFeature feature : features) {
            serializer.config(feature, true);
        }

        serializer.write(object);

        return out.toBytes(charsetName);
    } finally {
        out.close();
    }
}
}
