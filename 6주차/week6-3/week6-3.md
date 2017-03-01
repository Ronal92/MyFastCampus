[ Date : 2017. 02. 14(수) ]

-------------------------Today's Topic -------------------------
간단한 메모어플리케이션 만들어보기

 프로젝트명 : [MemoBasic]

 내용 : 안드로이드를 DB와 연동하여 간단한 메모 어플리케이션을 만듭니다.(쓰기 기능)

![](http://i.imgur.com/dfmLLOk.png)

- 왼쪽 상단이 시작화면입니다.(메모의 리스트를 화면에 보여줍니다.)

- (+)을 누르면 오른 쪽 상단 화면으로 전환됩니다.(메모장)

- 내용을 입력하고 (SAVE) 버튼을 누르면 다시 원래 화면으로 돌아와서 입력한 내용을 보여줍니다.

프로젝트 순서는 DB와 연동할 객체 클래스 생성(DBHelper.java) -> 테이블로써 사용할 클래스 생성(Memo.java) -> xml과 기능 구현 -> 액티비티와 프래그먼트간 통신할 인터페이스 생성(interfaces) 입니다.



# 1. DB 및 테이블 생성


##1.1 DBHelper.java

DBHelper.java에서 구현한 메소드는 아래입니다. week6-2의 DBHelper 클래스와 같습니다.

- DBHelper()

- onCreate()

- onUpgrade()

- getMemoDao() 

[DBhelper.java - 1]

![](http://i.imgur.com/3hCGPnM.png)

[DBhelper.java - 2]

![](http://i.imgur.com/XIn1p0e.png)

##1.2 Memo.java

[Memo.java]

![](http://i.imgur.com/V2ffPh3.png)

-위 코드는 아래 테이블을 만드는 겁니다. 이 프로젝트에서는 주로 id와 memo를 사용하고 date는 사용하지 않습니다. -

![](http://i.imgur.com/IRFnDWC.png)

-------------------------------------------------

# 2. xml과 기능 구현

## 2.1 화면 전체 UI 동작



![](http://i.imgur.com/DjbGx7Z.png)


## 2.2 MainActivity.java



### 역할 : MainActivity에 프래그먼트들을 연결시키고 DB table에서 데이터들을 가져옵니다.

[MainActivity - 1]

![](http://i.imgur.com/ku7cYle.png)

(1) onCreate() : ListFragment와 DetailFragment의 객체를 불러옵니다. 프래그먼트 매니저를 생성합니다. 그 외 초기화 작업(데이터 가져와서 세팅)을 진행합니다.

(2) loadData() : DB의 memo table에서 정보를 가져와서 datas에 저장합니다.

(3) setList() : ListFragment를 자신의 FrameLayout에 add 시킵니다.(화면에 메모 목록이 뜨게 합니다.)

[MainACtivity - 2]

![](http://i.imgur.com/Nil2iLV.png)

(4)여기는 인터페이스와 관련된 부분으로 **3. interfaces** 에서 설명 드리겠습니다.

##2.2 ListFragment.java

##2.3 ListAdapter.java

##2.4 DetailFragment.java



# 3. interfaces

