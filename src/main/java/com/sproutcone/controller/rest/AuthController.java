package com.sproutcone.controller.rest;

import com.sproutcone.service.usecase.sign.SignInService;
import com.sproutcone.service.usecase.sign.WithdrawService;
import com.sproutcone.service.usecase.sign.SignOutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final SignInService signInService;
    private final SignOutService signOutService;
    private final WithdrawService withdrawService;

    @GetMapping("/sign-in")
    public ResponseEntity<String> login(@RequestParam String code) {
        String appAccessToken = signInService.signIn(code);

        // 클라이언트에 우리 서비스의 JWT 반환
        return ResponseEntity
                .status(200)
                .header("Authorization", "Bearer " + appAccessToken)
                .body(null);
    }

    /**
     * @param authentication JwtFilter가 주입해주는 현재 사용자 인증 정보
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout(Authentication authentication) {
        if(signOutService.signOut(authentication)) {
            // (클라이언트는 이 응답을 받으면, 자신이 가진 앱의 JWT를 삭제해야 함)
            return ResponseEntity.ok("로그아웃 되었습니다.");
        }

        return ResponseEntity.ok("로그아웃에 실패했습니다");
    }

    @DeleteMapping("/withdraw")
    public ResponseEntity<String> withdraw(Authentication authentication) {
        if(withdrawService.signOff(authentication)) {
            // (클라이언트는 이 응답을 받으면, 자신이 가진 앱의 JWT를 삭제해야 함)
            return ResponseEntity.ok("회원 탈퇴 되었습니다.");
        }

        return ResponseEntity.ok("회원 탈퇴에 실패했습니다");
    }
}