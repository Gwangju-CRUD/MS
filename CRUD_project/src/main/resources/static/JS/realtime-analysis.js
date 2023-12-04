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

  function fetchImage(index) {
    $.ajax({
      url: "/deep/getOriginImages",
      type: "GET",
      data: {
        index: index,
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
    autoplayTimeout: 2000,
    autoplayHoverPause: true,
    margin: 50,
    onChanged: function (event) {
      // 현재 활성화된 아이템의 인덱스를 가져옴
      var currentIndex = this._current;
      // 만약 현재 아이템이 배열의 마지막 아이템이라면
      if (currentIndex === images.length - 1) {
        // 다음 이미지 데이터를 요청
        fetchImage(images.length);
      }
    },
  });

  // 초기 이미지 데이터 요청 (첫 2개)
  fetchImage(0);
  fetchImage(1);
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

$(".owl-carousel").owlCarousel({
  items: 6, // 슬라이드 표시할 항목 수
  loop: true, // 무한루프
  autoplay: true, // 자동 재생
  autoplayTimeout: 2000, // 시간간격(1초로해놈)
  autoplayHoverPause: true, // 마우스 호버 시 재생 정지
  margin: 50,
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
    text: "-1",
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
    data: ["0000", "0001", "0002", "0003", "0004", "0005", "0006", "0007"],
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
      data: [1, 1, -1, 1, 1, -1, 1],
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
      data: [-1, -1, 1, -1, -1, 1, -1],
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
