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
## 실습 내용
- ``git checkout 1-gradle-setting``로 브랜치 변경
- ``server/basic-test/build.gradle`` 파일을 수정
```groovy
dependencies {
    implementation("$boot:spring-boot-starter-web")
}
```
- ``@SpringBootApplication``을 위한 클래스 등록
```java
package com.sp.fc;
....
@SpringBootApplication
public class BasicTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(BasicTestApplication.class, args);
    }
}
```
- ``server/basic-test/src/main/resources/application.yml``파일 생성 및 내용 등록
```yaml
server:
  port: 9050
```
02:00