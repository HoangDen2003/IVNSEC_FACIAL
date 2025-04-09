package com.facial.dto.response;

import java.util.Date;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtTokenResponse {
    String token;
    Date expiresAt;
}
