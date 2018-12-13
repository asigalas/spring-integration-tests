package org.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.GatewayHeader;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Payload;

@Configuration
@EnableIntegration
@IntegrationComponentScan
public class PayloadAndGatewayHeader {

    @Bean
    public IntegrationFlow add() {
        return f -> f.transform(Message.class, m ->
                (Integer) m.getPayload() + m.getHeaders().get("operand", Integer.class));
    }

    @MessagingGateway
    interface ArithmeticGateway {

        @Gateway(requestChannel = "add.input", headers = @GatewayHeader(name = "operand", expression = "#args[1]"))
        //@Payload("#args[0]")
        Integer add(@Payload final int a, final int b);

    }

}
