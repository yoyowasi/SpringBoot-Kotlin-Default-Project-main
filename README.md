# [project-name]
- GitHub : https://github.com/[github-id]/[project-name]

## spec
- java 17
- kotlin 1.9.21
- spring-boot 3.2.1

## install && execute

### install
```bash
$ git clone git@github.com/[github-id]/[project-name].git

or

$ git clone https://github.com/[github-id]/[project-name].git
```

### run

#### Locally
```bash
gradle bootRun
```

#### Important
```text
1. 프로젝트명 변경
 - /settings.gradle
 - /.idea/.name
 
2. [project-name]을 검색하여 전부 프로젝트 명에 맞게 수정

3. .env template 파일을 복사하여 .env 파일을 생성 후 필요한 정보를 입력

4. application-local.yaml db 정보 수정
```
