package com.bit.persistance;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.bit.domain.Gallerys;
import com.bit.domain.Member;

//페이징 처리를 하기 위해 Crud가 아닌 페이징 기능이 들어있는 Jpa를 씀
@Repository
public interface GallerysRepository extends JpaRepository<Gallerys, String> {

	//카테고리 중복없이 다 가져오기
	@Modifying
	@Transactional
	@Query("select distinct g.category from Gallerys g")
	public String[] findAllCategory();
	
	//해당하는 카테고리의 갤네임 가져오기
	@Modifying
	@Transactional
	public List<Gallerys> findByCategory(String category);
	
	//갤네임 중복없이 다 가져오기
	@Modifying
	@Transactional
	@Query("select distinct g.galname from Gallerys g")
	public String[] findByGalname();

	/* ------------------------- 갤러리 수정/삭제 ------------------------ */
	//갤러리 수정
	@Modifying
	@Transactional
	@Query("UPDATE Gallerys g SET g.galname = ?1, g.category = ?2, g.master = ?3 "
			+ " WHERE g.galname = ?4")
	public int updateGallery(String galname, String category, Member master, String galnameOrg);
	
	//갤러리 삭제
	@Modifying
	@Transactional
	@Query("DELETE FROM Gallerys g WHERE g.galname = ?1")
	public int deleteByGalname(String galname);
}