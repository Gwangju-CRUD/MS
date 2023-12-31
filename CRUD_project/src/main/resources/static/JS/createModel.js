var video = document.getElementById("video");
var canvas = document.getElementById("canvas");
var context = canvas.getContext("2d");

var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");

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
  label = "normal"; // 정상 이미지 라벨 설정
});

$("#capture_error").click(function () {
  capturing = !capturing;
  label = "error"; // 불량 이미지 라벨 설정
});

setInterval(function () {
  if (capturing) {
    canvas.width = video.videoWidth;
    canvas.height = video.videoHeight;
    context.drawImage(video, 0, 0, canvas.width, canvas.height);
    var imageData = canvas.toDataURL("image/png");
    var base64Image = imageData.split(",")[1];
    console.log(base64Image);

    sendDataToServer(base64Image, label);
  }
}, 1000 / 30); // 1초에 30번 요청

function sendDataToServer(base64Image, label) {
  // $.ajax 호출의 Promise를 반환하도록 수정
  return new Promise((resolve, reject) => {
    $.ajax({
      url: "http://218.157.38.54:8002/upload_image/",
      method: "post",
      data: JSON.stringify({
        encoded_image_data: String(base64Image),
        label: label,
      }),
      contentType: "application/json",
      success: function (data) {
        var normal = data.normal;
        var error = data.error;
        // 화면에 각각의 장수 표시
        $("#normal_count").text(normal);
        $("#error_count").text(error);
        resolve(); // ajax 호출이 성공하면 Promise를 resolve
      },
      error: function (error) {
        console.error("Error:", error);
        reject(); // ajax 호출이 실패하면 Promise를 reject
      },
    });
  });
}

$("#delete_images").click(function () {
  deleteImagesFromServer(); // 이미지 삭제 버튼 클릭 시 서버에 이미지 삭제 요청
});

function deleteImagesFromServer() {
  $.ajax({
    url: "http://218.157.38.54:8002/delete_images/",
    method: "delete", // DELETE 메서드 사용
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
  var modelName = $("#model_name").val(); // 입력받은 모델 이름
  createModelFromServer(modelName);
});

function createModelFromServer(modelName) {
  // 모델 이름이 비어있는지 확인
  if (!modelName) {
    alert("모델 이름을 입력해주세요");
    return;
  }

  $.ajax({
    url: "/deep/create_model",
    method: "post",
    data: JSON.stringify({ name: modelName }), // 입력받은 모델 이름을 데이터로 전달
    contentType: "application/json",
    beforeSend: function (xhr) {
      xhr.setRequestHeader(header, token);
    },
    success: function (data) {
      var parsedData = JSON.parse(data);
      alert(parsedData.message);
      //location.href = "/main"; // 페이지 이동
    },
    error: function (error) {
      console.error("Error:", error);
    },
  });
}

// 컨트롤러에 이미지 업로드 요청
$("#upload_images").click(function () {
  $.ajax({
    url: "/deep/uploadImages",
    type: "POST",
    beforeSend: function (xhr) {
      xhr.setRequestHeader(header, token);
    },
    success: function (data) {
      alert("이미지 업로드 성공");
    },
    error: function (request, error) {
      alert("이미지 업로드 실패: " + error);
    },
  });
});

// 폴더 이미지 선택으로 모델 가중치 생성

function readFilesAndSend(files, label) {
  var promises = []; // 각 파일의 전송 상태를 추적하는 Promise 객체를 저장할 배열

  for (var i = 0; i < files.length; i++) {
    var promise = new Promise((resolve, reject) => {
      var reader = new FileReader();
      reader.onload = function (event) {
        var base64Image = event.target.result.split(",")[1];
        sendDataToServer(base64Image, label)
          .then(() => {
            resolve(); // sendDataToServer가 성공하면 Promise를 resolve
          })
          .catch(() => {
            reject(); // sendDataToServer가 실패하면 Promise를 reject
          });
      };
      reader.readAsDataURL(files[i]);
    });

    promises.push(promise);
  }

  return Promise.all(promises); // 모든 이미지의 전송이 완료될 때까지 대기하는 Promise 객체를 반환
}

$("#folder_image_create").click(async function () {
  var normalFiles = document.getElementById("folder_path_normal").files;
  var errorFiles = document.getElementById("folder_path_error").files;
  var modelName = $("#imgae_model_name").val();

  // 모든 이미지 파일을 서버로 전송하고, 그 작업이 완료되면 모델 생성 요청을 보냄
  await readFilesAndSend(normalFiles, "normal"); // await 키워드 추가
  await readFilesAndSend(errorFiles, "error"); // await 키워드 추가

  // 모든 이미지 파일을 서버로 전송한 후에 모델 생성 요청을 보냄
  createModelFromServer(modelName);
});
