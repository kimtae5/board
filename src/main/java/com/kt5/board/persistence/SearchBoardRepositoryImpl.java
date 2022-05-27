package com.kt5.board.persistence;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.kt5.board.model.Board;
import com.kt5.board.model.QBoard;
import com.kt5.board.model.QMember;
import com.kt5.board.model.QReply;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class SearchBoardRepositoryImpl extends QuerydslRepositorySupport implements SearchBoardRepository {

	public SearchBoardRepositoryImpl() {
		super(Board.class);
	}

	@Override
	public Board search() {
		//쿼리를 수행할 수 있는 Querydsl객체를 찾아옴
		QBoard board = QBoard.board;
		QMember member = QMember.member;
		QReply reply = QReply.reply;
		
		/* 결과를 Board Entity으로 받음
		//Query겍체를 생성
		JPQLQuery<Board> jpqlQuery = from(board);
		
		//member와 join
		jpqlQuery.leftJoin(member).on(board.member.eq(member));
		//reply와 join
		jpqlQuery.leftJoin(reply).on(reply.board.eq(board));
		
		//필요한 데이터를 조회하는 구문을 추가
		jpqlQuery.select(board,member.email, reply.count()).groupBy(board);
		
		//결과 가져오기
		List<Board> result = jpqlQuery.fetch();
		
		System.out.println(result);
		*/
		
		//결과를 tuple로 받기
		//tuple은 관걔형 데이터베이스에서 하나의 행을 지칭하는 용어
		//프로그래밍에서는 일반적으로 여러 종류의 데이터가 묶여서하나의 데이터를 나타내는 자료형
		//Map과 다른 점은 Map은 key로 세부 데이터를 접근하지만 Tuple은 인덱스로도 접근이 가능하고
		//대부분의 경우 Tuple은 수정이 불가능
		JPQLQuery<Board> jpqlQuery = from(board);
		//member와 join
		jpqlQuery.leftJoin(member).on(board.member.eq(member));
		//reply와 join
		jpqlQuery.leftJoin(reply).on(reply.board.eq(board));
		
		JPQLQuery<Tuple> tuple = jpqlQuery.select(board, member.email, reply.count());
		tuple.groupBy(board);
		
		//결과 가져오기
		List<Tuple> result = tuple.fetch();
		
		
		System.out.println(result);
		
		
		return null;
		
		

	}

	@Override
	public Page<Object[]> searchPage(String type, String keyword, Pageable pageable) {
		log.info("searchPage.............................");
		QBoard board = QBoard.board;
		QReply reply = QReply.reply;
		QMember member = QMember.member;
		JPQLQuery<Board> jpqlQuery = from(board);
		jpqlQuery.leftJoin(member).on(board.member.eq(member));
		jpqlQuery.leftJoin(reply).on(reply.board.eq(board));
		//SELECT b, w, count(r) FROM Board b
		//LEFT JOIN b.writer w LEFT JOIN Reply r ON r.board = b
		JPQLQuery<Tuple> tuple = jpqlQuery.select(board, member, reply.count());
		//동적인 쿼리 수행을 위한 객체 생성
		BooleanBuilder booleanBuilder = new BooleanBuilder();
		//bno가 0보다 큰 데이터를 추출하는 조건
		BooleanExpression expression = board.bno.gt(0L);
		//booleanBuilder.and(expression);
		
		//type이 검색항목
		if(type != null){
			String[] typeArr = type.split("");
			//검색 조건을 작성하기
			BooleanBuilder conditionBuilder = new BooleanBuilder();
			for (String t:typeArr) {
				switch (t){
					case "t":conditionBuilder.or(board.title.contains(keyword));
					break;
					case "c":conditionBuilder.or(board.content.contains(keyword));
					break;
					case "w":conditionBuilder.or(member.email.contains(keyword));
					break;
				}
			}
			booleanBuilder.and(conditionBuilder);
		}
		
		//조건적용
		tuple.where(booleanBuilder);
		//데이터 정렬 order by
		//정렬 조건 가져오기
		//tuple.orderBy(board.bno.desc());  하나의 조건으로만 정렬
		Sort sort = pageable.getSort();
		sort.stream().forEach(order -> {
			Order direction = order.isAscending()? Order.ASC: Order.DESC;
			String prop = order.getProperty();
			PathBuilder orderByExpression = new PathBuilder(Board.class, "board");
			tuple.orderBy(new OrderSpecifier(direction, orderByExpression.get(prop)));
		});
		//그룹화
		tuple.groupBy(board);
		//페이징 처리
		tuple.offset(pageable.getOffset());
		tuple.limit(pageable.getPageSize());
		
		//결과를 가져오기
		List<Tuple> result = tuple.fetch();
		log.info(result);
		log.info("COUNT: " +tuple.fetchCount());
		return new PageImpl<Object[]>(
			result.stream().map(t -> t.toArray()).collect(Collectors.toList()),	pageable, tuple.fetchCount());
		
	}

}
