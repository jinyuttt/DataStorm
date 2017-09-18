/**
 * 
 */
package dataStrom.bus.broker;

import dataStrom.bus.net.ProtocolType;

/**
 * @author jinyu
 *
 */
public class Busbroker {
public String  address;
public String netType;
public ProtocolType  protocol;
public Busbroker(String addressList)
{
    this.address=addressList;
}
}
