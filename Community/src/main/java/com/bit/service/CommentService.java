package com.bit.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bit.domain.Comment;
import com.bit.domain.Posts;
import com.bit.persistance.CommentRepository;
import com.bit.persistance.MemberRepository;
import com.bit.persistance.PostsRepository;
import com.bit.vo.CommentVO;

@Service
@RequiredArgsConstructor
public class CommentService {
	
	//final 주입, 요즘 많이 씀
	private final CommentRepository cRepo;	
	private final MemberRepository mRepo;
	private final PostsRepository pRepo;
	
//	//생성자 주입 방식 예시
//	@Autowired
//	public CommentService(CommentRepository cRepo, MemberRepository mRepo, PostsRepository pRepo) {
//		this.cRepo = cRepo;
//		this.mRepo = mRepo;
//		this.pRepo = pRepo;
//	}
	
	//페이징을 위한 선언
    @PersistenceContext
	private EntityManager em;
	
	//댓글쓰기
	@Transactional
	public void commentSub(CommentVO vo) {
		Comment comment = new Comment();
		comment.setContent(vo.getContent());
		comment.setMember(mRepo.findByUserid(vo.getMember()));
		comment.setPost(pRepo.findById(vo.getPost()).get());
		cRepo.save(comment);
	}
	
	//댓글수정
	@Transactional
	public void commentUpdate(int cno, String content) {
		cRepo.commentUpdate(cno, content);
	}

	//댓글삭제
	@Transactional
	public void commentDelete(int cno) {
		cRepo.deleteByCno(cno);
	}
	
	//댓글 조회
	@Transactional
	public List<CommentVO> getComment(int pno) {
		//DB에서 해당 게시글 데이터를 가져온다.
		Posts post = pRepo.findById(pno).get();
		
		List<CommentVO> commentList = new ArrayList<CommentVO>();
		cRepo.findByPost(post).forEach(comm->{
			commentList.add(new CommentVO(comm));
		});
		
		return commentList;
	}	
	
	//로그인한 유저의 총 댓글의 개수 받아오기
	@Transactional
	public int findAllMyCommentCnt(String userid) {
		return
		((Long) em.createQuery("select count(*) from Comment "
							 + "where member_userid = '" + userid + "'")
				  .getSingleResult())
				  .intValue();
	}
	
	//로그인한 유저의 댓글 리스트 받아오기
    @Transactional
	public List<Comment> findCommentListPaging(int startIndex,
											   int pageSize,
											   String userid) {
		return 
		em.createQuery("select c from Comment c "
					 + "where member_userid = '" + userid + "' "
					 + "order by c.cno DESC", Comment.class)
		  .setFirstResult(startIndex)
		  .setMaxResults(pageSize)
		  .getResultList();
	}
}