package com.bit.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bit.domain.Notice;
import com.bit.domain.Pagination;
import com.bit.domain.Posts;
import com.bit.service.GalleryService;
import com.bit.service.NoticeService;
import com.bit.service.PostService;

import lombok.extern.java.Log;

@Controller
@Log
//요즘 많이 쓰는 final 주입으로 코드수정
@RequiredArgsConstructor
public class MainController {
	
	private final GalleryService gServ;
	private final NoticeService nServ;
	private final PostService pServ;

	//생성자 주입법 예시
//	@Autowired
//	public MainController(GalleryService gServ, NoticeService nServ, PostService pServ) {
//		this.gServ = gServ;
//		this.nServ = nServ;
//		this.pServ = pServ;
//	}
	
	//메인 페이지 호출
	@GetMapping("/main/index")
	public String main(Model model, @RequestParam(defaultValue = "1") int page) {
		
		log.info("메인 페이지 호출");
		
		//공용메소드 호출
		CommonMethod.getCategoryAndGalname(model, gServ);
		
		// 공지사항 리스트 호출
		//해당 공지사항의 총 게시물 수 
	    int totalListCnt = nServ.findAllNoticeCnt();

	    //생성인자로 총 게시물 수, 현재 페이지를 전달
	    Pagination pagination = new Pagination(totalListCnt, page);

	    //DB select start index
	    int startIndex = pagination.getStartIndex();
	    
	    //페이지 당 보여지는 게시글의 최대 개수
	    int pageSize = pagination.getPageSize();
	    
	    //공지사항 불러오기
	    List<Notice> notice = nServ.findNoticeListPaging(startIndex, pageSize);
	    
	    //베스트 게시글 불러오기
	    List<Posts> topHitPosts = pServ.getTopHitPosts();
	    		
	    //모델에 붙여서 html로 보내주기
	    model.addAttribute("noticeList", notice);
	    model.addAttribute("bestList", topHitPosts);
		return "main/index";
	}
}