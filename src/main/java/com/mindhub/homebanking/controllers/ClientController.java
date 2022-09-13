package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.Services.ClientServices;
import com.mindhub.homebanking.dto.ClientDTO;
import com.mindhub.homebanking.models.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import static java.util.stream.Collectors.toList;

//El controlador se encarga de escuchar y responder peticiones

@RestController
public class ClientController {

    @Autowired
    private ClientServices clientServices;//Le dice a Spring que cree automáticamente una instancia(objeto) de ClientService llamada clientService

    @GetMapping("/api/clients") //Asociar la petición a la ruta "/api/clients"
    public List<ClientDTO> getClients(){ //Retorna una lista de clientes
        return  clientServices.getAllClients().stream().map(client -> new ClientDTO(client)).collect(toList()); //Para obtener la lista primero encontramos a todos los clientes en la base de datos, luego llama a stream para poder utilizar map, en map instanciamos nuevos clientesDTO a partir de los clientes que nos entrega la base de datos, luego utilizando el método collect(toList()) mostramos esos datos sangrados (indentados)
    }

    @GetMapping("/api/clients/{id}")//Asociar la petición a la ruta variable "/api/clients/{id}"
    public ClientDTO getClient(@PathVariable Long id){ //Retorna un cliente dada una id(que está indicada en la dirección variable) en particular
        return new ClientDTO(clientServices.findClientById(id)); //Encontramos a un cliente por su id, luego usamos map para relacionar , pasándole el cliente al constructor del ClientDTO, si no encuentra al cliente retorna null
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/api/clients")
    public ResponseEntity<Object> register(

            @RequestParam String name, @RequestParam String lastName,

            @RequestParam String email, @RequestParam String password) {



        if (name.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {

            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);

        }



        if (clientServices.findClientByEmail(email) !=  null) {

            return new ResponseEntity<>("Account email already in use", HttpStatus.FORBIDDEN);

        }

        Client client = new Client(name, lastName, email, passwordEncoder.encode(password));

        clientServices.saveClient(client);

        return new ResponseEntity<>(client, HttpStatus.CREATED);

    }

    @GetMapping("/api/clients/current")
    public ClientDTO getClientByEmail(Authentication autenticacion){
        return  new ClientDTO(clientServices.findClientByEmail(autenticacion.getName()));
    }
}


