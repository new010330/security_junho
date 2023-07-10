package com.study.security_junho.web.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;


import com.study.security_junho.service.notice.NoticeService;
import com.study.security_junho.web.dto.CMRespDto;
import com.study.security_junho.web.dto.notice.AddNoticeReqDto;
import com.study.security_junho.web.dto.notice.GetNoticeListRespDto;
import com.study.security_junho.web.dto.notice.GetNoticeRespDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/v1/notice")
@RequiredArgsConstructor
public class NoticeRestController {
	
	private final NoticeService noticeService;
	
	@Value("${file.path}")
	private String filePath;
	
	@PostMapping("")
	public ResponseEntity<?> addNotice(AddNoticeReqDto addNoticeReqDto) {
//		log.info(">>>{}:", addNoticeReqDto);
//		log.info(">>> fileName: {}", addNoticeReqDto.getFile().get(0).getOriginalFilename());
//		log.info("filePath: {}", filePath);
		
		int noticeCode = 0;
		
		try {
			noticeCode = noticeService.addNotice(addNoticeReqDto);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(new CMRespDto<>(-1, "Failed", noticeCode));
		}
		
		
		return ResponseEntity.ok().body(new CMRespDto<>(1, "complete creation", noticeCode));
	}
	
	@GetMapping("/{noticeCode}")
	public ResponseEntity<?> getNotice(@PathVariable int noticeCode) {
		
		GetNoticeRespDto getNoticeRespDto = null;
		
		try {
			getNoticeRespDto = noticeService.getNotice(null, noticeCode);
			if(getNoticeRespDto == null) {
				return ResponseEntity.badRequest().body(new CMRespDto<>(-1, "database failed", null));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(new CMRespDto<>(-1, "database error", null));
		}
		
		
		return ResponseEntity.ok().body(new CMRespDto<>(1, "success", getNoticeRespDto));
	}
	
	@GetMapping("/{flag}/{noticeCode}")
	public ResponseEntity<?> getNotice(@PathVariable String flag, @PathVariable int noticeCode) {
		GetNoticeRespDto getNoticeRespDto = null;
		if(flag.equals("pre") || flag.equals("next")) {
			try {
				getNoticeRespDto = noticeService.getNotice(flag, noticeCode);
				if(getNoticeRespDto == null) {
					return ResponseEntity.badRequest().body(new CMRespDto<>(-1, "datebase failed", null));
				}
			} catch (Exception e) {
				e.printStackTrace();
				return ResponseEntity.internalServerError().body(new CMRespDto<>(-1, "datebase error", null));
			}			
		}else {
			return ResponseEntity.ok().body(new CMRespDto<>(1, "request failed", null));
		}
		return ResponseEntity.ok().body(new CMRespDto<>(1, "success", getNoticeRespDto));
	}
	
	@GetMapping("/file/download/{fileName}")
	public ResponseEntity<?> downloadFile(@PathVariable String fileName) throws IOException {
		Path path = Paths.get(filePath + "notice/" + fileName);
		String contentType = Files.probeContentType(path);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentDisposition(ContentDisposition.builder("attachment")
														.filename(fileName, StandardCharsets.UTF_8)
														.build());
		headers.add(HttpHeaders.CONTENT_TYPE, contentType);
		Resource resource = new InputStreamResource(Files.newInputStream(path));
		
		return ResponseEntity.ok().headers(headers).body(resource);
	}
	
	@GetMapping("/list/{page}")
	public ResponseEntity<?> getNoticeList(@PathVariable int page, @RequestParam String searchFlag, @RequestParam String searchValue) {
		List<GetNoticeListRespDto> listDto = null;
		try {
			listDto = noticeService.getNoticeList(page, searchFlag, searchValue);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(new CMRespDto<>(-1, "database error", listDto));
		}
		
		return ResponseEntity.ok().body(new CMRespDto<>(1, "success", listDto));
	}
}
