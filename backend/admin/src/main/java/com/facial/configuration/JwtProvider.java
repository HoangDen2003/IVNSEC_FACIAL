package com.facial.configuration;

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
import com.nimbusds.jwt.JWTClaimsSet;

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

        //      Payload JWT
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                //                .issuer("Hoang") // Thiết lập người phát hành (issuer) của JWT
                .issueTime(new Date())
                .expirationTime(ext)
                .jwtID(UUID.randomUUID().toString())
                //                .claim("scope", buildScope(user))
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

    //    public SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
    //        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
    //        SignedJWT signedJWT = SignedJWT.parse(token);
    //
    //        Date expiryTime = (isRefresh)
    //                ? new Date(signedJWT
    //                .getJWTClaimsSet()
    //                .getIssueTime()
    //                .toInstant()
    //                .plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS)
    //                .toEpochMilli())
    //                : signedJWT.getJWTClaimsSet().getExpirationTime();
    //
    //        var verified = signedJWT.verify(verifier);
    //
    //        if (!(verified && expiryTime.after(new Date()))) throw new AppException(ErrorCode.UNAUTHENTICATED);
    //
    //        String jit = signedJWT.getJWTClaimsSet().getJWTID();
    //
    //        if (invalidatedTokenRepository.existsById(jit)) throw new AppException(ErrorCode.UNAUTHENTICATED);
    //
    //        return signedJWT;
    //    }
    //
    //    private String buildScope(User user) {
    //        if (user.getGlobal_role() == null) return "ROLE_GUEST";
    //        GlobalRole globalRole =
    //                globalRoleRepository.findById(user.getGlobal_role().getId()).orElse(null);
    //        // Nếu không tìm thấy GlobalRole từ DB, trả về null (có thể bạn cần xử lý trường hợp này ở nơi khác)
    //        if (globalRole == null) {
    //            return "ROLE_GUEST"; // Giá trị mặc định, có thể thay đổi tùy theo yêu cầu
    //        }
    //
    //        // Chuyển dấu cách trong tên GlobalRole thành dấu "_"
    //        return "ROLE_" + globalRole.getName().replace(" ", "_");
    //    }
}
