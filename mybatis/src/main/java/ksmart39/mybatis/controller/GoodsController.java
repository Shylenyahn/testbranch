package ksmart39.mybatis.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import ksmart39.mybatis.domain.Goods;
import ksmart39.mybatis.service.GoodsService;
import ksmart39.mybatis.service.MemberService;

@Controller
public class GoodsController {
	private final GoodsService goodsService;
	
	//log4j
	private static final Logger log = LoggerFactory.getLogger(GoodsController.class);

	
	//@Autowired //선생님은 생략함... 아마 여기에 오토와일드 쓰는거겠지?
	public GoodsController(GoodsService goodsService) {
		this.goodsService = goodsService;
	}
	
	//수정폼 만들어봅시다
	@GetMapping("/modifyGoods")
	public String modifyGoods(@RequestParam(name="goodsCode",required = false)String goodsCode,Model model) {
		
		log.info("================================================================");
		log.info("화면에서 입력받은 값(회원수정폼): {}" +goodsCode);
		log.info("================================================================");
		
		Goods goods = goodsService.getGoodsInfoByCode(goodsCode);
		model.addAttribute("title", "상품수정폼");
		model.addAttribute("goods", goods);
		
		return "goods/modifyGoods";
	}
	
	//수정 
	@PostMapping("/modifyGoods")
	public String modifyGoods(Goods goods) {
		log.info("================================================================");
		log.info("화면에서 입력받은 값(수정화면폼): {}"+goods);
		log.info("================================================================");
		
		goodsService.modifyGoods(goods);
		
		return "redirect:/goodsList";
	}
	
	
	
	//등록폼
	@GetMapping("/addGoods")
	public String addGoods(Model model) {
		model.addAttribute("title", "상품등록");
		return "goods/addGoods";
	}
	
	//등록
	@PostMapping("/addGoods")
	public String addGoods(Goods goods,HttpSession session,RedirectAttributes reAttr) {
		String goodsSellerId = (String)session.getAttribute("SID");
		//1. 등록 
		if(goodsSellerId != null) {
			goods.setGoodsSellerId(goodsSellerId);
			goodsService.addGoods(goods);
			reAttr.addAttribute("memberId",goodsSellerId );
		}
		//2. 등록 후에
		return "redirect:/goodsList";
	}
	
	
	//goodsList
	@GetMapping("/goodsList") //@ResponseBody 이걸 붙이니까 goods/goodsList 경로 이름만 나오더라...
	public String getGoodsList(Model model,@RequestParam(name="memberId",required = false)String memberId) {
		///////생각 못한 부분
		Map<String,Object> paramMap= new HashMap<String,Object>();
		
		if(memberId != null)paramMap.put("memeberId", memberId);
		//////생각 못한부분
		
		List<Goods> goodsList= goodsService.getGoodsList(paramMap); //안의 파라미터 생각 못함  //넣으려면 서비스단에  List<Goods> getGoodsList()  -> List<Goods> getGoodsList(Map<String, Object> paramMap)로 바뀌어야 된다.
		
		model.addAttribute("title","상품목록");
		model.addAttribute("goodsList",goodsList);
		return "goods/goodsList";
		//return goodsList;  //쓰려면 String 리턴데이터 타입이 아니라 List<Goods>로 해야된다.
	}
}
