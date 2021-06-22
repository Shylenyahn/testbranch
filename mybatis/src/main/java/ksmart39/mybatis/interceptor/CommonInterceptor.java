package ksmart39.mybatis.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import ksmart39.mybatis.dao.MemberMapper;

//빈으로 등록할 때 쓰는 어노테이션
@Component  
public class CommonInterceptor implements HandlerInterceptor{
	
	
	
	//컨트롤러 가기 전에 어떠한 작업을 하겠다.
	
	/*
	 * //DI
	 * 
	 * @Autowired private MemberMapper memberMapper;
	 */
	
	
	private static final Logger log = LoggerFactory.getLogger(CommonInterceptor.class);

	
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) //Object handler -> 핸들러매핑 클래스
			throws Exception {
		HandlerMethod method=(HandlerMethod) handler;
		log.info("CommonInterceptor==========================================================START");
		log.info("Acess Info==========================================================START");
		log.info("PORT     		 ::::::       {}",request.getLocalPort());
		log.info("serverName     ::::::       {}",request.getServerName());
		log.info("Method    	 ::::::       {}",request.getMethod());
		log.info("URI     		 ::::::       {}",request.getRequestURI());
		log.info("Controller     ::::::       {}",method.getBean().getClass().getSimpleName());
		log.info("Acess Info==========================================================END");
		
		return HandlerInterceptor.super.preHandle(request, response, handler);  //false->컨트롤러 가지마 라는 의미   //무한루프(true로 했을 때)일 경우-> 리다이렉트수가 너무 많다. 라고 나옴
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		log.info("CommonInterceptor==========================================================END");
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		log.info("CommonInterceptor==========================================================AFTER");
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}

}
