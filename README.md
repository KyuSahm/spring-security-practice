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
# Gradle의 이해
## GroupId Vs Artifact Id
- 참고 문서: https://maven.apache.org/guides/mini/guide-naming-conventions.html
- GroupId
  - GroupId는 프로젝트를 정의하는 고유한 식별자 정보
  - GroupId는 Java package name rules을 따라야 함
- ArtifactId
  - 버전 없는 Jar파일 이름
  - 특수문자는 사용할 수 없고, 소문자만 사용되어야 함
- 정리하자면, GroupId는 큰 틀을 의미하고, ArtifactId는 그 안에 작은 틀을 의미
  - 예를 들어, 회사에서 정산시스템을 만든다면, GroupId는 회사명, ArtifactId는 주문정산, 월급정산 등 이런식으로 정의할 수 있음

## Gradle build.gradle의 동작원리
### build.gradle은 무엇인가?
- ``build.gradle``은 파일 자체가 ``Project Object(객체)``로, ``Project Object``는 Project 인터페이스를 구현하는 객체
- ``Project Object``는 Project 단위에서 필용한 작업을 수행하기 위해 모든 Method와 Property를 모아놓은 슈퍼 객체
```java
public interface Project extends Comparable<Project>, ExtensionAware, PluginAware {    
}
```
- ``build.gradle``에 작성하는 수많은 코드들은 모두 Project 오브젝트의 ``Property``와 ``Method``가 되며, ``Project Object``는 프로젝트 이름부터 필드, 메소드를 모두 포함하는 객체가 됨
- ``Project Object``의 대표적인 Method는 java application용 build.gradle이 가진 ``plugins, repositories, dependencies, application``
- Gradle Task를 이용해서 java application을 빌드하면, build task는 이 메소드들을 수행시킴
![Project_Methods](./images/Project_Methods.png)
- 위의 이미지의 ``{}``로 감싸여진 부분은 메서드의 인자로 받아지는 Groovy의 ``Closure``인데, Groovy의 Closure는 Java나 Kotlin의 ``Lamda``와 같음
- 따라서, ``{}`` 블록 내부의 메소드들은 아래의 이미지처럼 인자로 넘겨질 수 도 있음
![Project_Methods_Closure](./images/Project_Methods_Closure.png)
### build.gradle의 Property
- ``build.gradle``에는 Project Object를 위한 Property를 정의할 수 있음
#### Property 재정의하기
- Property를 정의하는 것은 간단. 아무 곳에난 아래의 문법을 사용하면 됨
```groovy
project.[property name] = [value]
```
- 혹은 project를 생략하고 사용할 수도 있음
```groovy
[property name] = [name]
```
- 예를 들어, Project Object의 group를 재정의하고 싶다면 아래와 같이 사용
  - ``group`` 또는 ``project.group``로 해당 Property에 접근 가능
```groovy
group = 'com.example'
project.group = 'com.kotlinworld'

repositories {
    println group // com.kotlinworld가 출력
    mavenCentral()
}
```
- 하지만, 이렇게 지정하는 것은 Project Object에 미리 정의된 Property만 정의하는 것이 가능하고, Custom Property를 만들려면 다른 방법을 사용해야 함
#### Custom Property 만들기
- Custom Property를 생성하기 위해서는 Project 객체의 extension에 넣는 방식을 사용
  - ``project.ext``를 통해 extension에 접근
```groovy
project.ext.[custom property name] = [value]
```
- project.ext에 넣어진 변수는 Groovy의 특수한 문법을 사용해 project object에서 직접 접근이 가능
```groovy
project.[custom property name]
```
- 예를 들어, blogName이란 custom property를 설정한 다음 출력하기 위해서는 다음과 같이 사용 가능
```groovy
project.ext.blogName = 'kotlin world'

repositories {
    println project.blogName // kotlin world 출력
    mavenCentral()
}
```
#### build.gradle의 Method
- ``build.gradle``에는 Project object를 위한 Method를 정의할 수 있음
- 대표적인 Method들은 바로 ``build.gradle``의 ``repositories`` method와 ``dependencies`` method임
```groovy
repositories {
    mavenCentral()
}

dependencies {
    testImplementation "junit:junit:4.13.2"
    implementation "com.google.guava:guava:30.1.1-jre"
}
```
- 위의 Method들은 ``build.gradle`` 속에 Method로 존재
  - 내부에 들어가는 Closure(Lambda 식)은 프로젝트가 빌드될 때 해당 메소드를 수행하는 task에 의해 수행됨
![repositories_and_dependencies](./images/repositories_and_dependencies.png)  
- 위의 Method들은 미리 빌드된 Method들임
  - Custom Method를 생성하기 위해서는 Method를 별도로 정의해야 함
#### Custom Method 생성하기
- ``Groovy``의 Lambda식인 ``Closure``와 Gradle의 ``ext``를 활용해 Custom Method를 손쉽게 생성 가능
```groovy
ext.[Method name] = { param1, param2 ->
    [Method Body]
}

project.ext.[Method name] = { param1, param2 ->
    [Method Body]
}
```
- 예를 들어, blogName을 출력하는 Custom Method를 생성하고 싶다면 아래와 같이 작성
```groovy
project.ext.getBlogName = { 
    return project.blogname
}
```
## Gradle 프로젝트 구성
- Gradle은 우선 ``settings.gradle`` 파일을 참고해 프로젝트의 구조를 파악
- 그런다음, 개별 프로젝트를 ``build.gradle``을 통해 빌드하는 방식으로 동작

### settings.gradle
settings 에서는 전체 프로젝트의 구조를 빌드한다. 일반적으로는
```groovy
rootProject.name="project-name"
include "project-name"
```
으로 한개의 프로젝트를 구성하지만, 여러 모듈 프로젝트들을 포함하는 경우에는

```groovy
rootProject.name="project-name"
include ":sub-project1"
include ":sub-project2"
```
와 같이 하위 프로젝트들을 포함시켜 준다.
하지만, 모듈 프로젝트들이 많아서 이들을 group 으로 관리하고 싶다면, 다음과 같이 자동 빌드하는 스크립트를 쓰면 편리하다.
```groovy
rootProject.name = 'security-gradle3'

["comp", "web", "server"].each {

    def compDir = new File(rootDir, it)
    if(!compDir.exists()){
        compDir.mkdirs()
    }

    compDir.eachDir {subDir ->

        def gradleFile = new File(subDir.absolutePath, "build.gradle")
        if(!gradleFile.exists()){
            gradleFile.text =
                    """

                    dependencies {

                    }

                    """.stripIndent(20)
        }

        [
                "src/main/java/com/sp/fc",
                "src/main/resources",
                "src/test/java/com/sp/fc",
                "src/test/resources"
        ].each {srcDir->
            def srcFolder = new File(subDir.absolutePath, srcDir)
            if(!srcFolder.exists()){
                srcFolder.mkdirs()
            }
        }

        def projectName = ":${it}-${subDir.name}";
        include projectName
        project(projectName).projectDir = subDir
    }
}
```
### build.gradle
Root 폴더의 ``build.gradle``에서는 전체 하위 프로젝트의 공통 설정에 대한 사항을 기술해 넣는다.
``build.gradle``는 크게 3가지 분류로 나뉜다.
- ``buildscript``
  - gradle 빌드 스크립트 자체를 위한 의존성이나 변수 Task, Plugin 을 지정
- ``allprojects``
  - 모든 프로젝트에 공통으로 넣을때 사용
- ``subprojects``
  - ``allprojects`` 와 비슷한 역할을 하지만, 차이점은 ``allprojects``의 경우 root project마저 같이 설정
  - 즉, java로 구성하면 root project 역시도 그렇게 되는 것 
```groovy
buildscript {
    ext {
        spring = "2.4.1"
        boot = "org.springframework.boot"
        lombok = "org.projectlombok:lombok"
    }
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("$boot:spring-boot-gradle-plugin:$spring")
    }
}

allprojects {
    group = "com.sp.fc"
    version = "1.0.0"
}

subprojects {
    apply plugin: "java"
    apply plugin: boot
    apply plugin: "io.spring.dependency-management"
    apply plugin: "idea"

    repositories {
        mavenCentral()
    }

    configurations {
        developmentOnly
        runtimeClasspath {
            extendsFrom developmentOnly
        }
    }

    dependencies {
        developmentOnly("$boot:spring-boot-devtools")
        implementation "$boot:spring-boot-starter-security"
        implementation 'com.fasterxml.jackson.core:jackson-annotations'

        compileOnly lombok
        testCompileOnly lombok
        annotationProcessor lombok
        testAnnotationProcessor lombok

        testImplementation "$boot:spring-boot-starter-test"
    }

    test {
        useJUnitPlatform()
    }
}


["comp", "web"].each {
    def subProjectDir = new File(projectDir, it)
    subProjectDir.eachDir {dir->
        def projectName = ":${it}-${dir.name}"
        project(projectName){
            bootJar.enabled(false)
            jar.enabled(true)
        }
    }
}
["server"].each {
    def subProjectDir = new File(projectDir, it)
    subProjectDir.eachDir {dir->
        def projectName = ":${it}-${dir.name}"
        project(projectName){

        }
    }
}

help.enabled(false)
```
### 프로젝트 auth reload 확성화 하기
- IntelliJ 에서 compiler.automake.allow.when.app.running 을 체크하고
- 설정의 Build project automatically 를 체크하고
- Run configuration 에서 On 'Update' action 과 On frame deactivation 의 값을 적절하게 수정해 줍니다.

참고 사이트 : https://velog.io/@bread_dd/Spring-Boot-Devtools
### 실습
- Step 01. ``gradle`` 명령어를 통한 초기 프로젝트 구성
```bash
# 01. gradle init
gusam@DESKTOP-RF6D56E MINGW64 /d/Workspace/spring-security-practice/practice/02. gradle 프로젝트 구성 (main)
$ gradle init
<-------------> 0% EXECUTING [123ms]
# 02. Select build script DSL
Select type of project to generate:
  1: basic
  2: application
  3: library
  4: Gradle plugin
Enter selection (default: basic) [1..4] 1

<======-------> 50% EXECUTING [229ms]
><======-------> 50% EXECUTING [424ms]
<======-------> 50% EXECUTING [728ms]
<======-------> 50% EXECUTING [924ms]
<======-------> 50% EXECUTING [3s]
Select build script DSL:
  1: Groovy
  2: Kotlin
Enter selection (default: Groovy) [1..2] 1

<======-------> 50% EXECUTING [4s]
# 03. Project name. "sp-fastcampus-spring-sec"
Project name (default: 02. gradle 프로젝트 구성): sp-fastcampus-spring-sec
<======-------> 50% EXECUTING [6s]
<======-------> 50% EXECUTING [9s]
<======-------> 50% EXECUTING [11s]
<======-------> 50% EXECUTING [14s]
<======-------> 50% EXECUTING [16s]

> Task :init
Get more help with your project: Learn more about Gradle by exploring our samples at https://docs.gradle.org/6.9.1/samples

BUILD SUCCESSFUL in 20s
2 actionable tasks: 2 executed

gusam@DESKTOP-RF6D56E MINGW64 /d/Workspace/spring-security-practice/practice/02. gradle 프로젝트 구성 (main)
# 04. 생성된 파일 확인
$ ll
total 14
-rw-r--r-- 1 gusam 197609  206  6월 26 12:39 build.gradle
drwxr-xr-x 1 gusam 197609    0  6월 26 12:38 gradle/
-rwxr-xr-x 1 gusam 197609 5766  6월 26 12:38 gradlew*
-rw-r--r-- 1 gusam 197609 2763  6월 26 12:38 gradlew.bat
-rw-r--r-- 1 gusam 197609  383  6월 26 12:39 settings.gradle
```
- Step 02. IntelliJ로 ``build.gradle`` 파일을 열기 및 관련 파일 수정
  - Step 02-1. ``setting.gradle``파일의 내용을 Copy & Paste
  - Step 02-2. gradle refresh 처리: ``comp``, ``server``, ``web`` 디렉토리 생성 확인    
```groovy
rootProject.name = 'sp-fastcampus-spring-sec'

["comp", "web", "server"].each {

    def compDir = new File(rootDir, it)
    if(!compDir.exists()){
        compDir.mkdirs()
    }

    compDir.eachDir {subDir ->

        def gradleFile = new File(subDir.absolutePath, "build.gradle")
        if(!gradleFile.exists()){
            gradleFile.text =
                    """

                    dependencies {

                    }

                    """.stripIndent(20)
        }

        [
                "src/main/java/com/sp/fc",
                "src/main/resources",
                "src/test/java/com/sp/fc",
                "src/test/resources"
        ].each {srcDir->
            def srcFolder = new File(subDir.absolutePath, srcDir)
            if(!srcFolder.exists()){
                srcFolder.mkdirs()
            }
        }

        def projectName = ":${it}-${subDir.name}";
        include projectName
        project(projectName).projectDir = subDir
    }
}
```
- Step 03. IntelliJ의 ``server``폴더 아래에 ``basic-test``폴더를 생성한 후, gradle refresh 처리
  - ``basic-test`` 폴더 아래에 ``src`` 폴더와 ``build.gradle`` 파일이 생성됨을 확인
- Step 04. IntelliJ의 root 아래에 build.gradle을 아래와 같이 수정
  - Root 폴더의 ``build.gradle``에서는 전체 하위 프로젝트의 공통 설정에 대한 사항을 기술해 넣는다.
  - ``build.gradle``는 크게 3가지 분류로 나뉜다.
    - ``buildscript``: gradle 빌드 스크립트 자체를 위한 의존성이나 변수 Task, Plugin 을 지정
    - ``allprojects``: 모든 프로젝트에 공통으로 넣을때 사용
    - ``subprojects``: ``allprojects`` 와 비슷한 역할을 하지만, 차이점은 ``allprojects``의 경우 root project마저 같이 설정. 즉, java로 구성하면 root project 역시도 그렇게 되는 것
  - 아래의 ``build.gradle` 파일 분석
    - buildscript.ext: 추가(extension) 변수 설정
        - spring: spring version
        - boot: springboot group id 지정
        - lombok: lombok의 group id와 artifact id를 지정
    - buildscript.dependencies: springboot를 gradle로 빌드할 때 사용하는 라이브러리
    - allprojects: 모든 프로젝트에서 사용하게 될 group id와 version id를 정의
    - subprojects
      - ``apply plugin: "java"``
        - main과 test 폴더 아래에 위치한 java, resources 폴더를 Source와 resource 폴더로 인식함
        - Gradle에 관련된 Task들이 추가됨
          - ``build.assemble, build.build, build.buildDependents, build.buildNeeded, build.classes, build.clean, build.jar, build.testClasses``
          - ``other.compileJava, other.compileTestJava, other.processResources, other.processTestResources``
          - ``documentation.javadoc``
          - ``verification.check, verification.test`
      - ``apply plugin: boot apply plugin: "io.spring.dependency-management" apply plugin: "idea"``
        - spring boot와 관련된 Task들이 추가됨
          - ``build.bootJar``: 단독으로 실행가능한 jar을 빌드
            - 실행
              - 직접 jar 파일이 있는 곳에서 java -jar으로 실행해도 되고
              - 루트 디렉토리에서 아래 명령어를 실행해도 됨: ``$./gradlew bootRun``
          - ``application.bootRun, build.bootBuildImage, build.bootJar, build.bootJarMainClassName``
      - ``repositories`` 메소드: ``mavenCentral()``을 사용
      - ``dependencies`` 메소드: 라이브러리 의존성
    - ``bootJar.enabled``: 폴더 아래에 프로젝트 모듈이 생성될 때, ``server`` 폴더에서만 boot jar가 생성되도록 설정 
```groovy
buildscript {
    ext {
        spring = "2.4.1"
        boot = "org.springframework.boot"
        lombok = "org.projectlombok:lombok"
    }
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("$boot:spring-boot-gradle-plugin:$spring")
    }
}

allprojects {
    group = "com.sp.fc"
    version = "1.0.0"
}

subprojects {
    apply plugin: "java"
    apply plugin: boot
    apply plugin: "io.spring.dependency-management"
    apply plugin: "idea"

    repositories {
        mavenCentral()
    }

    configurations {
        developmentOnly
        runtimeClasspath {
            extendsFrom developmentOnly
        }
    }

    dependencies {
        developmentOnly("$boot:spring-boot-devtools")
        implementation "$boot:spring-boot-starter-security"
        implementation 'com.fasterxml.jackson.core:jackson-annotations'

        compileOnly lombok
        testCompileOnly lombok
        annotationProcessor lombok
        testAnnotationProcessor lombok

        testImplementation "$boot:spring-boot-starter-test"
    }

    test {
        useJUnitPlatform()
    }
}


["comp", "web"].each {
    def subProjectDir = new File(projectDir, it)
    subProjectDir.eachDir {dir->
        def projectName = ":${it}-${dir.name}"
        project(projectName){
            bootJar.enabled(false)
            jar.enabled(true)
        }
    }
}
["server"].each {
    def subProjectDir = new File(projectDir, it)
    subProjectDir.eachDir {dir->
        def projectName = ":${it}-${dir.name}"
        project(projectName){

        }
    }
}

help.enabled(false)
``
- Step 05. IntelliJ의 ``comp``폴더 아래에 ``common-utils``폴더를 생성한 후, gradle refresh 처리
  - ``comp\common-utils`` 폴더 아래에 모듈 생성(``src`` 폴더와 ``build.gradle`` 파일이 생성됨을 확인)
- Step 06. ``comp-compcommon-utils`` 프로젝트에서 Person Class 생성 후, ``server-basic-test`` 프로젝트에서 사용하기
  - ``server-basic-test`` 프로젝트의 build.gradle에 ``comp-compcommon-utils`` 프로젝트 의존성 추가
```groovy
dependencies {
    implementation("$boot:spring-boot-starter-web")
    compile project(":comp-common-utils")
}
```
# Spring Security란?
## 시큐리티가 필요한 이유
웹사이트는 각종 서비스를 하기 위한 리소스와 서비스를 사용하는 유저들의 개인 정보를 가지고 있습니다. 이들 리소스를 보호하기 위해서 일반적으로 웹 사이트는 두가지 보안 정책을 설정해야 합니다.

- 서버 리소스
- 유저들의 개인정보

![fig-0-site-securities.png](images/fig-0-site-securities.png)

## 인증 (Authentication)
사이트에 접근하는 사람이 누구인지 시스템이 알아야 합니다. 익명사용자(anonymous user)를 허용하는 경우도 있지만, 특정 리소스에 접근하거나 개인화된 사용성을 보장 받기 위해서는 반드시 로그인하는 과정이 필요합니다. 로그인은 보통 ``username / password`` 를 입력하고 로그인하는 경우와 sns 사이트를 통해 인증을 대리하는 경우가 있습니다.

- UsernamePassword 인증
  - Session 관리
  - 토큰 관리 (sessionless)
- Sns 로그인 (소셜 로그인) : 인증 위임

## 인가 혹은 권한(Authorization)
사용자가 누구인지 알았다면 사이트 관리자 혹은 시스템은 로그인한 사용자가 어떤 일을 할 수 있는지 권한을 설정합니다. 권한은 특정 페이지에 접근하거나 특정 리소스에 접근할 수 있는 권한여부를 판단하는데 사용됩니다. 개발자는 권한이 있는 사용자에게만 페이지나 리소스 접근을 허용하도록 코딩해야 하는데, 이런 코드를 쉽게 작성할 수 있도록 프레임워크를 제공하는 것이 ``스프링 시큐리티 프레임워크(Spring Security Framework)`` 입니다.
- Secured : deprecated
- PrePostAuthorize
- AOP

## 메모리 사용자 인증
간단히 특정된 소스를 위한 서비스나 테스트를 위해 사용하는 용도로 사용합니다. 스프링 시큐리티를 테스트 하기 위한 용도로 사용합니다.
- 기본 사용자 로그인
- ``application.yml``에 설정하고 로그인하기
- ``UserDetailService``를 이용하기
- ``WebSecurityConfigurerAdapter``를 사용하기

## 실습할 내용
1. basic-test 서버에 기본 사용자(user) 로 로그인한다
2. ``applicaiton.yml``에 user1 을 만들고 로그인한다.
3. 어플리케이션 객체에 ``UserDetailsService Bean``을 만들어서 로그인을 한다.
4. ``SecurityConfig``를 만들고 이를 통해 로그인 한다.
5. ``SecurityMessage (UserDetails, message)`` 를 통해 /user 페이지와 /admin 페이지 접근 권한을 테스트 한다.
## 실습한 내용
- ``git checkout 1-gradle-setting``로 브랜치 변경
- ``server/basic-test/build.gradle`` 파일을 수정
```groovy
dependencies {
    implementation("$boot:spring-boot-starter-web")
}
```
- ``@SpringBootApplication``을 위한 클래스 등록
```java
package com.sp.fc.web;
....
@SpringBootApplication
public class BasicTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(BasicTestApplication.class, args);
    }
}
```
- ``server/basic-test/src/main/resources/application.yml``파일 생성 및 서버 포트 설정
```yaml
server:
  port: 9050
```
- Controller Class 작성
```java
package com.sp.fc.web.controller;
....
@RestController
public class HomeController {
    @RequestMapping("/")
    public String index() {
        return "홈페이지";
    }
}
```
- 서버 부팅 후, Browser를 통해 ``http://localhost:9050``에 접속하면 로그인 팝업 창이 뜸: 기본적으로 Spring Security가 동작하고 있기 때문
![localhost_on_spring_security](./images/localhost_on_spring_security.png)
- 기본적인 사용자 명인 ``user``와 서버의 로그에 찍히는 패스워드를 이용해서 로그인 하면 됨
```bash
2022-06-27 22:52:41.497  INFO 11076 --- [  restartedMain] .s.s.UserDetailsServiceAutoConfiguration : 
Using generated security password: c3283119-f124-4938-846a-055a2c35cba5
2022-06-27 22:52:41.613  INFO 11076 --- [  restartedMain] o.s.s.web.Def
```
- 로그인하면, 원하는 내용이 웹브라우저에 표시됨
![localhost_after_login](./images/localhost_after_login.png)
- 매번 암호를 확인해서 입력하기 힘들므로, ``server/basic-test/src/main/resources/application.yml``에 암호를 설정해서 사용할 수 있음
```yaml
server:
  port: 9050
spring:
  security:
    user:
      name: user1
      password: 1111
      roles: USER
```
- 현재 사용하고 있는 Authentication 정보를 브라우저에 찍어보는 방법
  - ``SecurityContextHolder``의 Context에서 Authentication 정보를 찍어봄
  - 브라우저에서 ``localhost:9050/auth``에 접속
```java
package com.sp.fc.web.controller;
....
@RestController
public class HomeController {
    ....
    @RequestMapping("/auth")
    public Authentication auth() {
        return SecurityContextHolder
                .getContext()
                .getAuthentication();
    }
}
```
![Authentification_Info](./images/Authentification_Info.png)
- Controller Method에 대한 접근 권한 설정 방법
  - 참조: https://copycoding.tistory.com/278
  - 방법1: ``WebSecurityConfigureAdapter``를 상속한 ``WebSecurityConfig``를 작성
  - 방법2: Controller에 annotation을 추가하여 관리하는 방법
    - ``@PreAuthorize, @PostAuthorize, @Secured``를 사용('hasRole)
```java
package com.sp.fc.web.controller;
....
@RestController
public class HomeController {
    ....
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    @RequestMapping("/user")
    public SecurityMessage user() {
        return SecurityMessage.builder()
                .auth(SecurityContextHolder.getContext().getAuthentication())
                .message("사용자 정보")
                .build();
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @RequestMapping("/admin")
    public SecurityMessage admin() {
        return SecurityMessage.builder()
                .auth(SecurityContextHolder.getContext().getAuthentication())
                .message("관리자 정보")
                .build();
    }
}

package com.sp.fc.web.dto;
....
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SecurityMessage {
    private Authentication auth;
    private String message;
}
```
- 하지만, ``USER`` 권한을 갖는 ``user1``이 ``localhost:9050/user``과 ``localhost:9050/admin``에 모두 접근 가능한 문제가 발생
  - **정보 탈취의 가능성이 있음**
  - 해결 방법: ``WebSecurityConfigurerAdapter``를 상속한 WebSecurityConfig를 선언
    - ``@EnableWebSecurity``와 ``@EnableGlobalMethodSecurity(prePostEnabled = true)`` annotate해서 Method의 ``@PreAuthorize``와 ``@PostAuthorize``를 가능하도록 설정하면 됨
```java
package com.sp.fc.web.config;
....
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
}
```
- ``user1``으로 접속한 후, ``localhost:9050/admin``에 접속하면 ``403`` 에러가 발생
![admin_page_403_error](./images/admin_page_403_error.png)
- ``applcation.yml``에서는 하나의 User만 설정해서 사용가능
  - ``AuthenticationManagerBuilder`` ``inMemoryAuthentification``을 이용하여 사용자를 추가할 수 있음
  - ``inMemoryAuthentification``을 사용하면 더 이상 ``application.yml``에 등록한 사용자는 사용 못하게 됨
    - ``Bad credentials`` 에러가 발생
  - **아래와 같이 명시하면, Password Encoder를 사용하지 않아서 서버에서 에러가 발생함**
```java
package com.sp.fc.web.config;
.....
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser(User.builder()
                        .username("user2")
                        .password("2222")
                        .roles("USER"))
                .withUser(User.builder()
                        .username("admin")
                        .password("3333")
                        .roles("ADMIN"));
    }
}
```
```bash
2022-06-28 23:18:52.450 ERROR 23948 --- [nio-9050-exec-8] o.a.c.c.C.[.[.[/].[dispatcherServlet]    : Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception

java.lang.IllegalArgumentException: There is no PasswordEncoder mapped for the id "null"
	at org.springframework.security.crypto.password.DelegatingPasswordEncoder$UnmappedIdPasswordEncoder.matches(DelegatingPasswordEncoder.java:254) ~[spring-security-core-5.4.2.jar:5.4.2]
	at org.springframework.security.crypto.password.DelegatingPasswordEncoder.matches(DelegatingPasswordEncoder.java:202) ~[spring-security-core-5.4.2.jar:5.4.2]
	at org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter$LazyPasswordEncoder.matches(WebSecurityConfigurerAdapter.java:595) ~[spring-security-config-5.4.2.jar:5.4.2]
	at org.springframework.security.authentication.dao.DaoAuthenticationProvider.additionalAuthenticationChecks(DaoAuthenticationProvider.java:76) ~[spring-security-core-5.4.2.jar:5.4.2]
```
- 아래의 코드처럼, Password Encoder를 사용해서 비밀번호를 암호화 처리
  - admin으로 ``localhost:9050/admin, localhost:9050, localhost:9050/auth``에 접속되지만,
  - ``localhost:9050/user``에는 접속 안됨
```java
package com.sp.fc.web.config;
....
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser(User.builder()
                        .username("user2")
                        .password(passwordEncoder().encode("2222"))
                        .roles("USER"))
                .withUser(User.builder()
                        .username("admin")
                        .password(passwordEncoder().encode("3333"))
                        .roles("ADMIN"));
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```
- 주소에 ``http://localhost:9050/logout``을 입력해서 로그아웃 할 수 있음
- Spring Security는 기본적으로 모든 페이지(/, /auth, /user, /admin)를 로그인이 없이는 사용 못하도록 막음
  - 왜냐면, ``WebSecurityConfigurerAdapter``의 ``configure()`` 메소드가 모든 페이지를 인증을 한 후, 사용하도록 설정
    - ``http.authorizeRequests((requests) -> requests.anyRequest().authenticated());``
```java
public abstract class WebSecurityConfigurerAdapter implements WebSecurityConfigurer<WebSecurity> {
....    
    protected void configure(HttpSecurity http) throws Exception {
		this.logger.debug("Using default configure(HttpSecurity). "
				+ "If subclassed this will potentially override subclass configure(HttpSecurity).");
		http.authorizeRequests((requests) -> requests.anyRequest().authenticated());
		http.formLogin();
		http.httpBasic();
	}
....
}    
```    
- 만약, 특정 페이지(``/``)는 로그인 없이 사용하기를 원한다면?
  - ``protected void configure(HttpSecurity http) throws Exception`` 함수를 재정의 하면 됨
  - ``antMatchers``를 이용해서, ``/``은 로그인 없이 사용하도록 ``permitAll()``을 호출 
  - ``antMatchers``로 맵핑되지 않은 페이지들은 ``anyRequest().authenticated()``를 이용해서 로그인을 요청
```java
package com.sp.fc.web.config;
....
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    .....
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests((requests) ->
                requests.antMatchers("/").permitAll()
                        .anyRequest().authenticated());
        http.formLogin();
        http.httpBasic();
    }
    ....
}
``` 
- 서버 재시작 후, ``http://localhost:9050/``은 로그인 없이 접근 가능 
- 다른 페이지로 접근하면, 로그인을 요청 받음
- 전체 소스
  - ``.\practice\03. 스프링 시큐리티란\sp-fastcampus-spring-sec`` 참조
# Spring Security의 전체 구조
## Servlet Container
- ``Tomcat``와 같은 웹 애플리케이션을 ``Servlet Container``라고 부르는데, 이런 웹 애플리케이션(J2EE Application)은 기본적으로 ``Filter``와 ``Servlet``으로 구성되어 있음
![fig-1-servlet-container](./images/fig-1-servlet-container.png)
- Filter은 Chain처럼 엮여있기 때문에 ``Filter Chain``이라고도 불리는데, 모든 request는 이 ``Filter Chain``을 반드시 거쳐야만 Servlet 서비스에 도착하게 됨
## Spring Security의 큰 그림
- 그래서 ``Spring Security``는 ``DelegatingFilterProxy``라는 필터를 만들어 ``Main Filter Chain``에 끼워넣고, 그 아래 다시 ``SecurityFilterChain Group``을 등록함
- 결국, ``DelegatingFilterProxy``를 이용하여 API에 따라서 동작시키는 ``Filter Chain``을 다르게 가져갈 수 있음
![fig-2-spring-big-picture](./images/fig-2-spring-big-picture.png)
- 이 ``Filter Chain``은 반드시 한 개 이상이고, ``URL Pattern``에 따라 적용되는 필터체인을 다르게 할 수 있음
- 본래의 Main Filter를 반드시 통과해야만 Servlet에 들어갈 수 있는 단점을 보완하기 위해서 ``Filter Chain Proxy(DelegatingFilterProxy)``를 두었다고 할 수 있음
- ``web resource``의 경우 Pattern을 따르더라도 Filter를 무시(ignore)하고 통과시켜주기도 함
## Security Filters
- 이 ``Filter Chain``에는 다양한 필터들이 들어갑니다.
![fig-4-filters](./images/fig-4-filters.png)
- 각각의 Filter는 **단일 Filter 단일 책임(?)원칙**처럼, 각기 서로 다른 관심사를 해결함. 예를 들면 아래와 같음
  - ``HeaderWriterFilter`` : Http Header를 검사한 후, 필요한 Header를 추가하거나 삭제
  - ``CorsFilter`` : 허가된 사이트나 클라이언트의 요청인지 검사
  - ``CsrfFilter`` : POST나 PUT과 같이 서버 Resource를 변경하는 경우, 서버가 내려보낸 Resource에서 올라온 요청인지 검사 (중요)
  - ``LogoutFilter`` : 현재의 Request가 로그아웃을 하겠다는 건지 검사
  - ``UsernamePasswordAuthenticationFilter`` : 현재의 Request가 ``username / password`` 로 로그인을 하려는지 검사 후, 로그인 정보를 이용하여 Authentification을 사용자에게 부여해 주거나 인증 실패 처리를 해줌
  - ``ConcurrentSessionFilter`` : 하나의 사용자가 동시에 여기저기서 로그인 하는걸 허용할 것인가?
  - ``BearerTokenAuthenticationFilter`` : Authorization Header에 Bearer 토큰을 검사해서 인증 처리
  - ``BasicAuthenticationFilter`` : Authorization Header에 Basic 토큰을 검사해서 인증처리
  - ``RequestCacheAwareFilter`` : 방금 요청한 request 이력이 다음에 필요할 수 있으니 캐시에 담아놓는 역할
  - ``SecurityContextHolderAwareRequestFilter`` : 보안 관련 Servlet 3 스펙을 지원하기 위한 Filter
  - ``RememberMeAuthenticationFilter`` : ``RememberMe``는 세션이 만료되더라도 Login을 다시하지 않고, 사용하도록 처리해주는 기능. 아직 Authentication 인증이 안된 경우라면 ``RememberMe Cookie``를 검사해서 인증 처리해주는 기법
  - ``AnonymousAuthenticationFilter`` : Filter Chain의 이전 Filter들에 의해서 아직도 인증이 되지 않았다면 Anonymous User로 Authentification을 채워주는 역할
  - ``SessionManagementFilter`` : 서버에서 지정한 세션정책에 맞게 사용자가 사용하는지 검사
  - ``ExceptionTranslationFilter`` : 해당 Filter 이후(``FilterSecurityInterceptor``)에 인증이나 권한 예외가 발생하면 ``ExceptionTranslationFilter``가 잡아서 처리
  - ``FilterSecurityInterceptor`` : 여기까지 살아서 왔다면 Authentification이 있다는 거니, 사용자  request의 Controller Method 들어갈 권한이 있는지, 리턴한 결과를 사용자에게 보내줘도 되는건지 마지막으로 점검
  - 그 밖에... ``OAuth2, Saml2, Cas, X509`` 등에 관한 Filter들도 있음
- Filter는 넣거나 뺄 수 있고, 순서를 조절할 수 있음 (이때, 필터의 순서가 매우 critical 할 수 있기 때문에 기본 필터들은 그 순서가 어느정도 정해져 있음)
## 특정 Request에서 어떤 Filter들이 동작하는 확인하는 방법
- ``WebSecurityConfigurerAdapter``를 상속받아서 구현한 클래스에 ``@EnableWebSecurity(debug = true)`` annotation에서 ``debug`` 옵션을 켜줌
```java
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
....
}
```
```bash
....
servletPath:/admin
pathInfo:null
headers: 
host: localhost:9050
user-agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:101.0) Gecko/20100101 Firefox/101.0
accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8
accept-language: en-US,en;q=0.5
accept-encoding: gzip, deflate, br
referer: http://localhost:9050/login
connection: keep-alive
cookie: JSESSIONID=8F88F2B96AE73D48A0F1DC01AEF67C5B
upgrade-insecure-requests: 1
sec-fetch-dest: document
sec-fetch-mode: navigate
sec-fetch-site: same-origin
sec-fetch-user: ?1


Security filter chain: [
  WebAsyncManagerIntegrationFilter
  SecurityContextPersistenceFilter
  HeaderWriterFilter
  CsrfFilter
  LogoutFilter
  UsernamePasswordAuthenticationFilter
  DefaultLoginPageGeneratingFilter
  DefaultLogoutPageGeneratingFilter
  BasicAuthenticationFilter
  RequestCacheAwareFilter
  SecurityContextHolderAwareRequestFilter
  AnonymousAuthenticationFilter
  SessionManagementFilter
  ExceptionTranslationFilter
  FilterSecurityInterceptor
]
....
```
- ``WebSecurityConfigurerAdapter``를 상속한 클래스의 ``configure(HttpSecurity http)`` 메소드에서 다양한 Filter들을 Enable/Disable 시킬 수 있음
  - ``http.authorizeRequests(), http.formLogin(), http.httpBasic()``를 주석처리하게 되면, 아래의 Filter들을 사용하지 않게 됨
    - ``UsernamePasswordAuthenticationFilter``
    - ``DefaultLoginPageGeneratingFilter`` 
    - ``DefaultLogoutPageGeneratingFilter``
    - ``BasicAuthenticationFilter``
    - ``FilterSecurityInterceptor``
```java
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    ....
    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests((requests) ->
//                requests.antMatchers("/").permitAll()
//                        .anyRequest().authenticated());
//        http.formLogin();
//        http.httpBasic();
    }
    ....
}
```
```bash
...
Security filter chain: [
  WebAsyncManagerIntegrationFilter
  SecurityContextPersistenceFilter
  HeaderWriterFilter
  CsrfFilter
  LogoutFilter
  RequestCacheAwareFilter
  SecurityContextHolderAwareRequestFilter
  AnonymousAuthenticationFilter
  SessionManagementFilter
  ExceptionTranslationFilter
]
...
```
- 아래의 코드처럼, ``Filter Chain``에서 다양한 Filter들을 disable시킬 수 있음 
  - ``HeaderWriterFilter``
  - ``CsrfFilter``
  - ``LogoutFilter``
  - ``RequestCacheAwareFilter``  
```java
package com.sp.fc.web.config;
....
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests((requests) ->
//                requests.antMatchers("/").permitAll()
//                        .anyRequest().authenticated());
//        http.formLogin();
//        http.httpBasic();
        http.headers().disable()
            .csrf().disable()
            .logout().disable()
            .requestCache().disable();
    }
}
```
```bash
......
Security filter chain: [
  WebAsyncManagerIntegrationFilter
  SecurityContextPersistenceFilter
  SecurityContextHolderAwareRequestFilter
  AnonymousAuthenticationFilter
  SessionManagementFilter
  ExceptionTranslationFilter
]
```
- 특정 API에 대해서 Filter Chain을 적용하고 싶은 경우
  - ``WebSecurityConfigurerAdapter``를 상속한 클래스의 ``configure(HttpSecurity http)`` 메소드에서 ``HttpSecurity``에 ``antMatcher()``를 적용하면 됨
```java
package com.sp.fc.web.config;
....
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
       // 모든 API에 대해 아래의 Filter Chain 적용
       // http.antMatcher("/**");
       // "/api" 아래의 모든 API에 대해 아래의 Filter Chain 적용
       http.antMatcher("/api/**");
       http.authorizeRequests((requests) ->
               requests.antMatchers("/").permitAll()
                       .anyRequest().authenticated());
       http.formLogin();
       http.httpBasic();
    }
}
```
- 두 개 이상의 ``Filter Chain``을 구성하고 싶다면?
  - ``WebSecurityConfigurerAdapter``를 상속한 여러 개의 Config Class를 정의하면 됨
  - 여러 개의 클래스(``Filter Chain``)가 있으므로, ``@Order`` Annotation을 통해서 순서를 지정해 줌
```java
package com.sp.fc.web.config;
....
@Order(1)
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    .....
}
```
## 실습하기
- 필터 설정하고 확인하기
- 필터를 ignore 하기
- 서로 다른 필터 체인을 타도록 하기

## 로그인하기
- Spring Framework에서 로그인을 한다는 것은 authenticated 가 true인 ``Authentication`` 객체를 ``SecurityContext``에 갖고 있는 상태를 말함
  - 단, Authentication이 AnonymousAuthenticationToken만 아니면 됨
  ```
  로그인 == Authentication(authenticated = true) only if Authentication != AnonymousAuthenticationToken
  ```
### Authentication (인증)의 기본 구조
- Filter들 중에 일부 Filter는 인증 정보에 관여
  - 이들 Filter가 하는 일은 ``AuthenticationManager``를 통해 Authentication을 인증하고 그 결과를 SecurityContextHolder에 넣어주는 일임
  - ``ProviderManger``은 ``AuthenticationManager``의 구현체 클래스
  - ``ProviderManger``은 parent란 참조를 통해 다른 ``ProviderManger``를 가리킴
  - ``ProviderManger``은 여러 개의 ``AuthenticationProvider``로 구성
![fig-3-authentication](./images/fig-3-authentication.png)
- Authentication(인증 토큰)을 제공하는 Filter들
  - ``UsernamePasswordAuthenticationFilter``
    - 폼 로그인 -> ``UsernamePasswordAuthenticationToken``
  - ``RememberMeAuthenticationFilter``
    - ``remember-me Cookie`` 로그인 -> ``RememberMeAuthenticationToken``
  - ``AnonymousAuthenticationFilter``
    - 로그인하지 않았다는 것을 인증함 -> ``AnonymousAuthenticationToken``
  - ``SecurityContextPersistenceFilter``
    - 기존 로그인을 유지함(기본적으로 ``session``을 이용)
  - ``BearerTokenAuthenticationFilter``
    - ``JWT`` 로그인
  - ``BasicAuthenticationFilter``
    - ``Authorization`` Header에 Username과 Password를 담아서 Base64로 Encoding해서 보내줌
    - ``ajax`` 로그인 -> ``UsernamePasswordAuthenticationToken``
  - ``OAuth2LoginAuthenticationFilter``
    - 소셜 로그인 -> ``OAuth2LoginAuthenticationToken, OAuth2AuthenticationToken``
  - ``OpenIDAuthenticationFilter``
    - ``OpenID`` 로그인
  - ``Saml2WebSsoAuthenticationFilter``
    - ``SAML2`` 로그인
  - ... 기타
- Authentication을 제공(Provide) 하는 인증제공자는 여러 개가 동시에 존재할 수 있고, 인증 방식에 따라 ``ProviderManager``도 복수로 존재할 수 있음
- ``Authentication``은 Interface로 아래와 같은 정보들을 갖고 있음
  - ``Set<GrantedAuthority> authorities``: 인증된 권한 정보
  - ``principal``: 인증 대상에 관한 정보. 주로 ``UserDetails`` 객체가 옴
  - ``credentials``: 인증 확인을 위한 정보. 주로 ``Login Id or Password``가 오지만, ``Password``는 인증 후에는 보안을 위해 삭제함
  - ``details``: 로그인 Request에 대한 상세 정보. ``IP, 세션정보, 기타 인증요청에서 사용했던 정보``들.
  - ``boolean authenticated``: 인증이 되었는지를 체크

