package ru.kozelsk.alliance.services.realty.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kozelsk.alliance.models.realty.admin.ClientRealty;
import ru.kozelsk.alliance.repositories.realty.admin.ClientRealtyRepository;

import java.util.List;

@Service
public class ClientRealtyService {

    private final ClientRealtyRepository realtyRepository;

    @Autowired
    public ClientRealtyService(ClientRealtyRepository realtyRepository) {
        this.realtyRepository = realtyRepository;
    }

    public ClientRealty findOne(int id) {
        return realtyRepository.findById(id).orElse(null);
    }

    public List<ClientRealty> findAll() {
        return realtyRepository.findAll();
    }

    public void save(ClientRealty upClient) {
        realtyRepository.save(upClient);
    }

    public void delete(int id) {
        realtyRepository.deleteById(id);
    }

    public void update(ClientRealty upClient, int id) {
        upClient.setId(id);
        realtyRepository.save(upClient);
    }
}
