package org.example;

import static org.assertj.core.api.Assertions.assertThat;

import org.example.PayloadAndGatewayHeader.ArithmeticGateway;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

@Import(PayloadAndGatewayHeader.class)
@RunWith(SpringRunner.class)
public class PayloadAndGatewayHeaderTest {

    @Autowired
    private ArithmeticGateway gateway;

    @Test
    public void add() {
        assertThat(gateway.add(1, 1)).isEqualTo(2);
    }

}
