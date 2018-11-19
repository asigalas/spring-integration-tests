package org.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.transformer.GenericTransformer;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

@Configuration
public class LambdaVsAnonymous {

    @Bean
    public IntegrationFlow lambda(MessageChannel outputChannel) {
        return f -> f
                .enrichHeaders(h -> h.header("add", 1))
                .<Message<Integer>, Integer>transform(m -> m.getPayload() + m.getHeaders().get("add", Integer.class))
                .channel(outputChannel);
    }

    @Bean
    public IntegrationFlow anonymous(MessageChannel outputChannel) {
        return f -> f
                .enrichHeaders(h -> h.header("add", 1))
                .transform(new GenericTransformer<Message<Integer>, Integer>() {
                    @Override
                    public Integer transform(Message<Integer> source) {
                        return source.getPayload() + source.getHeaders().get("add", Integer.class);
                    }
                })
                .channel(outputChannel);
    }

}
