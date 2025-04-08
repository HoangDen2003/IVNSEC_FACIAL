package com.facial.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ProviderJwt {
    @Value("${jwt.signerKey}")
    public String SIGNER_KEY;

    @Value("${jwt.valid-duration}")
    public long VALID_DURATION;

    @Value("${jwt.refreshable-duration}")
    public long REFRESHABLE_DURATION;
}
