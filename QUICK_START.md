# ⚡ 빠른 시작 가이드

## 5분 안에 개발 환경 구축하기

### 전제 조건
- ✅ Docker Desktop 설치 완료
- ✅ Java 17+ 설치
- ✅ Node.js 18+ 설치

> 자세한 설치 방법은 [DEVELOPMENT_SETUP.md](./DEVELOPMENT_SETUP.md) 참고

---

## 1️⃣ 프로젝트 클론

```bash
git clone https://github.com/YOUR_USERNAME/community-platform.git
cd community-platform
```

---

## 2️⃣ Docker로 DB 시작

```bash
docker-compose up -d
```

**확인:**
```bash
docker-compose ps
# community-platform-db가 "Up" 상태여야 함
```

---

## 3️⃣ 백엔드 실행

**터미널 1번:**

```bash
# 윈도우
gradlew.bat bootRun

# 맥/리눅스
./gradlew bootRun
```

**확인:** http://localhost:8080/actuator/health

```json
{"status":"UP"}
```

---

## 4️⃣ 프론트엔드 실행

**터미널 2번:**

```bash
cd frontend
npm install
npm run dev
```

**확인:** http://localhost:5173

---

## ✅ 완료!

이제 개발을 시작할 수 있습니다!

- 회원가입: http://localhost:5173/register
- 로그인: http://localhost:5173/login

---

## 🛑 작업 종료

```bash
# 프론트엔드 중지: Ctrl + C
# 백엔드 중지: Ctrl + C

# Docker 중지 (선택사항)
docker-compose stop
```

---

## 🔄 다음 날 시작하기

```bash
# Docker가 중지되어 있다면
docker-compose start

# 백엔드 실행
./gradlew bootRun

# 프론트엔드 실행
cd frontend && npm run dev
```

---

## ❓ 문제 발생 시

### 포트 충돌 (5432 already in use)

```bash
# 포트 사용 중인 프로세스 확인
# 윈도우
netstat -ano | findstr :5432

# 맥/리눅스
lsof -i :5432

# 해결: 로컬 PostgreSQL 중지하거나 Docker 포트 변경
```

### Flyway 마이그레이션 실패

```bash
# DB 초기화
docker-compose down -v
docker-compose up -d
# 백엔드 재시작
```

### 더 자세한 도움말

[DEVELOPMENT_SETUP.md](./DEVELOPMENT_SETUP.md)의 FAQ 섹션을 확인하세요!
