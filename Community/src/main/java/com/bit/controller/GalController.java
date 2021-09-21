package com.bit.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.bit.domain.Pagination;
import com.bit.domain.Posts;
import com.bit.service.GalleryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

//유저가 쓰는 페이지 호출
@Controller
@Log
//요즘 많이 쓰는 final 주입으로 코드수정
@RequiredArgsConstructor
public class GalController {
	
	private final GalleryService gServ;
	
	//로그인한 유저의 게시물 리스트 호출
	@GetMapping("/member/myPostList")
	public void myPage(Model model,
					   @RequestParam(defaultValue = "1") int page, 
					   @RequestParam(defaultValue = "none") String galname, 
					   @SessionAttribute("userid") String userid) {
		
		log.info("내 게시물 리스트 호출");
		
		//공용메소드 호출
		CommonMethod.getCategoryAndGalname(model, gServ);
		
		//로그인한 유저의 총 게시물 수 
	    int totalListCnt = 0;
	    if(!galname.equals("none")) {
	    	totalListCnt = gServ.findGalMyListPagingCnt(userid, galname);
	    } else {
	    	totalListCnt = gServ.findAllMyCnt(userid);
	    }

	    //생성인자로 총 게시물 수, 현재 페이지를 전달
	    Pagination pagination = new Pagination(totalListCnt, page);

	    //DB select start index
	    int startIndex = pagination.getStartIndex();
	    
	    //페이지 당 보여지는 게시글의 최대 개수
	    int pageSize = pagination.getPageSize();
	    
	    //로그인한 유저의 posts에 리스트들 불러와서 저장
	    List<Posts> posts = gServ.findMyListPaging(startIndex, pageSize, userid);
	    if(!galname.equals("none")) {
	    	posts = gServ.findGalMyListPaging(startIndex, pageSize, userid, galname);
	    }
	    
	    String[] allGalname = gServ.getGalname();
	    //모델에 붙여서 html로 보내주기	    
	    model.addAttribute("allGalname", allGalname);
	    model.addAttribute("myPostList", posts);
	    model.addAttribute("pagination", pagination);
	}
	
	//해당하는 리스트 페이지 호출 및 해당하는 갤러리 게시글 리스트 불러오기
	@GetMapping("gallery/list")
	public void list(Model model,
					 @RequestParam("galname") String galname,
					 @RequestParam(defaultValue = "1") int page) {
		
		log.info("갤러리 리스트페이지 호출");
		
		//공용메소드 호출
		CommonMethod.getCategoryAndGalname(model, gServ);
		
	    //해당 갤러리의 총 게시물 수 
	    int totalListCnt = gServ.findAllCnt(galname);

	    //생성인자로 총 게시물 수, 현재 페이지를 전달
	    Pagination pagination = new Pagination(totalListCnt, page);

	    //DB select start index
	    int startIndex = pagination.getStartIndex();
	    
	    //페이지 당 보여지는 게시글의 최대 개수
	    int pageSize = pagination.getPageSize();
	    
	    //해당 갤러리의 posts에 리스트들 불러와서 저장
	    List<Posts> posts = gServ.findListPaging(startIndex, pageSize, galname);

	    //모델에 붙여서 html로 보내주기
	    model.addAttribute("postList", posts);
	    model.addAttribute("pagination", pagination);
	    model.addAttribute("galname", galname);
	    
	}
	
	//요청게시판 리스트 호출
	@GetMapping("gallery/requestList")
	public void requestList(Model model,
					 @RequestParam("galname") String galname,
					 @RequestParam(defaultValue = "1") int page) {
		
		log.info("갤러리 리스트페이지 호출");
		
		//공용메소드 호출
		CommonMethod.getCategoryAndGalname(model, gServ);
		
	    //해당 갤러리의 총 게시물 수 
	    int totalListCnt = gServ.findAllCnt(galname);

	    //생성인자로 총 게시물 수, 현재 페이지를 전달
	    Pagination pagination = new Pagination(totalListCnt, page);

	    //DB select start index
	    int startIndex = pagination.getStartIndex();
	    
	    //페이지 당 보여지는 게시글의 최대 개수
	    int pageSize = pagination.getPageSize();
	    
	    //해당 갤러리의 posts에 리스트들 불러와서 저장
	    List<Posts> posts = gServ.findListPaging(startIndex, pageSize, galname);

	    //모델에 붙여서 html로 보내주기
	    model.addAttribute("postList", posts);
	    model.addAttribute("pagination", pagination);
	    model.addAttribute("galname", galname);
	}
	
	//통합검색결과 리스트 호출
	@GetMapping("gallery/searchList")
	public String searchList(Model model, 
							 @RequestParam(value = "type", defaultValue = "c") String type,
							 @RequestParam("keyword") String keyword,
							 @RequestParam(defaultValue = "1") int page) {
		
		log.info("검색결과 리스트 호출");

		//공용메소드 호출
		CommonMethod.getCategoryAndGalname(model, gServ);
		
	    //해당 갤러리의 총 게시물 수 
		int totalListCnt = gServ.searchByTypeAndKeywordCnt(type, keyword);

	    //생성인자로 총 게시물 수, 현재 페이지를 전달
	    Pagination pagination = new Pagination(totalListCnt, page);

	    //DB select start index
	    int startIndex = pagination.getStartIndex();
	    
	    //페이지 당 보여지는 게시글의 최대 개수
	    int pageSize = pagination.getPageSize();
	    
	    //해당 갤러리의 posts에 리스트들 불러와서 저장
	    List<Posts> posts
	    = gServ.searchByTypeAndKeyword(startIndex, pageSize, type, keyword);

	    //모델에 붙여서 html로 보내주기
	    model.addAttribute("postList", posts);
	    model.addAttribute("type", type);
	    model.addAttribute("pagination", pagination);
	    
	    return "gallery/searchList";
	}
	
	//갤러리 내부에서 검색 시 결과 그 화면에 보여주기
	@GetMapping("/searchGal")
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
	    int totalListCnt
	    = gServ.searchByTypeAndKeywordWithGalnameCnt(type, keyword, galname);

	    //생성인자로 총 게시물 수, 현재 페이지를 전달
	    Pagination pagination = new Pagination(totalListCnt, page);

	    //DB select start index
	    int startIndex = pagination.getStartIndex();
	    
	    //페이지 당 보여지는 게시글의 최대 개수
	    int pageSize = pagination.getPageSize();
	    
	    //해당 갤러리의 posts에 리스트들 불러와서 저장
	    List<Posts> posts
	    = gServ.searchByTypeAndKeywordWithGalname(startIndex, pageSize, type, keyword, galname);

	    //모델에 붙여서 html로 보내주기
	    
	    model.addAttribute("postList", posts);
	    model.addAttribute("pagination", pagination);
	    model.addAttribute("galname", galname);
	    model.addAttribute("type", type);
	    
	    if (galname.equals("요청게시판")) {
	    	return "/gallery/requestList";
	    } else {
	        return "/gallery/list";
	    }
	}
}