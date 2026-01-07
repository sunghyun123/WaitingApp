# 학교 캡스톤 프로젝트

프로젝트 제목 : 웨이팅 앱/웹

개요 : 현재 웨이팅 대기열, 예상시간을 고객에게 보여주고 알림을 보낼 수 있는 웨이팅 앱/웹을 만든다

인스턴스 퍼블릭 ip 주소 : 52.78.88.82

rds 엔드포인트 : [mydb2025.c16agemciyrr.ap-northeast-2.rds.amazonaws.com](http://mydb2025.c16agemciyrr.ap-northeast-2.rds.amazonaws.com/)

예상 대기 시간 확인

- 명령어 정리
    
    터미널
    
    1. jar 파일 빌드 : ./gradlew build  
    2. 3306 포트 확인 : netstat -ano | findstr :3306
    3. 프로세스 중지 : taskkill /PID 2124 /F  
    
    cmd
    
    1. mysql 접속 : mysql -h [mydb2025.c16agemciyrr.ap-northeast-2.rds.amazonaws.com](http://mydb2025.c16agemciyrr.ap-northeast-2.rds.amazonaws.com/) -P 3306 -u admin -p
    
    git bash
    
    1. 인스턴스 접속 : ssh -i "C:/Users/SungHyun/my2025.pem" [ubuntu@gwnuwaiting.duckdns.org](mailto:ubuntu@gwnuwaiting.duckdns.org)
    2. jar 파일 인스턴스에 업로드 : 
    
    scp -i ~/my2025.pem C:/Users/SungHyun/IdeaProjects/Backend/build/libs/Backend-0.0.1-SNAPSHOT.jar [ubuntu@](mailto:ubuntu@3.36.61.211)[gwnuwaiting.duckdns.org](mailto:ubuntu@gwnuwaiting.duckdns.org):/home/ubuntu/
    
    1. 실행 : java -jar Backend-0.0.1-SNAPSHOT.jar
    2. 
    
    nohup java -jar Backend-0.0.1-SNAPSHOT..jar > output.log 2>&1 &
    

- ✅ 정리된 웨이팅 애플(엔드워크) 전체 시나리오 (Notion용)
    
    ## 1. 고객 입장 (User Flow)
    
    ### ▶ 메인 페이지
    
    - 현재 매장의 **대기 팀 수** 표시
    - **예산 대기 시간** 표시
    - "대기 등록하기" 버튼 제공
    
    ### ▶ 대기열 등록 페이지
    
    - 고객이 **휴대폰 번호 입력**
    - "등록하기" 버튼 클릭 시 대기 등록 완료
    - 등록 완료 후 안내 문구 표시 ("등록이 완료되었습니다" 등)
    
    ### ▶ 입장 알림 수신
    
    - 일정 대기 후, 관리자가 호추 후 알림톡/SMS 경로 개인과 연결되지 않고 알림 보내기
    
    ---
    
    ## 2. 관리자 입장 (Admin Flow)
    
    ### ▶ 관리자 로그인 페이지
    
    - ID/PW 입력 후 로그인
    - (신규 관리자 등록 기능 제공)
    
    ### ▶ 관리자 메인 페이지
    
    - **오늘 날짜** 기준:
        - 가까운 "현재 대기 중" 리스트
        - "입장 완료" 리스트 표시
    - 표시 항목:
        - 대기 번호
        - 전화번호 (4번째 이후 마스킹 가능)
        - 대기 등록 시간
        - (입장시) 입장 완료 시간
    
    ### ▶ 관리자 가능 기능
    
    - 호추 : 개개 고객에게 "입장 준비 안내" 톡 보내기
    - 예약 취소 : 개개 고객 대기 목록에서 제거
    - 입장 완료 처리 : 입장 처리 하고, 입장시간 기록
    
    ### ▶ 고객 검색 페이지
    
    - 전화번호 입력 시:
        - 고객 대기 정보 (대기번호, 휴대폰, 대기시간, 입장시간, 현재 상황) 조회
    
    ### ▶ 날짜별 통계 페이지
    
    - 달러 UI 제공
    - 특정 날짜 클릭 시:
        - 총 방문 인원수
        - 평균 대기시간 표시
    
    ### ▶ 날짜별 위이팅 기록 페이지
    
    - 선택한 날짜의 대기번호, 전화번호, 대기시간, 입장시간 목록 조회
    
    ---
    
    ## 3. 시스템 기능 (System Flow)
    
    ### ▶ 실시간 데이터 관리
    
    - 현재 대기 팀수 자동계산
    - 예산 대기시간 자동계산 및 홈 페이지에 전달
        - 계산 공식: (**현재 대기 팀수**) × (**기본 대기시간**)
    
    ### ▶ 대기 상황 가변 관리
    
    - 입장 처리 시:
        - 대기 목록에서 제거
        - 경로를 가진 가까운 목록에 입장시간과 함꺼 등록
    
    ### ▶ 대기 건 가지 결과 시간 업데이트
    
    - 개개 고객의 대기시간을 시간 가지고 자동 계산 하여 관리자에게 표시
    
    ### ▶ 하루 건 자동 초기화 기능 (추가)
    
    - 날짜가 넘어갈 경우:
        - 현재 "현재 대기 목록" 자동 초기화
        - 입장 파리, 입장 완료 파리에 계정
    
    ---
    
    # ✨ 전체 프로세스 표 (Summary)
    
    ```
    [고객]
    → 메인페이지 공간에서 대기수/예상대기시간 확인
    → 전화번호로 등록
    → 알림수신 후 매장 입장
    
    [관리자]
    → ID/PW 로그인
    → 현재/완료 목록 관리
    → 호추/취소/입장 처리
    → 고객검색, 통계 조회, 기록 관리
    → 대기시간 조정
    
    [시스템]
    → 대기수/예산시간 자동계산
    → 대기상황 시간 가지어 업데이트
    → 하루 건 변경시 자동 초기화
    
    ```
    
    ---
    

- 기능 분석
    - 고객
        1. 초기 화면에서 현재 웨이팅 인원 수를 알 수 있어야한다
        2. 초기 화면에서 현재 예상 대기 시간을 알 수 있어야한다
        3. 전화번호를 입력하여 웨이팅 번호를 부여받는다.
    - 관리자
        1. 초기 화면에서 로그인/비밀번호를 입력하여 관리자 메뉴로 진입할 수 있어야 한다.
        2. 관리자 메뉴에서 웨이팅 현황 버튼을 눌러 웨이팅 대기 목록을 반환 받을 수 있어야 한다.
        3. 대기 시간 조정을 눌러 현재 대기 시간을 변경할 수 있어야 한다.
        4. 웨이팅 대기 목록에서 현재 대기 인원의 대기번호/휴대폰번호/대기 시간을 받을 수 있어야한다.
        5. 각 웨이팅 목록에서 호출/알람/예약 취소 기능을 사용할 수 있어야 한다.
        6. 웨이팅 완료 목록을 받을 수 있어야한다.
        7. 고객 검색 페이지에서 날짜별로 조회가 가능하여야 한다.
        8. 고객 검색 페이지에서 핸드폰 번호로 조회가 가능하여야 한다.
        9. 날짜별 조회 페이지에서 해당 날짜의 웨이팅 기록 목록을 조회할 수 있어야 한다.
        10. 날짜별 조회 페이지에서 해당 날짜의 총 방문 인원 수/평균 대기 시간을 조회할 수 있어야 한다.

- 기능 세부 분석
    1. 현재 웨이팅 인원 조회
    2. 현재 예상 대기 시간 조회
    3. 휴대폰 번호를 입력받아 웨이팅 번호 부여
    4. id/pw를 입력받아 매장 로그인
    5. 현재 웨이팅 전체 대기 목록 조회
    6. 휴대폰 번호를 전달하여 서버로 부터 호출/입장 알림
    7. 예약 취소 버튼을 눌러 현재 대기 목록에서 제거
    8. 해당 날짜 웨이팅 완료 목록 조회
    9. 날짜별 총 방문 인원 수 조회
    10. 날짜별 평균 대기 시간 조회
    11. 날짜별 웨이팅 기록 조회
    12. 휴대폰 번호로 웨이팅 기록 조회
    13. 대기 시간 변경 기능

- 데이터 베이스 구조 설계
    
    
    [Customer]
    
    - id (PK)
    - phoneNumber(Unique)
    - waitingNumber //대기 순번
    - people //동반 인원 수
    - startTime //예약 시간
    - enteredTime //입장 시간
    - waitTime //웨이팅 시간
    - status //대기,입장,퇴장
    - date //날짜
    
    [Setting]
    
    - id (PK)
    - averageWaitTime //평균 대기 시간
    - tableCount //테이블 수
    - type //한식, 양식, 중식, 일식, 아시안식
    
    [WaitingConfig]
    
    - id (PK)
    - default_waiting_time (minutes)
    - updated_at

- ERD(Entity-Relationship Diagram)로 표현
    
    
    [Admin] ───< 관리
    
    [Waiting] (대기팀)
    
    - 전화번호
    - 상태(대기중, 완료, 취소)
    - 대기번호
    - 대기 시작시간
    - 입장시간
    - 호출 여부
    
    [WaitingConfig] (기본 대기시간 설정)
    
    - 기본 대기시간

- 개발 환경
    
    개발 언어 : 자바
    
    개발 프레임워크 : 스프링 부트
    
    데이터베이스 : MySql
    
    배포 : docker/aws ec2
    
    ![image.png](attachment:919d982a-6786-4245-8d65-91031fd1513b:image.png)
    
    ![image.png](attachment:9c9400be-4b93-4058-9182-97a0dd072bc1:image.png)
    
    ![image.png](attachment:c54304bd-ce6c-4a34-a62f-494dbc2b58bf:image.png)
    
    ![image.png](attachment:0c5b3926-cc19-4988-abca-1c4571462214:image.png)
    
    ![image.png](attachment:60779685-1c16-47f7-9c88-0851be96368c:image.png)
    

- 해당 프로젝트 설명서 (Notion 용)
    
    ### 프로젝트 개요
    
    - 클릭 함수: 컨설에 설정한 대기 번호, 확인하고자 하는 전화번호 구분을 기다릴 수 있는 필수 기능 구현
    - 필수 API 개발:
        - 대기 등록 / 취소시 현재 인원수 업데이트 API
        - 포토 입력시 대기 순위, 예상 대기 시간을 전달해주는 API
        - 로그인 API
        - 현재 대기 팀 목록 조회 API
        - 완료한 대기 팀 목록 조회 API
        - 전화번호로 검색시 해당 고객의 대기 회고 목록을 조회하는 API
        - 기간(날짜)을 입력해서 그 기간에 해당하는 고객 대기 회고 목록을 조회하는 API
        - 대기 예상 시간을 조정하는 API
    - 일반 프로젝트 프로세이싱이 가능한 간단한 프론트 테스트 페이지 구현
    
    ### 하단 개정 사항: 하루가 지나면 대기 목록 자동 초기화
    
    - 개념:
        - 대기 목록은 날짜가 변경되면 (다음 날이 되면) 자동으로 초기화한다.
    - 구현 방식:
        - 세그나널 시간과 인수가 관리되는 프로그램(예: 호시 프로겘 또는 cron job)을 이용.
        - 내부적으로 관리하는 파일에 현재 날짜를 기본으로 저장.
        - 현재 날짜와 해당 값이 둘째로 변경되면 팀 목록을 초기화.
    - 자세 구현:
        1. 서버 로그에 대기 팀 목록과 현재 날짜을 관리하는 파일 찾기
        2. 세그머에\uuc11c 해당 파일의 날짜가 변경되어있는지 판단
        3. 변경되었을 경우 팀 목록을 초기화
        4. 현재 날짜를 새로 업데이트
    
    ### 개발 규칙
    
    - 단일 상황에서 두 개 이상의 파일에서 날짜 목록을 관리하지 않는다.
    - 일정 오차가 발생할 경우에는 초기화 오류를 바로 감지할 수 있는 고도처리 구현.
    
    ### 참고
    
    - cron job (Spring Boot에서 Scheduled annotation을 이용)
    - 파일 저장에서 날짜가 필요한 경우 Properties와 같이 관리

- API 명세서
    
    
    | 기능 | 메서드 | URL | 요청데이터 | 응답데이터 |
    | --- | --- | --- | --- | --- |
    | 메인 대기 상태 조회 | GET | /api/waiting/main | 없음 | 현재 대기팀 수, 예상 대기시간 |
    | 대기 등록 | POST | /api/waiting/register | 전화번호 | 대기 등록 결과 |
    | 관리자 로그인 | POST | /api/admin/login | ID, PW | JWT 토큰 |
    | 신규 관리자 등록 | POST | /api/admin/register | ID, PW | 등록 결과 |
    | 대기 목록 조회 | GET | /api/admin/waiting-list | 없음 | 현재 대기 목록 |
    | 완료 목록 조회 | GET | /api/admin/completed-list | 없음 | 완료된 대기 목록 |
    | 고객 호출 알람 전송 | POST | /api/admin/call | 대기 ID | 호출 결과 |
    | 고객 예약 취소 | POST | /api/admin/cancel | 대기 ID | 취소 결과 |
    | 입장 완료 처리 | POST | /api/admin/complete | 대기 ID | 완료 처리 결과 |
    | 고객 검색 | POST | /api/admin/search-customer | 전화번호 | 해당 고객의 대기 이력 |
    | 기본 대기시간 조정 | POST | /api/admin/set-waiting-time | 새로운 기본 대기시간 | 설정 결과 |
    | 날짜별 통계 조회 | GET | /api/statistics/{date} | 없음 | 총 방문자수, 평균 대기시간 |
    | 날짜별 웨이팅 기록 조회 | GET | /api/statistics/records/{date} | 없음 | 해당 날짜 대기 기록 리스트 |

- 프로젝트 디렉토리 구조
    
    /waiting-app
    ├── src/main/java/com/example/waitingapp
    │    ├── config/
    │    │    └── SecurityConfig.java (로그인/보안 설정)
    │    ├── controller/
    │    │    ├── AdminController.java
    │    │    ├── CustomerController.java
    │    │    ├── WaitingController.java
    │    │    ├── StatisticsController.java
    │    ├── dto/
    │    │    ├── LoginRequest.java
    │    │    ├── WaitingRegisterRequest.java
    │    │    ├── WaitingResponse.java
    │    │    ├── CustomerSearchRequest.java
    │    │    ├── StatisticsResponse.java
    │    ├── entity/
    │    │    ├── Waiting.java
    │    │    ├── Customer.java
    │    │    ├── Admin.java
    │    ├── repository/
    │    │    ├── WaitingRepository.java
    │    │    ├── CustomerRepository.java
    │    │    ├── AdminRepository.java
    │    ├── service/
    │    │    ├── WaitingService.java
    │    │    ├── CustomerService.java
    │    │    ├── AdminService.java
    │    │    ├── StatisticsService.java
    │    ├── scheduler/
    │    │    └── DailyResetScheduler.java (날짜 변경시 대기 목록 초기화)
    │    └── WaitingAppApplication.java
    ├── src/main/resources/
    │    ├── application.yml (DB설정, JWT설정 등)
    └── build.gradle
