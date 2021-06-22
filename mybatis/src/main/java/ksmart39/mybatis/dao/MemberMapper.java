package ksmart39.mybatis.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import ksmart39.mybatis.domain.Member;

@Mapper
public interface MemberMapper {
	
	//회원 목록 조회
	public List<Member> getMemberList(Map<String,Object> paramMap);  //Map<String,Object> paramMap 추가
	//해쉬맵 혹은 searchDTO 만들면된다.... MemberMapper.xml에서는 파라미터타입은 하나만 받을 수 있으니까
	
	//회원 가입
	public int addMember(Member member);
	
	//회원정보 조회(수정폼)
	public Member getMemberInfoById(String memberId);
	
	//회원 정보 수정
	public int modifyMember(Member member);
	
	
	
	
	
	//회원테이블
	public int removeMemberById(String memberId);
	//상품테이블
	public int removeGoodsById(String memberId);
	//주문테이블(권한:구매자)
	public int removeOrderById(String memberId);
	//주문테이블(권한:판매자)
	public int removeOrderBySellerId(String memberId);
	//로그인테이블
	public int removeLoginById(String memberId);
	
	
}
