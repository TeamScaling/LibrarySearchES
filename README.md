# LibrarySearchES

<img src="https://img.shields.io/badge/-elasticsearch-yellowgreen"/>
<h1> 엘라스틱 서치 간단 사용 방법 </h1>

<h3> 1️⃣ 엘라스틱 서치 빌드 버전맞추기, properties 파일 작성 </h3>
순서대로 엘라스틱 클라이언트를 위한 빌드와 레스트 템플릿을 만들기 위한 빌드
<h4> implementation 'org.springframework.data:spring-data-elasticsearch:4.2.4'</h4> <br>
<h4> implementation group: 'org.elasticsearch.client', name: 'elasticsearch-rest-high-level-client', version: '7.17.9' </h4> <br>


// 9200번 포트를 이용해서 연결하겠습니다. <br>
elasticsearch.host.localhost.port= 9200 <br>
<br>
// 엘라스틱 서치의 저장소 (하나의 클러스터 안에 들어가는 서버별 저장소 이걸 노드라고 부릅니다. 지금 내 컴퓨터는 1노드입니다.) <br>
spring.data.elasticsearch.cluster-nodes=localhost:9200: <br>

<h3> 2️⃣ 엘라스틱 서치 간단 개념 </h3>
<h4> 지금까지 우리가 많이 사용했던 MySQL과 같은 관계형 DB와 엘라스틱 서치는 조금 다릅니다. </h4>
<h4> NoSQL이라고 해도 되지 않을까 싶습니다. 그만큼 유연한 작업이 가능하다고도 할 수 있습니다. </h4>
<h4> RDBMS가 논리적인 table로 이루어진 저장소라면 </h4>
<h4> ElasticSearch는 논리적인 indices들로 이루어진 저장소라고 보면 될 것 같습니다. </h4>
<h4> (indices는 index의 복수형? 여기서의 index는 MySQL의 index와는 다른 개념입니다.) </h4>
<h4> csv 파일또는 MySQL 또는 어딘가에 있는 데이터를 index로 만들어주어야 하는데 그 과정을 indexing이라고 합니다(?) </h4>

<h3> 3️⃣ 엘라스틱 서치 다운받고 실행하기 </h3>
<h4> 1. 엘라스틱 서치 다운받기 8.7.0 버전 (홈페이지 또는 도커에서 다운 받을 수 있습니다.) </h4>
<h4> https://www.elastic.co/kr/downloads/elasticsearch </h4>
<h4> 2. 터미널을 이용해서 엘라스틱 서치 폴더 안에 있는 bin에 들어갑니다. cd elasticsearch-8.7.0/bin </h4>
<h4> 3. 엘라스틱 서치를 실행시킵니다. elasticsearch 입력 </h4>
<h4> 4. localhost:9200 확인: 들어가보면 json형태로 글자가 보입니다. 여기까지 됐으면 스프링 부트로 갑니다. </h4>

<h3> 4️⃣ 엘라스틱 서치 인덱싱하고 스프링 부트 실행하기 </h3>
<h4> 0. 빌드 다 된 상태에서 해야하며 꼭 버전 맞춰야 합니다. 꼭! 꼭! 전 말헀습니다. </h4>
<h4> 1. 실행시켜보기: 아마 실행이 안 될 겁니다. 엘라스틱서치랑 repository랑 아직 세팅이 안되었기 때문에..  </h4>
<h4> 2. 클래스들 확인해보기: Books를 봐봅시다. Entity 자리에 Document가, Column 자리에 Field가, name이 indexName으로 되어있습니다. 논리적인 구조에 대한 연결이 그렇게 된다고 보면 되겠습니다. </h4>
<h4> 3. 익덱스 생성하기: 인덱스(MySQL의 테이블)를 생성할텐데 테이블을 만들듯 json형태로 만들어줍니다. 이것은 자바 코드로도 할 수 있고 Kibana콘솔, 포스트맨을 이용해서 할 수도 있습니다. json형태이다 보니 콘솔창을 이용해서 하는 것이 보기에 좋다고 생각합니다. 인덱스에 들어갈 타입, 분석기, 토크나이저, 필터 등을 이 때 생성하게 됩니다. 이후 ElasticsearchIndexCreator를 실행하면 인덱스가 생성됩니다. ❗️ 한번 생성된 인덱스(MySQL의 테이블)에 대해서는 변경이 쉽지 않으므로 만들 때 유의합시다. </h4>
<h4> 4. 데이터를 넣어주기: 데이터를 넣어주기 전에 Book의 각 필드들을 엘라스틱서치에 매핑해줍니다. 이건 여기에, 저건 저기에. 이후 DataLoader를 실행하면 데이터를 넣어주는 작업이 시작됩니다. 10분당 100만개면 (제 기준) 빠르게 데이터가 들어간 것이며 저의 경우에는 5분에 30만 개 정도 들어갔습니다. 중간에 끊기는 건 어떻게 해결할지... 그냥 끊어서 넣어주면 되었습니다. 더 좋은 방법이 있으면 알려주세요. </h4>
<h4> 5. LibraryserviceApplication 실행하기: 실행이 잘 됩니다. 안된다면 로그를 잘 읽고 해결해주세요. </h4>
