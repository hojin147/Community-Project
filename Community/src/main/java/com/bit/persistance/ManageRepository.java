package com.bit.persistance;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.bit.domain.Member;

//페이징 처리를 하기 위해 Crud가 아닌 페이징 기능이 들어있는 Jpa를 씀
@Repository
public interface ManageRepository extends JpaRepository<Member, String> {
	
	//회원 삭제시 실제 DB에서 없어지는게 아니라 타입을 바꿔줘서 비활성시킴
	@Modifying
	@Transactional
	@Query("UPDATE Member m set m.type = 'QUIT' where m.userid = ?1")
	void memberQuit(String userid);
}