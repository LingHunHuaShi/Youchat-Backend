package com.zzh.youchatbackend.common.token;

import com.zzh.youchatbackend.common.exception.BusinessException;
import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenUtils {
    RsaKeyProvider rsaKeyProvider;
    public static final Logger logger = LoggerFactory.getLogger(JwtTokenUtils.class);

    @Value("${auth.tokenExpireMinutes}")
    private Float expireInMinutes;

    @Autowired
    public JwtTokenUtils(RsaKeyProvider rsaKeyProvider) {
        this.rsaKeyProvider = rsaKeyProvider;
    }

    public String createToken(String uid) {
        JwtClaims claims = new JwtClaims();
        claims.setSubject(uid);
        claims.setExpirationTimeMinutesInTheFuture(expireInMinutes);
//        claims.setExpirationTimeMinutesInTheFuture(0.1f);
        claims.setIssuedAtToNow();
        claims.setGeneratedJwtId();
        claims.setNotBeforeMinutesInThePast(2);

        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setKey(rsaKeyProvider.getPrivateKey());
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);

        try {
            String token = jws.getCompactSerialization();
            return token;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    public JwtClaims verifyToken(String token) {
        JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                .setRequireExpirationTime()
                .setRequireSubject()
                .setVerificationKey(rsaKeyProvider.getPublicKey())
                .setJwsAlgorithmConstraints(AlgorithmConstraints.ConstraintType.PERMIT, AlgorithmIdentifiers.RSA_USING_SHA256)
                .setAllowedClockSkewInSeconds(60)
                .build();

        try {
            return jwtConsumer.processToClaims(token);
        } catch (InvalidJwtException e) {
            throw new BusinessException("Invalid token: Expired.");
        } catch (Exception e) {
            logger.error("Error Type: {}, Message: {}", e.getClass(), e.getMessage());
            return null;
        }
    }
}
