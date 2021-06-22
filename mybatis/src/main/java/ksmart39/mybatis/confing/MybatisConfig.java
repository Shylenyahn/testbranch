package ksmart39.mybatis.confing;



import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//import com.zaxxer.hikari.HikariConfig;

@Configuration
@MapperScan(basePackages = "ksmart39.mybatis.dao", sqlSessionFactoryRef = "mybatisSqlSessionFactory")  //Ref ->references란 의미   ,책이랑 다른점 type Alias 써줌
public class MybatisConfig {
	/*
	@Bean("db1")
	public DataSource db1() {
		HikariConfig hikariConfig = new HikariConfig();
		hikariConfig.setDriverClassName(driverClassName);
		hikariConfig.setJdbcUrl(jdbcUrl);
		hikariConfig.setUsername(username);
		hikariConfig.setPassword(password);
		return hikariConfig.getDataSource();
	}*/
	//이렇게 해놓은 뒤 아래에  public SqlSessionFactory mybatisSqlSessionFactory(/*@Qualifier("db1") 부분을 추가한다.*/ DataSource dataSource, ApplicationContext ctx) throws Exception{~}
	
	@Bean("mybatisSqlSessionFactory") //@Bean(value = "mybatisSqlSessionFactory")와 같다 // Bean 이름이  mybatisSqlSessionFactory이란 뜻
	public SqlSessionFactory mybatisSqlSessionFactory(DataSource dataSource, ApplicationContext ctx) throws Exception {// DataSource->application.properties에 있는 정보들이 담긴다.(DB연동 부분만 인듯?)
		
		SqlSessionFactoryBean mybatisSqlSessionFactoryBean = new SqlSessionFactoryBean();
		mybatisSqlSessionFactoryBean.setDataSource(dataSource);
		mybatisSqlSessionFactoryBean.setMapperLocations(ctx.getResources("classpath:/mapper/**/*.xml"));
		mybatisSqlSessionFactoryBean.setTypeAliasesPackage("ksmart39.mybatis.domain");
		
		return mybatisSqlSessionFactoryBean.getObject();  //빌드 시점에서 생성해준다...->mybatisSqlSessionFactoryBean.getObject();
	}
	
	
	
	@Bean("mybatisSqlSessionTemplate") //@Bean(value="mybatisSqlSessionTemplate") 와 같다  //Bean 이름이 mybatisSqlSessionTemplate이란 뜻
	public SqlSessionTemplate mybatisSqlSessionTemplate(@Qualifier(value="mybatisSqlSessionFactory")SqlSessionFactory sqlSessionFactory) { //@Qualifier->Bean으로 만들어진 이름이 있으면 그걸 그대로 가져오세요.
		
		
		return new SqlSessionTemplate(sqlSessionFactory);
	}
	
	
	
	
	
	
	
	
	
	
}
