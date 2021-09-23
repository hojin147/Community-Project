package com.bit.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.bit.domain.Gallerys;
import com.bit.domain.Posts;
import com.bit.repository.GallerysRepository;
import com.bit.vo.GallerysVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GalleryService {

	//final 주입
	private final GallerysRepository gRepo;
	
//	@Autowired
//	public GalleryService(GallerysRepository gRepo) {
//		this.gRepo = gRepo;
//	}
	
	//페이징을 위한 선언
    @PersistenceContext
	private EntityManager em;
    
	//선택한 카테고리에 따른 문자 리턴
	private String typeToColumnName(String type) {
		if(type.equals("t")) return "title";
		if(type.equals("u")) return "member_userid";
		return "content";
	}
	
	//카테고리 가져오기
	@Transactional
	public String[] getCategory() {
		String[] category = gRepo.findAllCategory();
		return category;
	}
	
	//해당 카테고리의 갤네임 가져오기
	@Transactional
	public List<Gallerys> getGalname(String category) {
		List<Gallerys> galnames = gRepo.findByCategory(category);
		System.out.println(galnames);
		return galnames;
	}
	
	//해당하는 갤러리의 글 리스트 받아오기
    @SuppressWarnings("unchecked")
	@Transactional
	public List<Posts> findListPaging(int startIndex, int pageSize, String galname) {
    	/*
    	return em.createQuery("select p from Posts p "
							+ "where gallery_galname = '" + galname + "' "
							+ "order by p.pno DESC", Posts.class)
		*/
    	
    	//ROWNUM을 사용하여 데이터가 많아질수록
    	//목록 불러오는 것에 대한 속도를 높이기 위해서 사용
    	
    	//Rownum 초기화하기
    	gRepo.setRownum();
    	return em.createNativeQuery("select \\@ROWNUM \\:= \\@ROWNUM+1, p.* from posts p"
    							  + " where gallery_galname = '"
    							  + galname + "' order by pno desc", Posts.class)
				 .setFirstResult(startIndex)
				 .setMaxResults(pageSize)
				 .getResultList();
	}
	
    //해당하는 갤러리의 총 게시물의 개수 받아오기
	@Transactional
	public int findAllCnt(String galname) {
		return ((Long) em.createQuery
				("select count(*) from Posts"
			   + " where gallery_galname = '" + galname + "'")
						 .getSingleResult())
						 .intValue();
	}
	
	//로그인한 유저의 글 리스트 받아오기
    @SuppressWarnings("unchecked")
	@Transactional
	public List<Posts> findMyListPaging(int startIndex, int pageSize, String userid) {
//		return em.createQuery("select p from Posts p "
//							+ "where member_userid = '" + userid + "' "
//							+ "order by p.pno DESC", Posts.class)
    	//Rownum 초기화하기
    	gRepo.setRownum();
		return em.createNativeQuery("select \\@ROWNUM \\:= \\@ROWNUM+1, p.* from Posts p "
								  + "where member_userid = '" + userid + "' "
								  + "order by p.pno DESC", Posts.class)
				 .setFirstResult(startIndex)
				 .setMaxResults(pageSize)
				 .getResultList();
	}
	
    //로그인한 유저의 총 게시물의 개수 받아오기
	@Transactional
	public int findAllMyCnt(String userid) {
		return ((Long) em.createQuery
				("select count(*) from Posts where member_userid = '" + userid + "'")
						 .getSingleResult())
						 .intValue();
	}
	
	//로그인한 유저가 선택한 갤러리 게시물만 받아오기
	@Transactional
	public List<Posts> findGalMyListPaging(int startIndex,
										   int pageSize,
										   String userid,
										   String galname) {
		return em.createQuery("select p from Posts p "
							+ "where member_userid = '" + userid + "' "
							+ "and gallery_galname ='"+ galname + "' "
							+ "order by p.pno DESC", Posts.class)
				 .setFirstResult(startIndex)
				 .setMaxResults(pageSize)
				 .getResultList();
	}
	
	//로그인한 유저가 선택한 갤러리의 총 게시물의 개수 받아오기
	@Transactional
	public int findGalMyListPagingCnt(String userid, String galname) {
		return
		((Long) em.createQuery("select count(*) from Posts p "
							 + "where member_userid = '" + userid + "' "
							 + "and gallery_galname ='"+ galname + "' "
							 + "order by p.pno DESC")
				 .getSingleResult())
				 .intValue();
	}
	
	//중복없이 갤러리 다 가져오기
	@Transactional
	public String[] getGalname() {
		return gRepo.findByGalname();
	}
	
	/* ------------------------- 갤러리 입력/수정/삭제 ------------------------ */
	
	//갤러리 이름으로 검색했을 때 행 갯수
	@Transactional
	public int findGalnameCnt(String galname) {
		return
		((Long) em.createQuery("select count(*) from Gallerys g "
							 + "where galname = '" + galname + "'")
				 .getSingleResult())
				 .intValue();
	}
	
	//갤러리 입력
	@Transactional
	public boolean insertGallery(GallerysVO vo) {
		if(findGalnameCnt(vo.getGalname()) != 0) {
			System.out.println("insertGallery() --------------- overlap galname");
			return false;
		} else {
			Gallerys gal = new Gallerys();
			gal.setGalname(vo.getGalname());
			gal.setCategory(vo.getCategory());
			gal.setMaster(vo.getMaster());
			gRepo.save(gal);
			return true;
		}
	}
	
	//모든 갤러리 이름을 카테고리로 정렬하여 전달
	@Transactional
	public List<Gallerys> galleryList() {
		return em.createQuery("select g from Gallerys g order by g.category", Gallerys.class)
				 .getResultList();
	}
	
	//카테고리별 갤러리 이름을 전달
	@Transactional
	public List<Gallerys> galleryListInCategory(String ctgr) {
		return em.createQuery("select g from Gallerys g WHERE g.category = '" + ctgr + "'", Gallerys.class)
				 .getResultList();
	}
	
	//갤러리 수정
	//해당 갤러리에 게시글이 있을 경우 수정 불가능함
	@Transactional
	public int updateGallery(GallerysVO vo, String galnameOrg) {
		//리턴값 : 업데이트 된 행 갯수
		return
		gRepo.updateGallery(vo.getGalname(), vo.getCategory(), vo.getMaster(), galnameOrg);
	}
	
	//갤러리 삭제
	@Transactional
	public int deleteGallery(String galname) {
		//리턴값 : 업데이트 된 행 갯수
		return gRepo.deleteByGalname(galname);
	}
	
	
	/* ----------------통합검색---------------- */
	//통합검색 결과 리스트
	@Transactional
	public List<Posts> searchByTypeAndKeyword(int startIndex,
											  int pageSize,
											  String type,
											  String keyword) {
		return em.createQuery("SELECT p FROM Posts p "
							+ "WHERE " + typeToColumnName(type) + " "
							+ "LIKE '%" + keyword + "%'", Posts.class)
				 .setFirstResult(startIndex)
				 .setMaxResults(pageSize)
				 .getResultList();
	}
	
	//통합검색 결과 갯수 받아오기
	@Transactional
	public int searchByTypeAndKeywordCnt(String type, String keyword) {
		return ((Long) em.createQuery("SELECT count(*) FROM Posts p "
									+ "WHERE " + typeToColumnName(type) + " "
									+ "LIKE '%" + keyword + "%'")
				.getSingleResult())
				.intValue();
	}
	
	/* ----------------갤러리별 검색---------------- */
	//갤러리별 검색 결과 리스트
	@Transactional
	public List<Posts> searchByTypeAndKeywordWithGalname(int startIndex,
														 int pageSize,
														 String type,
														 String keyword,
														 String galname) {
		return em.createQuery("SELECT p FROM Posts p "
							+ "WHERE " + typeToColumnName(type) + " "
							+ "LIKE '%" + keyword + "%' "
							+ "AND gallery_galname = '" + galname + "'", Posts.class)
				 .setFirstResult(startIndex)
				 .setMaxResults(pageSize)
				 .getResultList();
	}
	
	//갤러리별 검색 결과 갯수 받아오기
	@Transactional
	public int searchByTypeAndKeywordWithGalnameCnt(String type, String keyword, String galname) {
		return ((Long) em.createQuery("SELECT count(*) FROM Posts p "
									+ "WHERE " + typeToColumnName(type) + " "
									+ "LIKE '%" + keyword + "%' "
									+ "AND gallery_galname = '" + galname + "'")
						 .getSingleResult())
						 .intValue();
	}
}