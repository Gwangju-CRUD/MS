var video = document.getElementById("video");
var canvas = document.getElementById("canvas");
var context = canvas.getContext("2d");

navigator.mediaDevices
  .getUserMedia({ video: true })
  .then((stream) => {
    video.srcObject = stream;
    video.onloadedmetadata = (e) => {
      video.play();
    };
  })
  .catch((err) => {
    console.log("An error occurred: " + err);
  });
  
  
  
var capturing = false;
var label = null;

$("#capture_normal").click(function () {
  capturing = !capturing;
  label = "normal";  // 정상 이미지 라벨 설정
});

$("#capture_error").click(function () {
  capturing = !capturing;
  label = "error";  // 불량 이미지 라벨 설정
});


setInterval(function () {
  if (capturing) {
    canvas.width = video.videoWidth;
    canvas.height = video.videoHeight;
    context.drawImage(video, 0, 0, canvas.width, canvas.height);
    var imageData = canvas.toDataURL("image/png");
    var base64Image = imageData.split(",")[1];
    console.log(base64Image)

    sendDataToServer(base64Image, label);
  }
}, 1000 / 30); // 1초에 30번 요청



function sendDataToServer(base64Image, label) {
  $.ajax({
    url: "http://218.157.38.54:8002/upload_image/",
    method: "post",
    data: JSON.stringify({encoded_image_data: String(base64Image), label: label}),
    contentType: "application/json",
    success: function (data) {
      // 분류 결과와 정확도를 받아와 화면에 표시
      var normal = data.normal;
      var error = data.error;
      // 화면에 분류 결과와 정확도 표시
      $("#normal_count").text(normal);
      $("#error_count").text(error);
    },
    error: function (error) {
      console.error("Error:", error);
    },
  });
}



$("#delete_images").click(function () {
  deleteImagesFromServer();  // 이미지 삭제 버튼 클릭 시 서버에 이미지 삭제 요청
});

function deleteImagesFromServer() {
  $.ajax({
    url: "http://218.157.38.54:8002/delete_images/",
    method: "delete",  // DELETE 메서드 사용
    success: function (data) {
      // 서버로부터 받은 메시지를 화면에 표시
      alert(data.message);
      // 이미지 개수 표시를 0으로 초기화
      $("#normal_count").text(0);
      $("#error_count").text(0);
    },
    error: function (error) {
      console.error("Error:", error);
    },
  });
}


$("#create_model").click(function () {
  createModelFromServer(); 
});


function createModelFromServer() {
  var modelName = $("#model_name").val();  // 입력받은 모델 이름

  // 모델 이름이 비어있는지 확인
  if (!modelName) {
    alert("모델 이름을 입력해주세요");
    return;
  }

  $.ajax({
    url: "http://218.157.38.54:8002/create_model/",
    method: "post", 
    data: JSON.stringify({ name: modelName }),  // 입력받은 모델 이름을 데이터로 전달
    contentType: 'application/json',
    success: function (data) {
      alert(data.message);
      $("#create_model_msg").text("모델 생성 완료!");
      $("#model_name").val("");  // input 내용 비우기
    },
    error: function (error) {
      console.error("Error:", error);
    },
  });
}

