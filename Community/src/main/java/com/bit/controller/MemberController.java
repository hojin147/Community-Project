package com.bit.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bit.domain.Member;

import com.bit.service.MemberService;
import com.bit.vo.MemberVO;

import lombok.extern.java.Log;

//회원 정보 관련 페이지 호출
@Controller
@Log
public class MemberController {

	@Autowired
	MemberService mServ;

	@GetMapping("/member/login")
	public String loginPage() {
		log.info("로그인 페이지 호출");
		return "member/login";
	}

	// 로그인기능
	@PostMapping("/login") // 폼의 액션명
	public String login(HttpServletRequest req, RedirectAttributes rttr) {

		log.info("로그인");

		// 폼에 입력 값 받아와서 저장
		String id = req.getParameter("userid");
		String pw = req.getParameter("password");

		// DB데이터 가져오기
		MemberVO vo = mServ.login(req.getParameter("userid"));

		// 세션 설정
		HttpSession session = (HttpSession) req.getSession();

		// 아이디가 없음
		if (vo == null) {
			rttr.addFlashAttribute("msg", "noid");
			return "redirect:member/login";

		// ID, PW맞는지 안맞는지 확인 ++ 탈퇴한 회원이 아닌지 확인
		} else if (vo.getUserid().equals(id) && vo.getPw().equals(pw)) {
			if (vo.getType().equals("QUIT")) {
				log.info("quit user");
				rttr.addFlashAttribute("msg", "quitmember");
				return "redirect:member/login";
			}
			session.setAttribute("userid", vo.getUserid());
			session.setAttribute("type", vo.getType());
			return "redirect:main/index";

		// 그 외 비밀번호 안맞을 시
		} else {
			rttr.addFlashAttribute("msg", "fail");
			return "redirect:member/login";
		}
	}

	@GetMapping("/member/signUp")
	public String joinPage() {
		log.info("회원가입 페이지 호출");
		return "member/signUp";
	}

	// 로그아웃 버튼 액션 호출
	@GetMapping("/logout")
	public String logout(HttpSession session, RedirectAttributes rttr) {

		log.info("로그아웃하기");
		mServ.logout(session);

		rttr.addFlashAttribute("msg", "logout");

		return "redirect:main/index";
	}

	// 회원가입
	@PostMapping("/signUp")
	public String joinMember(HttpServletRequest req, RedirectAttributes rttr) {

		log.info("회원가입하기");

		String userid = req.getParameter("userid");
		String pw = req.getParameter("pw");
		String name = req.getParameter("name");
		String email = req.getParameter("email");
		String address = req.getParameter("address");
		String phone = req.getParameter("phone");

		// 객체 만들어서 붙여넣기
		Member vo = new Member(userid, pw, name, email, phone, address, "USER");

		// 입력받은 값 서비스로 넘겨서 결과 리턴받기
		mServ.signUp(vo);

		rttr.addFlashAttribute("msg", "success");

		return "redirect:/main/index";
	}

	// id중복체크
	@GetMapping("/idCheck")
	public String idCheck(HttpServletRequest req, RedirectAttributes rttr) {

		log.info("id중복체크 시작");

		String userid = req.getParameter("userid");
		String pw = req.getParameter("pw");
		String name = req.getParameter("name");
		String email = req.getParameter("email");
		String address = req.getParameter("address");
		String phone = req.getParameter("phone");

		log.info("idCheck 호출 : " + userid);

		if (mServ.idCheck(userid) && (userid.length() < 50)) {
			rttr.addFlashAttribute("msg", "checked");
			log.info("id checked");
		} else {
			rttr.addFlashAttribute("msg", "rejected");
			log.info("id rejected");
		}

		log.info("나머지값 돌려주기 : " + pw + name + email + address + phone);

		rttr.addFlashAttribute("uid", userid);
		rttr.addFlashAttribute("upw", pw);
		rttr.addFlashAttribute("uname", name);
		rttr.addFlashAttribute("uemail", email);
		rttr.addFlashAttribute("uaddress", address);
		rttr.addFlashAttribute("uphone", phone);

		return "redirect:/member/signUp";
	}

	// 회원정보 수정 페이지 호출
	@GetMapping("/member/memberUpdatePage")
	public String memberUpdatePage(Model model, @SessionAttribute("userid") String userid) {

		log.info("회원정보 수정 페이지 호출  ID : " + userid);

		// 로그인할때 붙었던 회원정보를 가져온다
		MemberVO vo = mServ.login(userid);

		model.addAttribute("member", vo);

		return "/member/memberUpdatePage";
	}

	// 회원정보 수정
	@PostMapping("/memberUpdate")
	public String memberUpdate(HttpServletRequest req, RedirectAttributes rttr) {

		log.info("회원정보 수정 시작");

		String userid = req.getParameter("userid");
		String name = req.getParameter("name");
		String email = req.getParameter("email");
		String address = req.getParameter("address");
		String phone = req.getParameter("phone");

		mServ.updateUser(userid, name, email, address, phone);

		// 세션 리프레쉬
		MemberVO vo = mServ.login(req.getParameter("userid"));
		HttpSession session = (HttpSession) req.getSession();

		session.setAttribute("userid", vo.getUserid());

		return "redirect:/main/index";
	}

	// 비밀번호 변경페이지 호출
	@GetMapping("/member/memberPwUpdatePage")
	public String memberPwUpdatePage(Model model, @SessionAttribute("userid") String userid) {

		log.info("회원 비밀번호 변경 페이지 호출  ID : " + userid);

		// 로그인할때 붙은 유저의 비밀번호를 가져온다
		MemberVO vo = mServ.login(userid);

		model.addAttribute("member", vo);

		return "/member/memberPwUpdatePage";
	}

	// 비밀번호 변경
	@PostMapping("/memberPwUpdate")
	public String memberPwUpdate(HttpServletRequest req, RedirectAttributes rttr) {

		log.info("회원비밀번호 수정 시작");

		String userid = req.getParameter("userid");
		String pw = req.getParameter("newpw");

		mServ.updateUserPw(userid, pw);

		return "redirect:/logout";
	}

	// 회원탈퇴 페이지 호출
	@GetMapping("/member/memberQuit")
	public String memberQuit(Model model, @SessionAttribute("userid") String userid) {

		log.info("회원탈퇴 페이지 호출  ID : " + userid);

		// 로그인할때 붙었던 회원정보를 가져온다
		MemberVO vo = mServ.login(userid);

		model.addAttribute("member", vo);

		return "member/memberQuit";
	}

	// 탈퇴 실행
	@PostMapping("/memberQuit")
	public String memberQuitProcess(@SessionAttribute("userid") String userid, RedirectAttributes rttr,
			HttpServletRequest req) {

		log.info("회원탈퇴 진행");

		// 실제 삭제는 아니고 타입을 바꾼다.
		mServ.memberQuit(userid);

		// 세션정보 가져와서 세션 끊기
		HttpSession session = (HttpSession) req.getSession();
		session.invalidate();

		return "redirect:main/index";
	}
}