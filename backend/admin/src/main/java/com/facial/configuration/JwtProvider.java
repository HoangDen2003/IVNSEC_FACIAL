package com.facial.configuration;

import java.text.ParseException;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.facial.constant.ProviderJwt;
import com.facial.dto.response.JwtTokenResponse;
import com.facial.entity.User;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
// @EnableMethodSecurity
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtProvider {
    ProviderJwt provider;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected long REFRESHABLE_DURATION;

    public JwtTokenResponse generateToken(User user) {
        //      Đây là một lớp đại diện cho phần "header" của một JSON Web Signature (JWS). Header này chứa thông tin về
        // thuật toán mã hóa và kiểu của token.
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        // expiration time
        Date ext = new Date(
                Instant.now().plus(Duration.ofSeconds(provider.VALID_DURATION)).toEpochMilli());

        //        Date ext = new Date(Instant.now()
        //                .plus(provider.VALID_DURATION, ChronoUnit.SECONDS));

        //      Payload JWT
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                //                .issuer("Hoang") // Thiết lập người phát hành (issuer) của JWT
                .issueTime(new Date())
                .expirationTime(ext)
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(provider.SIGNER_KEY.getBytes()));
            String token = jwsObject.serialize();
            return new JwtTokenResponse(token, ext);
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    public SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);

        //            Date expiryTime = (isRefresh)
        //                    ? new Date(signedJWT
        //                    .getJWTClaimsSet()
        //                    .getIssueTime()
        //                    .toInstant()
        //                    .plus(REFRESHABLE_DURATION, Duration.ofSeconds(provider.VALID_DURATION))
        //                    .toEpochMilli())
        //                    : signedJWT.getJWTClaimsSet().getExpirationTime();

        Date expiryTime;

        if (isRefresh) {
            Instant issueInstant = signedJWT.getJWTClaimsSet().getIssueTime().toInstant();
            Instant newExpiryInstant = issueInstant.plus(Duration.ofSeconds(provider.VALID_DURATION));
            expiryTime = Date.from(newExpiryInstant);
        } else {
            expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        }

        var verified = signedJWT.verify(verifier);

        //            if (!(verified && expiryTime.after(new Date()))) throw new
        // AppException(ErrorCode.UNAUTHENTICATED);

        String jit = signedJWT.getJWTClaimsSet().getJWTID();

        //            if (invalidatedTokenRepository.existsById(jit)) throw new AppException(ErrorCode.UNAUTHENTICATED);

        return signedJWT;
    }

    private String buildScope(User user) {
        // Chuyển dấu cách trong tên GlobalRole thành dấu "_"
        return "ROLE_" + user.getRole();
    }
}
