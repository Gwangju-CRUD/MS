var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");

$(document).ready(function () {
  // '모델 삭제' 탭을 클릭했을 때의 동작
  $("#modelDelete").click(function () {
    $("#modelDeleteTable").show(); // '모델 삭제' 내용을 보여줍니다.
    $("#addTrainingTable").hide(); // '모델 추가학습' 내용을 숨깁니다.

    $(this).addClass("active"); // '모델 삭제' 탭을 활성화 상태로 만듭니다.
    $("#addTraining").removeClass("active"); // '모델 추가학습' 탭을 비활성화 상태로 만듭니다.

    $("#modelDeleteBody").empty();
    $.ajax({
      url: "/deep/getModel",
      type: "POST",
      dataType: "json",
      beforeSend: function (xhr) {
        xhr.setRequestHeader(header, token);
      },
      success: function (models) {
        // 모델 데이터를 테이블에 추가합니다.
        models.forEach(function (model, index) {
          $("#modelDeleteBody").append(
            "<tr data-model-idx='" +
              model.modelIdx +
              "'>" +
              '<th scope="col">' +
              (index + 1) +
              "</th>" +
              '<th scope="col">' +
              model.modelDate +
              "</th>" +
              '<th scope="col">' +
              model.modelName +
              "</th>" +
              '<th scope="col"><button type="button" class="btn btn-danger">삭제</button></th>' +
              "</tr>"
          );
        });
      },
      error: function (request, status, error) {
        console.log("Ajax error: " + error);
      },
    });
  });

  // 모델 삭제버튼을 눌렀을 때의 동작
  $("#modelDeleteBody").on("click", ".btn-danger", function () {
    var modelIdx = $(this).closest("tr").data("model-idx"); // data('model-idx')를 사용하여 modelIdx를 찾습니다.
    var modelName = $(this).closest("tr").find("th").eq(2).text(); // 모델 이름을 찾습니다.

    if (confirm("정말로 " + modelName + " 모델을 삭제하시겠습니까?")) {
      // '/deleteModel' 엔드포인트로 삭제 요청을 보냅니다.
      $.ajax({
        url: "/deep/deleteModel",
        type: "POST",
        data: { modelIdx: modelIdx },
        beforeSend: function (xhr) {
          xhr.setRequestHeader(header, token);
        },
        success: function (response) {
          // 삭제가 성공적으로 이루어졌다면 행을 삭제합니다.
          if (response) {
            $(this).closest("tr").remove();
            $("#modelDelete").click();
          }
        },
        error: function (request, status, error) {
          console.log("Ajax error: " + error);
        },
      });
    }
  });

  // '모델 추가학습' 탭을 클릭했을 때의 동작
  $("#addTraining").click(function () {
    $("#modelDeleteTable").hide(); // '모델 삭제' 내용을 숨깁니다.
    $("#addTrainingTable").show(); // '모델 추가학습' 내용을 보여줍니다.

    $(this).addClass("active"); // '모델 추가학습' 탭을 활성화 상태로 만듭니다.
    $("#modelDelete").removeClass("active"); // '모델 삭제' 탭을 비활성화 상태로 만듭니다.
  });

  // 페이지가 로드되면 '모델 삭제' 탭을 기본적으로 선택합니다.
  $("#modelDelete").click();

  // ------------------여기까지 카드 선택--------------------------
});
