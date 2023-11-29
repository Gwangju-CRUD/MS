package com.crud.controller;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.crud.entity.Deep;
import com.crud.repository.DeepRepository;
import com.crud.service.DeepService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class DeepController {

	public final DeepRepository deepRepository;
	
	@Autowired
	private DeepService deepService;
	
	@GetMapping("imgUpload")
	public String imgUploadForm() {
		return "member/imgUpload";
	}
	
	@PostMapping("img/img_upload")
	public String img_upload(@RequestParam("file") MultipartFile file, RedirectAttributes rttr) {
		try {
            if (!file.isEmpty()) {
            	
            	 byte[] product_img = file.getBytes();
                 Deep image = new Deep();
                 image.setProduct_img(product_img); 
                 deepService.imgSave(image);
                 
                 rttr.addFlashAttribute("message", "업로드 성공");
                 
                 return "redirect:/imgUpload";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
		rttr.addFlashAttribute("message", "업로드 실패");
        return "redirect:/displayImage";
	}
	
	@GetMapping("/displayImage")
    public String displayImage(@RequestParam("deepId") Long deep_idx, Model model) {
        // 데이터베이스에서 이미지 데이터를 읽어옵니다.
        Deep deep = deepRepository.findById(deep_idx).orElse(null);

        if (deep != null) {
            // Base64로 이미지 데이터를 인코딩합니다.
            String base64Image = Base64.getEncoder().encodeToString(deep.getProduct_img());

            // 모델에 이미지 데이터를 추가하여 HTML에 전달합니다.
            model.addAttribute("base64Image", base64Image);
        }

        return "member/displayImage";
    }
	
}
