package com.qdone.common.mq;

import javax.jms.Destination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
/**
 * 生产者 
 */
@Component
public class JMSProducer {
	@Autowired
	private JmsTemplate jmsTemplate;

	public void sendMessage(Destination destination, String message) {
		this.jmsTemplate.convertAndSend(destination, message);
	}
}