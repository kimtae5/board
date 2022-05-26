package com.kt5.board.persistence;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kt5.board.model.Board;

public interface BoardRepository extends JpaRepository<Board, Long>{
	
	//Board 테이블에서 데이터를 가져올 때 Member 정보도 같이 가져오는 메서드
	@Query("select b, w from Board b left join b.member w where b.bno =:bno")
	Object getBoardWithMember(@Param("bno") Long bno);
	
	//하나의 글번호를 가지고 게시글과 댓글을 모두 가져오는 메서드
	//하나의 게시글에 여러 개의 댓글이 있으므로 리턴 타입은 List<Object[]>
	//결과는 게시글, 댓글 + 게시글, 댓글의 형태로 리턴 됩니다.
	@Query("SELECT b, r FROM Board b LEFT JOIN Reply r ON r.board = b WHERE b.bno = :bno")
	List<Object[]> getBoardWithReply(@Param("bno") Long bno);
	
	//목록 보기를 위한 메서드
	@Query(value ="SELECT b, w, count(r) FROM Board b LEFT JOIN b.member w LEFT JOIN "
			+ "Reply r ON r.board = b GROUP BY b",	countQuery ="SELECT count(b) FROM Board b")
	Page<Object[]> getBoardWithReplyCount(Pageable pageable);

	//게시글 번호를 가지고 데이터를 찾아오는 메서드
	@Query(value ="SELECT b, w, count(r) FROM Board b LEFT JOIN b.member w LEFT JOIN "
			+ "Reply r ON r.board = b where b.bno = :bno")
	Object getBoardByBno(@Param("bno") Long bno);




}
