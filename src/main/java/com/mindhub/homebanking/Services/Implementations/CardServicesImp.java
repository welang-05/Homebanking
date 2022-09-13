package com.mindhub.homebanking.Services.Implementations;

import com.mindhub.homebanking.Services.CardServices;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CardServicesImp implements CardServices {

    @Autowired
    private CardRepository cardRepository;

    @Override
    public Set<Card> findCardsByTitular(Client client){
        return cardRepository.findByTitular(client);
    }

    @Override
    public Card findCardByNumber(String number){
        return cardRepository.findByNumber(number);
    }

    @Override
    public Card findCardByColorAndTypeAndTitular(CardColor cardColor, CardType cardType, Client titular){
        return  cardRepository.findByCardColorAndCardTypeAndTitular(cardColor,cardType,titular);
    }

    @Override
    public Card findCardByColorAndTypeAndTitularAndDeleted(CardColor cardColor, CardType cardType, Client titular, boolean delete){
        return cardRepository.findByCardColorAndCardTypeAndTitularAndDeleted(cardColor, cardType,titular,delete);
    }


    @Override
    public void saveCard(Card card){
        cardRepository.save(card);
    }

    @Override
    public void deleteCard(Card card){
        cardRepository.delete(card);
    }
}
