package com.bit.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bit.service.CommentService;
import com.bit.service.GalleryService;
import com.bit.service.PostService;
import com.bit.vo.CommentVO;
import com.bit.vo.PostsVO;

import lombok.extern.java.Log;

//글쓰기 수정 삭제 관련
@Controller
@Log
//요즘 많이 쓰는 final 주입으로 코드수정
@RequiredArgsConstructor
public class PostController {

	private final CommentService cServ;
	private final GalleryService gServ;
	private final PostService pServ;

	// 글쓰기 페이지 호출
	@GetMapping("/gallery/writePost/{galname}")
	public String writePost(Model model,
							@PathVariable("galname") String galname) {

		log.info("글쓰기 페이지 호출");

		// 공용메소드 호출
		CommonMethod.getCategoryAndGalname(model, gServ);

		return "gallery/writePost";
	}

	// 글을쓰고 리스트로 돌아감
	// 포스트 방식의 요청매핑을 줄여쓴 것
	@PostMapping("/write")
	public String writing(Model model,
						  HttpServletRequest req,
						  RedirectAttributes rttr)
			throws UnsupportedEncodingException {

		// 폼에서 값 받기
		String userid = req.getParameter("userid");
		String galname = req.getParameter("galname");
		String title = req.getParameter("title");
		String content = req.getParameter("content");

		// 호출하기
		pServ.savePost(userid, galname, title, content);

		// 한글 깨지기 때문에 인코딩하는 코드 넣어줌
		String encodedParam = URLEncoder.encode(galname, "UTF-8");

		rttr.addFlashAttribute("msg", "postadded");

		if (galname.equals("요청게시판")) {
			return "redirect:/gallery/requestList?galname=" + encodedParam;
		} else {
			return "redirect:/gallery/list?galname=" + encodedParam;
		}
	}

	// 글 상세조회
	// 리스트에서 선택한 글에 대한 해당 pno와 userid를 가져온다
	@GetMapping("/gallery/postDetail/{pno}/{userid}")
	public String view(Model model,
					   @PathVariable("pno") int pno,
					   @PathVariable("userid") String userid,
					   HttpServletRequest request,
					   HttpServletResponse response) {

		log.info("게시글 하나 상세조회");

		/*
		 * 조회수 증가 혹은 증가안함 쿠키의 정보들을 불러가서 비교함
		 * 본인 게시물이거나, 한번 조회한 게시물은 쿠키가 없어질때까지
		 * 한번 조회수를 증가시키고 더 이상 증가시키지 않음.
		 */
		pServ.postHit(pno, userid, request, response);

		// 공용메소드 호출
		CommonMethod.getCategoryAndGalname(model, gServ);

		// 서비스에서 리턴받은 글을 저장한다.
		PostsVO postVO = pServ.getPost(pno);
		List<CommentVO> commentList = cServ.getComment(pno);

		// 저장한 글을 모델에 붙여서 보낸다
		model.addAttribute("post", postVO);
		model.addAttribute("commentList", commentList);

		return "gallery/postDetail";
	}

	// 요청 상세조회
	// 리스트에서 선택한 글에 대한 해당 pno와 userid를 가져온다
	@GetMapping("/gallery/requestDetail/{pno}/{userid}")
	public String requestview(Model model,
							  @PathVariable("pno") int pno,
							  @PathVariable("userid") String userid,
							  HttpServletRequest request,
							  HttpServletResponse response) {

		log.info("게시글 하나 상세조회");

		/*
		 * 조회수 증가 혹은 증가안함 쿠키의 정보들을 불러가서 비교함
		 * 본인 게시물이거나, 한번 조회한 게시물은 쿠키가 없어질때까지
		 * 한번 조회수를 증가시키고 더 이상 증가시키지 않음.
		 */
		pServ.postHit(pno, userid, request, response);

		// 공용메소드 호출
		CommonMethod.getCategoryAndGalname(model, gServ);

		// 서비스에서 리턴받은 글을 저장한다.
		PostsVO postVO = pServ.getPost(pno);
		List<CommentVO> commentList = cServ.getComment(pno);

		// 저장한 글을 모델에 붙여서 보낸다
		model.addAttribute("post", postVO);
		model.addAttribute("commentList", commentList);

		return "gallery/requestDetail";
	}

	// 해당 게시글 수정페이지 호출
	@GetMapping("/gallery/updatePosts/{pno}")
	public String updatePost(Model model, @PathVariable("pno") int pno) {

		// 공용메소드 호출
		CommonMethod.getCategoryAndGalname(model, gServ);

		log.info("글 수정 페이지 호출");

		// 서비스에서 리턴받은 글을 저장한다.
		PostsVO postVO = pServ.getPost(pno);

		// 저장한 글을 모델에 붙여서 보낸다
		model.addAttribute("post", postVO);

		return "gallery/updatePost";
	}

	// 글 수정하기
	@PostMapping("/postUpdate")
	public String modifyPOST(RedirectAttributes rttr, HttpServletRequest req) {

		log.info("게시글 수정 로직");

		PostsVO vo = new PostsVO();
		vo.setPno(Integer.parseInt(req.getParameter("pno")));
		vo.setTitle(req.getParameter("title"));
		vo.setContent(req.getParameter("content"));

		pServ.updatePost(vo);

		String galname = pServ.getPost(Integer.parseInt(req.getParameter("pno")))
							  .getGallery()
							  .getGalname();

		rttr.addFlashAttribute("msg", "updated");

		if (galname.equals("요청게시판")) {
			return "redirect:/gallery/requestDetail/"
					+ vo.getPno() + "/" + req.getParameter("userid");
		} else {
			return "redirect:/gallery/postDetail/"
					+ vo.getPno() + "/" + req.getParameter("userid");
		}
	}

	// 해당 글 삭제하기
	// 딜리트 작업 시 사용하는 매핑
	@DeleteMapping("/gallery/{pno}/{galname}")
	public String deletePOST(@PathVariable("pno") int pno,
							 @PathVariable("galname") String galname,
							 RedirectAttributes rttr)
							 throws UnsupportedEncodingException {

		log.info("삭제 번호 : " + pno);

		pServ.deletePost(pno);

		// 한글 깨지기 때문에 인코딩하는 코드 넣어줌
		String encodedParam = URLEncoder.encode(galname, "UTF-8");

		rttr.addFlashAttribute("msg", "postdeleted");

		if (galname.equals("요청게시판")) {
			return "redirect:/gallery/requestList?galname=" + encodedParam;
		} else {
			return "redirect:/gallery/list?galname=" + encodedParam;
		}
	}

//  	//추천수 증가(무한 증가로 인해 미완성임 손봐야함)
//  	@GetMapping("/gallery/up/{pno}")
//  	public String postUp(@PathVariable("pno") int pno, RedirectAttributes rttr,
//  						 @SessionAttribute("userid") String userid) {
//  		
//  		log.info("추천받은 게시글 번호 : " + pno);
//  		
//  		String redirectTarget = "/gallery/postDetail/" + pno;
//  		
//  		if (userid != null) {
//  			//로그인 정보가 있을 때
//  			pServ.postUp(pno);
//  			redirectTarget += ("/" + userid);
//  			rttr.addFlashAttribute("msg", "up");
//  		} else {
//  			//로그인 정보가 없을 때
//  			redirectTarget += ("/null");
//  			rttr.addFlashAttribute("msg", "upfail");
//  		}
//  		
//  		return "redirect:" + redirectTarget;
//  	}
}