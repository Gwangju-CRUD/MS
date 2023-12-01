
$('[data-toggle="modal"]').click(function() {
    var analysisType = $(this).attr('data-analysis');
    var analysisText = analysisType === 'video' ? '실시간 분석' : '이미지 분석';

    $('#analysisModalLabel').text(analysisText);
    $('.modal-body').text(analysisText + '을 시작하시겠습니까?');
    $('#startAnalysis').attr('data-analysis', analysisType);
});


$("#startAnalysis").click(function() {
    $(this).prop('disabled', true).text('로딩 중...');
    $('#analysisModal').modal('hide');

    var analysisType = $(this).attr('data-analysis');
    if (analysisType === 'video') {
        videoAysFromServer();
    } else if (analysisType === 'image') {
        imageAysFromServer();
    }
});








/*$("#videoAys").click(function() {
	$(this).prop('disabled', true).text('로딩 중...');
	videoAysFromServer();
});*/

function videoAysFromServer() {
	$.ajax({
		url: "http://218.157.38.54:8002/apply_weights/",
		method: "post",
		success: function(data) {
			console.log(data.message);
			setTimeout(function() {
				window.location.href = "/deep/videoAnalysis";
			}, 200);
		},
		error: function(error) {
			console.error("Error:", error);
			$('#videoAys').prop('disabled', false).text('실시간 분석');
		},
	});
}

/*$("#imageAys").click(function() {
	$(this).prop('disabled', true).text('로딩 중...');
	imageAysFromServer();
});*/

function imageAysFromServer() {
	$.ajax({
		url: "http://218.157.38.54:8002/apply_weights/",
		method: "post",
		success: function(data) {
			console.log(data.message);
			setTimeout(function() {
				window.location.href = "/deep/imgAnalysis";
			}, 200);
		},
		error: function(error) {
			console.error("Error:", error);
			$('#imageAys').prop('disabled', false).text('이미지 분석');
		},
	});
}