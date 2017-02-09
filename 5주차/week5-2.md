[ Date : 2017. 02. 07 ]



					---------------- Today's Topic -----------------
								(1) 구글맵 사용하기
								(2) GPS 사용하기
								(3) DDMS
								(4) ADB shell 사용하기
					------------------------------------------------


프로젝트명 : MyUtility


week5-1.md에서 만든 탭과 프래그먼트의 연결 작업을 응용하여 각 탭마다 지금까지 만든 기능들을 종합하였습니다. [계산기] / [단위변환] / [검색] / [현재위치] 입니다. 이 중에서 새로 배운 '구글맵 사용하기'를 소개하겠습니다!!

-------------------------------------------

#1. 구글맵 사용하기

[구글맵 사용하기]는 자신의 위치를 GPS를 사용하여 실시간으로 구글맵에 표시합니다.


![](http://i.imgur.com/2MBbhlH.png)

##1.1 구글맵과 GPS를 같이 사용하기 위해서는 먼저 manefest에 권한 설정을 해주어야 합니다.

![](http://i.imgur.com/hHiObjW.png)

**android.permission.INTERNET** - API가 Google Maps 서버에서 지도 타일을 다운로드할 때 사용합니다.

**android.permission.ACCESS_FINE_LOCATION** - 애플리케이션이 device의 위치를 실시간으로 접근하도록 허락합니다. (정확한 위치 정보)

**android.permission.ACCESS_COARSE_LOCATION** - 애플리케이션이 device의 위치를 실시간으로 접근하도록 허락합니다. (근사 위치 정보)


##1.2. 구글 맵 프로젝트 생성과 구글 API 키 가져오기

Google Maps Android API를 사용하려면 Google Developers Console에 앱을 등록하고 추가할 수 있는 API 키가 필요합니다.

###1.2.1
먼저 "Google Maps Activity"를 생성합니다.

![](http://i.imgur.com/PyyQbwG.png)

###1.2.2

 OnMapReadyCallback 인터페이스를 FourFragment에서 구현합니다.

			public class FourFragment extends Fragment implements OnMapReadyCallback {
			   ...............................
			}



###1.2.3

![](http://i.imgur.com/8QYtBPl.png)

(1) app/res/values/google_maps_api.xml로 이동합니다.

(2) 표시된 링크로 가면 Google Developers Console로 이동합니다. Console 화면에 나온대로 따라하면 구글 키 생성!

(3) 구글 key를 string 태그 안에 넣으면 됩니다. 

##1.3 Mapview 추가 

MyUtility 프로젝트에서 구글맵이 표시될 화면은 '현재위치' 탭 아래 화면입니다. 이 화면에 해당하는 "fragment_four.xml"에 mapview 프래그먼트를 추가하고 ID를 'mapView'로 합니다.

##1.4 Mapview 프래그먼트를 액티비티에 추가.
우리가 사용하는 액티비티에는 뷰페이저가 등록이 되어있습니다. 뷰페이저에 어뎁터를 setting시킴으로 화면에 표시가 되는데 이 때, 우리가 사용할 어뎁터로 fragmentStatePageAdapter를 상속하는 어뎁터를 사용할 겁니다. 

결론적으로 Mapview를 화면에 보여주기 위해 어디에 등록해야 하냐? 

----> Mapview를 프래그먼트에 등록시킨 후, 프래그먼트를 어뎁터 클래스에서 객체로 생성하여 반환해주면 됩니다.(MainActivity.java) 여기서는 프래그먼트의 onCreateView(프래그먼트가 화면에 보여질 때)에 아래 과정을 넣어주면 됩니다.(FourFragment.java)

![](http://i.imgur.com/lg3fo1W.png)

##1.5 OnMapReadyCallback 인터페이스 메소드 정의하
	

![](http://i.imgur.com/CrIWioO.png)


1.2.2에서 OnMapReadyCallback 인터페이스를 implements하면 onMapReady 콜백 메소드를 정의해주어야합니다. 여기서는 GoogleMap 객체(map)를 사용하여 가령 지도에 시드니 도시의 마커를 표시(.addMarker)하고 화면을 시드니가 있는 쪽으로 옮깁니다.(.moveCamer) 이때 zoom level은 15 정도만 줬습니다. 


> 여기까지가 구글맵을 띄우는 과정이었습니다!!! 휴.........
> 2장부터는 GPS를 사용하여 내 위치를 실시간으로 마크해주는 기능을 넣겠습니다

------------------------------------------------

#2. GPS 사용하기

				          	<GPS를 사용하는 법>
					 * 1. manefest에 FINE, COARSE 권한추가
					 * 2. runtime permission 코드에 추가
					 * 3. GPS Location Manager 정의
					 * 4. GPS가 ㅋ 져있는지 확인. 꺼져있다면 GPS화면으로 이동
					 * 5. Listener 실행
					 * 6. Listener 해제

1번은 1.1에서 설정했으므로 생략합니다^^

##2.1 runtime permission 코드에 추가

![](http://i.imgur.com/LMy7B0R.png)

GPS를 사용하기 위해서는 사용자에게 위치 탐색 권한을 요청해야 합니다. 
따라서 "Manifest.permission.ACCESS_FINE_LOCATION"와 "Manifest.permission.ACCESS_COARSE_LOCATION"를 반드시 권한체크 문과 권한체크 후 콜백문에 넣어주어야 합니다!!

![](http://i.imgur.com/pwzyY26.png)

##2.3



