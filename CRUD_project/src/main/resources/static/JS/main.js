

// -----------------------
// 숫자카운트
// ----------------------

const counter = ($counter, max) => {
    let now = max;
  
    const handle = setInterval(() => {
      $counter.innerHTML = Math.ceil(max - now);
  
      // 목표수치에 도달하면 정지
      if (now < 1) {
        clearInterval(handle);
      }
  
      // 증가되는 값이 계속하여 작아짐
      const step = now / 10;
  
      // 값을 적용시키면서 다음 차례에 영향을 끼침
      now -= step;
    }, 50);
  };
  
  window.onload = () => {
    // 카운트를 적용시킬 요소
    const $userCounter = document.querySelector(".usercount");
    const $singleCounter = document.querySelector(".singlecount");
    const $realtimeCounter = document.querySelector(".realtimecount");

    // 대기 시간 조정
    const delayTime = 1000; // 2초후 시작
  
    // Thymeleaf 변수 사용
    setTimeout(() => {
        counter($userCounter, memberCount);  
        counter($singleCounter, singleAnalysisCount);
        counter($realtimeCounter, realTimeAnalysisCount);
    }, delayTime);
  };