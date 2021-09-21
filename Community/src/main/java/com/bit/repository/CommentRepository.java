package com.bit.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.bit.domain.Comment;
import com.bit.domain.Posts;

//페이징 처리를 하기 위해 Crud가 아닌 페이징 기능이 들어있는 Jpa를 씀
@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
	
	//해당 댓글 수정
	@Modifying
	@Transactional
	@Query("UPDATE Comment c SET c.content = ?2 WHERE c.cno = ?1")
	void commentUpdate(int cno, String content);
	
	//해당 댓글 삭제
	@Modifying
	@Transactional
	@Query("DELETE FROM Comment c where c.cno = ?1")
	void deleteByCno(int cno);

	//댓글 리스트 가져오기
	@Modifying
	@Transactional
	public List<Comment> findByPost(Posts post);
}
