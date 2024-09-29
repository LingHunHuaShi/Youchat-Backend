package com.zzh.youchatbackend.common.token;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.security.*;
import java.util.Base64;

@Component
@Slf4j
public class RsaKeyGenerator {
    public void generateAndSaveKeys(String keyPath) throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        String publicKeyPem = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        String privateKeyPem = Base64.getEncoder().encodeToString(privateKey.getEncoded());

        saveKeyToFile(publicKeyPem, keyPath + "public-key.pem", "PUBLIC");
        saveKeyToFile(privateKeyPem, keyPath + "private-key.pem", "PRIVATE");
    }

    private void saveKeyToFile(String keyPem, String keyPath, String keyType) {
        try (FileWriter fileWriter = new FileWriter(keyPath)) {
            fileWriter.write("-----BEGIN " + keyType + " KEY-----\n");
            fileWriter.write(splitIntoLines(keyPem, 64)); // 每行64个字符
            fileWriter.write("-----END " + keyType + " KEY-----\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private String splitIntoLines(String base64String, int lineLength) {
        StringBuilder result = new StringBuilder();
        int index = 0;
        while (index < base64String.length()) {
            result.append(base64String, index, Math.min(index + lineLength, base64String.length()))
                    .append("\n");
            index += lineLength;
        }
        return result.toString();
    }
}
