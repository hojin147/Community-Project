package com.bit.persistance;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.bit.domain.Notice;

//페이징 처리를 하기 위해 Crud가 아닌 페이징 기능이 들어있는 Jpa를 씀
@Repository
public interface NoticeRepository extends JpaRepository<Notice, Integer>{

	//공지 수정 쿼리문
	@Modifying
	@Transactional
	@Query("UPDATE Notice n set n.title = ?2, n.content = ?3, n.updatedate = now() "
			+ " where n.nno = ?1")
	public int updateNotice(int nno, String title, String content);
	
	//공지 조회수 증가 쿼리문
	@Modifying
	@Transactional
	@Query("UPDATE Notice n set n.hit = n.hit+1 where n.nno = ?1")
	public int noticeHit(int nno);
}