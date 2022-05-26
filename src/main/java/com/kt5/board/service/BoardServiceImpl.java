
package com.kt5.board.service;

import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.kt5.board.BoardDTO;
import com.kt5.board.dto.PageRequestDTO;
import com.kt5.board.dto.PageResultDTO;
import com.kt5.board.model.Board;
import com.kt5.board.model.Member;
import com.kt5.board.persistence.BoardRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class BoardServiceImpl implements BoardService {
	private final BoardRepository boardRepository;
	
	@Override
	public Long register(BoardDTO dto) {
		log.info(dto);
		
		Board board = dtoToEntity(dto);
		boardRepository.save(board);
		return board.getBno();
	}

	@Override
	public PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {
		
		log.info(pageRequestDTO);
		
		//Entity를 DTO로 변환해주는 함수 생성
		//Repository의 메서드의 결과가 Object[]인데 이 배열의 요소를 가지고
		//BoardDTO를 생성해서 출력해야함
		Function<Object[], BoardDTO> fn = (en -> entityToDTO((Board)en[0],(Member)en[1],(Long)en[2]));
		
		//데이터를 조회 - bno의 내림차순 적용
		//상황에 따라서는 regDate 나 modDate로 정렬하는 경우도 있음
		Page<Object[]> result = boardRepository.getBoardWithReplyCount(pageRequestDTO
				.getPageable(Sort.by("bno").descending()));
		
		return new PageResultDTO<>(result, fn);
		
	}

	@Override
	public BoardDTO getBoard(Long bno) {
		Object result = boardRepository.getBoardByBno(bno);
		Object [] ar = (Object []) result;
		
		return entityToDTO((Board) ar[0], (Member)ar[1], (Long)ar[2]);
	}

}
