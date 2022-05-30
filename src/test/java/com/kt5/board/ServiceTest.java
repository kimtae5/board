package com.kt5.board;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kt5.board.dto.PageRequestDTO;
import com.kt5.board.dto.PageResultDTO;
import com.kt5.board.dto.ReplyDTO;
import com.kt5.board.service.BoardService;
import com.kt5.board.service.ReplyService;

@SpringBootTest
public class ServiceTest {

	@Autowired
	private BoardService boardService;
	
	//@Test
	public void testRegister() {
		BoardDTO dto = BoardDTO.builder().title("test").content("test...")
				.memberEmail("user1@kt5.com").build();
		
		Long bno = boardService.register(dto);
		System.out.println(bno);
	}
	
	//@Test
	public void testList() {
		//1페이지 10개
		PageRequestDTO pageRequestDTO = new PageRequestDTO();
		
		PageResultDTO<BoardDTO, Object[]> result =	boardService.getList(pageRequestDTO);
		
		for (BoardDTO boardDTO : result.getDtoList()) {
			System.out.println(boardDTO);
		}
	}
	
	//@Test
	public void testGetBoard() {
		Long bno = 40L;
		BoardDTO boardDTO = boardService.getBoard(bno);
		System.out.println(boardDTO);
	}
	
	//@Test
	public void testDeleteBoard() {
		Long bno = 99L;
		boardService.removeWithReplies(bno);
	}
	
	//@Test
	public void testModifyBoard() {
				
		BoardDTO boardDTO = BoardDTO.builder().bno(100L).title("제목 수정")
				.content("내용 수정").build();
		boardService.modifyBoard(boardDTO);
	}
	
	@Autowired
	private ReplyService replyService;
	
	//댓글 목록 가져오기 테스트 
	@Test
	public void testGetList() {
		Long bno = 1L;//데이터베이스에 존재하는 번호
		List<ReplyDTO> replyDTOList = replyService.getList(bno);
		replyDTOList.forEach(replyDTO -> System.out.println(replyDTO));
	}
	
}
