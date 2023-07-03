//package tw.hicamp.member.config;
//
//import com.google.api.client.auth.oauth2.Credential;
//import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
//import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
//import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
//import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
//import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
//import com.google.api.client.http.javanet.NetHttpTransport;
//import com.google.api.services.gmail.Gmail;
//import com.google.api.services.gmail.GmailScopes;
//import com.google.api.client.json.JsonFactory;
//import com.google.api.client.json.gson.GsonFactory;
//import com.google.api.client.util.store.FileDataStoreFactory;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.security.GeneralSecurityException;
//import java.util.Collections;
//import java.util.List;
//
//@Configuration
//public class GmailConfig {
//
//    @Value("${application.name}")
//    private String applicationName;
//
//    private static final String CREDENTIALS_FILE_PATH = "/json/credentials.json";
//    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
//    private static final List<String> SCOPES = Collections.singletonList(GmailScopes.GMAIL_SEND);
//    private static final String TOKENS_DIRECTORY_PATH = "/json/";
//
//    private Credential getCredentials(final NetHttpTransport httpTransport) throws IOException {
//        // Load client secrets
//        InputStream in = GmailConfig.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
//        if (in == null) {
//            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
//        }
//        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
//
//        // Build flow and trigger user authorization request
//        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
//                httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
//                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
//                .setAccessType("offline")
//                .build();
//
//        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
//    }
//
//    @Bean
//    public Gmail gmailService() throws GeneralSecurityException, IOException {
//        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
//        return new Gmail.Builder(httpTransport, GsonFactory.getDefaultInstance(), getCredentials(httpTransport))
//                .setApplicationName(applicationName)
//                .build();
//    }
//}