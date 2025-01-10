package ru.kozelsk.alliance.services.users;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Random;

@Service
public class SmsService {

    @Value("${smsru.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    // генерация случайного числа для кода
    public String generatedVerificationCode(){
        Random random = new Random();
        int code = 1000 + random.nextInt(9000);
        return String.valueOf(code);
    }

    // отправка смс через sms.ru
    public boolean sendSms(String phoneNumber, String message) {
        try{
            if (phoneNumber == null || phoneNumber.length() < 8 || phoneNumber.length() > 15) {
                System.out.println("Номер телефона должен содержать от 8 до 15 символов");
                return false;
            }

            // Приведение номера телефона к международному формату, если необходимо
            if (!phoneNumber.startsWith("+")) {
                phoneNumber = "+7" + phoneNumber; // Пример для России
            }

            // Проверка длины номера телефона после приведения к международному формату
            if (phoneNumber.length() < 8 || phoneNumber.length() > 15) {
                System.out.println("Номер телефона должен содержать от 8 до 15 символов");
                return false;
            }

            // кодировка сообщения для URL
            String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8.toString());

            // формирование json-тела запроса
            String requestBody = String.format(
                    "{\"number\":\"%s\",\"destination\":\"%s\",\"text\":\"%s\"}",
                    "79678137186", phoneNumber, encodedMessage
            );

            // устаннавливаем заголовки
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + apiKey);

            // создаем HTTP-запрос
            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    "https://api.exolve.ru/messaging/v1/SendSMS",
                    HttpMethod.POST, request, String.class
            );

            System.out.println("МТС Exolve response" + response.getBody());

            if(response.getStatusCode().is2xxSuccessful()){
                return true;
            }else {
                System.out.println("ошибка при отправке сообщения: " + response.getBody());
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }
}
