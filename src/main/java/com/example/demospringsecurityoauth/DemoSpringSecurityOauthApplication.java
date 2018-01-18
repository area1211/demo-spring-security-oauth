package com.example.demospringsecurityoauth;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.sql.DataSource;


@EnableResourceServer // API 서버를 OAuth2 인증받게 만드는 역할, 기본 옵션은 모든 API의 모든 요청에 대해 OAuth2 인증을 받도록 한다.
@EnableAuthorizationServer // OAuth2 인증서버를 활성화 시켜주는 역할
@SpringBootApplication
public class DemoSpringSecurityOauthApplication extends ResourceServerConfigurerAdapter{

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.headers().frameOptions().disable();
		http.authorizeRequests()
				.anyRequest().permitAll()
				.antMatchers("/authorization-code-test").access("#oauth2.hasScope('read')");
	}

	/**
	 * API를 조회시 출력될 테스트 데이터
	 * @param memberRepository
	 * @return
	 */
	@Bean
	public CommandLineRunner commandLineRunner(MemberRepository memberRepository) {
		return args -> {
			memberRepository.save(new Member("이철수", "chulsoo", "test111"));
			memberRepository.save(new Member("김정인", "jungin11", "test222"));
			memberRepository.save(new Member("류정우", "jwryu991", "test333"));
		};
	}

	@Bean
	public TokenStore JdbcTokenStore(DataSource dataSource) {
		return new JdbcTokenStore(dataSource);
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoSpringSecurityOauthApplication.class, args);
	}
}


/**
 * 권한 코드 테스트를 위해 만든 컨트롤러
 */
@Controller
@RequestMapping("test")
class TestController {
	@RequestMapping("authorization-code")
	@ResponseBody
	public String authorizationCodeTest(@RequestParam("code") String code) {
		String curl = String.format("curl " +
				"-F \"grant_type=authorization_code\" " +
				"-F \"code=%s\" " +
				"-F \"scope=read\" " +
				"-F \"client_id=foo\" " +
				"-F \"client_secret=bar\" " +
				"-F \"redirect_uri=http://localhost:8080/test/authorization-code\" " +
				"\"http://foo:bar@localhost:8080/oauth/token\"", code);
		return curl;
	}
}