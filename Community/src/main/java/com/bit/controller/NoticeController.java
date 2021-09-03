package com.bit.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bit.domain.Notice;
import com.bit.domain.Pagination;
import com.bit.service.GalleryService;
import com.bit.service.NoticeService;
import com.bit.vo.NoticeVO;

import lombok.extern.java.Log;

//공지사항 관련
@Controller
@Log
public class NoticeController {
	
	@Autowired
	NoticeService nServ;
	
	@Autowired
	GalleryService gServ;
	
	//공지사항 리스트 페이지 호출
	@GetMapping("/manage/noticeList")
	public void notice(Model model,
					   @RequestParam(defaultValue = "1") int page) {
		
		log.info("공지사항 리스트페이지 호출");
		
		//공용메소드 호출
		CommonMethod.getCategoryAndGalname(model, gServ);
		
		 //해당 갤러리의 총 게시물 수 
	    int totalListCnt = nServ.findAllNoticeCnt();

	    //생성인자로 총 게시물 수, 현재 페이지를 전달
	    Pagination pagination = new Pagination(totalListCnt, page);

	    //DB select start index
	    int startIndex = pagination.getStartIndex();
	    
	    //페이지 당 보여지는 게시글의 최대 개수
	    int pageSize = pagination.getPageSize();
	    
	    //해당 갤러리의 posts에 리스트들 불러와서 저장
	    List<Notice> notice = nServ.findNoticeListPaging(startIndex, pageSize);

	    //모델에 붙여서 html로 보내주기
	    model.addAttribute("noticeList", notice);
	    model.addAttribute("pagination", pagination);
	}
	
	//공지사항 리스트 페이지 호출
	@GetMapping("/noticeSearch")
	public String galSearchlist(Model model,
							@RequestParam("galname") String galname,
							@RequestParam(defaultValue = "1") int page,
							@RequestParam(value = "type", defaultValue = "c") String type,
							@RequestParam("keyword") String keyword)
							throws UnsupportedEncodingException {
		
		log.info("갤러리 내부 검색결과 호출");
		
		//공용메소드 호출
		CommonMethod.getCategoryAndGalname(model, gServ);
		
		//해당 검색결과의 총 게시물 수 
		int totalListCnt = nServ.findNoticeSearchListPaging(type, keyword);
		
		//생성인자로 총 게시물 수, 현재 페이지를 전달
		Pagination pagination = new Pagination(totalListCnt, page);
		
		//DB select start index
		int startIndex = pagination.getStartIndex();
		
		//페이지 당 보여지는 게시글의 최대 개수
		int pageSize = pagination.getPageSize();
		
		//해당 갤러리의 posts에 리스트들 불러와서 저장
	    List<Notice> notice
	    = nServ.searchByTypeAndKeywordWithNoticeCnt(startIndex, pageSize, type, keyword);
		
	    //모델에 붙여서 html로 보내주기
	    model.addAttribute("noticeList", notice);
	    model.addAttribute("pagination", pagination);	
	    model.addAttribute("type", type);
	    
		return "manage/noticeList";
	}
	
    //공지사항 상세조회
  	//리스트에서 선택한 글에 대한 해당 nno와 userid를 가져온다
  	@GetMapping("/manage/noticeDetail/{nno}/{userid}")
  	public String view(Model model, @PathVariable("nno") int nno,
  									@PathVariable("userid") String userid,
  									HttpServletRequest request,
  									HttpServletResponse response) {
  		
  		log.info("게시글 하나 상세조회");
  		
  		/*
  		조회수 증가 혹은 증가안함
  		쿠키의 정보들을 불러가서 비교함
  		본인 게시물이거나, 한번 조회한 게시물은
  		쿠키가 없어질때까지 한번 조회수를 증가시키고
  		더 이상 증가시키지 않음.
  		*/
  		nServ.noticeHit(nno, userid, request, response);
  		
  		//공용메소드 호출
  		CommonMethod.getCategoryAndGalname(model, gServ);
  		
  		//서비스에서 리턴받은 글을 저장한다.
  		NoticeVO noticeVO = nServ.getNotice(nno);
  		
  		//저장한 글을 모델에 붙여서 보낸다
  		model.addAttribute("notice", noticeVO);
  		
  		return "manage/noticeDetail";
  	}
  	
  	//공지사항 쓰는 페이지 호출
  	@GetMapping("/manage/noticeWrite")
  	public String noticeWritePage(Model model) {
  		
		log.info("공지사항쓰기 페이지 호출");
		
		//공용메소드 호출
		CommonMethod.getCategoryAndGalname(model, gServ);
		
		return "/manage/noticeWrite";
  	}
  	
    //공지사항을 쓰고 공지사항 리스트로 돌아감
    //포스트 방식의 요청매핑을 줄여쓴 것
    @PostMapping("/insertNotice")
    public String noticeWriting(Model model,
    					  HttpServletRequest req,
    					  RedirectAttributes rttr) {
    	
    	//폼에서 값 받기
    	String userid = req.getParameter("userid");
    	String title = req.getParameter("title");
    	String content = req.getParameter("content");
    	
    	//호출하기
    	nServ.saveNotice(userid, title, content);
    	
    	return "redirect:/manage/noticeList";
    }
    
	//공지시항 수정페이지 호출
  	@GetMapping("/manage/noticeUpdate/{nno}")
  	public String updateNotice(Model model, @PathVariable("nno") int nno) {
  		
  		//공용메소드 호출
  		CommonMethod.getCategoryAndGalname(model, gServ);
  		
  		log.info("공지사항 수정 페이지 호출");
  		
  		//서비스에서 리턴받은 글을 저장한다.
  		NoticeVO noticeVO = nServ.getNotice(nno);
  		
  		//저장한 글을 모델에 붙여서 보낸다
  		model.addAttribute("notice", noticeVO);
  		
  		return "manage/noticeUpdate";
  	}
  	
  	//공지사항 수정하기
  	@PostMapping("/noticeUpdate")
  	public String modifyNotice(RedirectAttributes rttr, HttpServletRequest req,
  			@SessionAttribute("userid") String userid) {
  		
  		log.info("공지사항 수정 로직");
  		
  		NoticeVO vo = new NoticeVO();
  		vo.setNno(Integer.parseInt(req.getParameter("nno")));
  		vo.setTitle(req.getParameter("title"));
  		vo.setContent(req.getParameter("content"));
  		
  		nServ.updateNotice(vo);
  		
  		rttr.addFlashAttribute("msg", "updated");
		
  		return "redirect:/manage/noticeDetail/" + vo.getNno() + "/" + userid;
  	}
    
  	//공지 삭제하기
  	@GetMapping(value = "/noticeDelete/{nno}")
  	public String deleteNotice(@PathVariable("nno") int nno) {
  		
  		log.info("삭제 번호 : " + nno);
  		
  		nServ.deleteNotice(nno);
    	
    	return "redirect:/manage/noticeList";
  	}
}