package org.example;

import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.PollableChannel;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@ContextConfiguration(classes = {
        LambdaVsAnonymousTest.ContextConfiguration.class,
        LambdaVsAnonymous.class
})
@RunWith(SpringRunner.class)
public class LambdaVsAnonymousTest {

    @Autowired
    @Qualifier("lambda.input")
    private MessageChannel lambdaInputChannel;

    @Autowired
    @Qualifier("anonymous.input")
    private MessageChannel anonymousInputChannel;

    @Autowired
    @Qualifier("outputChannel")
    private PollableChannel outputChannel;

    @Test
    public void lambda() {
        lambdaInputChannel.send(MessageBuilder.withPayload(0).build());
        Object payload = outputChannel.receive(100).getPayload();

        SoftAssertions assertions = new SoftAssertions();
        assertions.assertThat(payload).isInstanceOf(Integer.class);
        assertions.assertThat((Integer) payload).isEqualTo(1);
        assertions.assertAll();
    }

    @Test
    public void anonymous() {
        anonymousInputChannel.send(MessageBuilder.withPayload(0).build());
        Object payload = outputChannel.receive(100).getPayload();

        SoftAssertions assertions = new SoftAssertions();
        assertions.assertThat(payload).isInstanceOf(Integer.class);
        assertions.assertThat((Integer) payload).isEqualTo(1);
        assertions.assertAll();
    }

    @TestConfiguration
    @EnableIntegration
    public static class ContextConfiguration {

        @Bean
        public MessageChannel outputChannel() {
            return MessageChannels.queue().get();
        }

    }

}
