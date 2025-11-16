package com.sproutcone.controller.rest;

import com.sproutcone.service.usecase.sign.SignInService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/kakao")
@RequiredArgsConstructor
public class AuthController {

    private final SignInService signInService;

    @GetMapping("/callback")
    public ResponseEntity<String> kakaoLogin(@RequestParam String code) {
        String appAccessToken = signInService.signIn(code);

        // 8. 클라이언트에 우리 서비스의 JWT 반환
        return ResponseEntity
                .status(200)
                .header("Authorization", "Bearer " + appAccessToken)
                .body(null);
    }
}