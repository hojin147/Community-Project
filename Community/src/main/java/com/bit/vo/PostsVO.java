package com.bit.vo;

import java.sql.Timestamp;
import java.util.List;

import com.bit.domain.Comment;
import com.bit.domain.Gallerys;
import com.bit.domain.Member;

import lombok.AllArgsConstructor;
import lombok.Data;

import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostsVO {
	
	private int pno;
	private String title;
	private Gallerys gallery;
	private String content;
	private Member member;
	private Timestamp postdate;
	private Timestamp updatedate;
	private int hit;
	private int up;
	private List<Comment> comments;
}