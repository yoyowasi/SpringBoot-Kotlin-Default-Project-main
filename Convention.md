# [project-name] Server Convention( Style Guide )

## Layout
```sh
  Project
   │  README.md
   │  Convention.md
   ├─ core
   │   ├─  dto
   │   └─  entity
   └─infra
   │   ├─  config
   │   └─  repository
   └─ lib
   │   ├─  annotation
   │   ├─  component
   │   ├─  dto
   │   ├─  enum
   │   ├─  exception
   │   ├─  handler
   │   ├─  interceptor
   │   ├─  object
   │   └─  util
   └─ main
       ├─  Controller
       ├─  Service
       └─  Component
```

## Functions

### A/HC/LC Pattern

There is a useful pattern to follow when naming functions:

```
prefix? + action (A) + high context (HC) + low context? (LC)
```

Take a look at how this pattern may be applied in the table below.

| Name                   | Prefix   | Action (A) | High context (HC) | Low context (LC) |
|------------------------|----------|------------|-------------------|------------------|
| `getUser`              |          | `get`      | `User`            |                  |
| `getUserMessages`      |          | `get`      | `User`            | `Messages`       |
| `shouldDisplayMessage` | `should` | `Display`  | `Message`         |                  |
| `isPaymentEnabled`     | `is`     | `Enabled`  | `Payment`         |                  |

---

## variable

- Pick **camelCase** naming convention and follow it.

```kotlin
/* Bad */
val page_count = 5
val active = true
val ShouldUpdate = true

/* Good */
val pageCount = 5
val isActive = true
val shouldUpdate = true
```

- Like a prefix, variable names can be made singular or plural depending on whether they hold a single value or multiple
  values.

```Kotlin
/* Bad */
val friends = 'Bob'
val friend = ['Bob', 'Tony', 'Tanya']

/* Good */
val friend = 'Bob'
val friends = ['Bob', 'Tony', 'Tanya']
```

## Entity

#### Naming

- @Table 어노테이션 name = snake_case
- Class Name - UpperCamelCase

```kotlin
@Entity
@Data
@NoArgsConstructor
@Table(name = "table_name")
data class TableName (
  
)
```

#### Column
```kotlin
@Entity
@Data
@NoArgsConstructor
@Table(name = "table_name")
data class TableName (
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var idx: Long? = null,

  @Column(name = "col1_name")
  var col1_name: number,
)
```

#### Request DTO (Input)

- 동사 + 엔티티 이름 + InDto

```kotlin
data class CreateUserInDto (
  
)
```

#### Response DTO (Output)

- 동사 + 엔티티 이름 + OutDto

```kotlin
data class CreateUserOutDto (
  
)
```

## Git Convention

### tag

| Tag                | Description                                 |
|--------------------|:--------------------------------------------|
| `Feat`             | 새로운 기능을 추가할 경우                              |
| `Fix`              | 버그를 고친 경우                                   |
| `Design`           | CSS 등 사용자 UI 디자인 변경                         |
| `!BREAKING CHANGE` | 커다란 API 변경의 경우                              |
| `!HOTFIX	`         | 급하게 치명적인 버그를 고쳐야하는 경우                       |
| `Style`            | 코드 포맷 변경, 세미 콜론 누락, 코드 수정이 없는 경우            |
| `Refactor`         | 프로덕션 코드 리팩토링                                |
| `Comment`          | 필요한 주석 추가 및 변경                              |
| `Docs`             | 문서를 수정한 경우                                  |
| `Test`             | 테스트 추가, 테스트 리팩토링(프로덕션 코드 변경 X)              |
| `Chore`            | 빌드 태스트 업데이트, 패키지 매니저를 설정하는 경우(프로덕션 코드 변경 X) |
| `Rename`           | 파일 혹은 폴더명을 수정하거나 옮기는 작업만인 경우                |
| `Remove`           | 파일을 삭제하는 작업만 수행한 경우                         |


### branch
```sh
main -> dev -> feture/jira-issue-number(feature/PF-1)
```

### commit message
```sh
[jira-issue-number] Tag: commit message
[PF-1] feat: add user api
```