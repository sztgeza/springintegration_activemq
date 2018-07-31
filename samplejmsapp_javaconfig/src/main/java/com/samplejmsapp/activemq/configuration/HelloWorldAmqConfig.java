package com.samplejmsapp.activemq.configuration;

import com.samplejmsapp.activemq.services.HelloWorldMessageHandler;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.jms.Jms;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import javax.jms.ConnectionFactory;

@Configuration
@EnableIntegration
public class HelloWorldAmqConfig {

    @Value("${spring.activemq.broker-url}")
    private String brokerUrl;

    @Value("${spring.activemq.user}")
    private String user;

    @Value("${spring.activemq.password}")
    private String password;

    public static final String HELLO_WORLD_QUEUE = "hello.world.queue";

    @Bean
    public ConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL(brokerUrl);
        connectionFactory.setUserName(user);
        connectionFactory.setPassword(password);
        return connectionFactory;
    }

    @Bean(name = "jmsInputFlowWithMessageHandler")
    public IntegrationFlow buildReceiverFlowWithMessageHandler() {
        return IntegrationFlows
                .from(Jms.messageDrivenChannelAdapter(connectionFactory()).destination(HELLO_WORLD_QUEUE))
                .handle(messageHandler())
                .get();
    }

    @Bean(name = "messageHandler")
    public MessageHandler messageHandler() {
        return new HelloWorldMessageHandler();
    }

    @Bean(name = "jmsInputFlowWithServiceActivator")
    public IntegrationFlow buildReceiverFlowWithServiceActivator() {
        return IntegrationFlows
                .from(Jms.messageDrivenChannelAdapter(connectionFactory()).destination(HELLO_WORLD_QUEUE))
                .channel("jmsInputChannel")
                .get();
    }

    @Bean(name = "jmsInputChannel")
    public MessageChannel jmsInputChannel() {
        return new PublishSubscribeChannel();
    }

    //Consumes the message.
    @ServiceActivator(inputChannel="jmsInputChannel")
    public void receive(String msg){
        System.out.println("Received Message: " + msg);
    }

}
