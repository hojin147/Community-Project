package com.bit.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bit.domain.Notice;
import com.bit.persistance.MemberRepository;
import com.bit.persistance.NoticeRepository;
import com.bit.vo.NoticeVO;

import lombok.extern.java.Log;

@Service
@Log
public class NoticeService {

	@Autowired
	NoticeRepository nRepo;
	
	@Autowired
	MemberRepository mRepo;
	
	//페이징을 위한 선언
    @PersistenceContext
	private EntityManager em;
    
	/*
	 * 공지사항쓰는 메소드
	폼에서 글쓰기 버튼을 누르면 각 값들이 여기로 오고 
	붙은 값들을 DB에 저장한다.
	*/
    @Transactional
    public void saveNotice(String userid, String title, String content) {
    	Notice notice = new Notice();
    	notice.setMember(mRepo.findByUserid(userid));
    	notice.setTitle(title);
    	notice.setContent(content);
        nRepo.save(notice);
    }
	
	//공지사항 글 리스트 받아오기
    @Transactional
	public List<Notice> findNoticeListPaging(int startIndex, int pageSize) {
		return em.createQuery("select n from Notice n order by n.nno DESC", Notice.class)
					.setFirstResult(startIndex)
					.setMaxResults(pageSize)
					.getResultList();
	}
	
    //공지사항 총 게시물의 개수 받아오기
	@Transactional
	public int findAllNoticeCnt() {
		return ((Long) em.createQuery
				("select count(*) from Notice")
					.getSingleResult())
					.intValue();
	}
	
	//공지사항 검색 결과 리스트
	@Transactional
	public List<Notice> searchByTypeAndKeywordWithNoticeCnt(int startIndex, int pageSize,
															String type, String keyword) {
		return em.createQuery("SELECT n FROM Notice n "
							+ "WHERE " + typeToColumnName(type) + " "
							+ "LIKE '%" + keyword + "%'", Notice.class)
				 .setFirstResult(startIndex)
				 .setMaxResults(pageSize)
				 .getResultList();
	}
	
	//공지사항 검색 결과 갯수 받아오기
	@Transactional
	public int findNoticeSearchListPaging(String type, String keyword) {
		return ((Long) em.createQuery("SELECT count(*) FROM Posts p "
									+ "WHERE " + typeToColumnName(type) + " "
									+ "LIKE '%" + keyword + "%'")
						 .getSingleResult())
						 .intValue();
	}
	
	private String typeToColumnName(String type) {
		if(type.equals("t")) return "title";
		if(type.equals("u")) return "member_userid";
		return "content";
	}
	
	//공지사항 하나 상세조회
	@Transactional
	public NoticeVO getNotice(int nno) {
		//DB에서 해당 게시글 데이터를 가져온다.
		Notice notice = nRepo.findById(nno).get();
		
		//객체를 만들고 저장한다.
		NoticeVO noticeVO = new NoticeVO();
		noticeVO.setNno(notice.getNno());
		noticeVO.setTitle(notice.getTitle());
		noticeVO.setContent(notice.getContent());
		noticeVO.setMember(notice.getMember());
		noticeVO.setPostdate(notice.getPostdate());
		noticeVO.setUpdatedate(notice.getUpdatedate());
		noticeVO.setHit(notice.getHit());
		
		return noticeVO;
	}
	
	//공지 수정
    @Transactional
    public void updateNotice(NoticeVO vo) {
    	nRepo.updateNotice(vo.getNno(), vo.getTitle(), vo.getContent());
    }
	
	//해당 nno의 공지 삭제하기
    @Transactional
    public void deleteNotice(int nno) {
    	nRepo.deleteById(nno);
    }
	
    //조회수 증가비교 메소드
	@Transactional
	public void noticeHit(int nno, String userid,
						HttpServletRequest request,
						HttpServletResponse response) {
		
		//필수코드
		//어떤 게시물의 조회수인지 가져옴
		NoticeVO noticeVO = getNotice(nno);
		
		//쿠키 로직 첫번째
		//쿠키를 불러오고 쿠키가 있는지 변수로 확인
		Cookie[] cookies = request.getCookies();
		int visitor = 0;
		
		for(Cookie cookie : cookies) {
			log.info(cookie.getName());
			//쿠키중에 userid와 일치하는게 있는지 확인
			if(cookie.getName().equals(userid) || 
					userid.equals(noticeVO.getMember().getUserid())) {
				
				visitor = 1;
				log.info("일치하니까 증가안함");
				
				//접속한 게시물 번호 유무 확인
				if(cookie.getValue().contains("nno") || 
						userid.equals(noticeVO.getMember().getUserid())) {
					
					log.info("게시물 번호가 있으니까 증가안함");
				} else {
					log.info("없으면 페이지 번호 추가해주고 조회수 늘리기");
					cookie.setValue(cookie.getValue() + "_" + "nno");
					response.addCookie(cookie);
					nRepo.noticeHit(nno);
				}
			}
		}
		
		if(visitor == 0) {
			log.info("쿠키가 없으면 쿠키 만들어주고 조회수 늘리기");
			Cookie cookie1 = new Cookie(userid, "nno");
			response.addCookie(cookie1);
			nRepo.noticeHit(nno);
		}
		
		//쿠키 로직 두번째
//		Cookie viewCookie=null;
//		Cookie[] cookies=request.getCookies();
//
//		System.out.println("cookie : "+cookies);
//		    
//        if(cookies !=null) {
//			for (int i = 0; i < cookies.length; i++) {
//                //만들어진 쿠키들을 확인하며, 만약 들어온 적 있다면 생성되었을 쿠키가 있는지 확인
//				if(cookies[i].getName().equals(userid)) {
//					log.info("if문 쿠키 이름 : "+cookies[i].getName());
//				
//                //찾은 쿠키를 변수에 저장
//					viewCookie=cookies[i];
//				}
//			}
//		} else {
//			log.info("cookies 확인 로직 : 쿠키가 없습니다.");
//		}
//        
//		//만들어진 쿠키가 없음을 확인
//		if(viewCookie==null) {
//			log.info("viewCookie 확인 로직 : 쿠키 없당");
//            try {
//            	//이 페이지에 왔다는 증거용(?) 쿠키 생성
//				Cookie newCookie=new Cookie(userid,"readCount");
//				response.addCookie(newCookie);
//                
//                //쿠키가 없으니 증가 로직 진행
//				pRepo.postHit(pno);
//			} catch (Exception e) {
//				log.info("쿠키 넣을때 오류 나나? : "+e.getMessage());
//				e.getStackTrace();
//			}
//        //만들어진 쿠키가 있으면 증가로직 진행하지 않음
//		} else {
//			log.info("viewCookie 확인 로직 : 쿠키 있당");
//			String value=viewCookie.getValue();
//			log.info("viewCookie 확인 로직 : 쿠키 value : "+value);
//		}
	}
}
