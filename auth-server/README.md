# VoteSKill-Auth_Server

# 웹 마피아 게임 VoteSKill

2023.07.14 ~ 2023.08.18 간 진행한 웹 마피아 게임 프로젝트 **VoteSKill** 을 소개하기 위해 작성한 글 입니다.

# 💁‍♂️ 프로젝트 소개

![image](https://github.com/VoteSKill/VoteSKill-Auth_Server/assets/77713508/e9aad66b-1761-40b5-a8f4-fbd04ee464ca)

**VoteSKill** 은 음성과 화상으로 즐길 수 있는 웹 마피아 게임 입니다.

**기간** : 2023.07.14 ~ 2023.08 18
**인원** : `Front-end` 3명 `Back-end` 3명

# 🎥 시연 영상

[🔗 UCC](https://youtu.be/j4NiSKCaPRE)

[🔗 게임 흐름](https://youtu.be/LzV36h3NDpc)

[🔗직업별 스킬](https://youtu.be/Z24hOQjm3eY)

# 🎯 기획 의도

![image](https://github.com/VoteSKill/VoteSKill-Auth_Server/assets/77713508/7fe94a9a-2f1a-4280-a25c-bca7730e2e41)

> 3년 4개월 간의 코로나로 인한 생활 방식의 변화
> 

사람들은 비대면 환경에서 일상생활을 온라인으로 전환하였고,
원격교육, 원격 업무, 온라인 회의와 같은 다양한 비대면 서비스를 적극적으로 이용하고 있습니다.
비대면 환경에서도 다양한 즐거움을 찾게 되었고 게임이라는 엔터테인먼트의 수요도 증가함을 볼 수 있습니다
이에 따라 시장에 존재하지 않으며 **화상,음성** 으로 인한 생동감을 불어넣어주고 남녀노소 모두를 타겟으로 할수 있는 **마피아 게임**을 기획하였습니다.

# 🎮 게임 소개

### 직업

![image](https://github.com/VoteSKill/VoteSKill-Auth_Server/assets/77713508/9705f134-d43b-4acd-b57d-e49bb2ac2bd6)

> 마피아1, 의사1, 경찰1, 특수직업 (군인, 성직자, 정치인, 기자)이 랜덤 배정되며  6인이 갖춰줘야 게임을 시작 할 수 있습니다.
> 
- 마피아 : 밤에 한명을 죽일 수 있다.
- 의사 : 밤에 마피아에게 살해 당할 것 같은 한명을 골라, 대상을 살릴수 있다.
- 경찰 : 밤에 한명을 조사하여 마피아 여부를 알 수 있다.
- 군인 : 마피아의 타겟이 된경우 한 차례 버틸수 있다. 단, 이후 직업이 공개된다.
- 성직자 : 밤에 죽은 플레이어 한명을 부활 시킨다. (1회)
- 정치인 : 투표시 2표로 인정되며 투표로 처형 되지 않는다.
- 기자 : 밤에 한명을 지목하여 직업을 모두에게 알려준다. (1회)

### 게임 진행

![image](https://github.com/VoteSKill/VoteSKill-Auth_Server/assets/77713508/f9b0da81-c84e-402d-b8da-1fbde381f6ab)

**게임 시작**

각 플레이어는 게임 시작시 랜덤한 직업을 부여 받는다.

**아침 - 토론**
토론을 하며 심리전을 통하여 마피아를 추리한다.

**점심 - 투표**
각 플레이어는 의심가는 플레이어를 지목하여 투표를 할 수 있다.

**저녁 - 투표 결과**
투표를 종합하여 과반 수 이상의 득표자는 처형 된다.

**밤 - 스킬 사용**
각자 직업에 맞는 스킬을 사용한다.

**아침 - 스킬 사용 결과**
스킬 사용 결과가 공개된다.

**사망 시**
카메라와 마이크가 강제로 꺼진다.

**게임 종료시**
승리 팀을 발표하며 대기실로 이동한다.

### 승리 조건

- 마피아 : 시민의 수가 마피아의 수보다 작거나 같으면 게임 승리
- 시민 : 마피아를 제거하면 게임 승리

# 🖥 게임 화면

### 게임 접속 화면

![image](https://github.com/VoteSKill/VoteSKill-Auth_Server/assets/77713508/e25c1cf4-72d3-497e-ad35-07a5cc995537)

카카오 로그인을 통하여 게임에 접속할 수 있습니다.
회원이 아닐 시 닉네임을 입력하여 게임에 가입합니다.

### 로비

![image](https://github.com/VoteSKill/VoteSKill-Auth_Server/assets/77713508/a1118b66-1890-48f6-8d45-27eee6a08be4)

로그인 시 로비로 이동합니다.
로비에서는 현재 생성된 방 목록들이 표시되며, 방만들기를 통하여 방을 만들 수 있습니다.

### 대기방

![image](https://github.com/VoteSKill/VoteSKill-Auth_Server/assets/77713508/03196a5a-9d76-4a93-a2c1-7a6b3cea6f6e)

대기방에서는 채팅을 할 수 있으며 방장은 START 버튼을 눌러 게임을 시작합니다.

### 게임 시작

![image](https://github.com/VoteSKill/VoteSKill-Auth_Server/assets/77713508/c2fd8989-1f8c-4346-b637-ea0dc60a9af0)

게임 시작 시 직업을 배정 받습니다.

### 투표

![image](https://github.com/VoteSKill/VoteSKill-Auth_Server/assets/77713508/9d5c10eb-96fe-4ac1-8ebb-b6cc039caf38)

2분간의 토론 후 의심가는 한명을 지목하여 투표를 합니다.

### 스킬 사용

![image](https://github.com/VoteSKill/VoteSKill-Auth_Server/assets/77713508/695ec480-1d2e-444f-8a69-6b442adb47f4)

투표 종료후 직업의 고유 스킬을 사용합니다.

### 사용 결과

![image](https://github.com/VoteSKill/VoteSKill-Auth_Server/assets/77713508/15c134ac-8acb-4e91-be5b-fe27e29d3361)

능력 사용 결과가 공개되며 사망한 사람의 화면이 꺼집니다.

### 게임 종료

![image](https://github.com/VoteSKill/VoteSKill-Auth_Server/assets/77713508/8e0215f4-45ba-4654-b704-6e49bab529a1)

# 📝 컨벤션

### 커밋 컨벤션

![image](https://github.com/VoteSKill/VoteSKill-Auth_Server/assets/77713508/3790521d-2713-4776-bfb7-ececcd08c374)

> 유다시티 커밋 컨벤션을 참조 하였고,
Gitmoji 를 활용하여 직관적인 커밋의도를 파악하고자 하였습니다.
> 
- ✨ **feat** : 새로운 기능과 관련된 것을 의미한다.
→ ✨feat : Introduce new features.
- 🐛 **fix** : 오류와 같은 것을 수정했을 때 사용한다.
→ 🐛fix :Fix a bug.
- ✅ **test** : test를 추가하거나 수정했을 때를 의미한다.
→ ✅test : Add or update tests.
- 📝 **docs** : 문서와 관련하여 수정한 부분이 있을 때 사용한다.
→ 📝docs : Add or update documentation.
- 💄 **style** : 코드의 변화와 관련없는 포맷이나 세미콜론을 놓친 것과 같은 부분들을 의미한다.
→💄 style**:** Add or update the UI and style files.
- ♻️ **refactor** : 코드의 리팩토링을 의미한다.
→ ♻️ refactor **:** Refactor code.
- 🔧 **chore** : 패키지 매니저 설정 등 여러가지 production code와 무관한 부분 들을 의미한다.
→🔧 chore: Add or update configuration files.

### 코딩 컨벤션

> 네이버의 캠퍼스 핵데이 Java 코딩 컨벤션 을 적용하였습니다.
참고 : https://github.com/naver/hackday-conventions-java
> 

# ⚙️ 사용 기술

![image](https://github.com/VoteSKill/VoteSKill-Auth_Server/assets/77713508/c62d45e4-e828-4745-94a6-52101bbb4ae8)

# 📒 아키텍처 구조

![image](https://github.com/VoteSKill/VoteSKill-Auth_Server/assets/77713508/334d7282-c50f-432a-ab56-c605530b7e9a)
