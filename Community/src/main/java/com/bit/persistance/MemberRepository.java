package com.bit.persistance;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.bit.domain.Member;

public interface MemberRepository extends CrudRepository<Member, String>{

	//로그인을 위한 아이디로 멤버 객체 가져오는 추상메소드
	public Member findByUserid(String userid);
	
	//회원정보수정
	@Modifying
	@Transactional
	@Query("UPDATE Member m set m.name = ?2, m.email = ?3, m.address = ?4, m.phone = ?5 "
			+ "where m.userid = ?1")
	public int memberUpdate(String userid, String name, String email, String address, String phone);
	
	//비밀번호 변경
	@Modifying
	@Transactional
	@Query("UPDATE Member m set m.pw = ?2 where m.userid = ?1")
	public int memberPwUpdate(String userid, String pw);
	
	//회원탈퇴시 실제 DB에서 없어지는게 아니라 타입을 바꿔줘서 비활성 시킴
	@Modifying
	@Transactional
	@Query("UPDATE Member m set m.type = 'QUIT' where m.userid = ?1")
	public void quitMember(String userid);
	
//	//회원삭제
//	@Modifying
//	@Transactional
//	@Query("DELETE FROM Member m where userid = ?1")
//	public boolean deleteByUserid(String userid);
}