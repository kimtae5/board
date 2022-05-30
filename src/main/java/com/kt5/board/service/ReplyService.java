package com.kt5.board.service;

import java.util.List;

import com.kt5.board.dto.ReplyDTO;
import com.kt5.board.model.Board;
import com.kt5.board.model.Reply;

public interface ReplyService {
	public Long register(ReplyDTO replyDTO); //댓글의 등록
	
	//게시글 번호를 이용해서 댓글의 목록을 가져오는 메서드
	public List<ReplyDTO> getList(Long bno); //특정 게시물의 댓글 목록
	
	public void modify(ReplyDTO replyDTO); //댓글 수정 하는 메서드
	
	public void remove(Long rno); //댓글 삭제하느 메서드

	//ReplyDTO 객체를 Reply 객체로 변환하는 메서드
	default Reply dtoToEntity(ReplyDTO replyDTO){
		//ReplyDTO는 bno를 가지고 있지만 Reply Entity는 Board를 가지고 있음.
		Board board = Board.builder().bno(replyDTO.getBno()).build();
		Reply reply = Reply.builder().rno(replyDTO.getRno()).text(replyDTO.getText())
		.replyer(replyDTO.getReplyer()).board(board).build();
		return reply;
		}
	
	//Reply Entity를 ReplyDTO 객체로 변환하는 메서드
	default ReplyDTO entityToDTO(Reply reply) {
		ReplyDTO dto = ReplyDTO.builder().rno(reply.getRno()).text(reply.getText())
		.replyer(reply.getReplyer()).regDate(reply.getRegDate()).modDate(reply.getModDate()).build();
		return dto;
	}
}
