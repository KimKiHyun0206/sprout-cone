// ShootingStars: React를 JS로 재구현한 파일

document.addEventListener("DOMContentLoaded", () => {
    const container = document.querySelector("#shooting-stars-container");

    if (!container) return;

    const createStar = (delay) => {
        const star = document.createElement("div");
        star.classList.add("shooting-star");

        // 랜덤 좌표 및 애니메이션 시간
        const randomTop = Math.random() * 20;
        const randomLeft = Math.random() * 100;
        const randomDuration = 2 + Math.random() * 2;

        star.style.top = `${randomTop}%`;
        star.style.left = `${randomLeft}%`;
        star.style.animationDelay = `${delay}s`;
        star.style.animationDuration = `${randomDuration}s`;

        container.appendChild(star);
    };

   
    for (let i = 0; i < 3; i++) {
        createStar(i * 10);
    }
});
