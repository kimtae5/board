package com.kt5.board.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kt5.board.dto.ReplyDTO;
import com.kt5.board.service.ReplyService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/replies/")
@Log4j2
@RequiredArgsConstructor
public class ReplyController {

	private final ReplyService replyService;//자동주입을 위해 final
	
	//댓글 가져오기
	@GetMapping(value = "/board/{bno}", produces =	MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ReplyDTO>> getListByBoard(@PathVariable("bno") Long bno){
		
		log.info("bno: " + bno);
		List<ReplyDTO> list = replyService.getList(bno);
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
	
	//댓글 삽입
	@PostMapping("")
	public ResponseEntity<Long> register(@RequestBody ReplyDTO replyDTO){
		log.info("ReplyDTO : " + replyDTO);
		Long rno = replyService.register(replyDTO);
		return new ResponseEntity<>(rno, HttpStatus.OK);
	}
	
	//댓글 삭제
	@DeleteMapping("/{rno}")
	//클라이언트에서 Json 형태로 보낸 문자열을 ReplyDTO로 변경해서 저장
	public ResponseEntity<String> remove(@PathVariable("rno") Long rno) {
	log.info("rno:" + rno );
	replyService.remove(rno);
	return new ResponseEntity<>("Success", HttpStatus.OK);
	}
	
	//댓글 수정
	@PutMapping("/{rno}")
	public ResponseEntity<String> modify(@RequestBody ReplyDTO replyDTO) {
	log.info("replyDTO:" + replyDTO);
	replyService.modify(replyDTO);
	return new ResponseEntity<>("Success", HttpStatus.OK);
	}
	
	
	
	
	
}
