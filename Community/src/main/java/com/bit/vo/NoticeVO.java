package com.bit.vo;

import java.sql.Timestamp;
import com.bit.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticeVO {

	private int nno;
	private String title;
	private String content;
	private Member member;
	private int hit;
	private Timestamp postdate;
	private Timestamp updatedate;
}