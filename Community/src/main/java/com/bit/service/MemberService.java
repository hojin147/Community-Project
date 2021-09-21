package com.bit.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.bit.domain.Member;
import com.bit.repository.MemberRepository;
import com.bit.vo.MemberVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	//final 주입
	private final MemberRepository mRepo;
	
	//페이징을 위한 선언
    @PersistenceContext
	private EntityManager em;
	
	//로그인 및 회원정보수정 정보 가져오기
	//DB에서 아이디와 비밀번호 가져와서 저장해서 vo 리턴
	@Transactional
	public MemberVO login(String userid) {
		Member member = mRepo.findByUserid(userid);
		
		//아이디가 없으면 객체가 널이됨
		if(member == null) {
			return null;
			
		//아이디가 있는 경우
		} else {
			MemberVO vo = new MemberVO();
			vo.setUserid(member.getUserid());
			vo.setPw(member.getPw());
			vo.setName(member.getName());
			vo.setType(member.getType());
			vo.setEmail(member.getEmail());
			vo.setPhone(member.getPhone());
			vo.setAddress(member.getAddress());
			return vo;
		}
	}
	
	//로그아웃
	@Transactional
	public void logout(HttpSession session) {
		//세션삭제
		session.invalidate();
	}
	
	//아이디 중복확인
	@Transactional
	public boolean idCheck(String userid) {
		//중복 아이디 없는 경우
		if(mRepo.findByUserid(userid) == null) return true;
		//중복 아이디 있는 경우
		return false;
	}

	//회원가입
	@Transactional
	public void signUp(Member vo) {
		//DB저장
		mRepo.save(vo);
	}
	
	//회원정보 수정
	@Transactional
	public void updateUser(String userid, String name, String email,
						   String address,String phone) {
		mRepo.memberUpdate(userid, name, email, address, phone);
	}
	
	//회원비밀번호 변경
	@Transactional
	public void updateUserPw(String userid, String pw) {
		mRepo.memberPwUpdate(userid, pw);
	}
	
	//회원탈퇴
	@Transactional
	public void memberQuit(String userid) {
		//실제 DB에서 없어지는게 아니라 타입을 바꿔줘서 비활성시킴
		mRepo.quitMember(userid);
	}
}