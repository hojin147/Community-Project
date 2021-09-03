package com.bit.domain;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

//게시글 엔티티
@Getter @Setter
@Entity @Table
@ToString(exclude = "comments")
@EqualsAndHashCode(of = "pno")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class Posts {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int pno;
	private String title;
	
	@ManyToOne
	private Gallerys gallery;
	private String content;
	
	@ManyToOne
	private Member member;
	
	@CreationTimestamp
	private Timestamp postdate;
	
	@UpdateTimestamp
	private Timestamp updatedate;
	
	private int hit;
	private int up;
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<Comment> comments;
	
	//쓴 글 저장을 위한 생성자
	public Posts(String title, Gallerys gallery, String content, Member member) {
		super();
		this.title = title;
		this.gallery = gallery;
		this.content = content;
		this.member = member;

		this.pno = 0;
		this.hit = 0;
		this.up = 0;
		this.comments = null;
	}
}