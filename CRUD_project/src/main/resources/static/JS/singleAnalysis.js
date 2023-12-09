var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");

// 현재 페이지 번호를 저장하는 변수
var currentPageGood = 0;
var currentPageBad = 0;
var pageSize = 5;
// AJAX 요청과 HTML 생성 코드를 별도의 함수로 분리합니다.
function loadLogData(type, page, size) {
  // 로딩 인디케이터 표시
  $("#loadingIndicator").removeClass("d-none");

  $(".page-link").addClass("disabled");
  console.log(
    `Sending request to /deep/getAysLog with param: ${"단건"}, type: ${type}, page: ${page}, size: ${size}`
  );
  $.ajax({
    url: "/deep/getAysLog",
    type: "post",
    data: { param: "단건", type: type, page: page, size: size },
    beforeSend: function (xhr) {
      xhr.setRequestHeader(header, token);
    },
    success: function (data) {
      console.log("Received data: ", data);
      var html = "";
      data.content.forEach(function (row) {
        html += `
        <div class="card mb-3 card-red">
        <div class="row no-gutters" >

          <div class="col-sm-1 col-md-1 col-12 fixed-card">
            <div class="img-container" >
              <img class="img-thumbnail" data-toggle="modal" data-target="#imageModal" src="data:image/png;base64,${row.base64ProductImg
              }" alt="Image" />
            </div>
          </div>


          <p class="card-text-sub" style="margin-top: 30px; margin-left: 20px;">${row.predictionJdm} / ${row.predictionDate}</p>
          
            <div class="card-body text-lefts" >
              <h5 class="card-title" style="font-size: 1vw;">${row.predictionJdm}</h5>
              <p class="card-text" style="font-size: 0.8vw;">${(row.predictionAccuracy * 100)
              .toString()
              .slice(0, 4)}%</p>
              <p class="card-text" style="font-size: 0.8vw;">날짜: ${row.predictionDate}</p>
            </div>
          
    
        </div>
      </div>
              `;
      });
      if (type === "정상") {
        $("#goodModelTableBody").html(html);
        $("#goodPageNumber").text(data.number + 1);
        if (data.first) {
          $("#goodPreviousPage").addClass("disabled");
        } else {
          $("#goodPreviousPage").removeClass("disabled");
        }
        if (data.last) {
          $("#goodNextPage").addClass("disabled");
        } else {
          $("#goodNextPage").removeClass("disabled");
        }
      } else if (type === "불량") {
        $("#badModelTableBody").html(html);
        $("#badPageNumber").text(data.number + 1);
        if (data.first) {
          $("#badPreviousPage").addClass("disabled");
        } else {
          $("#badPreviousPage").removeClass("disabled");
        }
        if (data.last) {
          $("#badNextPage").addClass("disabled");
        } else {
          $("#badNextPage").removeClass("disabled");
        }
      }

      // 페이지 이동 버튼 활성화
      $(".page-link").removeClass("disabled");

      // 로딩 인디케이터 숨김

      $("#loadingIndicator").addClass("d-none");
    },
    error: function (jqXHR, textStatus, errorThrown) {
      console.log("AJAX Error: ", textStatus);

      // 페이지 이동 버튼 활성화
      $(".page-link").removeClass("disabled");

      // 로딩 인디케이터 숨김

      $("#loadingIndicator").addClass("d-none");
    },
  });
}

$(document).ready(function () {
  // 페이지 로드 시에 첫 번째 페이지의 데이터를 불러옵니다.
  loadLogData("정상", currentPageGood, pageSize);
  loadLogData("불량", currentPageBad, pageSize);

  // 페이지 이동 버튼을 클릭하면 새로운 페이지의 데이터를 불러옵니다.
  $("#goodPreviousPage").click(function (event) {
    event.preventDefault();
    if (!$(this).hasClass("disabled") && currentPageGood > 0) {
      console.log("뒤로가는버튼");
      currentPageGood--;
      loadLogData("정상", currentPageGood, pageSize);
    }
  });

  $("#goodNextPage").click(function (event) {
    event.preventDefault();
    if (!$(this).hasClass("disabled")) {
      console.log("앞으로가는버튼");
      currentPageGood++;
      loadLogData("정상", currentPageGood, pageSize);
    }
  });

  $("#badPreviousPage").click(function (event) {
    event.preventDefault();
    if (!$(this).hasClass("disabled") && currentPageBad > 0) {
      console.log("뒤로가는버튼2");
      currentPageBad--;
      loadLogData("불량", currentPageBad, pageSize);
    }
  });

  $("#badNextPage").click(function (event) {
    event.preventDefault();
    if (!$(this).hasClass("disabled")) {
      console.log("앞으로가는버튼2");
      currentPageBad++;
      loadLogData("불량", currentPageBad, pageSize);
    }
  });

  $("#refreshButton").click(function () {
    // 현재 페이지의 데이터를 다시 로드
    loadLogData("정상", currentPageGood, pageSize);
    loadLogData("불량", currentPageBad, pageSize);
  });
});

// 선택 이미지들 분석 후 로그 저장하기
async function readFilesAndSend(files) {
  var completedCount = 0;

  for (var i = 0; i < files.length; i++) {
    var reader = new FileReader();
    await new Promise((resolve, reject) => {
      reader.onload = function (event) {
        var base64Image = event.target.result.split(",")[1];
        sendDataToServer(base64Image)
          .then(() => {
            completedCount++;
            var percentage = (completedCount / files.length) * 100;
            $(".progress-bar")
              .css("width", percentage + "%")
              .attr("aria-valuenow", percentage);
            $(".h5.mb-0.mr-3.font-weight-bold.text-gray-800").text(
              percentage.toFixed(2) + "%"
            );
            resolve();
          })
          .catch((error) => {
            console.error("Error in readFilesAndSend:", error);
            reject(error);
          });
      };
      reader.readAsDataURL(files[i]);
    });
  }
}
// 전역 범위에 함수를 정의합니다.
function sendDataToServer(base64Image) {
  return new Promise((resolve, reject) => {
    $.ajax({
      url: "http://218.157.38.54:8002/predict/",
      type: "POST",
      data: JSON.stringify({ encoded_image_data: base64Image }),
      contentType: "application/json",
      success: function (response) {
        console.log(response);
        $.ajax({
          url: "/deep/aysUpload",
          method: "post",
          data: JSON.stringify({ ...response, class: "단건" }),
          contentType: "application/json",
          beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
          },
          success: function (data) {
            console.log("test");
            resolve(); // 두 번째 AJAX 요청이 성공하면 Promise를 resolve
          },
          error: function (error) {
            console.error("Error in second AJAX request:", error);
            reject(); // 두 번째 AJAX 요청이 실패하면 Promise를 reject
          },
        });
      },
      error: function (jqXHR, textStatus, errorMessage) {
        console.log(errorMessage);
        reject(); // 첫 번째 AJAX 요청이 실패하면 Promise를 reject
      },
    });
  });
}

// --------------------------
// 원형 차트
// ---------------------------

document.addEventListener("DOMContentLoaded", () => {
  const pieChart = echarts.init(document.querySelector("#pieChart"));
  pieChart.setOption({
    title: {
      text: "금일 양불판정 차트",
      left: "center",
    },
    tooltip: {
      trigger: "item",
    },
    legend: {
      orient: "vertical",
      left: "left",
    },
    series: [
      {
        name: "개수",
        type: "pie",
        radius: "70%",
        data: [
          {
            value: 1300,
            name: "정상",
            itemStyle: {
              color: '#5ac79f',
            },
          },
          {
            value: 700,
            name: "불량",
            itemStyle: {
              color: "#f27e7e",
            },
          },
        ],
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: "rgba(0, 0, 0, 0.5)",
          },
        },
      },
    ],
  });

  // 차트 크기 조절
  window.addEventListener("resize", () => {
    pieChart.resize();
  });
});

// --------------------------
// 분석로그
// div 보이기 / 숨기기 설정
// 정상탭과 불량탭 active 설정
// ---------------------------

window.onload = function () {
  $(document).ready(function () {
// 초기 설정: 아무 탭도 활성화하지 않음
$(".goodmodeltable").hide();
$(".badmodeltable").hide();

// 정상
$("#goodModelTableBody").click(function () {
  console.log("정상 탭a 클릭됨");
    $(".goodmodeltable").show();
    $(".badmodeltable").hide();

    $(this).addClass("active");
    $("#badModelTableBody").removeClass("active");

    // 추가된 부분
    $("#goodModelTable").addClass("active");
    $("#badModelTable").removeClass("active");
});

// 불량
$("#badModelTableBody").click(function () {
  console.log("불량 탭 클릭됨");
    $(".goodmodeltable").hide();
    $(".badmodeltable").show();

    $(this).addClass("active");
    $("#goodModelTableBody").removeClass("active");

    // 추가된 부분
    $("#goodModelTable").removeClass("active");
    $("#badModelTable").addClass("active");
});

// 초기에 정상 탭을 활성화
$("#goodModelTableBody").click();



// -----------------------
// 모달에 이미지 경로 설정
// ----------------------

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
  const $goodCounter = document.querySelector(".goodcount");
  const $badCounter = document.querySelector(".badcount");

  // 목표 수치
  const max = 17249;

  // 대기 시간 조정
  const delayTime = 2000; // 2초후 시작

  setTimeout(() => {
    counter($goodCounter, max);
    counter($badCounter, max);
  }, delayTime);
};
  });
};

//////////

