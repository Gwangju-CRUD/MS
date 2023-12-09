var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");
var myChart = null;
var globalResult; // 전역 변수 선언

function getGraphLog() {
  $.ajax({
    url: "/deep/getGraphLog",
    type: "POST",
    beforeSend: function (xhr) {
      xhr.setRequestHeader(header, token);
    },
    success: function (data) {
      let now = moment(); // 현재 시간
      let past = moment().subtract(30, "minutes"); // 30분 전

      let normalCounts = new Array(7).fill(0); // 정상 개수
      let abnormalCounts = new Array(7).fill(0); // 불량 개수
      let timeLabels = []; // x축 레이블 (시간)

      // 데이터를 분류하고 개수를 계산
      for (let item of data) {
        let time = moment(item.predictionDate, "YYYY년 MM월 DD일 HH시 mm분");

        if (time.isBetween(past, now)) {
          let index = Math.floor(now.diff(time, "minutes") / 5);
          if (item.predictionJdm === "정상") {
            normalCounts[index]++;
          } else {
            abnormalCounts[index]++;
          }
        }
      }

      // x축 레이블 생성
      for (let i = 0; i < 7; i++) {
        timeLabels.push(
          past
            .clone()
            .add(5 * i, "minutes")
            .format("HH:mm")
        );
      }

      // 그래프에 데이터를 추가하고 업데이트
      myChart.setOption({
        xAxis: {
          data: timeLabels,
        },
        series: [
          {
            data: normalCounts.reverse(),
          },
          {
            data: abnormalCounts.reverse(),
          },
        ],
      });
    },
    error: function (error) {
      console.error("Error:", error);
    },
  });
}

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
    `Sending request to /deep/getAysLog with param: ${"실시간"}, type: ${type}, page: ${page}, size: ${size}`
  );
  $.ajax({
    url: "/deep/getAysLog",
    type: "post",
    data: {
      param: "실시간",
      type: type,
      page: page,
      size: size,
    },
    beforeSend: function (xhr) {
      xhr.setRequestHeader(header, token);
    },
    success: function (data) {
      console.log("Received data: ", data);
      var html = "";
      data.content.forEach(function (row) {
        html += `
              <tr>
                <td>
                    <img class="img" src="data:image/png;base64,${
                      row.base64ProductImg
                    }" alt="Image" width="100" height="100" />
                </td>
                <td>${row.predictionDate}</td>
                <td>${(row.predictionAccuracy * 100)
                  .toString()
                  .slice(0, 4)}%</td>
                <td>${row.predictionJdm}</td>
              </tr>
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
// 전역 범위에 함수를 정의합니다.
function sendImageToFastAPI(imageData) {
  $.ajax({
    url: "http://218.157.38.54:8002/predict/",
    type: "POST",
    data: JSON.stringify({ encoded_image_data: imageData }),
    contentType: "application/json",
    success: function (response) {
      console.log(response);
      globalResult = response.result;
      tboxFlash(globalResult);
      $.ajax({
        url: "/deep/aysUpload",
        method: "post",
        data: JSON.stringify({ ...response, class: "실시간" }), // FastAPI에서 받아온 데이터를 그대로 전달
        contentType: "application/json",
        beforeSend: function (xhr) {
          xhr.setRequestHeader(header, token);
        },
        success: function (data) {
          console.log("test");
        },
        error: function (error) {
          console.error("Error in second AJAX request:", error);
        },
      });
    },
    error: function (jqXHR, textStatus, errorMessage) {
      console.log(errorMessage); // Optional
    },
  });
}

function tboxFlash(globalResult) {
  var tBox = document.querySelector(".t-box"); // '.t-box' 클래스를 가진 첫 번째 요소를 찾습니다.
  tBox.textContent = globalResult; // t-box의 텍스트 내용을 globalResult 값으로 설정합니다.
  // 결과 값에 따라 글씨 색상 변경
  if (globalResult === "정상") {
    tBox.style.color = "green"; // '정상'일 경우 글씨 색상을 초록색으로 설정
  } else if (globalResult === "불량") {
    tBox.style.color = "red"; // '불량'일 경우 글씨 색상을 빨간색으로 설정
  } else {
    tBox.style.color = "black"; // 그 외의 경우 글씨 색상을 기본값(검정색)으로 설정
  }
  // 1초 후에 텍스트와 색상을 초기 상태로 되돌림
  setTimeout(function () {
    tBox.textContent = ""; // 텍스트 내용 지우기
    tBox.style.color = "black"; // 글씨 색상을 기본값으로 되돌리기
  }, 1180);

  if (globalResult === "정상") {
    $(".owl-carousel .owl-item:first img").css(
      "box-shadow",
      "0px 0px 60px -3px green"
    );
  } else if (globalResult === "불량") {
    $(".owl-carousel .owl-item:first img").css(
      "box-shadow",
      "0px 0px 60px -3px red"
    );
  }
  setTimeout(function () {
    $(".owl-carousel .owl-item:first img").css("box-shadow", ""); // 테두리 제거
  }, 1180);
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

    // Owl Carousel 초기화
    $(".owl-carousel").owlCarousel({
      items: 6,
      loop: false, // 이미지가 더 이상 없을 때 슬라이드를 정지하기 위해 loop를 false로 설정합니다.
      autoplay: true,
      autoplayTimeout: 2150,
      autoplayHoverPause: false,
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
            getGraphLog();
            sendImageToFastAPI(images[0]);
          }, 0);
        }
      }, // 슬라이드가 완전히 변경된 후에 다음 이미지를 미리 로드합니다.
    });
    $(".owl-carousel").on("initialized.owl.carousel", function (event) {
      setTimeout(function () {
        $(".owl-stage").addClass("mt-5 mb-5 mr-5");
      }, 100);
    });

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
};

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
  },
  yAxis: {
    type: "value",
    axisLabel: {
      formatter: "{value} ",
    },
  },
  series: [
    {
      name: "정상",
      type: "line",
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

// 초기 옵션 설정
var option = {
  xAxis: {
    type: "category",
    // data: xAxisData
  },
  // 기타 옵션 설정...
};
