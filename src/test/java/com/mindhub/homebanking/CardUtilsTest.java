package com.mindhub.homebanking;

import com.mindhub.homebanking.utils.CardUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest
public class CardUtilsTest {

    @Test
    public void cardNumberIsCreated(){

        String cardNumber = CardUtils.getRandomNumber(4);

        assertThat(cardNumber.length(),equalTo(4));

    }

    @Test
    public void cvvNumberIsCreated(){

        String cvv = CardUtils.getRandomNumber(3);

        assertThat(cvv.length(),equalTo(3));

    }



}
