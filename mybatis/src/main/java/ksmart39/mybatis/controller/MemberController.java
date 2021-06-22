package ksmart39.mybatis.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ksmart39.mybatis.domain.Member;
import ksmart39.mybatis.service.GoodsService;
import ksmart39.mybatis.service.MemberService;

@Controller
public class MemberController {
	/**
	 * 필드 주입방식 (DI)
	 * MemberService memberService = new MemberService();   ->인터페이스라 이런식으로는 불가능하다. , 이미 @Controller 붙이는 순간 객체는 생성된거고 그 Bean을 주입시켜준 것일 뿐이다.(Run as->main 메소드로 실행될 때 객체생성 @Controller 이걸 만나면 컴파일 할 때 객체를 만들어놔야겠다는걸 알게 된다.) 
	 * 
	 * setter 주입방식
	 * setter 메소드 MemberService
	 * 
	 * 생성자메소드 주입방식
	 * 생성자메소드 MemberService
	 */
	
	/**
	 * 필드 주입방식
	 * //DI할 때 클래스간 연관관계 맺어줄 때 사용 @Autowired 를
	 * 
	 * @Autowired
	 * private MemberService memberService;
	 * @Autowired
	 * private GoodsService goodsService;
	 */
	
	
	/**
	 * setter 메소드 주입방식
	 * private MemberService memberService;
	 * private GoodsService goodsService;
	 * 
	 * @Autowired 
	 * public void setMemberService(MemberService memberService , GoodsService goodsService) {//, GoodsService goodsService 두개 이상일 때 이렇게 추가
	 * this.memberService = memberService; 
	 * this.goodsService= goodsService //2개 이상일 때
	 * }
	 */
	
	//LOG
	//메서드 안에서만 log찍을수 있는거다!
	//log치고 ctrl+space->logger
	private static final Logger log = LoggerFactory.getLogger(MemberController.class);
	
	
	
	//생성자 메서드 주입방식 2개 이상 할거면
	  private final MemberService memberService;
	  private final GoodsService goodsService;  //노랑 경고 not used는 호출해서 사용하지 않았다는 뜻이당....
	 
	
	
	//생성자 메소드 주입방식      (final 매우 중요! 순환참조 방지)
	//private final MemberService memberService;
	
	//spring framework 4.3이부터는 @Autowired 생략가능
	//Maven Dependencies쪽에 가서 라이브러리 보니까 spring-core 5.3.72네?
	@Autowired//일부러 남겨뒀다... 주석처리하면 신경 안쓸까봐....
	public MemberController(MemberService memberService,GoodsService goodsService) {  
		this.memberService = memberService;
		this.goodsService=goodsService;  //2개이상일 때
	}
	
	
	
	@PostConstruct
	public void memberControllerInit() {
		log.info("================================================================");
		log.info("MemberController 객체생성");
		log.info("================================================================");
		
		/*
		System.out.println("=====================================================");
		System.out.println("MemberController 객체생성");
		System.out.println("=====================================================");
		*/
	}
	
	//@ResponseBody 이게 있어야 json방식으로 넣어줄 수 있다.
	@PostMapping("/memberIdCheck")
	//메서드 훼손하기 싫으면 여기다가 @ResponseBody 쓰고 아래의 @ResponseBody는 지우면 된다.
	public @ResponseBody boolean memberIdCheck(@RequestParam(name="memberId",required = false)String memberId) {
		boolean idCheck =true;
		log.info("memberIdCheck memberId ::::: 		{}",memberId);
		//id check가 성공 == 중복된 아이디가 있다는 뜻 -> 이런 경우에는 false
		//idCheck 중복된 아이디있는 경우에는 false
		
		////추가한 부분 210615   //dao, mapper만들어서도 할 수 있다.(이번에는 기존에 있었던걸 잘 사용했을 뿐)-> 여러번 만들고 내걸로 만들어야!
		Member member = memberService.getMemberInfoById(memberId);
		
		if(member != null) idCheck =false;
		////추가한 부분 210615
		
		
		return idCheck;
	}
	
	
	
	
	
	
	//로그아웃은 세션이 노필요 그러므로 바로 컨트롤러로~
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate(); //세션을 초기화하겠다는 말
		return "redirect:/";
	}
	
	
	
	//로그인 처리
	@PostMapping("/login")
	public String login(@RequestParam(value="memberId",required=false)String memberId
						,@RequestParam(value="memberPw",required = false)String memberPw
						,HttpSession session 
						,RedirectAttributes reAttr) { //required = true면 생략 가능   , 밑에 if문(유효성검사) 쓰고 true에서 false해도 된다...  // HttpSession도 되지만 HttpServletRequest request 한 뒤 메서드내에서 request.get~~Session해도 된다.
		if(memberId != null && !"".equals(memberId) && memberPw != null && !"".equals(memberPw)) {
			
			Map<String,Object> resultMap= memberService.loginMember(memberId, memberPw);
						
			boolean loginCheck = (boolean)resultMap.get("loginCheck");
			Member loginMember = (Member)resultMap.get("loginMember");		
			
			if(loginCheck) {
				
				session.setAttribute("SID", loginMember.getMemberId());
				session.setAttribute("SNAME", loginMember.getMemberName());
				session.setAttribute("SLEVEL", loginMember.getMemberLevel());
				if("1".equals(loginMember.getMemberLevel())) {
					session.setAttribute("SLEVELNAME", "관리자");
				}else if("2".equals(loginMember.getMemberLevel())) {
					session.setAttribute("SLEVELNAME", "판매자");
				}else {
					session.setAttribute("SLEVELNAME", "구매자");
				}
				//System.out.println(memberId);
				return "redirect:/";
			}
		}
		
		reAttr.addAttribute("loginResult","등록된 회원이 없습니다.");  //"redirect:/login?loginResult=등록된 회원이 없습니다.";
		
		return "redirect:/login";
	}
	
	//로그인 화면
	@GetMapping("/login")
	public String login(Model model
						,@RequestParam(name="loginResult",required = false)String loginResult) {
		
		model.addAttribute("title","로그인화면");
		if(loginResult!=null) model.addAttribute("loginResult",loginResult);
		return "login/login";
	}
	
	
	
	//삭제 적용
	@PostMapping("/removeMember")
	public String removeMember(@RequestParam(value="memberId",required = false)String memberId
								,@RequestParam(value="memberPw",required = false)String memberPw
								,RedirectAttributes redirectAttr) {
		
		log.info("================================================================");
		log.info("화면에서 입력받은 값(회원탈퇴): {}"+memberId);
		log.info("화면에서 입력받은 값(회원탈퇴): {}"+memberPw);
		log.info("================================================================");
		/*
		System.out.println("=====================================================");
		System.out.println("화면에서 입력받은 값(회원탈퇴): "+memberId);
		System.out.println("화면에서 입력받은 값(회원탈퇴): "+memberPw);
		System.out.println("=====================================================");
		*/
		if(memberPw != null && !"".equals(memberPw)) {
			
			boolean result=memberService.removeMember(memberId, memberPw);
			if(result) {
				return "redirect:memberList";
			}
		}
		redirectAttr.addAttribute("memberId",memberId);
		redirectAttr.addAttribute("result","삭제 실패");
		
		return "redirect:/removeMember";
	}
	
	//회원삭제폼
	@GetMapping("/removeMember")
	public String remobeMember(@RequestParam(value="memberId",required=false)String memberId
			,@RequestParam(value="result",required=false)String result
			, Model model) {
		
		log.info("================================================================");
		log.info("화면에서 입력받은 값(회원탈퇴폼): {}"+memberId);
		log.info("================================================================");
		/*
		System.out.println("=====================================================");
		System.out.println("화면에서 입력받은 값(회원탈퇴폼): "+memberId);
		System.out.println("=====================================================");
		*/
		model.addAttribute("title","회원탈퇴폼");
		model.addAttribute("memberId",memberId);
		
		if(result!=null)model.addAttribute("result",result);
		
		return "member/removeMember";
	}
	
	
	//수정한 뒤 의 컨트롤러(수정폼에서 수정뒤 리스트화면으로 가기위해 구현)
	@PostMapping("/modifyMember")
	public String modifyMember(Member member) {
		
		log.info("================================================================");
		log.info("화면에서 입력받은 값(수정화면폼): {}"+member);
		log.info("================================================================");
		/*
		System.out.println("=====================================================");
		System.out.println("화면에서 입력받은 값(수정화면폼): "+member);
		System.out.println("=====================================================");
		*/
		memberService.modifyMember(member);
		
		return "redirect:/memberList";
	}
		
	/**
	 * @RequestParam(name="memberId",required=false) = request.getParameter("memberId");
	 * @RequestParam(name="memberId",required=false) String memberId
	 *								==
	 * String memberId =request.getParameter("memberId");
	 * @return
	 */
	//수정폼만들기
	@GetMapping("/modifyMember")
	public String modifyMember(@RequestParam(name = "memberId",required = false)String memberId
			, Model model) {
		
		log.info("================================================================");
		log.info("화면에서 입력받은 값(회원수정폼): {}" +memberId);
		log.info("================================================================");
		/*
		System.out.println("=====================================================================");
		System.out.println("화면에서 입력받은 값(회원수정폼):" +memberId);
		System.out.println("=====================================================================");
		*/
		//1.화면에 전달할 때, 회원 아이디로 회원테이블을 조회한 Member객체
		Member member = memberService.getMemberInfoById(memberId);
		//2.Model 화면에 전다할 객체 삽입 (addAttribute)
		model.addAttribute("title","회원수정폼");
		model.addAttribute("member",member);
		
		return "member/modifyMember";
	}
	
	
	
	
	
	
	/**
	 * 
	 * @param memberId, memberPw, ...등등등등=> Member dto의 멤버변수와 이름이 같다면 스프링이 알아서 바인딩(담아준다)해줍니다.
	 * 		  커맨드객체(Member)
	 * @return 
	 */
	//@GetMapping("/addMember") 작성 한 다음 작성함
	@PostMapping("/addMember")	
	public String addMember(Member member) {
		
		log.info("================================================================");
		log.info("화면에 입력 받은 값(회원가입) {}"+member);
		log.info("================================================================");
		
		/*
		System.out.println("======================================================================");
		System.out.println("화면에 입력 받은 값(회원가입)"+member);
		System.out.println("======================================================================");
		*/
		memberService.addMember(member);
		
		return "redirect:/memberList";
	}
	
	//addMember.html작성 후에 여기서 작성함
	@GetMapping("/addMember")
	public String addMember(Model model) {
		
		model.addAttribute("title","회원가입폼");
		
		return "member/addMember";
	}
	
	
	
	@GetMapping("/memberList")  //전체회원검색 경로 보여주기(내가 임의로 이름 정한거) /memberList 이란 이름의 주소가 있으면 아래 실행시켜줘
	public String getMemberList(Model model
								,@RequestParam(name="searchKey",required =false )String searchKey
								,@RequestParam(name="searchValue",required = false)String searchValue) {
		/* 이렇게 사용하면 스프링부트 쓸 이유가 없는 것이다. 왜냐? 내가 직접하는거니까...
		 * MemberService member =new MemberService();
		 * member.getMemberList();  
		 */
		
		log.info("================================================================");
		log.info("memberList : {}"+ searchKey);
		log.info("memberList : {}"+ searchValue);		
		log.info("================================================================");
		/*
		System.out.println("=========================================");
		System.out.println("memberList : "+ searchKey);
		System.out.println("memberList : "+ searchValue);
		System.out.println("=========================================");
		*/
		//map을 활용해서 검색 키워드 정리
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("searchKey",searchKey);
		paramMap.put("searchValue",searchValue);
		
		
		
		
		List<Member> memberList =memberService.getMemberList(paramMap); //paramMap 추가
		
		
		log.info("================================================================");
		log.info("memberList : {}"+ memberList);
		log.info("================================================================");
		
		/*
		System.out.println("=========================================");
		System.out.println("memberList : "+ memberList);
		System.out.println("=========================================");
		*/
		model.addAttribute("title","회원목록");
		model.addAttribute("memberList", memberList);
		//return "redirect:/";  //이렇게하면 02.전체화면 눌렀을 시 흰 화면이고 콘솔에 DB에 있는 정보를 List로 쭈욱 보여줌
		return "member/memberList";
	}
}
