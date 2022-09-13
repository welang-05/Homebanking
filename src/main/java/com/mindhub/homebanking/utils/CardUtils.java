package com.mindhub.homebanking.utils;

import com.mindhub.homebanking.Services.CardServices;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardType;

import java.util.Random;

public final class CardUtils {

    private CardUtils() {}

    public static String getRandomNumber(int cifras) {
        String azar = "";
        Random rand = new Random();
        for (int i = 0; i < cifras; i++) {
            azar = azar + rand.nextInt(10);
            if (cifras == 3 && azar.equals("0")) {
                azar = String.valueOf(rand.nextInt(9) + 1);
            }
        }
        return azar;
    }

    public static void updateCardQuota(Account account, CardServices cardServices){
        for (Card card : account.getCardSet()){
            if(card.getCardType()== CardType.DEBIT){
                card.setCardQuota(account.getBalance());
                cardServices.saveCard(card);
            }
        }
    }

}
