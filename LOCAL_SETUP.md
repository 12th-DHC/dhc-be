# Windows 로컬 RabbitMQ 테스트

Spring 서버는 IntelliJ에서 실행하고, RabbitMQ는 WSL 또는 Docker에서 실행합니다.
이미 WSL에 RabbitMQ를 설치했다면 Docker 설치 및 실행 단계는 건너뜁니다.
WSL 터미널에서 `sudo service rabbitmq-server start`로 실행하고,
`sudo rabbitmq-plugins enable rabbitmq_management`로 관리 화면을 활성화합니다.
`.env`의 RabbitMQ 아이디와 비밀번호는 WSL에서 생성한 계정과 일치해야 합니다.
WSL RabbitMQ와 Docker RabbitMQ를 같은 포트로 동시에 실행하지 마세요.
이 구성에는 MySQL이 포함되어 있지 않습니다. 기존 로컬 MySQL의 dhc DB를 사용합니다.

## 1. 최초 설치

1. [Docker Desktop for Windows](https://docs.docker.com/desktop/setup/install/windows-install/)를 설치합니다. 설치 중 WSL 2 옵션을 사용하고 재시작 안내가 나오면 재시작합니다.
2. Docker Desktop을 실행하고 엔진이 실행될 때까지 기다립니다. WSL 업데이트 안내가 나오면 해당 안내를 따릅니다.
3. IntelliJ 터미널을 새로 열고 `docker version`을 실행해 Client와 Server가 모두 표시되는지 확인합니다.
4. 프로젝트는 JDK 21을 요구합니다. IntelliJ Project Structure의 Project SDK를 21로 설정합니다. 없으면 Download JDK에서 21을 설치합니다. Settings → Build, Execution, Deployment → Build Tools → Gradle의 Gradle JVM도 21로 설정합니다.

## 2. 환경 파일

프로젝트 루트(build.gradle이 있는 폴더)의 `.env`를 사용합니다. 파일이 없다면:

```powershell
Copy-Item .env.example .env
```

이미 있는 `.env`는 덮어쓰지 마세요. `DB_PASSWORD`를 본인 로컬 MySQL 비밀번호로 변경합니다.
DB 주소/계정이 다르면 `DB_URL`, `DB_USERNAME`도 수정합니다. MySQL에 `dhc` DB가 있어야 합니다.
RabbitMQ 값은 처음에는 예시 그대로 사용하면 됩니다.

Spring이 `.env`를 properties 파일로 직접 읽으므로 별도 EnvFile 플러그인은 필요 없습니다.
`KEY=value` 형식을 사용하고 값에 따옴표를 붙이지 마세요. 이 방식은 일반 dotenv의 모든 문법을 지원하지 않습니다.
IntelliJ 실행 구성의 Working directory는 프로젝트 루트(`$PROJECT_DIR$`)로 설정합니다.
기존 실행 구성의 동일한 환경 변수가 있으면 `.env`보다 우선하므로 확인합니다.
`.env`는 Git에서 제외되고 `.env.example`만 공유됩니다. 예시 비밀번호는 로컬 전용입니다.

## 3. RabbitMQ 실행 및 큐 생성

프로젝트 루트의 PowerShell에서:

```powershell
docker compose -f compose.local.yml up -d
docker compose -f compose.local.yml ps
```

상태가 healthy가 될 때까지 기다립니다. 시작에 실패하면:

```powershell
docker compose -f compose.local.yml logs --tail 100 rabbitmq
```

1. http://localhost:15672 에 접속합니다.
2. `.env`의 계정으로 로그인합니다. 초기 예시는 `dhc` / `dhc-local-password`입니다.
3. Queues and Streams → Add a new queue에서 Virtual host `/`, Name `notification.test`, Durability `Durable`로 생성합니다. 나머지는 기본값을 사용합니다.

현재 애플리케이션은 큐를 자동 생성하지 않습니다. 반드시 API 호출 전에 생성하세요.
빈 exchange는 기본 exchange를 의미하며 routing key가 큐 이름과 일치해야 합니다.
큐와 데이터는 Docker 볼륨에 보관됩니다. 최초 실행 뒤 `.env`의 계정만 바꿔도 기존 RabbitMQ 계정이 변경되는 것은 아닙니다.

## 4. 앱 실행 및 발행 확인

1. 로컬 MySQL을 실행합니다.
2. IntelliJ에서 Spring Boot 메인 클래스를 실행합니다.
3. 테스트 날짜의 청소 검사 결과와 연결된 호실 데이터를 준비합니다. 학생 A/B의 이름과 이메일, 일반 검사 합격 여부가 모두 필요하며 일반 검사 불합격이면 사유도 필요합니다.
4. Swagger(http://localhost:8080/swagger-ui/index.html) 또는 Postman에서 관리자 로그인 후 관리자 액세스 토큰을 사용합니다.
5. `POST /admin/alarm`에 `{"date":"2026-09-29"}`를 보냅니다. 날짜는 준비한 검사 날짜로 바꿉니다. Authorization 헤더는 `Bearer <관리자 액세스 토큰>`입니다.
6. HTTP 202를 확인하고 RabbitMQ 관리 화면에서 `notification.test` → Get messages로 실제 JSON을 확인합니다. 검사 결과가 있는 호실 1개는 학생 A/B 총 2건입니다. 합격이면 `reason: null`, 불합격이면 사유가 있어야 합니다.

검사 데이터가 없는 날짜는 202여도 메시지가 없습니다. 202만으로 큐 도착을 검증할 수 없습니다.
같은 요청을 반복하면 메시지가 중복 발행됩니다. 이 테스트는 큐 도착 확인이며 실제 이메일 발송 확인은 별도 소비자가 필요합니다.

## 5. 단위 테스트와 PR

실제 RabbitMQ 연결과 NotificationPublisher의 JSON 발행·수신을 테스트하려면,
RabbitMQ가 실행 중인 상태에서 JDK 21을 사용하는 PowerShell에서 실행합니다.

```powershell
$env:RUN_RABBITMQ_INTEGRATION='true'
.\gradlew.bat test --tests project.dhc.NotificationServiceTest --tests project.dhc.NotificationRabbitIntegrationTest
Remove-Item Env:RUN_RABBITMQ_INTEGRATION
```

통합 테스트는 루트 `.env`의 접속 정보를 사용합니다. 임시 큐를 생성해 합격·불합격
메시지 2건과 JSON 변환을 확인한 뒤 큐를 삭제합니다. DB나 관리자 토큰은 필요하지 않습니다.
환경 변수 RUN_RABBITMQ_INTEGRATION=true가 없으면 이 통합 테스트는 건너뜁니다.
이 테스트는 `/admin/alarm` HTTP API나 DB 조회까지 검증하지는 않습니다.

IntelliJ에서 `NotificationServiceTest`를 실행하거나 JAVA_HOME이 JDK 21인 터미널에서:

```powershell
.\gradlew.bat test --tests project.dhc.NotificationServiceTest
```

이 테스트는 Publisher를 mock 처리하므로 실제 RabbitMQ 연결은 위 수동 테스트로 확인합니다.
PR에 실행한 테스트 결과, HTTP 응답 및 큐의 JSON 캡처(테스트 데이터)를 첨부합니다.
`.env`는 커밋하지 않습니다. 관련 소스 변경과 `.env.example`, `compose.local.yml`, 이 문서를 함께 검토합니다.

작업 후 RabbitMQ만 중지하려면:

```powershell
docker compose -f compose.local.yml stop
```

## 자주 막히는 부분

- docker 명령을 찾지 못함: Docker Desktop 설치 후 IntelliJ/터미널을 다시 엽니다.
- Docker daemon 연결 실패: Docker Desktop이 실행 중인지 확인합니다.
- 5672 또는 15672 포트 충돌: 기존 RabbitMQ/컨테이너가 해당 포트를 사용하는지 확인합니다.
- MySQL Access denied: `.env`의 DB 계정과 비밀번호를 확인합니다.
- Unknown database 'dhc': 로컬 MySQL에서 `CREATE DATABASE dhc;`로 DB를 생성합니다.
- 401/403: 관리자 로그인 토큰인지 확인합니다.
- NOTIFICATION_ROUTING_KEY 설정 오류: Working directory가 프로젝트 루트인지 확인하고 앱을 재시작합니다.
