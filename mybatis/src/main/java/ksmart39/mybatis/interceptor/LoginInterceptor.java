package ksmart39.mybatis.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class LoginInterceptor implements HandlerInterceptor{
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession session= request.getSession();
		String sessionId= (String)session.getAttribute("SID");
		String sessionLevel = (String)session.getAttribute("SLEVEL");
		String requestUri = request.getRequestURI();
		
		if(sessionId==null) {
			response.sendRedirect("/login");
			return false;  // 이거 안하면 무한 리다이렉트 ㅋㅋㅋㅋㅋ
		}else {
			if("2".equals(sessionLevel)) {//유효성검사 했기때문에 equals 쓸수 있는것... 원래는 null이기 때문에 에러가 날 수 있다.
				if(requestUri.indexOf("memberList")>-1 || requestUri.indexOf("addMember")>-1) { //-1은 없다는 말이랑 같음... requestUri.indexOf의 인덱스가 0부터 시작이니까
					
					response.sendRedirect("/");
					
					return false;
				}
			}
			if("3".equals(sessionLevel)) {
				if(requestUri.indexOf("memberList")>-1 || requestUri.indexOf("addMember")>-1) {
					response.sendRedirect("/");
									
					return false;
				}
				
			}
		}
		/**
		 * 상품등록, 상품리스트 까지 만들고 나서 합시다..
			else {
				//아닐경우
				//1. 세션 회원레벨(1,2,3)
				
				//2. request.getRequestURI() == "memberList" 관리자를 제외한 나머지 등급은 response.sendRedirect("/login");으로 갈 수 있게
				
				
			}
		 * 
		 * */
		
		
		return  true ;  //  true    ==HandlerInterceptor.super.preHandle(request, response, handler);
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}
	
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}
}
