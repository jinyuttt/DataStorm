/**
 * 
 */
package dataStrom.bus.consumer;

import java.io.IOException;

import dataStrom.bus.mq.Message;

/**
 * @author jinyu
 *
 */
public interface ConsumerHandler {
    void handle(Message msg, Consumer consumer) throws IOException;
}
