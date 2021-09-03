package com.bit.domain;

import java.util.List;

import javax.persistence.CascadeType;
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

//카테고리/갤러리네임 엔티티
@Setter @Getter
@Entity @Table
@ToString(exclude = {"posts", "galmanagers"})
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "galname")
public class Gallerys {
	
	@Id
	private String galname;
	private String category;
	
	@OneToOne
	private Member master;

	@OneToMany(cascade = CascadeType.ALL)
	private List<Posts> posts;
	
	@OneToMany
	private List<GalManager> galmanagers;
	
	public Gallerys(String galname, String category) {
		this.galname = galname;
		this.category = category;
	}
}