// Call the dataTables jQuery plugin
$(document).ready(function () {
  $("#dataTable").DataTable();

  var token = $("meta[name='_csrf']").attr("content");
  var header = $("meta[name='_csrf_header']").attr("content");

  // 모델 선택 select 요소와 분석 시작 버튼을 참조합니다.

  var startAnalysisButton = $("#startAnalysis");
  var modelSelect; // 전역 변수로 선언

  // 모달이 표시될 때마다 사용자의 모델 목록을 불러옵니다.
  $("#analysisModal").on("shown.bs.modal", function () {
    startAnalysisButton.prop("disabled", true).text("모델을 선택해주세요");

    // <select> 태그 동적 생성
    modelSelect = $("<select>").attr("id", "modelSelect"); // 전역 변수에 할당
    $(".modal-body").append(modelSelect);

    $.ajax({
      url: "/deep/getModel",
      method: "post",
      beforeSend: function (xhr) {
        xhr.setRequestHeader(header, token);
      },
      success: function (model_info) {
        modelSelect.empty();

        console.log(modelSelect);
        console.log(model_info);
        console.log(model_info.length);
        for (var i = 0; i < model_info.length; i++) {
          var option = $("<option>")
            .val(model_info[i].model)
            .text(model_info[i].modelName);
          modelSelect.append(option);
        }
        modelSelect.val(model_info[0].model); // 첫 번째 <option>을 선택합니다.
        startAnalysisButton.prop("disabled", false).text("분석 시작"); // "분석 시작" 버튼을 활성화합니다

        // 모델 선택 select 요소에서 선택이 변경되면 분석 시작 버튼을 활성화합니다.
        modelSelect.change(function () {
          if (modelSelect.val()) {
            startAnalysisButton.prop("disabled", false).text("분석 시작");
          } else {
            startAnalysisButton
              .prop("disabled", true)
              .text("모델을 선택해주세요");
          }
        });
      },
      error: function (error) {
        console.error("Error:", error);
      },
    });
  });

  // 모달이 닫힐 때 <select> 태그를 삭제합니다.
  $("#analysisModal").on("hidden.bs.modal", function () {
    $("#modelSelect").remove();
  });

  // ------------------ 모달 선택부 --------------------------

  $('[data-toggle="modal"]').click(function () {
    var analysisType = $(this).attr("data-analysis");
    var analysisText = analysisType === "video" ? "실시간 분석" : "이미지 분석";

    $("#analysisModalLabel").text(analysisText);
    $(".modal-body").text(analysisText + "을 시작하시겠습니까?");
    $("#startAnalysis").attr("data-analysis", analysisType);
  });

  $("#startAnalysis").click(function () {
    $(this).prop("disabled", true).text("로딩 중...");
    $("#analysisModal").modal("hide");

    var analysisType = $(this).attr("data-analysis");
    var modelPath = modelSelect.val(); //전역 변수 사용
    if (analysisType === "video") {
      videoAysFromServer(modelPath);
    } else if (analysisType === "image") {
      imageAysFromServer(modelPath);
    }
  });

  function videoAysFromServer(modelPath) {
    $.ajax({
      url: "http://218.157.38.54:8002/apply_weights/",
      method: "post",
      contentType: "application/json",
      data: JSON.stringify(modelPath),
      success: function (data) {
        console.log(data.message);
        setTimeout(function () {
          window.location.href = "/deep/realtimeAnalysis";
        }, 200);
      },
      error: function (error) {
        console.error("Error:", error);
        $("#videoAys").prop("disabled", false).text("실시간 분석");
      },
    });
  }

  function imageAysFromServer(modelPath) {
    $.ajax({
      url: "http://218.157.38.54:8002/apply_weights/",
      method: "post",
      contentType: "application/json",
      data: JSON.stringify(modelPath),
      success: function (data) {
        console.log(data.message);
        setTimeout(function () {
          window.location.href = "/deep/imgAnalysis";
        }, 200);
      },
      error: function (error) {
        console.error("Error:", error);
        $("#imageAys").prop("disabled", false).text("이미지 분석");
      },
    });
  }
});
