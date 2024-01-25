중고거래 웹 서비스 프로젝트
=============

프로젝트 기획 동기
-------------

* 백엔드의 기본적인 기본기를 다지면서 만들수있는 프로젝트를 찾아보았습니다.
* 보통 쇼핑몰과 게시판을 주로 하는데 중고거래 사이트가 쇼핑몰과 게시판 두 프로젝트의 성향을 가지고있어 선택했습니다.
  
핵심 기능
-------------
1. 스프링 시큐리티, @Validation을 활용한 자체 로그인 및 JWT 토큰 발행
2. 기본적인 중고거래 서비스 기능인 상품 등록, 찜, 흥정 여부 기능
3. WebSocket, Stomp, RabbitMQ를 활요한 동적 큐 구독, Topic Exchange 채팅 시스템

FE & BE 시스템 아키텍처
-------------

![FE   BE System Architecture](https://github.com/dorongrong/project-demo/assets/84131419/79e54099-0680-488a-8f77-a835fa1c3229)


ERD 다이어그램
-------------

![Copy of Copy of Copy of Project demo (1)](https://github.com/dorongrong/project-demo/assets/84131419/5291fc12-1c8e-4549-b61d-9d046396517f)



엔티티 관계도
-------------

![project-demo 엔티티 다이어그램](https://github.com/dorongrong/project-demo/assets/84131419/e2ce2569-60e3-4fd8-a669-3f4b840534eb)



