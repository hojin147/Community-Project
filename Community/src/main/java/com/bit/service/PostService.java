package com.bit.service;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.bit.domain.Posts;
import com.bit.repository.GallerysRepository;
import com.bit.repository.MemberRepository;
import com.bit.repository.PostsRepository;
import com.bit.vo.PostsVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

//게시물에 대한 로직작성
@Service
@Log
@RequiredArgsConstructor
public class PostService {
	
	//final주입, 요즘 유행
	private final PostsRepository pRepo;
	private final MemberRepository mRepo;
	private final GallerysRepository gRepo;
	
	//생성자 주입
//	@Autowired
//	public PostService(PostsRepository pRepo, MemberRepository mRepo, GallerysRepository gRepo) {
//		this.pRepo = pRepo;
//		this.mRepo = mRepo;
//		this.gRepo = gRepo;
//	}
	
	/*
	 * 글쓰는 메소드
	폼에서 글쓰기 버튼을 누르면 각 값들이 여기로 오고 
	붙은 값들을 DB에 저장한다.
	*/
    @Transactional
    public void savePost(String userid, String galname, String title, String content) {
    	Posts post = new Posts();
    	post.setMember(mRepo.findByUserid(userid));
    	post.setGallery(gRepo.findById(galname).get());
    	post.setTitle(title);
    	post.setContent(content);
        pRepo.save(post);
    }
	
	//게시글 하나 상세조회
	@Transactional
	public PostsVO getPost(int pno) {
		//DB에서 해당 게시글 데이터를 가져온다.
		Posts posts = pRepo.findById(pno).get();
		
		//객체를 만들고 저장한다.
		PostsVO postsVO = new PostsVO();
		postsVO.setPno(posts.getPno());
		postsVO.setTitle(posts.getTitle());
		postsVO.setContent(posts.getContent());
		postsVO.setHit(posts.getHit());		
		postsVO.setUp(posts.getUp());
		postsVO.setPostdate(posts.getPostdate());
		postsVO.setUpdatedate(posts.getUpdatedate());
		postsVO.setGallery(posts.getGallery());
		postsVO.setMember(posts.getMember());
		
		return postsVO;
	}

	//해당 pno의 게시글 삭제하기
    @Transactional
    public void deletePost(int pno) {
    	pRepo.deleteById(pno);
    }
    
    //게시글 수정
    @Transactional
    public void updatePost(PostsVO vo) {
    	pRepo.updatePosts(vo.getPno(), vo.getTitle(), vo.getContent());
    }
    
    //추천수 증가(무한증가로 인해 미완성임 수정요함)
    @Transactional
	public void postUp(int pno) {
		pRepo.postUp(pno);
	}
    
    //베스트 게시글 가져오기
	public List<Posts> getTopHitPosts() {
		return pRepo.selectByHitDesc();
	}
    
    //조회수 증가비교 메소드
	@Transactional
	public void postHit(int pno, String userid,
						HttpServletRequest request,
						HttpServletResponse response) {
		
		//필수코드
		//어떤 게시물의 조회수인지 가져옴
		PostsVO postVO = getPost(pno);
		
		//쿠키 로직 첫번째
		//쿠키를 불러오고 쿠키가 있는지 변수로 확인
		Cookie[] cookies = request.getCookies();
		int visitor = 0;
		
		for(Cookie cookie : cookies) {
			log.info(cookie.getName());
			//쿠키중에 userid와 일치하는게 있는지 확인
			if(cookie.getName().equals(userid) || 
					userid.equals((postVO.getMember()==null) ? "" : postVO.getMember().getUserid())) {
				
				visitor = 1;
				log.info("일치하니까 증가안함");
				
				//접속한 게시물 번호 유무 확인
				if(cookie.getValue().contains("pno") || 
						userid.equals((postVO.getMember()==null) ? "" : postVO.getMember().getUserid())) {
					
					log.info("게시물 번호가 있으니까 증가안함");
				} else {
					log.info("없으면 페이지 번호 추가해주고 조회수 늘리기");
					cookie.setValue(cookie.getValue() + "_" + "pno");
					response.addCookie(cookie);
					pRepo.postHit(pno);
				}
			}
		}
		
		if(visitor == 0) {
			log.info("쿠키가 없으면 쿠키 만들어주고 조회수 늘리기");
			Cookie cookie1 = new Cookie(userid, "pno");
			response.addCookie(cookie1);
			pRepo.postHit(pno);
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