package com.sp.fc.web.teacher;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Set;

@Component
public class TeacherAuthenticationProvider implements AuthenticationProvider, InitializingBean {
    // 실제 DB를 사용해서 사용자 정보를 가져와야 검증을 해줘야 하는 부분
    // 테스트 용으로 HashMap에 데이터를 고정적으로 주입
    private HashMap<String, TeacherPrincipal> teacherDB = new HashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        Set.of(
                new TeacherPrincipal("choi", "최선생", Set.of(new SimpleGrantedAuthority("ROLE_TEACHER"))),
                new TeacherPrincipal("kim", "김선생", Set.of(new SimpleGrantedAuthority("ROLE_TEACHER"))),
                new TeacherPrincipal("yang", "양선생", Set.of(new SimpleGrantedAuthority("ROLE_TEACHER")))
        ).forEach(s -> teacherDB.put(s.getId(), s));
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication instanceof TeacherAuthenticationToken) {
            TeacherAuthenticationToken token = (TeacherAuthenticationToken) authentication;
            if (teacherDB.containsKey(token.getCredentials()))
            {
                TeacherPrincipal principal = teacherDB.get(token.getCredentials());
                return TeacherAuthenticationToken.builder()
                        .principal(principal)
                        .credentials(null)
                        .details(principal.getUsername())
                        .authenticated(true)
                        .build();
            }
// 중요: 아래와 같이 코드를 짜면, authentication을 처리했다고 정리되므로, 다른 Authentication Provider에 대해 시도하지 않음
// 처리할 수 없는 경우, null로 리턴
//            else {
//                authentication.setAuthenticated(false);
//                return authentication;
//            }
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication == TeacherAuthenticationToken.class;
    }
}
