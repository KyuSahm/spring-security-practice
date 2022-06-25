# Spring Security 교육 소개
## 선수 지식
- Java: jdk 11 이상 (modern java에 대해 알아야 함)
- spring boot: 스프링 애플리케이션 프레임워크
- gradle: 프로젝트 관리 및 빌드
- JUnit5(Jupyter)와 spring test: 기본적인 기능 테스트를 위해 필요함
- Spring 관련 전반 지식: Web MVC, RestFul 서비스, Spring Data JPA, AP, SpEL
- lombok: Getter/Setter/Builder 메소드 등 지원
- thymeleaf: 웹 프로그램을 지원함
- mysql: 데이터 테스트
- IntelliJ IDE
## 이 강의에서 다룰 내용
- Gradle 멀티 프로젝트 구성과 모듈 프로젝트 개발
- Spring Security의 기본 구조
- Spring Security를 활용한 로그인 방법 (Authentication)
- Spring Security를 활용한 권한 체크 방법 (Authorization)
- Ajax와 OAuth2 인증: ajax/OAuth2 인증 방식
## 실전 프로젝트
- 프로젝트 소스: https://github.com/jongwon/sp-fastcampus-spring-sec
- 강의에 사용한 소스는 아래 GitHub 사이트를 통해 받아 볼 수 있음
- master에는 초기 설정만 되어 있음
- 각각의 branch를 이동을 해서 테스트를 진행할 수 있음

## 프로그램에 사용한 Live Template
- IntelliJ > Settings > Editor > Live Templates 에 설정한 템플릿들이다.

- data
```java
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
```

- test1 ( ... test13)
```java
@DisplayName("1. $END$")
@Test
void test_1(){

}
```
