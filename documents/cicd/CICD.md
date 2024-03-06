# 문제점 인식

이 프로젝트를 진행하기 전, 개발 환경에 대한 문제점을 생각해보았습니다.

제가 생각하는 기존의 개발 및 배포 프로세스는 다음과 같았습니다.

1. 기존 코드에서 기능을 추가하기 위해 새로운 브랜치를 만든 뒤, 기능을 추가합니다.
2. 테스트를 진행한 뒤, 문제가 없다면 PR을 날립니다.
3. PR에서 코드 리뷰를 진행하며 코드를 수정합니다.
4. 코드를 수정하며 테스트를 진행합니다.
5. 최종적으로 Allow를 받으면 develop 브랜치에 merge합니다.
6. develop브랜치에 원하는 기능들을 모두 merge했다면, 최종적으로 테스트 한 뒤 master브랜치에 merge합니다.
7. master브랜치를 instance에서 pull받은 뒤, build합니다.
8. build 테스트 후 문제가 없다면 기존의 프로세스를 종료한뒤, 새로 pull받은 jar파일을 실행시키며 배포합니다.

이 과정에서 PR을 진행하면서 문제가 있는 코드가 merge가 되는것을 막는것이 첫번째 우선이였고, 두번째로는 merge과정이 매우 번거롭고 merge마다 테스트를 진행하며 통합하는것이 번거로웠습니다.

또한 모두 merge한 뒤, instance에 접속하여 pull을 받고 기존의 프로세스를 찾아 종료하는 과정과, 새로 배포하는 과정이 모두 `수동`으로 이루어져있기 때문에, 어느 순간 한 과정에서 테스트를 놓쳐
문제가 있는 코드를 merge하는 등의 사고가 일어날 수 있습니다.

따라서 이 과정을 CI/CD를 통해 자동화하여 해결하고자 합니다.

# CI란?

`Continuous Integration` 의 약자로, 지속적인 통합을 의미합니다.

지속적 통합은 코드의 변경 사항을 지속적으로 빌드 및 테스트하며 공유되는 레포지토리에 통합되는것을 의미합니다.

# CD란?

`Continuous Deployment` or `Continuous Delivery`의 약자로, 지속적인 서비스 제공 혹은 지속적인 배포를 의미합니다.

지속적 배포는 자동화된 빌드 및 테스트를 거치며 문제가 발견되지 않는다면 자동으로 배포하는 자동화 배포 파이프라인입니다.

# CI/CD의 대표주자

`Jenkins` , `Github Actions` 이 두가지가 대표적으로 사용되는 CI/CD 툴이라고 생각합니다.

저는 이 두가지 배포툴 중에서 `Github Actions`을 이용하여 CI/CD 환경을 구축할것 입니다. `Jenkins`을 사용하지 않는 이유는, 이전에는 SSAFY에서 개발에 필요한 비용을 지원받으며 진행했기
때문에 `Jenkins` 인스턴스를 따로 두거나 도커를 이용하여 한 instance에서 진행해도 문제 없을만큼의 여유가 있었기 때문에 `Jenkins`을 사용했습니다.

하지만 현재는 개인이 하는 작은 프로젝트이고, 프리티어 instance를 사용하고 있기 때문에, `Jenkins` 인스턴스를 따로 구축하거나 한 instance에 도커를 이용하여 `Jenkins`을 구동시킬만한
사양이 되지 않을것이라 생각했습니다.

또한 Github에서 PR을 진행할때 테스트를 실패하면 Merge를 block시킬 수 있는 branch rules를 설정할 수 있는데, 이 동작이 `Github Actions`를 이용하기 때문에 CD
또한 `Github actions`를 이용하자 라고 생각했습니다.

# Github Actions에 대해 알아보자

Github Actions는 Repository에서 어떤 이벤트가 발생했을 때 특정 작업을 실행하거나 주기적으로 어떤 작업들을 실행시킬 수 있는 Github의 무료 CI/CD 툴 입니다.

## Workflows

자동화 혹은 스케줄링 등 원하는 작업을 진행시킬 작업흐름을 의미합니다.

이는 저장소 내에 `.github/workflows` 폴더에 YAML 파일로 설정이 가능하며, 하나의 저장소는 여러개의 워크플로우를 가질 수 있습니다.

아래에는 YAML파일에 설정할 수 있는 속성들에 대해 설명하겠습니다.

### [on]

워크플로우가 언제 실행이 되는지 설정해줄 수 있습니다.

```yaml
on:
  push:
    branches: [ "master" ]
```

위와같은 경우, master브랜치에 push 이벤트가 발생하게 된다면 해당 workflows 를 실행시키겠다고 설정할 수 있습니다.

### [jobs]

독립된 가상머신 혹은 컨테이너에서 처리되는 작업의 단위를 의미합니다.

이는 동시에 실행을 시킬 수 있으며, 순서를 지정해서 실행시킬 수 있습니다.

### [steps]

여러 단계의 명령을 순차적으로 실행하는 경우를 의미합니다.

저희는 여기서 두개의 작업을 진행해야 합니다.

## PR시에 빌드 테스팅

PR시에 ACTION을 실행시켜서 JDK셋팅, GRADLE 셋팅 이후 해당 프로젝트를 TEST하는 명령어를 STEP별로 실행시키는 작업을 아래와같이 작성했습니다.

```yaml
name: PR_Action
on: [ pull_request ]

jobs:
  gradle-test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4

      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'

      - name: Setup Gradle
        uses: gradle/actions/setup-gradle@417ae3ccd767c252f5661f1ace9f835f9654f2b5 # v3.1.0

      # Gradle 캐싱
      - name: Gradle Caching
        uses: actions/cache@v3
        with:
          path: |
            ~/.gradle/caches
            ~/.gradle/wrapper
          key: ${{ runner.os }}-gradle-${{ hashFiles('**/*.gradle*', '**/gradle-wrapper.properties') }}
          restore-keys: |
            ${{ runner.os }}-gradle-

      - name: Grant execution permission to gradlew
        run: sudo chmod 755 gradlew

      - name: Build with Gradle Wrapper
        run: ./gradlew clean test

```

그런 뒤, Github의 branch rules 중 `Require status checks to pass before merging` 을 체크하여 위의 액션인 `gradle-test`를 설정해주었습니다.
![image](https://github.com/Hyeon-Uk/backend-skill-reinforcement-with-board-project/assets/43038815/7d16dfbd-881b-49b3-9cd5-e57e802fe692)

이렇게 되면 PR시에 자동으로 test를 진행한 뒤, test를 통과하지 못한다면 아래와 같이 merge할 수 없도록 합니다.

실제로 테스트용 branch에서 아래와 같이 실패하는 테스트코드를 추가했습니다.
![image](https://github.com/Hyeon-Uk/backend-skill-reinforcement-with-board-project/assets/43038815/fd0d5678-ca35-4a3c-8cc5-6343c8e752fe)

이 브랜치를 master에 pull request를 날리게 된다면 아래와같이 merge할 수 없다고 막게 됩니다.
![image](https://github.com/Hyeon-Uk/backend-skill-reinforcement-with-board-project/assets/43038815/2df5b6cb-4332-417d-a863-64f97e9dcacd)

해당 테스트코드를 통과하도록 수정한다면, 아래와같이 merge할 수 있도록 바뀌게 됩니다.
![image](https://github.com/Hyeon-Uk/backend-skill-reinforcement-with-board-project/assets/43038815/a400e75c-4721-4574-a0af-0b8282910737)

## master branch에 merge

이렇게 테스트를 모두 마친 뒤, develop 을 거쳐 master로 merge시에, 아래와 같은 steps를 거쳐서 instance에 자동으로 배포하도록 workflow를 구성했습니다.

```yaml
# This workflow uses actions that are not certified by GitHub.
# They are provided by a third-party and are governed by
# separate terms of service, privacy policy, and support
# documentation.
# This workflow will build a Java project with Gradle and cache/restore any dependencies to improve the workflow execution time
# For more information see: https://docs.github.com/en/actions/automating-builds-and-tests/building-and-testing-java-with-gradle

name: Java CI/CD with Gradle

on:
  push:
    branches: [ "master" ]

jobs:
  build:
    runs-on: ubuntu-latest
    permissions:
      contents: read

    steps:
      - uses: actions/checkout@v4

      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'

      - name: Setup Gradle
        uses: gradle/actions/setup-gradle@417ae3ccd767c252f5661f1ace9f835f9654f2b5 # v3.1.0

      # Gradle 캐싱
      - name: Gradle Caching
        uses: actions/cache@v3
        with:
          path: |
            ~/.gradle/caches
            ~/.gradle/wrapper
          key: ${{ runner.os }}-gradle-${{ hashFiles('**/*.gradle*', '**/gradle-wrapper.properties') }}
          restore-keys: |
            ${{ runner.os }}-gradle-

      - name: Grant execution permission to gradlew
        run: sudo chmod 755 gradlew

      - name: Build with Gradle Wrapper
        run: ./gradlew build

      - name: Docker hub login
        if: success()
        run: docker login -u ${{secrets.DOCKER_ID}} -p ${{secrets.DOCKER_PASSWORD}}

      - name: Docker image build
        if: success()
        run: |
          docker build -t ${{secrets.DOCKER_USERNAME}}/test .
          docker push ${{secrets.DOCKER_USERNAME}}/test

      - name: Deploy to instance
        if: success()
        uses: appleboy/ssh-action@master
        with:
          host: ${{secrets.EC2_HOST}}
          username: ${{secrets.EC2_USERNAME}}
          key: ${{secrets.EC2_PRIVATE_KEY}}

          script: |
            sudo docker kill test
            sudo docker rm -f test
            sudo docker rmi ${{secrets.DOCKER_USERNAME}}/test
            sudo docker pull ${{secrets.DOCKER_USERNAME}}/test

            sudo docker run -d -p 8080:8080 \
            --name test ${{secrets.DOCKER_USERNAME}}/test
```

${{value}} 는 Github의 secrets variable에서 설정할 수 있습니다.

현재 master브랜치에는 스프링 프레임워크의 골격 이외의 컨트롤러가 존재하지 않아서 해당 포트로 접속하게된다면 아래와 같은 페이지가 보여집니다.

![image](https://github.com/Hyeon-Uk/backend-skill-reinforcement-with-board-project/assets/43038815/c8f76e8a-779e-43b4-b0b5-cb68ce20515c)

여기서, master브랜치에 "/"로 get 요청을 보낼 시에 hello world를 return 해주는 RestController를 추가하여 merge를 진행하면 아래와 같이 Action이 실행됩니다.

![image](https://github.com/Hyeon-Uk/backend-skill-reinforcement-with-board-project/assets/43038815/f9c1dc83-08af-4959-b1ac-67e218f04205)

해당 Action이 모두 실행을 마쳤다면 아래와 같은 상태가 될 것이고, instance에 안전하게 배포가 완료된것을 확인할 수 있습니다.
![image](https://github.com/Hyeon-Uk/backend-skill-reinforcement-with-board-project/assets/43038815/7632d78c-d830-4f85-8368-a3a440969f9a)

인스턴스에서도 잘 실행되고 있습니다.
![image](https://github.com/Hyeon-Uk/backend-skill-reinforcement-with-board-project/assets/43038815/3a3c3d3d-ab23-49d8-bbb0-052b5225b90c)

실제 인스턴스의 "/"로 접속을 하면, 위에서 merge했던 코드와 같이 hello world를 리턴받을 수 있습니다.
![image](https://github.com/Hyeon-Uk/backend-skill-reinforcement-with-board-project/assets/43038815/176eda03-1630-47ed-a7e0-12aa96f92a65)

# 결론

이렇게 해서 프로젝트의 기본적인 CI/CD환경을 구축했는데, DB를 셋팅하고 연결하는 과정에서 YAML파일의 수정이 있겠지만, 빌드와 배포에 노동력을 줄였기 때문에 개발에 더 집중할 수 있게되었습니다.

