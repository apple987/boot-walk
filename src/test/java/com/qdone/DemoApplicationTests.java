package com.qdone;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import javax.jms.Destination;

import org.apache.activemq.command.ActiveMQQueue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.qdone.common.mq.JMSProducer;
import com.qdone.module.controller.HelloController;
/**
 * 简单MockMVC测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

	private MockMvc mockMvc;
	
	@Autowired
    private JMSProducer jmsProducer;

	@Before
	public void setUp() throws Exception {
		/**
		 * 简单测试controller
		 */
		mockMvc = MockMvcBuilders.standaloneSetup(new HelloController()).build();
	}

	@Test
	public void testHello() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/testJson").accept(MediaType.APPLICATION_JSON_UTF8)).andDo(print());
	}
	
	

    @Test
    public void testJms() {
        Destination destination = new ActiveMQQueue("springboot.queue.test");
        for (int i=0;i<10;i++) {
            jmsProducer.sendMessage(destination,"hello,world!" + i);
        }
    }
	
}
