package com.crud.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.crud.entity.Analysis;
import com.crud.service.AnalysisService;

import lombok.RequiredArgsConstructor;

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

			// 문자열 포멧팅
			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy년 M월 d일 H시 m분");
			String formatDate = now.format(format);

			byte[] product_img = file.getBytes();
			Analysis analysis = new Analysis();

			// 추후 정확도, 판정결과 등이 결정되면 set만 해주면 됨
			analysis.setProductImg(product_img);
			analysis.setPredictionDate(formatDate);
			analysis.setPredictionAccuracy(98L);
			analysis.setPredictionJdm("정상");

			analysisService.imgSave(analysis);

			return "redirect:/imgPrint"; // 이미지 업로드 페이지로 리다이렉트
		} else {
			return "imgPrint";
		}

	}
}