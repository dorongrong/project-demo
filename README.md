중고거래 웹 서비스 프로젝트
=============
<br/>

Installation
-------------

* Docker Hub 에서 dorongrong/front, dorongrong/back, dorongrong/my-rabbitmq PULL 
* 도커 네트워크 생성 => docker network creat [network name]
* RabbitMQ 실행 => docker run -d --name my-rabbitmq --network [network name] -p 5672:5672 -p 15672:15672 -p 61613:61613 dorongrong/my-rabbitmq
* FrontEnd React Server 실행 => docker run --network [network name] -p 3000:3000 dorongrong/front
* BackEnd Spring Server 실행 => docker run --network [network name] -p 9097:9097 dorongrong/back

<br/>

프로젝트 기획 동기
-------------

* 백엔드의 기본적인 기본기를 다지면서 만들수있는 프로젝트를 찾아보았습니다.
* 보통 쇼핑몰과 게시판을 주로 하는데 중고거래 사이트가 쇼핑몰과 게시판 두 프로젝트의 성향을 가지고있어 선택했습니다.

<br/>
  
핵심 기능
-------------
1. 스프링 시큐리티, @Validation을 활용한 자체 로그인 및 JWT 토큰 발행
2. 기본적인 중고거래 서비스 기능인 상품 등록, 찜, 흥정 여부 기능
3. WebSocket, Stomp, RabbitMQ를 활요한 동적 큐 구독, Topic Exchange 채팅 시스템

<br/>

FE & BE 시스템 아키텍처
-------------

<br/>

![FE   BE System Architecture](https://github.com/dorongrong/project-demo/assets/84131419/79e54099-0680-488a-8f77-a835fa1c3229)

<br/>

ERD 다이어그램
-------------

<br/>

![Copy of Copy of Copy of Project demo (1)](https://github.com/dorongrong/project-demo/assets/84131419/5291fc12-1c8e-4549-b61d-9d046396517f)

<br/>

엔티티 관계도
-------------

<br/>

![project-demo 엔티티 다이어그램](https://github.com/dorongrong/project-demo/assets/84131419/e2ce2569-60e3-4fd8-a669-3f4b840534eb)

<br/>

기능
-------------

<br/>

### 회원가입 & @Validation


* Spring Security를 활용한 비밀번호 암호화 회원가입 및 로그인
* @Validation 을 활용해 DTO 검증
* CustomLoginFailureHandler과 JwtProvider 구현으로 인한 권한없는 사용자 접근 제어

<img src="https://github.com/dorongrong/project-demo/assets/84131419/edfcd0c5-b56f-4ac1-a071-d30b6e1f5e29" width="400" height="500"/>

<img src="https://github.com/dorongrong/project-demo/assets/84131419/850c125b-db1f-4958-9e9b-0038d729ba35" width="400" height="500"/>

<br/>

<br/>

### 상품등록 & @Validation

* HttpServletRequest를 활용한 이미지 업로드 및 S3 스토리지 저장
* RedirectAttributes를 활용한 상품 업로드 후 리다이렉트

<img src="https://github.com/dorongrong/project-demo/assets/84131419/007f5aba-9692-4c63-98a1-709d35b135d3" width="500" height="600"/>



<br/>
<br/>

### 등록된 상품 상세정보

* 찜기능과 JWT 토큰을 사용한 본인검증후 채팅 혹은 내 채팅방 버튼 활성

<img src="https://github.com/dorongrong/project-demo/assets/84131419/edfe96d8-bdf5-40ac-89c7-14bc4d12d46a" width="800" height="600"/>

<br/>
<br/>


### QueryDsl 동적 검색 & Pageable

* Spring Data Jpa의 약점인 복잡한 조회를 QueryDsl 을 활용한 Type-Safe와 동적쿼리로 보완

<img src="https://github.com/dorongrong/project-demo/assets/84131419/1364ac8d-e1d3-480a-aed1-b99986b5b715" width="600" height="600"/>

<img src="https://github.com/dorongrong/project-demo/assets/84131419/7d280ab9-a299-4048-a510-d92c3695de07" width="600" height="600"/>


<br/>
<br/>


### RabbitMQ를 활용한 채팅 및 읽음기능

* 확장성을 위한 외부 채팅 서버
* Topic Exchange 및 동적 큐 생성으로 Topic 구독

<img src="https://github.com/dorongrong/project-demo/assets/84131419/56630282-1252-4ba5-9ad5-08aa59348ece" width="500" height="700"/>



<br/>
<br/>
