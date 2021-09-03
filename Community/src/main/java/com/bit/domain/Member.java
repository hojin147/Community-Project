package com.bit.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

//멤버 엔티티
@Getter @Setter
@ToString(exclude = {"posts", "comments", "notices", "mastergal", "galmanager"})
@Entity
@Table
@EqualsAndHashCode(of = "userid")
@NoArgsConstructor
@AllArgsConstructor
public class Member {
	
	@Id
	private String userid;
	private String pw;
	private String name;
	private String email;
	private boolean mailcheck;
	private String phone;
	private String address;
	private String type;

	@OneToMany
	private List<Posts> posts;
	
	@OneToMany
	private List<Comment> comments;
	
	@OneToMany
	private List<Notice> notices;
	
	@OneToOne
	private Gallerys mastergal;
	
	@OneToOne
	private GalManager galmanager;
	
	//로그인을 위한 생성자
	public Member(String userid, String pw) {
		this.userid = userid;
		this.pw = pw;
	}
	
	//회원가입을 위한 생성자
	public Member(String userid, String pw, String name, String email, String phone, String address, String type) {
		super();
		this.userid = userid;
		this.pw = pw;
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.type = type;

		this.mailcheck = false;
		this.posts = null;
		this.comments = null;
		this.notices = null;
		this.mastergal = null;
		this.galmanager = null;
	}
}