package com.bit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.bit.repository.GallerysRepository;
import com.bit.repository.MemberRepository;
import com.bit.repository.PostsRepository;
import com.bit.service.GalleryService;
import com.bit.service.MemberService;

import lombok.extern.java.Log;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Log @Commit
public class DBTests2 {
	
	@Autowired
	MemberRepository mRepo;
	@Autowired
	MemberService mServ;
	@Autowired
	GallerysRepository gRepo;
	@Autowired
	PostsRepository pRepo;
	@Autowired
	GalleryService gServ;
	
	/*
	@Test
	public void galleryDummy() {


		Gallerys gallerys1 = new Gallerys();
		gallerys1.setCategory("스포츠");
		gallerys1.setGalname("해외야구");
		gallerys1.setMaster(mRepo.findByUserid("galler1"));
		gRepo.save(gallerys1);

		Gallerys gallerys2 = new Gallerys();
		gallerys2.setCategory("게임");
		gallerys2.setGalname("메이플스토리");
		gallerys2.setMaster(mRepo.findByUserid("galler2"));
		gRepo.save(gallerys2);

		Gallerys gallerys3 = new Gallerys();
		gallerys3.setCategory("게임");
		gallerys3.setGalname("LOL");
		gallerys3.setMaster(mRepo.findByUserid("galler3"));
		gRepo.save(gallerys3);

		Gallerys gallerys4 = new Gallerys();
		gallerys4.setCategory("연예&방송");
		gallerys4.setGalname("남자 연예인");
		gallerys4.setMaster(mRepo.findByUserid("galler4"));
		gRepo.save(gallerys4);

		Gallerys gallerys5 = new Gallerys();
		gallerys5.setCategory("연예&방송");
		gallerys5.setGalname("여자 연예인");
		gallerys5.setMaster(mRepo.findByUserid("galler5"));
		gRepo.save(gallerys5);

		Gallerys gallerys6 = new Gallerys();
		gallerys6.setCategory("교육&금융&IT");
		gallerys6.setGalname("자격증");
		gallerys6.setMaster(mRepo.findByUserid("galler6"));
		gRepo.save(gallerys6);

		Gallerys gallerys7 = new Gallerys();
		gallerys7.setCategory("교육&금융&IT");
		gallerys7.setGalname("외국어");
		gallerys7.setMaster(mRepo.findByUserid("galler7"));
		gRepo.save(gallerys7);

		Gallerys gallerys8 = new Gallerys();
		gallerys8.setCategory("여행&음식&생물");
		gallerys8.setGalname("국내여행");
		gallerys8.setMaster(mRepo.findByUserid("galler8"));
		gRepo.save(gallerys8);

		Gallerys gallerys9 = new Gallerys();
		gallerys9.setCategory("여행&음식&생물");
		gallerys9.setGalname("해외여행");
		gallerys9.setMaster(mRepo.findByUserid("galler9"));
		gRepo.save(gallerys9);

		Gallerys gallerys10 = new Gallerys();
		gallerys10.setCategory("취미생활");
		gallerys10.setGalname("스트리머");
		gallerys10.setMaster(mRepo.findByUserid("galler10"));
		gRepo.save(gallerys10);

		Gallerys gallerys11 = new Gallerys();
		gallerys11.setCategory("취미생활");
		gallerys11.setGalname("웹툰");
		gallerys11.setMaster(mRepo.findByUserid("galler11"));
		gRepo.save(gallerys11);
		
	}
	
	@Test
	public void insertPostsDummy() {
		
		
		for (int i = 110; i < 310; i++) {
			String title = "post"+i;
			Gallerys gallery = new Gallerys("LOL", "게임");
			String content = "LOL content content"+i;
			Member mem = mRepo.findByUserid("galler" + (i%10));
			Posts post = new Posts(title, gallery, content, mem);
			pRepo.save(post);
		}
	}
	*/
	
	@Test
	public void galleryTests() {
		//log.info("result:" + gRepo.updateGallery("마비노기", "게임", mRepo.findById("galler19").get(), "뿌요뿌요 테트리스"));;
		log.info("result:" + gRepo.deleteByGalname("마비노기 영웅전"));
	}
	
}