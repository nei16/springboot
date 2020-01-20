package com.nei.common.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nei.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Date;

/**
 * 用户登录过滤器
 */
public class JwtLoginFilter extends AbstractAuthenticationProcessingFilter {

    public JwtLoginFilter(String defaultFilterProcessesUrl, AuthenticationManager authenticationManager) {
        super(new AntPathRequestMatcher(defaultFilterProcessesUrl));
        setAuthenticationManager(authenticationManager);
    }

    /**
     * 从登录参数中获取用户名密码，调用 AuthenticationManager.authenticate() 进行校验
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse resp) throws AuthenticationException, IOException {
        User user = new ObjectMapper().readValue(req.getInputStream(), User.class);
        return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
    }

    /**
     * 校验成功来到 successfulAuthentication 回调，遍历用户角色并连接，利用 Jwt 生成token，分别是用户角色、主题、过期时间以及加密算法和密钥，将生成的token写到客户端
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse resp, FilterChain chain, Authentication authResult) throws IOException {
        Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
        StringBuffer as = new StringBuffer();
        for (GrantedAuthority authority : authorities) {
            as.append(authority.getAuthority()).append(",");
        }
        String jwt = Jwts.builder().claim("authorities", as) //配置用户角色
                .setSubject(authResult.getName())
                .setExpiration(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                .signWith(SignatureAlgorithm.HS512, JwtFilter.SIGNING_KEY)
                .compact();
        resp.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        PrintWriter out = resp.getWriter();
        out.write(jwt);
        out.flush();
        out.close();
    }

    /**
     * 校验失败返回错误提示给客户端
     */
    protected void unsuccessfulAuthentication(HttpServletRequest req, HttpServletResponse resp, AuthenticationException failed) throws IOException {
        resp.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        PrintWriter out = resp.getWriter();
        out.write("登录失败");
        out.flush();
        out.close();
    }
}
