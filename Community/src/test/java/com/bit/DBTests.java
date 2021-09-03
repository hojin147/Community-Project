package com.bit;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.bit.domain.Gallerys;
import com.bit.domain.Member;
import com.bit.domain.Posts;
import com.bit.persistance.GallerysRepository;
import com.bit.persistance.MemberRepository;
import com.bit.persistance.PostsRepository;
import com.bit.service.MemberService;

import lombok.extern.java.Log;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Log @Commit
public class DBTests {
	
	@Autowired
	MemberRepository mRepo;
	@Autowired
	MemberService mServ;
	@Autowired
	GallerysRepository gRepo;
	@Autowired
	PostsRepository pRepo;
	
	
//	@Test
//	public void insertMemberTest() {
//		String userid = "testid998";
//		String pw = "1234";
//		String name = "testername";
//		String email = "tester@email.com";
//		String address = "testerAddress";
//		String phone = "010-0000-0000";
//		Member vo = new Member(userid, pw, name, email, phone, address, "USER");
//		
//		mServ.signUp(vo);
//		
//	}
	
	/*
	@Test
	public void memberExistCheck() {
		if(mServ.idCheck("asd")) {
			log.info("true : 중복없음");
		} else {
			log.info("false : 중복");
		}
	}*/
	
//	@Test
//	public void galMembersDummy() {
//		for (int i = 0; i < 10; i++) {
//			String userid = "Testmember"+i;
//			String pw = "1234";
//			String name = "Testmember"+i;
//			String email = "Testmember@email.com";
//			String address = "Testmemberadd";
//			String phone = "010-0000-0000";
//			Member vo = new Member(userid, pw, name, email, phone, address, "USER");
//			
//			mServ.signUp(vo);
//		}
//	}
//	
//	@Test
//	public void galleryDummy() {
//		for (int i = 0; i < 10; i++) {
//
//			Gallerys gallerys0 = new Gallerys();
//			gallerys0.setCategory("스포츠");
//			gallerys0.setGalname("스포츠더미"+i);
//			gallerys0.setMaster(mRepo.findByUserid("galler0"));
//			gRepo.save(gallerys0);
//		}


//		Gallerys gallerys1 = new Gallerys();
//		gallerys1.setCategory("스포츠");
//		gallerys1.setGalname("해외야구");
//		gallerys1.setMaster(mRepo.findByUserid("galler1"));
//		gRepo.save(gallerys1);
//
//		Gallerys gallerys2 = new Gallerys();
//		gallerys2.setCategory("게임");
//		gallerys2.setGalname("메이플스토리");
//		gallerys2.setMaster(mRepo.findByUserid("galler2"));
//		gRepo.save(gallerys2);
//
//		Gallerys gallerys3 = new Gallerys();
//		gallerys3.setCategory("게임");
//		gallerys3.setGalname("LOL");
//		gallerys3.setMaster(mRepo.findByUserid("galler3"));
//		gRepo.save(gallerys3);
//
//		Gallerys gallerys4 = new Gallerys();
//		gallerys4.setCategory("연예&방송");
//		gallerys4.setGalname("남자 연예인");
//		gallerys4.setMaster(mRepo.findByUserid("galler4"));
//		gRepo.save(gallerys4);
//
//		Gallerys gallerys5 = new Gallerys();
//		gallerys5.setCategory("연예&방송");
//		gallerys5.setGalname("여자 연예인");
//		gallerys5.setMaster(mRepo.findByUserid("galler5"));
//		gRepo.save(gallerys5);
//
//		Gallerys gallerys6 = new Gallerys();
//		gallerys6.setCategory("교육&금융&IT");
//		gallerys6.setGalname("자격증");
//		gallerys6.setMaster(mRepo.findByUserid("galler6"));
//		gRepo.save(gallerys6);
//
//		Gallerys gallerys7 = new Gallerys();
//		gallerys7.setCategory("교육&금융&IT");
//		gallerys7.setGalname("외국어");
//		gallerys7.setMaster(mRepo.findByUserid("galler7"));
//		gRepo.save(gallerys7);
//
//		Gallerys gallerys8 = new Gallerys();
//		gallerys8.setCategory("여행&음식&생물");
//		gallerys8.setGalname("국내여행");
//		gallerys8.setMaster(mRepo.findByUserid("galler8"));
//		gRepo.save(gallerys8);
//
//		Gallerys gallerys9 = new Gallerys();
//		gallerys9.setCategory("여행&음식&생물");
//		gallerys9.setGalname("해외여행");
//		gallerys9.setMaster(mRepo.findByUserid("galler9"));
//		gRepo.save(gallerys9);
//
//		Gallerys gallerys10 = new Gallerys();
//		gallerys10.setCategory("취미생활");
//		gallerys10.setGalname("스트리머");
//		gallerys10.setMaster(mRepo.findByUserid("galler10"));
//		gRepo.save(gallerys10);
//
//		Gallerys gallerys11 = new Gallerys();
//		gallerys11.setCategory("취미생활");
//		gallerys11.setGalname("웹툰");
//		gallerys11.setMaster(mRepo.findByUserid("galler11"));
//		gRepo.save(gallerys11);
//		
//	}
	
	@Test
	public void postDummy() {
		
		for (int i = 0; i < 20; i++) {
			String title = "post"+i;
			Gallerys gallery = new Gallerys("로스트아크", "게임");
			String content = "로스트아크 content content"+i;
			Member mem = mRepo.findByUserid("ccc");
			Posts post = new Posts(title, gallery, content, mem);
			pRepo.save(post);
		}
	}		
	
//	//갤러리 객체 불러오기 테스트
//	@Test
//	public void galTest() {
//		List<Gallerys> gal = gRepo.findByCategory("스포츠");
//		System.out.println(gal);
//	}
}