// ============================
// Login Page Script
// ============================

// Kakao SDK 초기화
document.addEventListener("DOMContentLoaded", () => {
    const KAKAO_JS_KEY = "YOUR_KAKAO_JAVASCRIPT_KEY"; // 실제 키로 교체
    Kakao.init(KAKAO_JS_KEY);

    console.log("Kakao SDK Initialized:", Kakao.isInitialized());
});

// ============================
// 카카오 로그인 Redirect
// ============================
function redirectToKakaoLogin() {

    const REST_API_KEY = "7144eaa004645fd3d73f8a34e8dac7d7"; // 실제 값 입력
    const REDIRECT_URI = "http://localhost:8080/api/v1/auth/kakao/callback";

    const kakaoURL =
        "https://kauth.kakao.com/oauth/authorize"
        + "?response_type=code"
        + "&client_id=" + REST_API_KEY
        + "&redirect_uri=" + encodeURIComponent(REDIRECT_URI);

    window.location.href = kakaoURL;
}

// 전역에서 onclick 으로 접근 가능하도록 등록
window.redirectToKakaoLogin = redirectToKakaoLogin;