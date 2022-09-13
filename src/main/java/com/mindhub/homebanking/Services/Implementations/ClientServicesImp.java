package com.mindhub.homebanking.Services.Implementations;

import com.mindhub.homebanking.Services.ClientServices;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientServicesImp implements ClientServices {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public List<Client> getAllClients(){
        return clientRepository.findAll();
    }

    @Override
    public Client findClientById(long id){
        return clientRepository.findById(id).orElse(null);
    }

    @Override
    public Client findClientByEmail(String email){
        return clientRepository.findByEmail(email);
    }

    @Override
    public void saveClient(Client client){
        clientRepository.save(client);
    }
}
