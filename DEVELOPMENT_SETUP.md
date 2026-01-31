# 개발 환경 설정 가이드

이 가이드는 윈도우 데스크톱과 맥북에서 프로젝트를 설정하는 방법을 설명합니다.

---

## 📋 목차

1. [Docker 설치](#1-docker-설치)
2. [프로젝트 클론](#2-프로젝트-클론)
3. [로컬 개발 환경 시작](#3-로컬-개발-환경-시작)
4. [백엔드 실행](#4-백엔드-실행)
5. [프론트엔드 실행](#5-프론트엔드-실행)
6. [프로덕션 배포](#6-프로덕션-배포)
7. [FAQ](#7-faq)

---

## 1. Docker 설치

### 윈도우 (데스크톱)

1. **Docker Desktop 다운로드**
   - 공식 사이트: https://www.docker.com/products/docker-desktop/
   - "Download for Windows" 클릭

2. **설치**
   - 다운로드한 `Docker Desktop Installer.exe` 실행
   - WSL 2 백엔드 사용 옵션 체크 (권장)
   - 설치 완료 후 재부팅

3. **설치 확인**
   ```bash
   # PowerShell 또는 CMD에서 실행
   docker --version
   docker-compose --version
   ```

### 맥북 (MacOS)

1. **Docker Desktop 다운로드**
   - 공식 사이트: https://www.docker.com/products/docker-desktop/
   - Apple Silicon (M1/M2/M3): "Download for Mac - Apple Chip"
   - Intel: "Download for Mac - Intel Chip"

2. **설치**
   - 다운로드한 `.dmg` 파일 실행
   - Docker 아이콘을 Applications 폴더로 드래그
   - Docker Desktop 실행

3. **설치 확인**
   ```bash
   # 터미널에서 실행
   docker --version
   docker-compose --version
   ```

---

## 2. 프로젝트 클론

```bash
# GitHub에서 프로젝트 클론
git clone https://github.com/YOUR_USERNAME/community-platform.git
cd community-platform
```

---

## 3. 로컬 개발 환경 시작

### 3-1. Docker로 PostgreSQL 시작

```bash
# 프로젝트 루트에서 실행
docker-compose up -d
```

**명령어 설명:**
- `up`: 컨테이너 시작
- `-d`: 백그라운드 실행 (detached mode)

**확인:**
```bash
# 컨테이너 상태 확인
docker-compose ps

# 로그 확인
docker-compose logs postgres

# PostgreSQL 연결 테스트
docker exec -it community-platform-db psql -U postgres -d community
# 성공하면 postgres=# 프롬프트가 나타남
# \q 로 종료
```

### 3-2. Docker 중지 및 재시작

```bash
# 중지 (데이터 보존)
docker-compose stop

# 재시작
docker-compose start

# 완전히 제거 (데이터 삭제)
docker-compose down -v

# 다시 시작
docker-compose up -d
```

---

## 4. 백엔드 실행

### 4-1. Gradle 빌드 및 의존성 설치

```bash
# 윈도우
gradlew.bat build

# 맥/리눅스
./gradlew build
```

### 4-2. 로컬 개발 모드로 실행

```bash
# 윈도우
gradlew.bat bootRun --args='--spring.profiles.active=dev'

# 맥/리눅스
./gradlew bootRun --args='--spring.profiles.active=dev'
```

**또는 IDE에서 실행:**

**IntelliJ IDEA:**
1. `CommunityApplication.java` 우클릭
2. "Modify Run Configuration..."
3. VM options에 `-Dspring.profiles.active=dev` 추가
4. Run

**VS Code:**
1. `.vscode/launch.json` 생성
```json
{
  "version": "0.2.0",
  "configurations": [
    {
      "type": "java",
      "name": "Community Platform (Dev)",
      "request": "launch",
      "mainClass": "com.example.community.CommunityApplication",
      "vmArgs": "-Dspring.profiles.active=dev"
    }
  ]
}
```

### 4-3. 백엔드 동작 확인

브라우저에서:
- 헬스체크: http://localhost:8080/actuator/health
- API 테스트: http://localhost:8080/api-test.html

---

## 5. 프론트엔드 실행

```bash
cd frontend

# 의존성 설치 (처음 한 번만)
npm install

# 개발 서버 시작
npm run dev
```

브라우저에서 http://localhost:5173 접속

---

## 6. 프로덕션 배포

### 6-1. Supabase 환경변수 설정

**방법 1: 환경변수 사용 (권장)**

```bash
# 윈도우 (PowerShell)
$env:SPRING_PROFILES_ACTIVE="prod"
$env:DB_URL="jdbc:postgresql://YOUR-REGION.pooler.supabase.com:5432/postgres"
$env:DB_USERNAME="postgres.YOUR-PROJECT-ID"
$env:DB_PASSWORD="YOUR-DATABASE-PASSWORD"
$env:JWT_SECRET="YOUR-BASE64-ENCODED-SECRET"

# 맥/리눅스
export SPRING_PROFILES_ACTIVE=prod
export DB_URL="jdbc:postgresql://YOUR-REGION.pooler.supabase.com:5432/postgres"
export DB_USERNAME="postgres.YOUR-PROJECT-ID"
export DB_PASSWORD="YOUR-DATABASE-PASSWORD"
export JWT_SECRET="YOUR-BASE64-ENCODED-SECRET"
```

**방법 2: application-prod.properties 직접 수정 (비권장)**

`src/main/resources/application-prod.properties`에서 직접 값 입력
(주의: Git에 커밋하지 말 것!)

### 6-2. 프로덕션 모드로 실행

```bash
# 윈도우
gradlew.bat bootRun --args='--spring.profiles.active=prod'

# 맥/리눅스
./gradlew bootRun --args='--spring.profiles.active=prod'
```

---

## 7. FAQ

### Q1. "docker-compose: command not found" 에러

**A:** Docker Desktop이 실행 중인지 확인하세요.
- 윈도우: 작업 표시줄에서 Docker 아이콘 확인
- 맥: 메뉴바에서 Docker 아이콘 확인

### Q2. 포트 5432가 이미 사용 중이라는 에러

**A:** 로컬에 PostgreSQL이 이미 설치되어 있을 수 있습니다.

**해결 방법 1: 로컬 PostgreSQL 중지**
```bash
# 윈도우
services.msc 실행 → PostgreSQL 서비스 중지

# 맥
brew services stop postgresql
```

**해결 방법 2: Docker 포트 변경**
`docker-compose.yml`에서:
```yaml
ports:
  - "5433:5432"  # 5432 → 5433으로 변경
```

그리고 `application-dev.properties`에서:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5433/community
```

### Q3. Flyway 마이그레이션 에러

**A:** DB를 완전히 초기화하고 다시 시작:
```bash
docker-compose down -v
docker-compose up -d
# 애플리케이션 재시작
```

### Q4. 윈도우와 맥북에서 DB 데이터 공유하고 싶어요

**A:** Docker 볼륨은 로컬 저장이므로 공유되지 않습니다.

**옵션 1: Git으로 스키마만 공유 (권장)**
- Flyway 마이그레이션 파일은 Git에 커밋됨
- 각 환경에서 자동으로 동일한 스키마 생성

**옵션 2: 개발용 Supabase 사용**
- 무료 Supabase 프로젝트 생성
- 두 환경에서 동일한 Supabase 접속

### Q5. JWT Secret은 어떻게 생성하나요?

```bash
# 리눅스/맥
echo -n "my-super-secret-jwt-key-12345" | base64

# 윈도우 (PowerShell)
[Convert]::ToBase64String([Text.Encoding]::UTF8.GetBytes("my-super-secret-jwt-key-12345"))
```

### Q6. 프로파일을 지정하지 않으면?

**A:** 기본값은 `dev`입니다. (`application.properties`에서 설정)

---

## 🎯 빠른 시작 체크리스트

### 처음 시작할 때

- [ ] Docker Desktop 설치 및 실행
- [ ] 프로젝트 클론
- [ ] `docker-compose up -d` 실행
- [ ] 백엔드 실행 (`./gradlew bootRun`)
- [ ] 프론트엔드 실행 (`cd frontend && npm install && npm run dev`)
- [ ] http://localhost:5173 접속 확인

### 매일 개발할 때

- [ ] Docker Desktop 실행
- [ ] `docker-compose start` (중지했었다면)
- [ ] 백엔드 실행
- [ ] 프론트엔드 실행

### 작업 끝날 때

- [ ] 코드 커밋 및 푸시
- [ ] `docker-compose stop` (선택사항, 계속 켜둬도 됨)

---

## 📞 문제 발생 시

1. Docker 컨테이너 로그 확인: `docker-compose logs -f`
2. 백엔드 로그 확인 (콘솔 출력)
3. 프론트엔드 브라우저 콘솔 확인 (F12)

궁금한 점이 있으면 이슈를 등록해주세요!
