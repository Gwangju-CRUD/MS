var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");



var globalResult; // 전역 변수 선언


// 전역 범위에 함수를 정의합니다.
function sendImageToFastAPI(imageData) {
  $.ajax({
    url: "http://218.157.38.54:8002/predict/",
    type: "POST",
    data: JSON.stringify({ encoded_image_data: imageData }),
    contentType: "application/json",
    success: function (response) {
      globalResult = response.result; // 전역 변수에 결과 저장
      console.log(response);
      //DB넣기
      // $.ajax({
      //   url: "/deep/aysUpload",
      //   method: "post",
      //   data: JSON.stringify({ ...response, class: "실시간" }), // FastAPI에서 받아온 데이터를 그대로 전달
      //   contentType: "application/json",
      //   beforeSend: function (xhr) {
      //     xhr.setRequestHeader(header, token);
      //   },
      //   success: function (data) {
      //     console.log("test");
      //   },
      //   error: function (error) {
      //     console.error("Error in second AJAX request:", error);
      //   },
      // });
      printResultBasedOnCondition(); // 조건에 따라 결과 출력
      updateResultDisplay(); // 결과 표시 업데이트
      displayResultInTBox(); // t-box에 결과 표시 함수 호출
    },
    error: function (jqXHR, textStatus, errorMessage) {
      console.log(errorMessage); // Optional
    },
  });
}

// 다른 스크립트에서 전역 변수 사용
function printResult() {
  console.log(globalResult); // 전역 변수 출력
}


// function handleResultDisplay() {
//   var shadowBox = $('.shadow-effect');

//   // 판정에 따른 스타일 적용
//   if (globalResult === "정상") {
//     shadowBox.css('box-shadow', '0 0 27px green'); // 초록색 쉐도우 적용
//     console.log("%c정상", "color: green;"); // 콘솔에 초록색으로 "정상" 출력
//   } else if (globalResult === "불량") {
//     shadowBox.css('box-shadow', '0 0 27px red'); // 빨간색 쉐도우 적용
//     console.log("%c불량", "color: red;"); // 콘솔에 빨간색으로 "불량" 출력
//   }

//   // 1초 후에 스타일 초기화
//   setTimeout(function() {
//     shadowBox.css('box-shadow', ''); // 쉐도우 제거
//   }, 1180);
// }

// function handleResultDisplay() {
//   // .owl-stage 클래스를 가진 요소를 찾음
//   var shadowBox = $('.owl-carousel .owl-stage');

//   // 판정에 따른 스타일 적용
//   if (globalResult === "정상") {
//     shadowBox.css('box-shadow', '0 0 27px green'); // 초록색 쉐도우 적용
//     console.log("%c정상", "color: green;"); // 콘솔에 초록색으로 "정상" 출력
//   } else if (globalResult === "불량") {
//     shadowBox.css('box-shadow', '0 0 27px red'); // 빨간색 쉐도우 적용
//     console.log("%c불량", "color: red;"); // 콘솔에 빨간색으로 "불량" 출력
//   }

//   // 1초 후에 스타일 초기화
//   setTimeout(function() {
//     shadowBox.css('box-shadow', ''); // 쉐도우 제거
//   }, 1180);
// }




// AJAX 요청 성공 시 호출되는 함수 내에서 handleResultDisplay 호출
function sendImageToFastAPI(imageData) {
  $.ajax({
    url: "http://218.157.38.54:8002/predict/",
    type: "POST",
    data: JSON.stringify({ encoded_image_data: imageData }),
    contentType: "application/json",
    success: function (response) {
      globalResult = response.result; // 전역 변수에 결과 저장
      handleResultDisplay(); // 결과에 따라 판정 박스 표시 및 숨기기
    },
    error: function (jqXHR, textStatus, errorMessage) {
      console.log(errorMessage);
    },
  });
}

// 결과를 t-box에 표시하는 함수
function displayResultInTBox() {
  var tBox = document.querySelector('.t-box'); // '.t-box' 클래스를 가진 첫 번째 요소를 찾습니다.
  tBox.textContent = globalResult; // t-box의 텍스트 내용을 globalResult 값으로 설정합니다.
  // 결과 값에 따라 글씨 색상 변경
  if (globalResult === '정상') {
    tBox.style.color = 'green'; // '정상'일 경우 글씨 색상을 초록색으로 설정
  } else if (globalResult === '불량') {
    tBox.style.color = 'red'; // '불량'일 경우 글씨 색상을 빨간색으로 설정
  } else {
    tBox.style.color = 'black'; // 그 외의 경우 글씨 색상을 기본값(검정색)으로 설정
  }
    // 1초 후에 텍스트와 색상을 초기 상태로 되돌림
    setTimeout(function() {
      tBox.textContent = ''; // 텍스트 내용 지우기
      tBox.style.color = 'black'; // 글씨 색상을 기본값으로 되돌리기
    }, 1180);
}
// AJAX 요청 성공 시 호출되는 함수 내에서 displayResultInTBox 호출
function sendImageToFastAPI(imageData) {
  $.ajax({
    url: "http://218.157.38.54:8002/predict/",
    type: "POST",
    data: JSON.stringify({ encoded_image_data: imageData }),
    contentType: "application/json",
    success: function (response) {
      globalResult = response.result; // 전역 변수에 결과 저장
      displayResultInTBox(); // t-box에 결과 표시 함수 호출
      handleResultDisplay(); // 결과에 따라 판정 박스 표시 및 숨기기
    },
    error: function (jqXHR, textStatus, errorMessage) {
      console.log(errorMessage);
    },
  });
}




//슬라이드
window.onload = function () {
  $(document).ready(function () {
    // Tab 초기 설정
    $(".goodmodeltable").hide();
    $(".badmodeltable").hide();

    $("#goodmodeltab").click(function () {
      $(".goodmodeltable").show();
      $(".badmodeltable").hide();
      $(this).addClass("active");
      $("#badmodeltab").removeClass("active");
    });

    $("#badmodeltab").click(function () {
      $(".goodmodeltable").hide();
      $(".badmodeltable").show();
      $(this).addClass("active");
      $("#goodmodeltab").removeClass("active");
    });

    var images = [];
    var currentIndex = 1;

    function fetchImage(callback) {
      $.ajax({
        url: "/deep/getOriginImages",
        type: "GET",
        data: {
          index: currentIndex++,
        },
        success: function (data) {
          var imageUrl = "data:imgae/png;base64," + data;

          var imageElement = $(
            '<div class="owl-item"><img src="' +
              imageUrl +
              '" width="100px" height="300px;' +
              '" data-toggle="modal" data-target="#imageModal"></div>'
          );

          // 이미지 요소를 슬라이드쇼에 추가합니다.
          $(".owl-carousel")
            .trigger("add.owl.carousel", [imageElement])
            .trigger("refresh.owl.carousel");

          images.push(data);

          // 콜백 함수를 호출합니다.
          if (callback) {
            callback(data);
          }
        },
      });
    }
// 초기에 정상 탭을 활성화
$("#goodmodeltab").click();
$(document).ready(function () {
  // Owl Carousel 초기화
  $(".owl-carousel").owlCarousel({
    items: 6, loop: false, // 이미지가 더 이상 없을 때 슬라이드를 정지하기 위해 loop를 false로 설정합니다.
    autoplay: true,
    autoplayTimeout: 3150,
    autoplayHoverPause: true,
    smartSpeed: 1950,
    margin: 20,
    dots: false,
    rtl: true,
    onTranslated: function () {
      console.log("Slide has been translated");
      fetchImage();
      // 이미지 배열과 DOM 요소의 크기를 일정하게 유지합니다.
      if (images.length > 6) {
        setTimeout(function () {
          // setTimeout을 사용하여 이미지 추가와 제거를 비동기적으로 처리합니다.
          images.shift(); // 배열에서 첫 번째 이미지 데이터를 제거합니다.
          $(".owl-carousel")
          .trigger("remove.owl.carousel", [0]) // 슬라이드쇼에서 첫 번째 이미지 요소를 제거합니다.
          .trigger("refresh.owl.carousel");
          sendImageToFastAPI(images[0]);
        }, 0);
      }
      // $(".owl-carousel .owl-stage").css('box-shadow', '');
      console.log("Applying shadow to the first image");
      $(".owl-carousel .owl-item img").css('box-shadow', '');
      var firstImage = $(".owl-carousel .owl-item")
      .eq(2)
      .find('img');
      firstImage.addClass('shadow-effect');
    }, // 슬라이드가 완전히 변경된 후에 다음 이미지를 미리 로드합니다.
    });
    $(".owl-carousel").on('initialized.owl.carousel', function (event) {
      setTimeout(function(){
        $('.owl-stage').addClass('mt-5 mb-5 mr-5');
    }, 100);
    });
});


// JavaScript를 사용하여 창 크기에 따라 #highlight-box 조절
window.addEventListener('resize', adjustHighlightBox);

function adjustHighlightBox() {
  var highlightBox = document.getElementById('highlight-box');
  var windowWidth = window.innerWidth;

  // 기준이 되는 창 너비 설정 (예: 1000px)
  var baseWidth = 1950;

  // 현재 창 너비와 기준 너비의 차이에 따라 크기 조절
  var widthDiff = baseWidth - windowWidth;
  var newWidth = 290 - (widthDiff+100)/5.9; // 기본 너비에서 차이만큼 감소
  var newHeight = 400 - (widthDiff+100)/4.4; // 기본 높이에서 차이만큼 감소

  // 새로운 너비와 높이 적용
  highlightBox.style.width = newWidth + 'px';
  highlightBox.style.height = newHeight + 'px';
}

// 초기 로드시에도 한번 실행
adjustHighlightBox();


    



    // 초기 이미지 데이터 요청 (첫 7개)
    fetchImage(function (data) {
      sendImageToFastAPI(data);
    });
    fetchImage();
    fetchImage();
    fetchImage();
    fetchImage();
    fetchImage();
    fetchImage();
  });

  // 이미지 클릭 -> 모달이미지
  $(".owl-carousel img").on("click", function () {
    var imgSrc = $(this).attr("src");
    $("#modalImage").attr("src", imgSrc);
    $("#modalImageSrc").text("이미지 경로: " + imgSrc);
  });

  // 모달에 이미지 경로 설정
  $(document).ready(function () {
    // 이미지 클릭 -> 모달이미지
    $(".img-thumbnail").on("click", function () {
      var imgSrc = $(this).attr("src");
      console.log(imgSrc);
      $("#modalImage").attr("src", imgSrc);

      // 모달 바디 이미지 src 출력
      $("#modalImageSrc").text("이미지 경로: " + imgSrc);
    });
  });

  // 꺾은선 그래프 코드

  var dom = document.getElementById("container");
  var myChart = echarts.init(dom, null, {
    renderer: "canvas",
    useDirtyRect: false,
  });
  var app = {};

  var option;

  option = {
    title: {
      text: "",
    },
    tooltip: {
      trigger: "axis",
    },
    legend: {},
    toolbox: {
      show: true,
      feature: {
        dataZoom: {
          yAxisIndex: "none",
        },
        dataView: {
          readOnly: false,
        },
        magicType: {
          type: ["line", "bar"],
        },
        restore: {},
        saveAsImage: {},
      },
    },
    xAxis: {
      type: "category",
      boundaryGap: false,
      data: [
        "0000",
        "0001",
        "0002",
        "0003",
        "0004",
        "0005",
        "0006",
        "0007",
        "0008",
      ],
    },
    yAxis: {
      type: "value",
      axisLabel: {
        formatter: "{value} 상태",
      },
    },
    series: [
      {
        name: "정상",
        type: "line",
        data: [1, 1, -1, 1, 1, -1, 1, 1, 1],
        color: "#00AAFF",
        markPoint: {
          data: [
            {
              type: "max",
              name: "Max",
            },
            {
              type: "min",
              name: "Min",
            },
          ],
        },
        markLine: {
          data: [
            {
              type: "average",
              name: "Avg",
            },
          ],
        },
      },
      {
        name: "불량",
        type: "line",
        data: [-1, -1, 1, -1, -1, 1, -1, -1, -1],
        color: "rgb(251, 118, 123)",
        markPoint: {
          data: [
            {
              name: "周最低",
              value: -2,
              xAxis: 1,
              yAxis: -1.5,
            },
          ],
        },
        markLine: {
          data: [
            {
              type: "average",
              name: "Avg",
            },
            [
              {
                symbol: "none",
                x: "90%",
                yAxis: "max",
              },
              {
                symbol: "circle",
                label: {
                  position: "start",
                  formatter: "Max",
                },
                type: "max",
                name: "最高点",
              },
            ],
          ],
        },
      },
    ],
  };

  if (option && typeof option === "object") {
    myChart.setOption(option);
  }

  window.addEventListener("resize", myChart.resize);
};


function handleResultDisplay() {
  var shadowEffectBox = $('.shadow-effect');
  var owlStageBox = $('.owl-carousel .owl-stage-outer');

  // 판정에 따른 스타일 적용
  if (globalResult === "정상") {
    shadowEffectBox.css('box-shadow', '-30px 0px 45px -33px green'); // .shadow-effect에 초록색 쉐도우 적용
    owlStageBox.css('box-shadow', '30px 0px 45px -33px green'); // .owl-stage에 수정된 초록색 쉐도우 적용
    console.log("%c정상", "color: green;");
  } else if (globalResult === "불량") {
    shadowEffectBox.css('box-shadow', '-30px 0px 45px -33px red');   // .shadow-effect에 빨간색 쉐도우 적용
    owlStageBox.css('box-shadow', '30px 0px 45px -33px red');   // .owl-stage에 수정된 빨간색 쉐도우 적용
    console.log("%c불량", "color: red;");
  }

  // 1초 후에 스타일 초기화
  setTimeout(function() {
    shadowEffectBox.css('box-shadow', ''); // .shadow-effect의 쉐도우 제거
    owlStageBox.css('box-shadow', '');     // .owl-stage의 쉐도우 제거
  }, 1180);
}