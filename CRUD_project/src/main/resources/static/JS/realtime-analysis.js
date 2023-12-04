document.addEventListener("DOMContentLoaded", () => {
    const pieChart = echarts.init(document.querySelector("#pieChart"));
    pieChart.setOption({
        title: {
            text: "금일 양불판정 차트",
            left: "center"
        },
        tooltip: {
            trigger: "item"
        },
        legend: {
            orient: "vertical",
            left: "left"
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
                            color: "gray"
                        }
                    }, {
                        value: 700,
                        name: "불량",
                        itemStyle: {
                            color: "#EFDC05"
                        }
                    }
                ],
                emphasis: {
                    itemStyle: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: "rgba(0, 0, 0, 0.5)"
                    }
                }
            }
        ]
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

$('.owl-carousel').owlCarousel({

    items: 6, // 슬라이드 표시할 항목 수
    loop: true, // 무한루프
    autoplay: true, // 자동 재생
    autoplayTimeout: 1000, // 시간간격(1초로해놈)
    autoplayHoverPause: true, // 마우스 호버 시 재생 정지
    margin: 50
});

$(document).ready(function () {
    // 이미지 클릭 -> 모달이미지
    $('.owl-carousel img').on('click', function () {
        var imgSrc = $(this).attr('src');
        console.log(imgSrc);
        $('#modalImage').attr('src', imgSrc);

        // 모달 바디 이미지 src 출력
        $('#modalImageSrc').text('이미지 경로: ' + imgSrc);
    });

});

// 꺾은선 그래프 코드

var dom = document.getElementById('container');
var myChart = echarts.init(dom, null, {
    renderer: 'canvas',
    useDirtyRect: false
});
var app = {};

var option;

option = {
    title: {
        text: '-1'
    },
    tooltip: {
        trigger: 'axis'
    },
    legend: {},
    toolbox: {
        show: true,
        feature: {
            dataZoom: {
                yAxisIndex: 'none'
            },
            dataView: {
                readOnly: false
            },
            magicType: {
                type: ['line', 'bar']
            },
            restore: {},
            saveAsImage: {}
        }
    },
    xAxis: {
        type: 'category',
        boundaryGap: false,
        data: [
            '0000',
            '0001',
            '0002',
            '0003',
            '0004',
            '0005',
            '0006',
            '0007'
        ]
    },
    yAxis: {
        type: 'value',
        axisLabel: {
            formatter: '{value} 상태'
        }
    },
    series: [
        {
            name: '정상',
            type: 'line',
            data: [
                1,
                1,
                -1,
                1,
                1,
                -1,
                1
            ],
            markPoint: {
                data: [
                    {
                        type: 'max',
                        name: 'Max'
                    }, {
                        type: 'min',
                        name: 'Min'
                    }
                ]
            },
            markLine: {
                data: [
                    {
                        type: 'average',
                        name: 'Avg'
                    }
                ]
            }
        }, {
            name: '불량',
            type: 'line',
            data: [
                -1,
                -1,
                1,
                -1,
                -1,
                1,
                -1
            ],
            markPoint: {
                data: [
                    {
                        name: '周最低',
                        value: -2,
                        xAxis: 1,
                        yAxis: -1.5
                    }
                ]
            },
            markLine: {
                data: [
                    {
                        type: 'average',
                        name: 'Avg'
                    },
                    [
                        {
                            symbol: 'none',
                            x: '90%',
                            yAxis: 'max'
                        }, {
                            symbol: 'circle',
                            label: {
                                position: 'start',
                                formatter: 'Max'
                            },
                            type: 'max',
                            name: '最高点'
                        }
                    ]
                ]
            }
        }
    ]
};

if (option && typeof option === 'object') {
    myChart.setOption(option);
}

window.addEventListener('resize', myChart.resize);