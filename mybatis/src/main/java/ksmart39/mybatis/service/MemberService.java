package ksmart39.mybatis.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ksmart39.mybatis.dao.MemberMapper;
import ksmart39.mybatis.domain.Member;

@Service
@Transactional  //트랜젝션 수행해줌. 클래스 위에 선언되어 있기 때문에 클래스내 각 메소드단위로 적용
public class MemberService {
	/**
	 * 필드 주입방식 (DI)
	 * MemberMapper memberMapper = new MemberMapper();
	 * setter 메소드 memberMapper
	 * 생성자메소드 memberMapper
	 */
	
	/** 
	 * 필드 주입방식
	 * @Autowired 
	 * private MemberMapper memberMapper;
	 */
	/**
	 * 
	 * 
	 * setter 주입방식
	 * private MemberMapper memberMapper;
	 * @Autowired
	 * public void setMemberMapper(MemberMapper memberMapper) {
	 *	this.memberMapper=memberMapper;
	 * }
	*/
	
	//생성자 주입방식
	private final MemberMapper memberMapper;
	@Autowired
	public MemberService(MemberMapper memberMapper) {
		this.memberMapper=memberMapper;
	}
	
	
	private static final Logger log = LoggerFactory.getLogger(MemberService.class);

	
	@PostConstruct
	public void memberServiceInit() {
		log.info("================================================================");
		log.info("MemberService 객체생성");
		log.info("================================================================");
		
		/*
		System.out.println("====================================");
		System.out.println("MemberService 객체 생성");
		System.out.println("====================================");
		*/
	}
	
	
	//로그인
	public Map<String,Object> loginMember(String memberId, String memberPw) {
		//로그인 여부
		boolean loginCheck =false;
		//로그인 결과 를 돕는 Map 
		Map<String,Object> memberInfoMap = new HashMap<String,Object>();
		
		//로그인 처리
		Member member = memberMapper.getMemberInfoById(memberId);
	
		if(member!=null && memberPw.equals(member.getMemberPw())) {
			loginCheck=true;
			memberInfoMap.put("loginMember", member);
		}
		
		memberInfoMap.put("loginCheck", loginCheck);
		
		return memberInfoMap;
	}
	
	
	
	
	
	
	//삭제하기
	public boolean removeMember(String memberId, String memberPw) {//Member member로 받아도 되고  int->boolean으로
		//삭제 여부에 대한 boolean타입 만들기
		boolean removeCheck = false;
		
		//비밀번호가 맞으면 삭제가 될 수 있게
		Member member = memberMapper.getMemberInfoById(memberId);
		if(member!=null && memberPw.equals(member.getMemberPw())) {
			//삭제프로세스
			
			//판매자
			if("2".equals(member.getMemberLevel())) {
				//1. 주문, 상품
				//memberMapper.removeLoginById(memberId);//로그인이력
				memberMapper.removeOrderBySellerId(memberId);
				memberMapper.removeGoodsById(memberId);
			}
			
			//구매자
			if("3".equals(member.getMemberLevel())) {
				//1. 주문
				//memberMapper.removeLoginById(memberId);//로그인이력
				memberMapper.removeOrderById(memberId);
			}
			//로그인 이력
			memberMapper.removeLoginById(memberId);
			//회원
			memberMapper.removeMemberById(memberId);
			
			
			removeCheck = true;
		}
		
		return removeCheck;
	}
	
	
	
	//업데이트
	public int modifyMember(Member member) {
		
		return memberMapper.modifyMember(member);
	}
	
	
	//수정폼(아이디 하나만 받아서 나머지 정보를 수정폼에다가 넣어야되므로)
	public Member getMemberInfoById(String memberId) {
		
		return memberMapper.getMemberInfoById(memberId);
	}
	
	//회원조회
	public List<Member> getMemberList(Map<String,Object> paramMap){ //Map<String,Object> paramMap 추가
		
		List<Member> memberList =memberMapper.getMemberList(paramMap); //paramMap 추가
		
		
		return memberList;
	}
	
	
	//회원가입
	public int addMember(Member member) {
		int result = memberMapper.addMember(member);
		return result;
		//return memberMapper.addMember(member);   => 위에 두줄을 한줄로 쓴다면 이렇게 쓰면 된다.
	}
	
	
	
	
	
	
	
	
	
}
