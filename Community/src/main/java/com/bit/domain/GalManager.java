package com.bit.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//각 갤러리 갤러리매니저 엔티티
@Getter @Setter
@Entity @Table
@ToString
public class GalManager {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int gno;
	
	@ManyToOne
	private Gallerys gallery;
	
	@OneToMany
	private List<Member> member;
}