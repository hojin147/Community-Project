package com.bit;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.bit.domain.Gallerys;
import com.bit.domain.Member;
import com.bit.domain.Posts;
import com.bit.persistance.GallerysRepository;
import com.bit.persistance.MemberRepository;
import com.bit.persistance.PostsRepository;
import com.bit.service.GalleryService;

import lombok.ToString;
import lombok.extern.java.Log;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Log
@ToString
public class GalTest {

	@Autowired
	GalleryService gServ;
	
	@Autowired
	MemberRepository mRepo;
	
	
	@Autowired
	GallerysRepository gRepo;
	@Autowired
	PostsRepository pRepo;
	
	/*
	@Test
	public void sportsGal() {
		
		log.info("스포츠 카테고리 갤러리 이름 가져오기"); List<Gallerys> gal =
		gRepo.findByCategory("스포츠"); for(Gallerys i : gal) { System.out.println(i); }
		
		gRepo.findByCategory("스포츠").forEach(Gallerys->System.out.println(Gallerys));
		
	}*/
	
	@Test
	public void getPostContentByPno() {
		//log.info(pRepo.findById(66).toString()); //pno로 게시물 불러오기
		//제목으로 검색하기
		/*
		List<Posts> posts = pRepo.findByTitleContaining("10");
		log.info(""+posts.size());
		log.info(posts.toString()); *///제목으로 검색하기
		//멤버로 검색
		/*
		List<Posts> posts = pRepo.findByMemberContaining(mRepo.findByUseridContaining("7"));
		log.info(""+posts.size());
		log.info(posts.toString());*/
		/*
		List<Member> members = mRepo.findByUseridContaining("7");
		log.info(""+members.size());*/
		
	}
	
}
