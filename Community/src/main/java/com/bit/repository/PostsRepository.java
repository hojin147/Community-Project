package com.bit.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.bit.domain.Posts;

//페이징 처리를 하기 위해 Crud가 아닌 페이징 기능이 들어있는 Jpa를 씀
@Repository
public interface PostsRepository extends JpaRepository<Posts, Integer> {

	//게시글 수정 쿼리문
	@Modifying
	@Transactional
	@Query("UPDATE Posts p set p.title = ?2, p.content = ?3, p.updatedate = now() "
			+ " where p.pno = ?1")
	public int updatePosts(int pno, String title, String content);
	
	//조회수 증가 쿼리문
	@Modifying
	@Transactional
	@Query("UPDATE Posts p set p.hit = p.hit+1 where p.pno = ?1")
	public int postHit(int pno);
	
	//추천수 증가(무한 추천으로 미완성임)
	@Modifying
	@Transactional
	@Query("UPDATE Posts p set p.up = p.up+1 where p.pno = ?1")
	public int postUp(int pno);
	
	//조회수 높은 순으로 게시글 5개 가져오기(베스트 게시글)
	@Modifying
	@Transactional
	@Query(value = "SELECT * FROM Posts p ORDER BY p.hit DESC limit 5 offset 0", nativeQuery = true)
	public List<Posts> selectByHitDesc();
}
