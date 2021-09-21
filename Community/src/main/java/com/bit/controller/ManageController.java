package com.bit.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bit.domain.Gallerys;
import com.bit.domain.Member;
import com.bit.domain.Pagination;
import com.bit.persistance.GallerysRepository;
import com.bit.persistance.MemberRepository;
import com.bit.service.GalleryService;
import com.bit.service.ManageService;
import com.bit.service.MemberService;
import com.bit.vo.GallerysVO;
import com.bit.vo.MemberVO;

import lombok.extern.java.Log;

//관리자 페이지 관련 호출
@Controller
@Log
//요즘 많이 쓰는 final 주입으로 코드수정
@RequiredArgsConstructor
public class ManageController {
	
	private final GalleryService gServ;
	private final GallerysRepository gRepo;
	private final MemberService mServ;
	private final MemberRepository mRepo;
	private final ManageService mgServ;
	
	//갤러리 관리 리스트 페이지 호출
	@GetMapping("/manage/galleryList")
	public String galleryList(Model model,
							  @RequestParam(defaultValue = "1") int page) {
		
		log.info("갤러리 관리 리스트페이지 호출");
		
		//공용메소드 호출
		CommonMethod.getCategoryAndGalname(model, gServ);
		
		//총 갤러리 수 
	    int totalListCnt = mgServ.findAllGalCnt();

	    //생성인자로 총 게시물 수, 현재 페이지를 전달
	    Pagination pagination = new Pagination(totalListCnt, page);

	    //DB select start index
	    int startIndex = pagination.getStartIndex();
	    
	    //페이지 당 보여지는 갤러리 최대 개수
	    int pageSize = pagination.getPageSize();
	    
	    //갤러리 다 불러와서 저장
	    List<Gallerys> galnames
	    = mgServ.findGalListPaging(startIndex, pageSize);

	    //모델에 붙여서 html로 보내주기
	    model.addAttribute("galnames", galnames);
	    model.addAttribute("pagination", pagination);

		return "manage/galleryList";
	}
	
	//갤러리 관리 리스트페이지 검색결과 호출
	@GetMapping("/searchGalList")
	public String searchGalList(Model model,
							  @RequestParam(defaultValue = "1") int page,
							  @RequestParam(value = "type", defaultValue = "c") String type,
							  @RequestParam("keyword") String keyword) {
		
		log.info("갤러리 리스트 검색결과");
		
		//공용메소드 호출
		CommonMethod.getCategoryAndGalname(model, gServ);
		
		//총 갤러리 수 
	    int totalListCnt = mgServ.findAllGalSearchCnt(type, keyword);

	    //생성인자로 총 게시물 수, 현재 페이지를 전달
	    Pagination pagination = new Pagination(totalListCnt, page);

	    //DB select start index
	    int startIndex = pagination.getStartIndex();
	    
	    //페이지 당 보여지는 갤러리 최대 개수
	    int pageSize = pagination.getPageSize();
	    
	    //갤러리 다 불러와서 저장
	    List<Gallerys> galnames
	    = mgServ.findGalSearchListPaging(startIndex, pageSize, type, keyword);

	    //모델에 붙여서 html로 보내주기
	    model.addAttribute("galnames", galnames);
	    model.addAttribute("type", type);
	    model.addAttribute("pagination", pagination);

		return "manage/galleryList";
	}

	//갤러리 생성페이지 호출
	@GetMapping("/manage/insertGallery")
	public String insertGallery(Model model, 
			@RequestParam(defaultValue = "admin") String galmaster) {
		
		log.info("갤러리 생성 페이지 호출");
		
		//공용메소드 호출
		CommonMethod.getCategoryAndGalname(model, gServ);
		
		return "manage/insertGallery";
	}
	
	/* ------------------------- 갤러리 입력/수정/삭제 ------------------------ */
	//갤러리 생성 로직
	@PostMapping("/galleryInsert")
	public String galleryInsert(HttpServletRequest req,
			RedirectAttributes rttr) {
		log.info("갤러리 생성 시작");
		GallerysVO vo = new GallerysVO();
		vo.setGalname(req.getParameter("galname"));
		vo.setCategory(req.getParameter("category"));
		try {
			vo.setMaster(mRepo.findById(req.getParameter("galmasterip")).get());
		} catch (Exception e) {
			log.info("galmater 없음");
			rttr.addFlashAttribute("msg", "nogm");
			return "redirect:/manage/insertGallery";
		}
		if(!gServ.insertGallery(vo)) {
			log.info("갤러리명 중복");
			rttr.addFlashAttribute("msg", "overlap");
			return "redirect:/manage/insertGallery";
		}

		rttr.addFlashAttribute("msg", "istsucc");
		return "redirect:/manage/galleryList";
	}
	
	//갤러리 수정 페이지 호출
	@GetMapping("/manage/updateGallery")
	public String updateGallery(Model model, 
			   @RequestParam("galname") String galname) {
		
		log.info("갤러리 수정 페이지 호출");
		model.addAttribute("gal", gRepo.findById(galname).get());
		
		//공용메소드 호출
		CommonMethod.getCategoryAndGalname(model, gServ);
		
		return "manage/updateGallery";
	}
	
	//갤러리 수정 로직
	@PostMapping("/galleryUpdate/{galnameOrg}")
	public String galleryUpdate(HttpServletRequest req, 
			@PathVariable("galnameOrg") String galnameOrg,
			RedirectAttributes rttr) throws UnsupportedEncodingException {
		
		log.info("갤러리 수정 시작");
		
		GallerysVO vo = new GallerysVO();
		vo.setGalname(req.getParameter("galname"));
		vo.setCategory(req.getParameter("category"));
		try {
			vo.setMaster(mRepo.findById(req.getParameter("galmasterip")).get());
		} catch (Exception e) {
			log.info("galmater 없음");
			rttr.addFlashAttribute("msg", "nogm");
			String encodedParam = URLEncoder.encode(vo.getGalname(), "UTF-8");
			return "redirect:/manage/updateGallery?galname=" + encodedParam;
		}
		
		int udtResult = 0;
		try {
			udtResult = gServ.updateGallery(vo, galnameOrg);
		} catch (Exception e) {
			log.info("갤러리명 중복");
			rttr.addFlashAttribute("msg", "overlap");
			String encodedParam = URLEncoder.encode(vo.getGalname(), "UTF-8");
			return "redirect:/manage/updateGallery?galname=" + encodedParam;
		}
		
		if(udtResult == 0) {
			log.info("갤러리 수정 실패");
			rttr.addFlashAttribute("msg", "udtfail");
		} else {
			log.info("갤러리 수정 성공");
			rttr.addFlashAttribute("msg", "udtsucc");
		}
		
		return "redirect:/manage/galleryList";
	}
	
	//갤러리 삭제 페이지 호출
	@GetMapping("/manage/deleteGallery") //현재 html 파일 없음
	public String deleteGallery(Model model) {
		
		log.info("갤러리 삭제 페이지 호출");
		
		//공용메소드 호출
		CommonMethod.getCategoryAndGalname(model, gServ);
		
		return "manage/deleteGallery";
	}
	
	//갤러리 삭제 로직
	@GetMapping("/galleryDelete/{galname}")
	public String galleryDelete(@PathVariable("galname") String galname, 
								RedirectAttributes rttr) {
		
		int delResult = gServ.deleteGallery(galname);
		if(delResult == 0) {
			log.info("갤러리 삭제 실패");
			rttr.addFlashAttribute("msg", "delfail");
		} else {
			log.info("갤러리 삭제 성공");
			rttr.addFlashAttribute("msg", "delsucc");
		}
		return "redirect:/"; //현재 리타이렉트 페이지 없음
	}
	/* ------------------------- 갤러리 입력/수정/삭제 ------------------------ */
	
	//회원 리스트 페이지 호출
	@GetMapping("/manage/memberList")
	public void getMemberList(Model model, @RequestParam(defaultValue = "1") int page) {
		
		log.info("유저 리스트 호출");
		
		//공용메소드 호출
		CommonMethod.getCategoryAndGalname(model, gServ);
		
		//총 유저 수 
	    int totalListCnt = mgServ.findAllCnt();

	    //생성인자로 총 게시물 수, 현재 페이지를 전달
	    Pagination pagination = new Pagination(totalListCnt, page);

	    //DB select start index
	    int startIndex = pagination.getStartIndex();
	    
	    //페이지 당 보여지는 게시글의 최대 개수
	    int pageSize = pagination.getPageSize();
	    
	    //유저 리스트들 불러와서 저장
	    List<Member> member = mgServ.findListPaging(startIndex, pageSize);

	    //모델에 붙여서 html로 보내주기
	    model.addAttribute("memberList", member);
	    model.addAttribute("pagination", pagination);
	}
	
	//회원 삭제 실행
	@PostMapping("/memberDelete/{userid}")
	public String memberDelete(@PathVariable("userid") String userid) {
		log.info("회원삭제 진행 -> 타입 바꿔주기");
		
		/*
		회원 삭제 시 실제 DB에서 없어지는게 아니라
		타입을 바꿔줘서 비활성 시킴
		*/
		mgServ.memberQuit(userid);
		
		return "redirect:/manage/memberList";
	}
	
	//비밀번호 변경페이지 호출
	@GetMapping("/pwUpdate/{userid}")
	public String pwUpdate(@PathVariable("userid") String userid, Model model) {
		
		log.info("회원 비밀번호 변경 페이지 호출  ID : " + userid);
		
		MemberVO vo = mServ.login(userid);
		
		model.addAttribute("member", vo);
		
		return "/manage/pwUpdate";
	}
	
	//비밀번호 변경
	@PostMapping("/pwUpdate")
	public String dopwUpdate(HttpServletRequest req, RedirectAttributes rttr) {
		
		log.info("회원비밀번호 수정 시작");
		
		String userid = req.getParameter("userid");
		String pw = req.getParameter("newpw");
		
		mServ.updateUserPw(userid, pw);
		
		return "redirect:/manage/memberList";
	}
}