package ru.kozelsk.alliance.services.coursework;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.kozelsk.alliance.models.coursework.Coursework;
import ru.kozelsk.alliance.repositories.coursework.CourseworkRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class CourseworkService {

    private final CourseworkRepository courseworkRepository;
    private static final String uploadDir = "M:/uploads/coursework/";

    @Autowired
    public CourseworkService(CourseworkRepository courseworkRepository) {
        this.courseworkRepository = courseworkRepository;
    }

    public Optional<Coursework> findOne(int id){
        return courseworkRepository.findById(id);
    }

    public List<Coursework> findAll(){
        return courseworkRepository.findAll();
    }

    public void save(Coursework coursework){
        courseworkRepository.save(coursework);
    }

    public void save(Coursework coursework, MultipartFile image){

        String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
        Path path = Paths.get(uploadDir + fileName);

        try{
            Files.createDirectories(path.getParent());
            Files.write(path, image.getBytes());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        coursework.setImage(fileName);
        courseworkRepository.save(coursework);
    }


    public void updateWithoutImage(Coursework coursework, int id){
        Coursework courseworkOld = courseworkRepository.findById(id).get();
        coursework.setImage(courseworkOld.getImage());
        coursework.setId(id);
        courseworkRepository.save(coursework);
    }

    public void updateWithImage(Coursework coursework, int id, MultipartFile image){
        coursework.setId(id);
        String filePath = uploadDir + findOne(id).get().getImage();
        try{
            Files.deleteIfExists(Path.of(filePath));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        save(coursework, image);
    }


    public void delete(int id){

        String filePath = uploadDir + findOne(id).get().getImage();
        try{
            Files.deleteIfExists(Path.of(filePath));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        courseworkRepository.deleteById(id);
    }


}
