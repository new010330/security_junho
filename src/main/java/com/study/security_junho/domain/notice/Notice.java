package com.study.security_junho.domain.notice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.study.security_junho.web.dto.notice.GetNoticeListRespDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Notice {
	private int notice_code;
	private String notice_title;
	private int user_code;
	private String user_id;
	private String notice_content;
	private int notice_count;
	
	private int file_code;
	private String file_name;
	private LocalDateTime create_date;
	
	private int total_notice_count;
	
	public GetNoticeListRespDto toListDto() {
		return GetNoticeListRespDto.builder()
				.noticeCode(notice_code)
				.noticeTitle(notice_title)
				.userId(user_id)
				.createDate(create_date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
				.noticeCount(notice_count)
				.totalNoticeCount(total_notice_count)
				.build();
	}
}
