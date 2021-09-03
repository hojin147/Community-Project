package com.bit.domain;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

//공지사항 게시글 엔티티
@Getter @Setter
@ToString
@Entity
@Table
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class Notice {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int nno;
	private String title;
	private String content;
	
	@ManyToOne
	private Member member;
	private int hit;
	
	@CreationTimestamp
	private Timestamp postdate;
	
	@UpdateTimestamp
	private Timestamp updatedate;
	
	//쓴 공지사항 저장을 위한 생성자
	public Notice(String title, String content, Member member) {
			super();
			this.title = title;
			this.content = content;
			this.member = member;
			this.nno = 0;
			this.hit = 0;
	}
}