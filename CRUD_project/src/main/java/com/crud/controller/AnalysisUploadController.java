package com.crud.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.crud.entity.Analysis;
import com.crud.entity.DeepModel;
import com.crud.entity.Member;
import com.crud.entity.OriginImage;
import com.crud.repository.DeepModelRepository;
import com.crud.repository.OriginImageRepository;
import com.crud.service.AnalysisService;
import com.crud.service.ImageService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequestMapping("/deep")
@RestController
public class AnalysisUploadController {

	private static final Logger log = LoggerFactory.getLogger(AnalysisUploadController.class);

	@Autowired
	private AnalysisService analysisService;
	
    @Autowired
    private DeepModelRepository deepModelRepository;

	@Autowired
	private ImageService imageService;

	@Autowired
	private OriginImageRepository originImageRepository;
	
	@PostMapping("/aysUpload")
	@ResponseBody
	public void aysUpload(@RequestBody Map<String, Object> data) {
		
	    // FastAPI에서 받아온 데이터를 그대로 사용
        String result = (String) data.get("result");
        double score = (Double) data.get("score");
        String image = (String) data.get("image");
		String classData = (String) data.get("class");
        
        // Base64 인코딩된 이미지 데이터를 디코딩하여 바이트 배열로 변환
        byte[] imageBytes = Base64.getDecoder().decode(image);
        
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Member member = new Member();
		member.setMbId(authentication.getName());
		
		// 문자열 포멧팅
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy년 M월 d일 H시 m분");
		String formatDate = now.format(format);
		
		Analysis analysis = new Analysis();
		

		
		
		analysis.setProductImg(imageBytes);
		analysis.setPredictionDate(formatDate);
		analysis.setPredictionAccuracy((Double) score);
		analysis.setPredictionJdm(result);
		analysis.setMember(member);

		System.out.println(analysis.toString());	
		
		analysisService.imgSave(analysis);
        
        
	}
	
	
	@PostMapping("/create_model")
	@ResponseBody
	public String create_model(@RequestBody Map<String, String> modelName) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		System.out.println(authentication.getName());
		
	    // 사용자의 ID를 가져옵니다.
	    String userId = authentication.getName();
	    
	    // 문자열 포멧팅
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy년 M월 d일 H시 m분");
		String formatDate = now.format(format);

	    // RestTemplate 객체를 생성합니다.
	    RestTemplate restTemplate = new RestTemplate();

	    // FastAPI 서버의 URL을 설정합니다.
	    String url = "http://218.157.38.54:8002/create_model/";
	    
	    // 보낼 데이터를 설정합니다.
	    Map<String, String> params = new HashMap<>();
	    params.put("userId", userId);
	    params.put("currentTime", formatDate);
	    params.put("modelName", modelName.get("name")); 

	    // FastAPI 서버에 POST 요청을 보내고 응답을 받습니다.
	    ResponseEntity<String> response = restTemplate.postForEntity(url, params, String.class);


	    // 응답을 반환합니다.
	    return response.getBody();

	}
	
	@PostMapping("/getModel")
	@ResponseBody
	public List<DeepModel> getModel() {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
	    // 사용자의 ID를 가져옵니다.
	    String userId = authentication.getName();
	    System.out.println(authentication.getName());
	    
	    // 사용자 ID모델 정보를 조회합니다.
        List<DeepModel> models = deepModelRepository.findByMember_MbId(userId);
		
		return models;
	}

		
	@PostMapping("/uploadImages")
	@ResponseBody
	public ResponseEntity<Void> uploadImages() {
		try {
			String folderPath = "C:/Users/smhrd/Desktop/실전문서/data/aysfile";  // 이미지 폴더 경로 지정
			imageService.saveAllImagesInFolder(folderPath);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (IOException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	@GetMapping("/getOriginImages")
	@ResponseBody
	public ResponseEntity<?> getOriginImages(@RequestParam int index) {
		try {
			// 이미지 인덱스를 기반으로 데이터베이스에서 이미지를 가져옵니다.
			OriginImage image = originImageRepository.findById(index).orElse(null);
	
			if (image == null) {
				// 이미지가 null인 경우 로그를 출력하고, BAD_REQUEST 응답을 반환합니다.
				log.error("Image not found with index: " + index);
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
	
			// 이미지 데이터를 Base64로 인코딩합니다.
			byte[] oriImage = image.getOriImage();
			if (oriImage == null) {
				// 이미지 데이터가 null인 경우 로그를 출력하고, BAD_REQUEST 응답을 반환합니다.
				log.error("Image data is null for index: " + index);
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
	
			String encodedImage = Base64.getEncoder().encodeToString(oriImage);
	
			// 인코딩된 이미지 데이터를 문자열로 변환합니다.
			String imageDataString = new String(encodedImage);
	
			// 이미지 데이터를 응답합니다.
			return new ResponseEntity<>(imageDataString, HttpStatus.OK);
		} catch (Exception e) {
			// 에러 처리
			log.error("Error occurred while processing image with index: " + index, e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}


}
