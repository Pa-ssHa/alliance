package ru.kozelsk.alliance.services.excursion.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kozelsk.alliance.models.excursion.admin.ClientExcursion;
import ru.kozelsk.alliance.repositories.excursion.admin.ClientExcursionRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ClientExcursionService {

    private final ClientExcursionRepository clientExcursionRepository;

    @Autowired
    public ClientExcursionService(ClientExcursionRepository clientExcursionRepository) {
        this.clientExcursionRepository = clientExcursionRepository;
    }

    public Optional<ClientExcursion> findOne(int id) {
        return clientExcursionRepository.findById(id);
    }

    public List<ClientExcursion> findAll() {
        return clientExcursionRepository.findAll();
    }

    public void save(ClientExcursion clientExcursion) {
        clientExcursionRepository.save(clientExcursion);
    }

    public void delete(int id) {
        clientExcursionRepository.deleteById(id);
    }

    public void update(ClientExcursion clientExcursion, int id) {
        clientExcursion.setId(id);
        clientExcursionRepository.save(clientExcursion);
    }
}
