package org.example;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import org.springframework.test.context.ContextConfiguration;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import org.assertj.core.api.SoftAssertions;
import org.example.FilterAfterJsonTransformation.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.PollableChannel;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;

@ContextConfiguration(classes = {
        FilterAfterJsonTransformation.class,
        FilterAfterJsonTransformationTest.ContextConfiguration.class
})
@RestClientTest(FilterAfterJsonTransformation.class)
@RunWith(SpringRunner.class)
public class FilterAfterJsonTransformationTest {

    @Autowired
    @Qualifier("flow.input")
    private MessageChannel inputChannel;

    @Autowired
    @Qualifier("outputChannel")
    private PollableChannel outputChannel;

    @Autowired
    private MockRestServiceServer server;

    @Test
    public void flow() {
        server.expect(requestTo("http://localhost:80/test"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("{\"result\":0}", MediaType.APPLICATION_JSON));
        inputChannel.send(MessageBuilder.withPayload("{\"result\":0}").build());
        Object payload = outputChannel.receive(100).getPayload();

        SoftAssertions assertions = new SoftAssertions();
        assertions.assertThat(payload).isInstanceOf(Response.class);
        Response result = (Response) payload;
        assertions.assertThat(result.getResult()).isEqualTo(0);
        assertions.assertAll();
        server.verify();
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
