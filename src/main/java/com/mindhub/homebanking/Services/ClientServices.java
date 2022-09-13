package com.mindhub.homebanking.Services;

import com.mindhub.homebanking.models.Client;

import java.util.List;

public interface ClientServices {

    List<Client> getAllClients();

    Client findClientById(long id);

    Client findClientByEmail(String email);

    void saveClient(Client client);

}
