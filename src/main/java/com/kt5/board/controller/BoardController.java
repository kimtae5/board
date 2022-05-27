package com.kt5.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kt5.board.BoardDTO;
import com.kt5.board.dto.PageRequestDTO;
import com.kt5.board.service.BoardService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@Log4j2
@RequiredArgsConstructor
public class BoardController {
	private final BoardService boardService;
	
	//목록 보기 요청을 처리할 메서드
	@GetMapping({"/", "/board/list"})
	public String list(PageRequestDTO pageRequestDTO, Model model){
	log.info("목록보기 요청.." + pageRequestDTO);
	//서비스 메서드를 호출해서 결과를 저장
	model.addAttribute("result", boardService.getList(pageRequestDTO));
	return "board/list";
	}
	
	//게시물 작성을 처리할 메서드
	@GetMapping("/board/register")
	public void register(){
		log.info("게시물 등록으로 이동");
	}
	
	@PostMapping("/board/register")
	public String register(BoardDTO dto, RedirectAttributes redirectAttributes){
		log.info("게시물 처리중.." + dto);
		//새로 추가된 Entity의 번호 , 게시물 등록
		Long bno = boardService.register(dto);
		log.info("BNO: " + bno);
		//view 에 데이터 전달
		redirectAttributes.addFlashAttribute("msg", bno + " 삽입 성공");
		return "redirect:/board/list";
	}
	
	//상세보기 처리를 위한 메서드
	@GetMapping({"/board/read", "/board/modify"})
	//@ModelAttribute("이름") 파라미터를 받아서 이름으로 다음 요청에게 넘겨주는 역할을 수행
	public void read(@ModelAttribute("requestDTO") PageRequestDTO pageRequestDTO, Long bno, Model model){
		log.info("상세보기 처리중 bno: " + bno);
		log.info("상세보기 처리중 pageRequestDTO: " + pageRequestDTO);

		BoardDTO boardDTO = boardService.getBoard(bno);
		log.info("boardDTO : " + boardDTO);
		model.addAttribute("dto", boardDTO);
	}
	
	//수정을 처리할 메서드
	@PostMapping("/board/modify")
	//수정은 이전에 보고 있던 목록 보기로 돌아갈수 있어야 하기 때문에 목록 보기에 필요한 데이터가 필요
	public String modify(BoardDTO dto, @ModelAttribute("requestDTO") PageRequestDTO requestDTO,
	RedirectAttributes redirectAttributes){
		log.info("수정 처리중... dto : " + dto);
		log.info("수정 처리중... requestDTO : " + requestDTO);
		
		boardService.modifyBoard(dto);
		redirectAttributes.addAttribute("page",requestDTO.getPage());
		redirectAttributes.addAttribute("type",requestDTO.getType());
		redirectAttributes.addAttribute("keyword",requestDTO.getKeyword());
		redirectAttributes.addAttribute("bno",dto.getBno());
		return "redirect:/board/read";
	}
	
	//삭제를 처리할 메서드
	@PostMapping("/board/remove")
	public String remove(long bno, RedirectAttributes redirectAttributes){
		log.info("삭제처리... bno: " + bno);
		boardService.removeWithReplies(bno);
		redirectAttributes.addFlashAttribute("msg", bno +" 번 삭제 완료");
		return	"redirect:/board/list";
	}
	
	
	
	
}