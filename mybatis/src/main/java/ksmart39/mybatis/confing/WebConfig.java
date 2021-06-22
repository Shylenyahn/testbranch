package ksmart39.mybatis.confing;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import ksmart39.mybatis.interceptor.CommonInterceptor;
import ksmart39.mybatis.interceptor.LoginInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer{
	//DI주입
	private final CommonInterceptor commonInterceptor;
	private final LoginInterceptor loginInterceptor;
	
	public WebConfig(CommonInterceptor commonInterceptor,LoginInterceptor loginInterceptor) {
		this.commonInterceptor = commonInterceptor;
		this.loginInterceptor =loginInterceptor; 
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		
		
		// ex)localhost/css/main.css  <-- URL (자원의 위치)  역할 :파일 위치
		//---------------------------------------------------
		//URI   역할 : 식별자 
		//localhost/css/1(1번째 가져오겠다(식별자))
		// 멤버리스트에 첫번째만 가져오고 싶다면?
		//springboot pathvariable...?
		
		registry.addInterceptor(commonInterceptor)
				.addPathPatterns("/**")
				.excludePathPatterns("/css/**")     //    /css/** 에서  css/**로 되면 주소체계가 아니게 된다.
				.excludePathPatterns("/favicon.ico")
				.excludePathPatterns("/js/**");
		
		registry.addInterceptor(loginInterceptor)
				.addPathPatterns("/**")
				.excludePathPatterns("/")
				.excludePathPatterns("/addMember")
				.excludePathPatterns("/favicon.ico")
				.excludePathPatterns("/memberIdCheck")
				.excludePathPatterns("/login")
				.excludePathPatterns("/css/**")
				.excludePathPatterns("/js/**");
		
		WebMvcConfigurer.super.addInterceptors(registry);
	}
}
