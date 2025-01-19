package com.zzh.youchatbackend;

import com.zzh.youchatbackend.common.entity.enums.GroupStatsEnum;
import com.zzh.youchatbackend.common.entity.enums.PrivacyLevelEnum;
import com.zzh.youchatbackend.common.entity.po.Group;
import com.zzh.youchatbackend.module.chat.entity.enums.UserContactEnum;
import com.zzh.youchatbackend.common.entity.po.User;
import com.zzh.youchatbackend.common.mapper.UserMapper;
import com.zzh.youchatbackend.common.redis.RedisUtils;
import com.zzh.youchatbackend.common.token.JwtTokenUtils;
import com.zzh.youchatbackend.common.token.RsaKeyGenerator;
import com.zzh.youchatbackend.common.token.RsaKeyProvider;
import com.zzh.youchatbackend.module.auth.entity.vo.RegisterVO;
import com.zzh.youchatbackend.module.auth.service.AuthService;
import com.zzh.youchatbackend.module.chat.entity.vo.GroupVO;
import com.zzh.youchatbackend.module.chat.service.GroupService;
import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.ErrorCodes;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.lang.JoseException;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
class YouChatBackendApplicationTests {
    public static final Logger logger = LoggerFactory.getLogger(YouChatBackendApplicationTests.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Autowired
    private RsaKeyProvider rsaKeyProvider;

    @Autowired
    private RsaKeyGenerator rsaKeyGenerator;

    @Autowired
    private GroupService groupService;

    @Value("${auth.rsaKeyPath}")
    String rsaKeyPath;

    @Test
    void contextLoads() {
    }

    @Test
    void testMapper() {
        List<User> users = userMapper.selectList(null);
        users.forEach(System.out::println);
    }

    @Test
    void testRedisUtils() {
        Object obj = redisUtils.getObj("sdfsdf");
        logger.info(obj == null ? "null" : obj.toString());
    }


    @Test
    void testContactEnum() {
//        UserContactEnum userContactEnum =  UserContactEnum.getByPrefix("sdf");
        UserContactEnum userContactEnum =  UserContactEnum.getByName("grOuP");
        System.out.println(userContactEnum == null ? "null" : userContactEnum);
    }

    @Test
    void jwtExample() throws JoseException, MalformedClaimException {
        //
        // JSON Web Token is a compact URL-safe means of representing claims/attributes to be transferred between two parties.
        // This example demonstrates producing and consuming a signed JWT
        //

        // Generate an RSA key pair, which will be used for signing and verification of the JWT, wrapped in a JWK
        RsaJsonWebKey rsaJsonWebKey = RsaJwkGenerator.generateJwk(2048);

        // Give the JWK a Key ID (kid), which is just the polite thing to do
        rsaJsonWebKey.setKeyId("k1");

        // Create the Claims, which will be the content of the JWT
        JwtClaims claims = new JwtClaims();
        claims.setIssuer("Issuer");  // who creates the token and signs it
        claims.setAudience("Audience"); // to whom the token is intended to be sent
        claims.setExpirationTimeMinutesInTheFuture(10); // time when the token will expire (10 minutes from now)
        claims.setGeneratedJwtId(); // a unique identifier for the token
        claims.setIssuedAtToNow();  // when the token was issued/created (now)
        claims.setNotBeforeMinutesInThePast(2); // time before which the token is not yet valid (2 minutes ago)
        claims.setSubject("subject"); // the subject/principal is whom the token is about
        claims.setClaim("email","mail@example.com"); // additional claims/attributes about the subject can be added
        List<String> groups = Arrays.asList("group-one", "other-group", "group-three");
        claims.setStringListClaim("groups", groups); // multi-valued claims work too and will end up as a JSON array

        // A JWT is a JWS and/or a JWE with JSON claims as the payload.
        // In this example it is a JWS so we create a JsonWebSignature object.
        JsonWebSignature jws = new JsonWebSignature();

        // The payload of the JWS is JSON content of the JWT Claims
        jws.setPayload(claims.toJson());

        // The JWT is signed using the private key
        jws.setKey(rsaJsonWebKey.getPrivateKey());

        // Set the Key ID (kid) header because it's just the polite thing to do.
        // We only have one key in this example but a using a Key ID helps
        // facilitate a smooth key rollover process
        jws.setKeyIdHeaderValue(rsaJsonWebKey.getKeyId());

        // Set the signature algorithm on the JWT/JWS that will integrity protect the claims
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);

        // Sign the JWS and produce the compact serialization or the complete JWT/JWS
        // representation, which is a string consisting of three dot ('.') separated
        // base64url-encoded parts in the form Header.Payload.Signature
        // If you wanted to encrypt it, you can simply set this jwt as the payload
        // of a JsonWebEncryption object and set the cty (Content Type) header to "jwt".
        String jwt = jws.getCompactSerialization();


        // Now you can do something with the JWT. Like send it to some other party
        // over the clouds and through the interwebs.
        System.out.println("JWT: " + jwt);


        // Use JwtConsumerBuilder to construct an appropriate JwtConsumer, which will
        // be used to validate and process the JWT.
        // The specific validation requirements for a JWT are context dependent, however,
        // it is typically advisable to require a (reasonable) expiration time, a trusted issuer, and
        // an audience that identifies your system as the intended recipient.
        // If the JWT is encrypted too, you need only provide a decryption key or
        // decryption key resolver to the builder.
        JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                .setRequireExpirationTime() // the JWT must have an expiration time
                .setAllowedClockSkewInSeconds(30) // allow some leeway in validating time based claims to account for clock skew
                .setRequireSubject() // the JWT must have a subject claim
                .setExpectedIssuer("Issuer") // whom the JWT needs to have been issued by
                .setExpectedAudience("Audience") // to whom the JWT is intended for
                .setVerificationKey(rsaJsonWebKey.getKey()) // verify the signature with the public key
                .setJwsAlgorithmConstraints( // only allow the expected signature algorithm(s) in the given context
                        AlgorithmConstraints.ConstraintType.PERMIT, AlgorithmIdentifiers.RSA_USING_SHA256) // which is only RS256 here
                .build(); // create the JwtConsumer instance

        try
        {
            //  Validate the JWT and process it to the Claims
            JwtClaims jwtClaims = jwtConsumer.processToClaims(jwt);
            System.out.println("JWT validation succeeded! " + jwtClaims);
        }
        catch (InvalidJwtException e)
        {
            // InvalidJwtException will be thrown, if the JWT failed processing or validation in anyway.
            // Hopefully with meaningful explanations(s) about what went wrong.
            System.out.println("Invalid JWT! " + e);

            // Programmatic access to (some) specific reasons for JWT invalidity is also possible
            // should you want different error handling behavior for certain conditions.

            // Whether or not the JWT has expired being one common reason for invalidity
            if (e.hasExpired())
            {
                System.out.println("JWT expired at " + e.getJwtContext().getJwtClaims().getExpirationTime());
            }

            // Or maybe the audience was invalid
            if (e.hasErrorCode(ErrorCodes.AUDIENCE_INVALID))
            {
                System.out.println("JWT had wrong audience: " + e.getJwtContext().getJwtClaims().getAudience());
            }
        }
    }

    @Test
    void testToken() {
        String jwk = jwtTokenUtils.createToken("testUid");
        logger.info("Generated JWK: " + jwk);

        try {
            JwtClaims claims = jwtTokenUtils.verifyToken(jwk);
            logger.info("Decrypted claims:" + claims);
        } catch (Exception e) {
            logger.error("Error when verify token", e);
        }
    }

    @Test
    void testRsaKeyProvider() throws NoSuchAlgorithmException {
        rsaKeyGenerator.generateAndSaveKeys(rsaKeyPath);

        logger.info("public-key: {}", rsaKeyProvider.getPublicKey());
        logger.info("private-key: {}", rsaKeyProvider.getPrivateKey());
    }

    @Test
    void testGroupCreation() {
        Group newGroup = Group.builder()
                .groupName("group-one")
                .groupStats(GroupStatsEnum.NORMAL)
                .ownerUid("SUPREMEADMIN")
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .groupNotice("TEST NOTICE 1")
                .privacyLevel(PrivacyLevelEnum.LEVEL_1)
                .build();

        GroupVO groupVO = new GroupVO(newGroup, null, null);
        groupService.createGroup(groupVO);
    }

}
