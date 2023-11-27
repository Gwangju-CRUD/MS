package com.crud.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.crud.entity.Analysis;
import com.crud.entity.Member;
import com.crud.service.AnalysisService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class AnalysisController {

	@Autowired
	private AnalysisService analysisService;

	@GetMapping("imgPrint")
	public String imgUploadForm(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {

		Page<Analysis> analysisList = this.analysisService.getAllAnalysis(page);

		model.addAttribute("analysisList", analysisList);

		return "analysis/imgUpload";
	}

	@GetMapping("imgUpload")
	public String goImgUpload(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {

		Page<Analysis> analysisList = this.analysisService.getAllAnalysis(page);

		model.addAttribute("analysisList", analysisList);

		return "analysis/imgUpload";
	}

	@PostMapping("imgUpload")
	public String img_upload(@RequestParam("file") MultipartFile file, Model model) throws IOException {

		if (!file.isEmpty()) {

			// Analysis 엔터티의 FK인 mbId를 set해주는 로직
			// Authentication = 현재 유저의 세션정보를 담고 있는 객체
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			Member member = new Member();
			member.setMbId(authentication.getName());
			
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
			analysis.setMember(member); // Authentication로 가져온 member 객체를 넘김

			analysisService.imgSave(analysis);

			return "redirect:/imgPrint"; // 이미지 업로드 페이지로 리다이렉트
			
		} else {
			return "imgPrint";
		}

	}
}