package ru.kozelsk.alliance.services.insurance.admin;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kozelsk.alliance.models.insurance.admin.ClientInsurance;
import ru.kozelsk.alliance.repositories.insurance.admin.ClientInsuranceRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ClientInsuranceService {

    private final ClientInsuranceRepository clientInsuranceRepository;

    @Autowired
    public ClientInsuranceService(ClientInsuranceRepository clientInsuranceRepository) {
        this.clientInsuranceRepository = clientInsuranceRepository;
    }

    public Optional<ClientInsurance> findOne(int id){
        return clientInsuranceRepository.findById(id);
    }

    public List<ClientInsurance> findAll(){
        return clientInsuranceRepository.findAll();
    }

    public void save(ClientInsurance clientInsurance){
        clientInsuranceRepository.save(clientInsurance);
    }

    public void delete(int id){
        clientInsuranceRepository.deleteById(id);
    }

    public void update(ClientInsurance clientInsurance, int id){
        clientInsurance.setId(id);
        clientInsuranceRepository.save(clientInsurance);
    }
}
