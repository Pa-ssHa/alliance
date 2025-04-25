package ru.kozelsk.alliance.services.insurance;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.kozelsk.alliance.models.coursework.Coursework;
import ru.kozelsk.alliance.models.insurance.Insurance;
import ru.kozelsk.alliance.repositories.insurance.InsuranceRepository;

import java.io.IOException;
import java.lang.classfile.Opcode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class InsuranceService {

    private final InsuranceRepository insuranceRepository;
    private static final String uploadDir = "M:/uploads/insurance/";

    @Autowired
    public InsuranceService(InsuranceRepository insuranceRepository) {
        this.insuranceRepository = insuranceRepository;
    }

    public Optional<Insurance> findOne(int id) {
        return insuranceRepository.findById(id);
    }

    public List<Insurance> findAll() {
        return insuranceRepository.findAll();
    }

    public void save(Insurance insurance) {
        insuranceRepository.save(insurance);
    }

    public void save(Insurance insurance, MultipartFile image){

        String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
        Path path = Paths.get(uploadDir + fileName);

        try{
            Files.createDirectories(path.getParent());
            Files.write(path, image.getBytes());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        insurance.setImage(fileName);
        insuranceRepository.save(insurance);
    }

    public void delete(int id){

        String filePath = uploadDir + findOne(id).get().getImage();
        try{
            Files.deleteIfExists(Path.of(filePath));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        insuranceRepository.deleteById(id);
    }

    public void updateWithoutImage(Insurance insurance, int id){
        Insurance insuranceOld = insuranceRepository.findById(id).get();
        insurance.setImage(insuranceOld.getImage());
        insurance.setId(id);
        insuranceRepository.save(insurance);
    }

    public void updateWithImage(Insurance insurance, int id, MultipartFile image){
        insurance.setId(id);
        String filePath = uploadDir + findOne(id).get().getImage();
        try{
            Files.deleteIfExists(Path.of(filePath));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        save(insurance, image);
    }
}
