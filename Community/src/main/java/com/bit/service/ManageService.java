package com.bit.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bit.domain.Gallerys;
import com.bit.domain.Member;
import com.bit.persistance.ManageRepository;

@Service
@RequiredArgsConstructor
public class ManageService {

	//final 주입
	private final ManageRepository mgRepo;

//	@Autowired
//	public ManageService(ManageRepository mgRepo) {
//		this.mgRepo = mgRepo;
//	}
	
	//페이징을 위한 선언
    @PersistenceContext
	private EntityManager em;
    
	private String typeToGalColumnName(String type) {
		if(type.equals("c")) {
			return "category";
		}
		return "galname";
	}
	
	//총 갤러리 개수 받아오기
  	@Transactional
  	public int findAllGalCnt() {
  		return
  		((Long) em.createQuery("select count(*) from Gallerys g")
  				  .getSingleResult())
  				  .intValue();
  	}
  	
  	//갤러리 다 받아오기
    @Transactional
  	public List<Gallerys> findGalListPaging(int startIndex, int pageSize) {
  		return em.createQuery("select g from Gallerys g order by g.category ASC", Gallerys.class)
  				 .setFirstResult(startIndex)
  				 .setMaxResults(pageSize)
  				 .getResultList();
  	}
      
	//검색한 갤러리 개수 받아오기
    @Transactional
	public int findAllGalSearchCnt(String type, String keyword) {
    	return ((Long) em.createQuery("select count(*) from Gallerys g "
      								+ "WHERE " + typeToGalColumnName(type) + " "
      								+ "LIKE '%" + keyword + "%'")
      					 .getSingleResult())
      					 .intValue();
    }
      
	//검색한 갤러리 다 받아오기
    @Transactional
    public List<Gallerys> findGalSearchListPaging(int startIndex, int pageSize,
    											  String type, String keyword) {
    	return em.createQuery("select g from Gallerys g "
      						+ "WHERE " + typeToGalColumnName(type) + " "
      						+ "LIKE '%" + keyword + "%' "
      						+ "order by g.category ASC", Gallerys.class)
      			 .setFirstResult(startIndex)
      			 .setMaxResults(pageSize)
      			 .getResultList();
    }
    
	//타입이 "USER"인 회원전체 리스트 받아오기
    @Transactional
	public List<Member> findListPaging(int startIndex, int pageSize) {
    	String type  = "USER";
		return em.createQuery("select m from Member m where type  = '" 
								+ type + "'", Member.class)
					.setFirstResult(startIndex)
					.setMaxResults(pageSize)
					.getResultList();
	}
	
    //타입이 "USER"인 회원의 명 수 받아오기
	@Transactional
	public int findAllCnt() {
		String type  = "USER";
		return ((Long) em.createQuery
				("select count(*) from Member where type = '" + type + "'")
					.getSingleResult())
					.intValue();
	}

	//회원삭제
	public void memberQuit(String userid) {
		//실제 DB에서 없어지는게 아니라 타입을 바꿔줘서 비활성 시킴
		mgRepo.memberQuit(userid);
	}
	
	//회원삭제
//	@Transactional
//	public void memberDelete(String userid) {
//		mgRepo.deleteById(userid);
//		
//	}
}