package com.mindhub.homebanking.repository;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Set;

@RepositoryRestResource //Trabajará con las restricciones de REST
public interface CardRepository extends JpaRepository <Card, Long> {

    Card findByNumber(String number);

    Set<Card> findByTitular(Client titular);

    Set<Card> findByCardTypeAndTitular(CardType cardType, Client titular);
    Set<Card> findByCardColorAndTitular(CardColor cardColor, Client titular);
    Card findByCardColorAndCardTypeAndTitular(CardColor cardColor, CardType cardType, Client titular);

    Card findByCardColorAndCardTypeAndTitularAndDeleted(CardColor cardColor, CardType cardType, Client titular, boolean deleted);

}
