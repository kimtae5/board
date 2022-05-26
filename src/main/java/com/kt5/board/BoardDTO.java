package com.kt5.board;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class BoardDTO {
	private Long bno;
	private String title;
	private String content;
	private LocalDateTime regDate;
	private LocalDateTime modDate;
	
	//작성자 정보
	private String memberEmail; //작성자의 이메일(id)
	private String memberName; //작성자의 이름
	
	//댓글의 개수
	private int replyCount; //해당 게시글의 댓글 수


}
