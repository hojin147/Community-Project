package com.bit.vo;

import java.sql.Timestamp;

import com.bit.domain.Comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//서비스와 컨트롤러 사이에서 데이터를 주고 받는것
//해당 테이블에서 실제로 CRUD를 할 필드를 정의해둔 것
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentVO {
	
	private int cno;
	private String member;
	private int post;
	private String content;
	private Timestamp regdate;
	
	public CommentVO(Comment c) {
		this.cno = c.getCno();
		this.member = (c.getMember()==null) ? "" : c.getMember().getUserid();
		this.post = c.getPost().getPno();
		this.content = c.getContent();
		this.regdate = c.getRegdate();
	}
}