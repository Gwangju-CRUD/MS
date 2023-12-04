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
              color: "gray",
            },
          },
          {
            value: 700,
            name: "불량",
            itemStyle: {
              color: "#EFDC05",
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

$(document).ready(function () {
  // 초기 설정: 아무 탭도 활성화하지 않음
  $(".goodmodeltable").hide();
  $(".badmodeltable").hide();

  // 정상
  $("#goodmodeltab").click(function () {
    $(".goodmodeltable").show();
    $(".badmodeltable").hide();

    $(this).addClass("active");
    $("#badmodeltab").removeClass("active");
  });

  // 불량
  $("#badmodeltab").click(function () {
    $(".goodmodeltable").hide();
    $(".badmodeltable").show();

    $(this).addClass("active");
    $("#goodmodeltab").removeClass("active");
  });

  // 초기에 정상 탭을 활성화
  $("#goodmodeltab").click();
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
