# 📊 Board Excel Download

## 개요

게시글(`Board`) 데이터를 **엑셀(.xlsx)** 파일로 다운로드하는 기능.
Apache POI 기반이며 기본은 **XSSFWorkbook**, 프로파일로 **SXSSFWorkbook(스트리밍)** 전환 가능.

---

## 구현 범위 (1차)

### 기능

* 게시글 목록 엑셀 다운로드
* `.xlsx` 형식 지원 (Apache POI XSSF)
* Chunk 단위 데이터 조회
* 최대 다운로드 행 수 제한
* 동시 다운로드 요청 제한
* 실행 로그 기록
- Workbook 생성 책임 분리 (`ExcelWorkbookFactory`)
- 프로파일 기반 XSSF/SXSSF 전환

---

## 구조

| 클래스                       | 역할                         |
| ------------------------- | -------------------------- |
|---------------------------|----------------------------|
| `ExcelDownloadController` | 엑셀 다운로드 API                |
| `ExcelDownloadService`    | Workbook 생성 및 응답 스트림 write |
| `ExcelDownloadPolicy`     | XSSF 최대 행수 제한              |
| `ExcelDownloadLimiter`    | 동시 다운로드 제한                 |
| `ExcelWorkbookFactory`    | Workbook 생성 추상화            |
| `XssfWorkbookFactory`     | 기본(XSSF) Workbook 생성       |
| `SxssfWorkbookFactory`    | 스트리밍(SXSSF) Workbook 생성    |

---

## 엑셀 컬럼

| 컬럼        | 설명     |
|-----------|--------|
| ID        | 게시글 번호 |
| Title     | 제목     |
| Writer    | 작성자    |
| ViewCount | 조회수    |
| CreatedAt | 게시일    |
| UpdatedAt | 수정일    |

> `content` 컬럼은 `@Lob` 필드로 길이가 길어
> 파일 크기 증가 및 성능 저하를 방지하기 위해 제외

---

## 구현 방식

### XSSFWorkbook 기반 생성

* 전체 워크북을 메모리에 유지하는 방식
* 단순한 구조로 빠른 구현 가능
- Workbook 생성은 `ExcelWorkbookFactory`로 통일

```java
try (Workbook wb = workbookFactory.create()) {
    boardExcelWriter.write(wb);
    wb.write(outputStream);
}
```

---

### SXSSFWorkbook (스트리밍)

- 프로파일: `excel-sxssf`
- window size = 200
- temp 파일 압축 사용

---

### Chunk 조회

- `LIMIT` 기반 조회
- 메모리 사용량 최소화 목적

```text
page = 0
size = 5000
while (chunk not empty) {
}
```

---

### 최대 행수 제한

* 사전 count 조회 후 제한 초과 시 다운로드 차단
* XSSFWorkbook 메모리 한계 방어 목적
- 사전 count 조회 후 제한 초과 시 다운로드 차단
- XSSFWorkbook 메모리 사용량 고려

```text
MAX_ROWS_XSSF = 30,000
```

---

### 동시 다운로드 제한

* 엑셀 다운로드 요청은 WAS 스레드와 DB 커넥션을 장시간 점유
* Semaphore 기반 동시 실행 제한 적용
- Semaphore 기반 제한 적용

```text
MAX_CONCURRENT_DOWNLOADS = 3
```

---

### 로그

* 다운로드 row 수
* 실행 시간
* 성공 / 실패 여부
- 다운로드 row 수
- 실행 시간
- 성공 / 실패 여부

---


### 대용량 데이터

* XSSFWorkbook 특성상 행 수 증가 시 메모리 사용량 급증
* 행 수 제한으로만 방어 중
- XSSFWorkbook 특성상 메모리 사용량이 빠르게 증가
- 행수 제한으로 방어

---

### 서버 부하/병목

* 요청 1건당 톰캣 스레드 1개 장시간 점유
* 동시 요청 증가 시 일반 API 응답 지연 가능
* 동시 실행 제한으로 부분 완화 상태
- 동시 요청 증가 시 일반 API 응답 지연 가능
- 동시 다운로드 제한으로 완화

---

### 동기 처리

* 요청 → 생성 → 다운로드까지 동기 처리
* 데이터 증가 시 사용자 대기 시간 증가

---


## 향후 개선 계획

- 대용량 다운로드 UX 개선
- 비동기 엑셀 생성 방식 검토

> 현재 상태는 **WorkbookFactory 적용 + XSSF/SXSSF 전환 가능** 상태.
