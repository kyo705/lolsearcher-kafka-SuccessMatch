# SuccessMatches App

> [LolSearcher App](https://github.com/kyo705/LolSearcher#lolsearcher)과
> [FailMatchIds App](https://github.com/kyo705/lolsearcher-persistance-failMatchId#failmatchids-app)
> 으로부터 발생한 match 데이터들을 데이터베이스에 저장하는 애플리케이션


## 프로젝트 깃 브런치

> - **main** — 실제 메인 브런치(완성본)
> - **develop** — 다음 버전을 위한 개발 브런치(테스트용)

## 프로젝트 커밋 메시지 카테고리

> - [INITIAL] — repository를 생성하고 최초에 파일을 업로드 할 때
> - [ADD] — 신규 파일 추가
> - [UPDATE] — 코드 변경이 일어날때
> - [REFACTOR] — 코드를 리팩토링 했을때
> - [FIX] — 잘못된 링크 정보 변경, 필요한 모듈 추가 및 삭제
> - [REMOVE] — 파일 제거
> - [STYLE] — 디자인 관련 변경사항

## 프로젝트 내 적용 기술
> - Back-End
>   - 언어 : Java
>   - 프레임 워크 : SpringBoot
>   - 빌드 관리 툴 : Gradle
>   - ORM : JPA(Hibernate)
> - DevOps
>   - MessageQueue : Kafka
>   - DBMS : MariaDB

## 기능 요구 사항
> 1. MessageQueue로부터 매치 데이터들을 가져옴
> 2. 가져온 Match 데이터를 DB에 저장함
> 3. 1~2번까지 과정을 계속 반복