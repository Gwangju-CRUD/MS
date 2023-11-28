package com.crud.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.crud.entity.Analysis;
import com.crud.entity.AnalysisResult;
import com.crud.entity.Image;
import com.crud.entity.Member;
import com.crud.service.AnalysisService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/deep")
@RequiredArgsConstructor
@Controller
public class AnalysisController {

	@Autowired
	private AnalysisService analysisService;

	@GetMapping("imgPrint")
	public String imgUploadForm(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {

		// 이미지와 관련된 엔터티 데이터를 DB에서 가져옵니다.
		Page<Analysis> analysisList = this.analysisService.getAllAnalysis(page);

		// 모델에 데이터를 추가하여 Thymeleaf에 전달합니다.
		model.addAttribute("analysisList", analysisList);

		return "analysis/imgUpload"; // Thymeleaf 템플릿 파일 이름
	}

	@GetMapping("imgUpload")
	public String goImgUpload(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
		// 이미지와 관련된 엔터티 데이터를 DB에서 가져옵니다.
		Page<Analysis> analysisList = this.analysisService.getAllAnalysis(page);

		// 모델에 데이터를 추가하여 Thymeleaf에 전달합니다.
		model.addAttribute("analysisList", analysisList);

		return "analysis/imgUpload"; // Thymeleaf 템플릿 파일 이름
	}

	@PostMapping("imgUpload")
	public String img_upload(@RequestParam("file") MultipartFile file, Model model) throws IOException {

		if (!file.isEmpty()) {

			// Analysis 엔터티의 FK인 mbId를 set해주는 로직
			// Authentication = 현재 유저의 세션정보를 담고 있는 객체
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			Member member = new Member();
			member.setMbId(authentication.getName());

			// 이미지 파일을 byte로 변환
			byte[] byteArr = file.getBytes();
			// byte를 Base64로 인코딩
			String encodedString = Base64.getEncoder().encodeToString(byteArr);

			// FastAPI 서버에 전송할 객체 생성
			Image imageData = new Image();
			imageData.setEncoded_image_data(encodedString);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			HttpEntity<Image> entity = new HttpEntity<>(imageData, headers);

			// FastAPI 서버에 요청
			RestTemplate restTemplate = new RestTemplate();
			String fastApiUrl = "http://218.157.38.54:8002/predict/";
			ResponseEntity<AnalysisResult> response = restTemplate.exchange(fastApiUrl, HttpMethod.POST, entity,
					AnalysisResult.class);

			AnalysisResult result = response.getBody();

			// FastAPI 서버로부터 받은 응답 출력
			System.out.println("Score: " + result.getScore());
			System.out.println("Result: " + result.getResult());

			// 문자열 포멧팅
			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy년 M월 d일 H시 m분");
			String formatDate = now.format(format);

			byte[] product_img = file.getBytes();
			Analysis analysis = new Analysis();

			// 추후 정확도, 판정결과 등이 결정되면 set만 해주면 됨
			analysis.setProductImg(product_img);
			analysis.setPredictionDate(formatDate);
			analysis.setPredictionAccuracy((Double) result.getScore());
			analysis.setPredictionJdm(result.getResult());
			analysis.setMember(member);

			analysisService.imgSave(analysis);

			return "redirect:/deep/imgPrint"; // 이미지 업로드 페이지로 리다이렉트
		} else {
			return "/deep/imgPrint";
		}

	}

	@GetMapping("/main")
	public String goMain() {
		return "analysis/deepMain";
	}
	
	@GetMapping("/videoAnalysis")
	private String goAnalysis() {

		return "analysis/videoAnalysis";
	}

	@GetMapping("/create")
	private String goCreate() {

		return "analysis/createModel";
	}

	@GetMapping("/imgAnalysis")
	private String goImgAnalysis() {

		return "analysis/imgAnalysis";
	}
}