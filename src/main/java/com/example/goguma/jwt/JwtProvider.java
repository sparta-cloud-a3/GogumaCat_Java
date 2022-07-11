package com.example.goguma.jwt;

import com.example.goguma.dto.UserRequestDto;
import com.example.goguma.model.User;
import com.example.goguma.repository.UserRepository;
import com.example.goguma.security.UserDetailsServiceImpl;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtProvider {
    // jwt token 생성 및 복호화 할 때 사용할 secret key
    // 암호화 필요!
    private final String secretKey ="CATGOGUMACATGOGUMACATGOGUMACATGOGUMACATGOGUMACATGOGUMACATGOGUMACATGOGUMACATGOGUMACATGOGUMA";//256비트 이상이여야 한다.
    // access token 유효 시간
    private long accessExpireTime = (60 * 60 * 1000L) * 4; // 4시간 후
    // refresh token 유효 시간 -> access token 의 유효시간보다 길게 준다.
    private final long refreshExpireTime = (60 * 60 * 1000L) * 10; // 10시간 후
    private final UserDetailsServiceImpl userDetailsService;
    private final UserRepository userRepository;

    @Transactional
    public String createAccessToken(UserRequestDto.LoginDto loginDto) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("type", "token");

        Map<String, Object> payloads = new HashMap<>();
        // login 시 들어오는 데이터 중 회사 아이디를 받아 payload 부분에 추가
        // payloads.put("지정할_키_값", loginDTO.get으로 가져올 데이터());
        payloads.put("username", loginDto.getUsername());

        // 토큰을 생성하는 현재 시간을 받아옴
        Date expiration = new Date();
        // 현재 시간에 위에서 지정한 access token 만료 시간을 더해서 토큰 유효 시간을 지정
        expiration.setTime(expiration.getTime() + accessExpireTime);

        // 위에서 만든 header와 payload 를 가지고 jwt 생성
        String jwt = Jwts
                .builder()
                .setHeader(headers)           // header
                .setClaims(payloads)          // payload
                .setSubject("user")           // token 용도
                .setExpiration(expiration)    // 토큰 만료 시간
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        User user = userRepository.findByUsername(loginDto.getUsername()).orElseThrow(
                () -> new NullPointerException("Username이 존재하지 않습니다."));
        user.accessToken(jwt);
        System.out.println("여기는 AccessToken : "+jwt);

        return jwt;
    }
    @Transactional
    public Map<String, String> createRefreshToken(UserRequestDto.LoginDto loginDto) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("type", "token");

        Map<String, Object> payloads = new HashMap<>();
        payloads.put("username", loginDto.getUsername());

        Date expiration = new Date();
        expiration.setTime(expiration.getTime() + refreshExpireTime);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
        String refreshTokenExpirationAt = simpleDateFormat.format(expiration);

        String jwt = Jwts
                .builder()
                .setHeader(headers)
                .setClaims(payloads)
                .setSubject("user")
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        User user = userRepository.findByUsername(loginDto.getUsername()).orElseThrow(
                () -> new NullPointerException("Username이 존재하지 않습니다."));
        user.refreshToken(jwt);
        System.out.println("여기가 리프레쉬 토큰 : " +jwt);
        Map<String, String> result = new HashMap<>();
        result.put("refreshToken", jwt);
        result.put("refreshTokenExpirationAt", refreshTokenExpirationAt);
        return result;
    }

    public String getUserInfo(String token) {
        // token 데이터를 시크릿 키를 이용하여 파싱하여 payload 안에 있는 username 가져옴
        Claims body = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        return (String) body.get("username");
    }

    public User getUser(String token) {
        Claims body = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        String username = (String) body.get("username");
        return userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 게시물입니다.")
        );
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserInfo(token));
        // 받아온 사용자 정보와 역할 목록을 이용하여 authentication 을 만들어서 넘겨줌(우린 역할 목록 사용하지않음)
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String resolveToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String token = null;
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("mytoken")) {
                    token = cookie.getValue();
                }
            }
        }
        return token;
    }

    public boolean validateJwtToken(ServletRequest request, String authToken) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException e) {
            // 잘못된 JWT 구조
            request.setAttribute("exception", "MalformedJwtException");
        } catch (ExpiredJwtException e) {
            // JWT 토큰 유효시간이 만료됨
            request.setAttribute("exception", "ExpiredJwtException");
        } catch (UnsupportedJwtException e) {
            // JWT 가 예상하는 형식과 다른 형식이거나 구성
            request.setAttribute("exception", "UnsupportedJwtException");
        } catch (IllegalStateException e) {
            // JWT 의 서명실패(변조 데이터)
            request.setAttribute("exception", "IllegalStateException");
        }
        catch (IllegalArgumentException e) {
            // 불법적이거나 부적절한 인수를 받음
            request.setAttribute("exception", "IllegalArgumentException");
        }
        return false;
    }

    @Transactional
    public String createKakaoAccessToken(String username) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("type", "token");

        Map<String, Object> payloads = new HashMap<>();
        // login 시 들어오는 데이터 중 회사 아이디를 받아 payload 부분에 추가
        // payloads.put("지정할_키_값", loginDTO.get으로 가져올 데이터());
        payloads.put("username", username);

        // 토큰을 생성하는 현재 시간을 받아옴
        Date expiration = new Date();
        // 현재 시간에 위에서 지정한 access token 만료 시간을 더해서 토큰 유효 시간을 지정
        expiration.setTime(expiration.getTime() + accessExpireTime);

        // 위에서 만든 header와 payload 를 가지고 jwt 생성
        String jwt = Jwts
                .builder()
                .setHeader(headers)           // header
                .setClaims(payloads)          // payload
                .setSubject("user")           // token 용도
                .setExpiration(expiration)    // 토큰 만료 시간
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new NullPointerException("Username이 존재하지 않습니다."));
        user.accessToken(jwt);
        System.out.println("여기는 AccessToken : "+jwt);

        return jwt;
    }

    @Transactional
    public Map<String, String> createKakaoRefreshToken(String username) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("type", "token");

        Map<String, Object> payloads = new HashMap<>();
        payloads.put("username", username);

        Date expiration = new Date();
        expiration.setTime(expiration.getTime() + refreshExpireTime);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
        String refreshTokenExpirationAt = simpleDateFormat.format(expiration);

        String jwt = Jwts
                .builder()
                .setHeader(headers)
                .setClaims(payloads)
                .setSubject("user")
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new NullPointerException("Username이 존재하지 않습니다."));
        user.refreshToken(jwt);
        System.out.println("여기가 리프레쉬 토큰 : " +jwt);
        Map<String, String> result = new HashMap<>();
        result.put("refreshToken", jwt);
        result.put("refreshTokenExpirationAt", refreshTokenExpirationAt);
        return result;
    }
}
