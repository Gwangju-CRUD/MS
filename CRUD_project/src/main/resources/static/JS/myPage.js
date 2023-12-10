
// dropAreaId와 fileListId : 두 매개변수를 통해 드롭 영역과 파일 목록을 식별
function DropFile(dropAreaId, fileListId) {
    let dropArea = document.getElementById(dropAreaId);
    let fileList = document.getElementById(fileListId);
  
    // preventDefaults : 이벤트의 기본 동작을 방지하고 이벤트 전파를 중지
    function preventDefaults(e) {
      e.preventDefault();
      e.stopPropagation();
    }
    
    // highlight : 드롭 영역에 들어올 때 스타일을 변경하여 강조
    function highlight(e) {
      preventDefaults(e);
      dropArea.classList.add("highlight");
    }
    // unhighlight : 드롭 영역을 떠날 때 스타일을 원래대로 돌려놓음
    function unhighlight(e) {
      preventDefaults(e);
      dropArea.classList.remove("highlight");
    }
    
    // handleDrop : 파일을 드롭했을 때 호출됨
    //              파일을 처리하고 미리보기를 생성
    function handleDrop(e) {
      unhighlight(e);
      let dt = e.dataTransfer;
      let files = dt.files;
  
      handleFiles(files);
  
      const fileList = document.getElementById(fileListId);
      if (fileList) {
        fileList.scrollTo({ top: fileList.scrollHeight });
      }
    }
  
    // handleFiles : 드롭된 파일 목록을 배열로 변환
    //               각 파일에 대해 previewFile 함수를 호출
    function handleFiles(files) {
      files = [...files];
      // files.forEach(uploadFile);
      files.forEach(previewFile);
    }
    
    // previewFile : 파일의 미리보기를 생성하기 위해 renderFile 함수를 호출
    function previewFile(file) {
      console.log(file);
      renderFile(file);
    }

    // renderFile : FileReader를 사용하여 파일을 읽고,
    //              읽기가 완료되면 미리보기 이미지를 갱신
    function renderFile(file) {
      let reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onloadend = function () {
        
        // 모달에서 img id = preview인 곳에 새 이미지가 들어감
        // 이 이미지를 모달에 '변경' 을 누르면
        // 프로필이미지도 변경되어야함
        let img = dropArea.getElementsByClassName("preview")[0];
        img.src = reader.result;
        img.style.display = "block";


      };
    }
    // dragenter, dragover, dragleave, drop 이벤트에 대한 리스너를 dropArea에 등록
    // 대응하는 함수가 호출 -> 스타일을 변경하고 파일을 처리
    dropArea.addEventListener("dragenter", highlight, false);
    dropArea.addEventListener("dragover", highlight, false);
    dropArea.addEventListener("dragleave", unhighlight, false);
    dropArea.addEventListener("drop", handleDrop, false);
  
    // 드롭된 파일 객체 반환
    return {
      handleFiles
    };

}

  // DropFile 클래스 인스턴스 생성,
  // id가 "drop-file"이며 파일 목록의 ID가 "files"인 요소들을 사용하여 초기화
  const dropFile = new DropFile("drop-file", "files");