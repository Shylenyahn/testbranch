package ksmart39.mybatis.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import ksmart39.mybatis.domain.Goods;

@Mapper
public interface GoodsMapper {
	
	//상품목록조회
	public List<Goods> getGoodsList(Map<String, Object> paramMap); //컨트롤러->서비스->매퍼   똑같이 Map<String, Object> paramMap 이 필요하니까
	
	//상품등록
	public int addGoods(Goods goods);
	
	//상품수정(수정폼)
	public Goods getGoodsInfoByCode(String goodsCode);
	
	//상품수정
	public int modifyGoods(Goods goods);
	
	
	
	//상품삭제
}
