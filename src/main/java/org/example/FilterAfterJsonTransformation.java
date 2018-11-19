package org.example;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.Transformers;
import org.springframework.integration.http.dsl.Http;
import org.springframework.integration.mapping.support.JsonHeaders;
import org.springframework.messaging.MessageChannel;

@Configuration
public class FilterAfterJsonTransformation {

    @Bean
    public IntegrationFlow flow(RestTemplateBuilder restTemplateBuilder, MessageChannel outputChannel) {
        return f -> f
                .handle(Http.outboundGateway("http://localhost:80/test", restTemplateBuilder.build())
                        .httpMethod(HttpMethod.GET).expectedResponseType(String.class))
                .enrichHeaders(h -> h.header(JsonHeaders.TYPE_ID, Response.class))
                .transform(Transformers.fromJson())
                .filter(Response.class, p -> true)
                .channel(outputChannel);
    }

    public static class Response {
        private int result;
        public int getResult() { return result; }
        public void setResult(int result) { this.result = result; }
    }

}
