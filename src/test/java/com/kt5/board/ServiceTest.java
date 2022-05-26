package com.kt5.board;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kt5.board.dto.PageRequestDTO;
import com.kt5.board.dto.PageResultDTO;
import com.kt5.board.service.BoardService;

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
	
	@Test
	public void testModifyBoard() {
				
		BoardDTO boardDTO = BoardDTO.builder().bno(100L).title("제목 수정")
				.content("내용 수정").build();
		boardService.modifyBoard(boardDTO);
	}
	
	
}
