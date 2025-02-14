package ru.kozelsk.alliance.services.users;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class FirebaseService {

    private FirebaseAuth firebaseAuth;

    /*@PostConstruct
    public void init() throws IOException {
        FileInputStream serviceAccount = new FileInputStream("src/main/resources/alliance-2490a-firebase-adminsdk-c4hni-df9687e97c.json");
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();
        FirebaseApp.initializeApp(options);
        firebaseAuth = FirebaseAuth.getInstance();
    }*/

    @PostConstruct
    public void init() {
        try {
            FileInputStream serviceAccount = new FileInputStream("src/main/resources/alliance-2490a-firebase-adminsdk-c4hni-df9687e97c.json");
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
            FirebaseApp.initializeApp(options);
            firebaseAuth = FirebaseAuth.getInstance();
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize firebase", e);
        }
    }

    /*public boolean verifyIdToken(String idToken) {
        try{
            FirebaseToken decodedToken = firebaseAuth.verifyIdToken(idToken);
            String uid = decodedToken.getUid();
            return true;
        }catch (Exception e) {
            System.out.println("error in time the check of ID token" + e.getMessage());
            return false;
        }
    }*/

    public boolean verifyIdToken(String idToken) {
        try{
            firebaseAuth.verifyIdToken(idToken);
            return true;
        }catch (FirebaseAuthException e) {
            System.out.println("error in time the check of ID token" + e.getMessage());
            return false;
        }
    }

    public FirebaseToken getDecodedToken(String idToken) throws FirebaseAuthException{
        return firebaseAuth.verifyIdToken(idToken);
    }
}
