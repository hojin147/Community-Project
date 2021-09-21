package com.bit.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bit.domain.Comment;
import com.bit.domain.Pagination;
import com.bit.service.CommentService;
import com.bit.service.GalleryService;
import com.bit.vo.CommentVO;

import lombok.extern.java.Log;

//댓글 관련 컨트롤러
@Controller
@Log
//요즘 많이 쓰는 final 주입으로 코드수정
@RequiredArgsConstructor
public class CommentController {
	
	private final CommentService cServ;
	private final GalleryService gServ;
	
	//댓글쓰기
	@PostMapping("/commentSub/{pno}")
	public String commentSub(@PathVariable("pno") int pno,
							 @SessionAttribute("userid") String userid, 
							 HttpServletRequest req,
							 RedirectAttributes rttr) {
		
		log.info("댓글 요청받은 게시글 번호 : " + pno);
  		
  		String redirectTarget = "/gallery/postDetail/" + pno;
  		
  		if (userid != null) {
  			//로그인 정보가 있을 때
  			CommentVO vo = new CommentVO();
  			vo.setPost(pno);
  			vo.setMember(userid);
  			vo.setContent(req.getParameter("content"));
  			
  			cServ.commentSub(vo);
  			redirectTarget += ("/" + userid);
  			rttr.addFlashAttribute("msg", "comment");
  		} else {
  			//로그인 정보가 없을 때
  			redirectTarget += ("/null");
  			rttr.addFlashAttribute("msg", "commentfail");
  		}
		
		return "redirect:" + redirectTarget;
	}
	
	//댓글수정
	@PostMapping("/commentUpdate/{pno}/{cno}")
	public String commentUpdate(@PathVariable("pno") int pno, 
								@PathVariable("cno") int cno,
								@SessionAttribute("userid") String userid, 
								HttpServletRequest req) {
		
		cServ.commentUpdate(cno, req.getParameter("content"));
		
		return "redirect:/gallery/postDetail/" + pno + "/" + userid;
	}
	
	//댓글삭제
	@GetMapping("/commentDelete/{pno}/{cno}")
	public String commentDelete(@PathVariable("pno") int pno, 
								@PathVariable("cno") int cno,
								@SessionAttribute("userid") String userid, 
								RedirectAttributes rttr) {
		//해당하는 댓글 삭제
		cServ.commentDelete(cno);
		
		rttr.addFlashAttribute("msg", "commentdeleted");
		
		return "redirect:/gallery/postDetail/" + pno + "/" + userid;
	}
	
	//로그인한 유저의 댓글 리스트 호출
	@GetMapping("/member/myComment")
	public void myPage(Model model,
					   @RequestParam(defaultValue = "1") int page, 
					   @SessionAttribute("userid") String userid) {
		
		log.info("내 댓글 리스트 호출");
		
		//공용메소드 호출
		CommonMethod.getCategoryAndGalname(model, gServ);
		
		//로그인한 유저의 총 댓글 수 
	    int totalListCnt = cServ.findAllMyCommentCnt(userid);

	    //생성인자로 총 댓글 수, 현재 페이지를 전달
	    Pagination pagination = new Pagination(totalListCnt, page);

	    //DB select start index
	    int startIndex = pagination.getStartIndex();
	    
	    //페이지 당 보여지는 댓글의 최대 개수
	    int pageSize = pagination.getPageSize();
	    
	    //로그인한 유저의 comment에 리스트들 불러와서 저장
	    List<Comment> comment
	    = cServ.findCommentListPaging(startIndex, pageSize, userid);

	    String[] allGalname = gServ.getGalname();
	    
	    //모델에 붙여서 html로 보내주기	    
	    model.addAttribute("allGalname", allGalname);
	    model.addAttribute("myCommentList", comment);
	    model.addAttribute("pagination", pagination);
	}
}