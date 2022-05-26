package com.kt5.board.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kt5.board.model.Reply;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
	

}
