package com.mindhub.homebanking.Services;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;

import java.util.Set;

public interface CardServices {

    Set<Card> findCardsByTitular(Client client);

    Card findCardByNumber(String number);

    Card findCardByColorAndTypeAndTitular(CardColor cardColor, CardType cardType, Client titular);

    Card findCardByColorAndTypeAndTitularAndDeleted(CardColor cardColor, CardType cardType, Client titular, boolean delete);

    void saveCard(Card card);

    void deleteCard(Card card);

}
