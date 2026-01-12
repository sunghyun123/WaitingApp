# 📌 웨이팅 앱 / 웹 (Waiting App)

매장 방문 고객의 대기 현황과 예상 대기 시간을 확인하고,
관리자가 대기 목록을 관리할 수 있는 웨이팅 관리 서비스입니다.

Spring Boot 기반의 백엔드 API 서버를 직접 설계·구현하고
AWS EC2 + RDS 환경에 배포하는 것을 목표로 진행한 프로젝트입니다.

###  아키텍쳐
![img.png](img.png)


### 🛠️ 기술 스택

* Backend: Java, Spring Boot

* Database: MySQL (AWS RDS)

* Infra: AWS EC2

* Build / Deploy: Gradle, JAR 배포

* Version Control: Git, GitHub

### ✨ 주요 기능

* 고객 기능

* 현재 웨이팅 팀 수 조회

* 예상 대기 시간 조회

* 휴대폰 번호로 웨이팅 등록

* 관리자 기능

* 관리자 로그인

* 현재 대기 목록 조회

* 대기 완료 / 취소 처리

* 고객 전화번호로 대기 이력 조회

* 날짜별 방문 인원 및 평균 대기 시간 조회

### 🧩 핵심 구현 내용

* 현재 대기 팀 수를 기준으로 예상 대기 시간 자동 계산
* 대기 상태를 대기 / 완료 / 취소로 구분하여 관리
* 날짜 변경 시 대기 목록을 초기화하기 위해 Spring Scheduler(@Scheduled) 사용
* Controller / Service / Repository 계층 분리로 기본적인 구조 설계

### 📁 프로젝트 구조

src/main/java/com/example/waitingapp

 ├── controller

 ├── service

 ├── repository

 ├── entity

 ├── dto

 └── scheduler

### 🚀 실행 방법

**프로젝트 빌드**

* ./gradlew build


**JAR 실행**

* java -jar Backend-0.0.1-SNAPSHOT.jar
