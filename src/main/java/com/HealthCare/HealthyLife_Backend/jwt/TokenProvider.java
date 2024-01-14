package com.HealthCare.HealthyLife_Backend.jwt;

import com.ljw.jwttest.dto.TokenDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
// 토큰 생성, 토큰 검증, 토큰에서 회원 정보 추출
public class TokenProvider {
    private static final String AUTHORITIES_KEY = "auth"; // 토큰에 저장되는 권한 정보의 key
    private static final String BEARER_TYPE = "Bearer"; // 토큰의 타입
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 1; // 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7L; // 1일
    private final Key key; // 토큰을 서명하기 위한 Key

    // 주의점 : @Value 어노테이션은 springframework의 어노테이션이다.
    // 모든 표준 JWS 알고리즘으로 디지털로 서명된 압축 JWTS(JWSs로도 알려진)를 생성하고 파싱하고 증명함
    // HS512: HMAC using SHA-512
    public TokenProvider(@Value("${jwt.secret}") String secretKey) {
        this.key = Keys.secretKeyFor(SignatureAlgorithm.HS512); // HS512 알고리즘을 사용하는 키 생성
    }

    // 토큰 생성
    public TokenDto generateTokenDto(Authentication authentication) {
        // 권한 정보 문자열 생성, authentication 객체로부터 권한(authorities) 정보를 추출
        // authentication은 사용자의 인증 정보를 나타내는 인터페이스임!
        String authorities = authentication.getAuthorities().stream()
                // GrantedAuthority 객체를 권한 문자열로 매핑
                .map(GrantedAuthority::getAuthority)
                // 각 권한 문자열을 쉼표(,)로 구분된 하나의 문자열로 결합
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime(); // 현재 시간
        // 토큰 만료 시간 설정
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        Date refreshTokenExpiresIn = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);

        // Access 토큰 생성
        String accessToken = Jwts.builder()
                // 토큰의 주체 설정 (일반적으로는 사용자명)
                .setSubject(authentication.getName())
                // 토큰에 추가적인 클레임(Claim) 설정, 여기서는 권한 정보를 설정
                .claim(AUTHORITIES_KEY, authorities)
                // 토큰의 만료 시간 설정
                .setExpiration(accessTokenExpiresIn)
                // 토큰 서명 알고리즘과 서명에 사용할 Key 설정
                .signWith(key, SignatureAlgorithm.HS512)
                // 최종적으로 토큰을 생성하고 문자열로 변환
                .compact();

        // 리프레시 토큰 생성
        String refreshToken = Jwts.builder()
                // 토큰의 만료 시간 설정
                .setExpiration(refreshTokenExpiresIn)
                // 토큰의 주체 설정 (일반적으로는 사용자명)
                .setSubject(authentication.getName())
                // 토큰에 추가적인 클레임(Claim) 설정, 여기서는 권한 정보를 설정
                .claim(AUTHORITIES_KEY, authorities)
                // 토큰 서명 알고리즘과 서명에 사용할 Key 설정
                .signWith(key, SignatureAlgorithm.HS512)
                // 최종적으로 토큰을 생성하고 문자열로 변환
                .compact();

        log.info("TOKENPRO RFTK : {}", refreshToken);


        // 토큰 정보를 담은 TokenDto 객체 생성
        return TokenDto.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .accessTokenExpiresIn(accessTokenExpiresIn.getTime())
                .refreshToken(refreshToken)
                .refreshTokenExpiresIn(refreshTokenExpiresIn.getTime())
                .build();
    }

    // 토큰에서 회원 정보 추출
    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);
        // Payload : 서버에서 클라이언트에게 발급된 토큰에 관련된 데이터로, 사용자의 식별 정보, 권한,
        // 그리고 기타 사용자에 대한 정보를 포함한 내용

        // 토큰 복호화에 실패하면
        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 토큰에 담긴 권한 정보들을 가져옴
        // ?는 와일드카드로, 어떤 구체적인 타입인지는 알 수 없지만, GrantedAuthority 또는 그 하위 타입의 객체들을 담을 수 있다는 의미
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // 권한 정보들을 이용해 유저 객체를 만들어서 반환, 여기서 User 객체는 UserDetails 인터페이스를 구현한 객체
        User principal = new User(claims.getSubject(), "", authorities);

        // 유저 객체, 토큰, 권한 정보들을 이용해 인증 객체를 생성해서 반환
        return new UsernamePasswordAuthenticationToken(principal, accessToken, authorities);
    }

    // 토큰의 유효성 검증
    public boolean validateToken (String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    // 토큰 복호화 : "토큰 복호화"는 토큰의 서명을 확인하고, 토큰의 Payload 부분을 해독하는 과정을 의미
    private Claims parseClaims(String accessToken) {
        try {
            // Jwts.parserBuilder()를 사용하여 토큰 파서를 생성하고, 서명 키를 설정함
            // setSigningKey(key) 메서드를 통해 서버에서 사용하는 서명 키를 설정
            // build() 메서드를 통해 최종적으로 토큰 파서를 생성
            // parseClaimsJws(accessToken)를 사용하여 토큰을 파싱하고, Jws<Claims> 객체를 반환
            // Jws<Claims> 객체에서 getBody() 메서드를 사용하여 페이로드(Claims)를 추출
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    // access 토큰 재발급
    public String generateAccessToken(Authentication authentication) {
        return generateTokenDto(authentication).getAccessToken();
    }
}
