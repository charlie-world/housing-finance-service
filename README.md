# housing-finance-service

## 개발 스펙

```sh
- jdk: openjdk 1.8.0
- languge: scala 2.12.7
- orm: slick 3.3.0
- web framework: akka http 10.0.11
- jwt: java-jwt 3.8.2
- data type: monix.task 2.3.0
```

## 실행 방법

```sh
(docker 가 설치 되어 있다는 가정 안에서, jar 폴더 내의 미리 만들어진 jar 파일로 docker build)
docker-compose up
```

## 완전 빌드 를 하고 싶다면

```sh
(sbt 1.2.6 버전 설치)
sbt assembly
cp ./target/scala-2.12/app.jar ./jar/app.jar

docker-compose up
```


## 개발 전략

### 1 데이터 삽입 API

#### API
```yaml
POST http://localhost:8080/api/v1/housing-finance/init

Request Header
Authorization: Bearer <JWT_TOKEN>

Response
{
  "name": "주택금융 공급현황",
  "data": [
    {
      "year": 2005, 
      "total_amount": 150123, 
      "detail_amount": {
        "국민은행": 1234,
        ...
      }
    },
    ...
  ]
}
```
#### 해당 작업 수행 원리 O(n) // n 은 파일의 라인 수

```yaml
1. CSV file 을 line 단위로 읽고, HousingFinanceFileEntity 로 변환 시킨다.

case class HousingFinanceFileEntity(instituteId: Long, year: Int, month: Int, amount: Long)

2. 모든 HousingFinanceFileEntity 들 (Seq[HousingFinanceFileEntity]) 을 (instituteId, year) 로 grouping 한다.
3. 그룹핑 된 단위로 DB 에서 credit_guarantee 테이블에 해당 엔티티가 있는지 조회
4. update or insert 작업 수행 후 (instituteId, year) return
5. 4에서 얻은 (instituteId, year) 기준으로 DB 조회를 통해 total_amount, avg_amount 를 년도별로 구하고, 해당 사항 summary_table 에 저장
```


### 2 년도별 각 기관 의 전체 지원금 중 가장 큰 기관 과 년도 출력 API


#### API
```yaml
GET http://localhost:8080/api/v1/housing-finance/most-supported-institute

Request Header
Authorization: Bearer <JWT_TOKEN>

Response
{
  "year": "2018",
  "bank": "국민은행"
}
```
#### 해당 작업 수행 원리 O(n) // n = summary table 의 entity 개수

```yaml
1. summary_table 에 
select year, instituteId from summary
join institute on
institute.institute_id = summary.institute_id 
order by total_amount desc limit 1

구문 수행 후 나오는 (institute_name, year) 을 return

2. 해당 사항을 json format 에 맞게 response

```


### 3 특정 기관의 년도별 평균 지원 금액 중 가장 작은 값 과 큰 값을 반환 API


#### API
```yaml
GET http://localhost:8080/api/v1/housing-finance/institute/min-and-max-annual-amount?institute-name={기관명}

Request Header
Authorization: Bearer <JWT_TOKEN>

Response
{
  "bank": "국민은행", 
  "support_amount": [
    {
      "year": 2017,
      "amount": 0
    },
    {
      "year": 2006,
      "amount": 123
    }
  ]
}
```
#### 해당 작업 수행 원리 O(n) // n = summary table 의 entity 개수

```yaml
1. summary_table 에 
select year, avg_amount from summary
join institute on
institute.institute_id = summary.institute_id
where institute_name = "국민은행" 
order by avg_amount desc

구문 수행 후 나오는 Seq[(year, avg_amount)] 를 head 와 last 만 추출 후 return

2. 해당 사항을 json format 에 맞게 response

```
 

### (선택문제) 유저서비스 구현


#### APIs
```yaml
회원 가입
POST http://localhost:8080/api/v1/users

Request Body
{
  "email": "abc@abc.com",
  "password": "password"
}

Response entity
{
  "jwt": "asdfasdfasdfasdfasdfasdfasdfa"
}


로그인
POST http://localhost:8080/api/v1/users/login

Request Body
{
  "email": "abc@abc.com",
  "password": "password"
}

Response entity
{
  "jwt": "asdfasdfasdfasdfasdfasdfasdfa"
}

토큰 갱신
GET http://localhost:8080/api/v1/users/refresh

Request Header
Authorization: Bearer <JWT_TOKEN>

Response entity
{
  "jwt": "asdfasdfasdfasdfasdfasdfasdfa"
}

```

#### 구현 특이 사항

```yaml
- jwt 토큰 생성 시 expireAt 을 현 시간 기준으로 1시간으로 설정
- secret 은 코드 안에 있는 static 한 값으로 서명
- payload 에 user_id 필드를 넣어 token verify 시 user_id 추출 이 가능
- verify 시 expireAt 시간이 지나면 401 리턴
- refresh 시 제대로 된 jwt token 이라면 새로운 token 생성 후 리턴, 그외 401 리턴
```

