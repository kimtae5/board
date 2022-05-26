package com.kt5.board.persistence;


import org.springframework.data.jpa.repository.JpaRepository;

import com.kt5.board.model.Member;

public interface MemberRepository extends JpaRepository<Member, String> {
	

}
