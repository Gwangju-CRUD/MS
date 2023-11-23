package com.crud.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.crud.entity.Analysis;
import com.crud.service.AnalysisService;

@Controller
public class AnalysisController {

	@Autowired
	private AnalysisService analysisService;

	@GetMapping("imgPrint")
	public String imgUploadForm(Model model) {
		// 이미지와 관련된 엔터티 데이터를 DB에서 가져옵니다.
		List<Analysis> analysisList = analysisService.getAllAnalysis();

		// 모델에 데이터를 추가하여 Thymeleaf에 전달합니다.
		model.addAttribute("analysisList", analysisList);

		return "analysis/displayImage"; // Thymeleaf 템플릿 파일 이름
	}

	@GetMapping("imgUpload")
	public String goImgUpload() {
		return "analysis/imgUpload";
	}
	
	@PostMapping("imgUpload")
	public String img_upload(@RequestParam("file") MultipartFile file, Model model) throws IOException {

		if (!file.isEmpty()) {
			byte[] product_img = file.getBytes();
			Analysis analysis = new Analysis();
			
			analysis.setProductImg(product_img);
			analysis.setPredictionDate(LocalDateTime.now());
			analysis.setPredictionAccuracy(98L);
			analysis.setPredictionJdm("정상");
			
			analysisService.imgSave(analysis);
			
			return "redirect:/imgPrint"; // 이미지 업로드 페이지로 리다이렉트
		}
		
		return null;

	}
}