package com.kt5.board;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.kt5.board.model.Board;
import com.kt5.board.model.Member;
import com.kt5.board.model.Reply;
import com.kt5.board.persistence.BoardRepository;
import com.kt5.board.persistence.MemberRepository;
import com.kt5.board.persistence.ReplyRepository;

@SpringBootTest
public class RepositoryTest {
	
	@Autowired
	private MemberRepository memberRepository;
	
	//@Test
	public void insertMembers() {
		IntStream.rangeClosed(1, 100).forEach(i -> {
			Member member = Member.builder().email("user" + i + "@kt5.com")
			.password("1111").name("사용자" + i).build();
			memberRepository.save(member);
		});
	}
	
	@Autowired
	private BoardRepository boardRepository;
	
	//@Test
	public void insertBoard() {
		IntStream.rangeClosed(1,100).forEach(i -> {
			Member member = Member.builder().email("user1@kt5.com").build();
			Board board = Board.builder().title("제목..."+i).content("내용...." + i).member(member)
					.build();
			boardRepository.save(board);
		});
	}
	
	@Autowired
	private ReplyRepository replyRepository;

	//@Test
	public void insertReply() {
		IntStream.rangeClosed(1, 300).forEach(i -> {
			//1부터 100까지의 임의의 번호
			long bno = (long)(Math.random() * 100) + 1;
			Board board = Board.builder().bno(bno).build();
			Reply reply = Reply.builder().text("댓글......." +i).board(board).replyer("guest" + bno).build();
			replyRepository.save(reply);
		});
	}
	
	//하나의 Board데이터를 조회하는 메서드
	@Transactional
	//@Test
	public void readBoard() {
		Optional<Board> result = boardRepository.findById(100L);
		//데이터를 출력
		System.out.println(result.get());
		System.out.println(result.get().getMember());
	}
	
	//하나의 Reply데이터를 조회하는 메서드
	@Transactional
	//@Test
	public void readReply() {
		Optional<Reply> result = replyRepository.findById(717L);
		//데이터를 출력
		System.out.println(result.get());
		System.out.println(result.get().getBoard());
	}
	
	
	//@Test
	public void testReadWithWriter() {
		//데이터 조회
		Object result = boardRepository.getBoardWithMember(100L);
		//JPQL의 결과가 Object 인경우는 Object[]로 강제 형 변환해서 사용
		System.out.println("-------------------------------------------------- ");
		System.out.println((Object[])result);
		System.out.println(Arrays.toString((Object[])result));
		System.out.println("-------------------------------------------------- ");
	}

	//@Test
	public void testGetBoardWithReply() {
		List<Object[]> result = boardRepository.getBoardWithReply(40L);
		for(Object[] ar : result) {
			System.out.println(Arrays.toString(ar));
		}
	}
	
	//@Test
	public void testWithReplyCount(){
		Pageable pageable = PageRequest.of(0,10, Sort.by("bno").descending());
		Page<Object[]> result =	boardRepository.getBoardWithReplyCount(pageable);
		result.get().forEach(row -> {
			Object[] ar = (Object[])row;
			System.out.println(Arrays.toString(ar));
		});
	}
	
	@Test
	public void testWithBno(){
		Object result = boardRepository.getBoardByBno(20L);
		Object[] ar = (Object[])result;
		System.out.println(Arrays.toString(ar));
	}
	
	
	
	

}
