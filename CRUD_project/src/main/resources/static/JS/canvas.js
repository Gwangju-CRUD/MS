$(document).ready(function () {
  setInterval(function () {
    canvas.width = video.videoWidth;
    canvas.height = video.videoHeight;
    context.drawImage(video, 0, 0, canvas.width, canvas.height);
    var imageData = canvas.toDataURL("image/png");
    var base64Image = imageData.split(",")[1];
    console.log(base64Image);

    sendDataToServer(base64Image);
  }, 1000 / 1); // 1초에 1번 요청

  function sendDataToServer(base64Image) {
    $.ajax({
      url: "http://218.157.38.54:8002/predict/",
      method: "post",
      data: JSON.stringify({ encoded_image_data: String(base64Image) }),
      contentType: "application/json",
      success: function (data) {
        // 분류 결과와 정확도를 받아와 화면에 표시
        var result = data.result;
        var score = data.score;
        // 화면에 분류 결과와 정확도 표시
        $("#result").text(result);
        $("#score").text(score.toFixed(2)); // 소수 둘째 자리까지 표시

        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");
        console.log(header);

        $.ajax({
          url: "/deep/aysUpload",
          method: "post",
          data: JSON.stringify(data), // FastAPI에서 받아온 데이터를 그대로 전달
          contentType: "application/json",
          beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
          },
          success: function (response) {
            console.log("test");
          },
          error: function (error) {
            console.error("Error in second AJAX request:", error);
          },
        });
      },
      error: function (error) {
        console.error("Error:", error);
      },
    });
  }
});
