package com.tave.alarmissue.fcm.config;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @Value("${firebase.secret-key-path}")
    private String secretKeyPath;

    /**
     * @PostConstruct를 사용하여 애플리케이션 시작 시 FirebaseApp을 초기화(한번만 실행)
     */
    @PostConstruct
    public void initialize() throws IOException {
        ClassPathResource resource = new ClassPathResource(secretKeyPath);

        try (InputStream serviceAccount = resource.getInputStream()) {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            // 이미 초기화된 FirebaseApp이 없는 경우에만 초기화
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }
        }
    }

    /**
     * @return FirebaseMessaging 인스턴스
     */
    @Bean
    public FirebaseMessaging firebaseMessaging() {
        return FirebaseMessaging.getInstance();
    }
}