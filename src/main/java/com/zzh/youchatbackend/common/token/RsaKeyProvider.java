package com.zzh.youchatbackend.common.token;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jose4j.jwk.RsaJsonWebKey;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;


@Component
@Slf4j
@Getter
public class RsaKeyProvider {
    private final PublicKey publicKey;
    private final PrivateKey privateKey;

    public RsaKeyProvider() throws Exception {
        String publicKeyPem = new String(Files.readAllBytes(Paths.get("src/main/resources/public-key.pem")));
        String privateKeyPem = new String(Files.readAllBytes(Paths.get("src/main/resources/private-key.pem")));
        this.publicKey = getPublicKeyFromPEM(publicKeyPem);
        this.privateKey = getPrivateKeyFromPEM(privateKeyPem);
    }

    private PrivateKey getPrivateKeyFromPEM(String key) throws Exception {
        String privateKeyPEM = key.replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");

        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyPEM);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }

    private PublicKey getPublicKeyFromPEM(String key) throws Exception {
        String publicKeyPEM = key.replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");

        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyPEM);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

}
