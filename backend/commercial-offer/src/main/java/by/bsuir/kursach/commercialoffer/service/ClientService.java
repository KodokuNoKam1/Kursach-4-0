package by.bsuir.kursach.commercialoffer.service;

import by.bsuir.kursach.commercialoffer.model.Client;
import by.bsuir.kursach.commercialoffer.repository.ClientRepository;
import org.springframework.stereotype.Service;

@Service
public class ClientService {
    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client createClient(Client client) {
        return clientRepository.save(client);
    }

    public Client updateClient(Long id, Client updatedClient) {
        Client client = clientRepository.findById(id).orElseThrow();
        client.setName(updatedClient.getName());
        client.setCompany(updatedClient.getCompany());
        client.setEmail(updatedClient.getEmail());
        client.setPhone(updatedClient.getPhone());
        return clientRepository.save(client);
    }
}