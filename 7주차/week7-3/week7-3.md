[ Date : 2017. 02. 23 ]

				----------------- Today's Topic	-----------------
							(1) Thread 개념
							(2) Looper & Handler
							(3) Runnable
							(4) HandlerThread
							(5) AsyncTask
				------------------------------------------------



  - 프로젝트명 : [ThreadBasic], [AsyncTask]

  - 내용 : 안드로이드에서 사용하게 될 Thread 기초 내용을 다루었습니다. (Looper와 Handler 개념, AsyncTask 사용)

------------------------------------------

# 1. Thread 개념

Thread는 사용자가 보는 화면이 아닌 곳에서(백그라운드) 내부적으로 오래 걸리는 작업들을 처리해줍니다. 만약 백그라운드 처리를 해주지 않는다면 사용자는 메인 화면에서 발생된 이벤트 처리에 대해 빠른 응답을 얻지 못하고 장시간 기다리거나 혹은 ANR을 보게 됩니다.

>> ANR이란?

>> - Input 이벤트(키를 누르거나 화면을 터치하는 등)에 5초안에 반응을 하지 않을 때
>> - BroadcatReceiver 가 10초내로 실행을 끝내지 않을 때 (UI가 없는 브로드캐스트 리시버, 서비스도 실행 주체가 메인스레드이므로 긴 시간을 소모하는 작업인 경우 ANR을 발생시킨다.)
>> ( 출처 : [http://itmining.tistory.com/3](http://itmining.tistory.com/3)  )


----------------------------------------------

# 2. Looper & Handler

Main Thread( 보통 UI를 직접 건드리는 MainActivity에서 동작하는 Thread) 와 Sub Thread 간에는 통신하는 방법이 필요합니다.

가령, UI를 변경하는 경우 안드로이드 자체에서 UI 접근 권한을 Main Thread에게만 허용하기 때문에 Sub Thread는 반드시 Looper와 Handler를 통해 UI를 변경할 수 있습니다.

##2.1 작동원리

![](https://realm.io/assets/img/news/kr/android-thread-looper-handler.png)

(1) Main Thread는 내부적으로 Message Queue와 Looper, Handler를 가지고 있습니다.

(2) Message Queue는 다른 Thread 혹은 자기 자신으로부터 전달받은 Message를 FIFO queue에 보관합니다.

(3) Looper는 내부적으로 무한히 돌면서 Message Queue에 Message 객체가 있는지를 확인합니다. 있다면 Handler에게 전달합니다.

(4) Handler는 Looper를 통해 받은 Message를 처리하거나 / 다른 Thread에서 받은 Message를 Message Queue에 넣습니다.

여기까지가 Looper와 Handler의 작동원리입니다. 참고로 realm 사이트가 굉장히 잘 정리되어 있어서 인용을 하였습니다. ㅎㅎ
( 출처 : [https://realm.io/kr/news/android-thread-looper-handler/](https://realm.io/kr/news/android-thread-looper-handler/) )

##2.2 코드

###2.2.1 화면 출력

![](http://i.imgur.com/Qq7EoVg.png)

--> 모바일 화면에서 Thread를 제어할 수 있는 Start 버튼과 Stop 버튼을 생성합니다. 

--> Start 버튼을 누르면 텍스트 뷰가 timer가 동작하고 Stop 버튼을 누르면 timer가 멈춥니다.


###2.2.2 코드

전체적으로 코드는 MainActivity.java 하나의 클래스 안에서 구현하였습니다. 핵심은 Handler와 내부에 선언된 Thread 입니다.

[MainActivity.java]

![](http://i.imgur.com/bMILPke.png)

(1) 사용자가 선언한 쓰레드 클래스는 반드시 "Thread"를 상속해야 합니다.

(2) 쓰레드 동작시 run()이 자동적으로 호출됩니다. 이제 무한 루프를 사용하여 쓰레드의 동작을 제어하도록 합니다. (즉, thread 를 중단시킬 수 있는 키값(flag)을 꼭 세팅해서 메인 thread가 종료시에 같이 종료될 수 있도록 해야한다. )

(3) Message 객체(msg)에는 보낼 데이터 정보를 담습니다. 

>> 메시지에 들어가는 내용.
				
					msg.what(int);
					msg.obj(Object);
					msg.arg1(int);
					msg.arg2(int);

(4) Handler에서 제공하는 sendMessage를 사용하여 Handler에게 메시지를 전송하면 됩니다.

>> sendEmptyMessage vs sendMessage
		
		 sendEmptyMessage(int what)는, ()안에 what의 값을 넣지만
		 sendMessage는 message를 담아서 보냅니다.

(5) sec는 초 단위입니다. 쓰레드 While 루프안에서 매번 1씩 증가시키되 Thread.sleep(1000)으로 1초의 delay를 줍니다.


[MainActivity.java]

![](http://i.imgur.com/rr9yG9D.png)

--> 쓰레드가 sendEmptyMessage 또는 sendMessage를 보낼 때 처리하는 역할입니다.

(1) 메세지가 처리해야 하는 구분값(msg.what) 받습니다.

(2) 각 case문에서 필요한 연산을 처리하면 됩니다. 여기서는 msg.arg1의 값인 sec을 텍스트뷰에 출력 시켰습니다.


[MainActivity.java]

![](http://i.imgur.com/N9Z1Wit.png)

--> Start 버튼 리스너에서 thread를 동작시키면 됩니다.

(1) 키값을 true로 바꿔놓습니다.

(2) thread를 선언하고 thread.start()로 쓰레드를 동작시킵니다.


[MainActivity.java]

					    @Override
					    protected void onDestroy() {
					        flag = false;
					        thread.interrupt();
					        super.onDestroy();
					    }

사용자가 액티비티를 나갈때, Thread의 동작을 멈추게 하고(flag = false), 메모리에서 제거합니다.(thread.interrupt())

------------------------------------------------

#3. Runnable

--> Runnable은 쓰레드를 구현하는 또다른 방법입니다. 보통 2장과 같은 방식은 Thread 클래스를 상속받으면 다른 클래스를 상속받을 수 없기 때문에, Runnable 인터페이스를 구현합니다.

## 사용방법

#####선언

            class CustomThread implements Runnable{
				@Override
				public void run(){
				...........
				}
			}

#####사용

			public class MainActivity extends AppCompatActivity {
				public static void main(String[] args){
					Runnable runnable = new CustomThread();
					Thread thread = new Thread(r);
					thread.start();
			
				}
			}


--> Runnable 인터페이스만을 구현한 CustomThread는 Thread를 상속한게 아니기 때문에 start()가 없습니다. 따라서 별도의 쓰레드를 생성해주고 Runnable 객체를 넘겨주면 됩니다.

##4. HandlerThread

### 왜 사용하는가?
 쓰레드간 통신을 위해서는 Looper가 필요합니다. 만약 프로그래머가 SubThread1과 SubThread2를 통신하게 하기 위해서는 둘 중 한곳에 Looper가 있어야 되고 프로그래머는 따로 이 Looper를 만들어주어야 합니다.

### 해결책
 
HandlerThread는 일반적인 스레드를 확장한 클래스로 내부에 반복해서 루프를 도는 Looper를 가집니다. 자동으로 Looper 내부의 Message Queue도 생성되므로 이를 통해 스레드로 Message나 Runnable을 전달받을 수 있습니다.

### 코드

만약 SubThread2가 SubThread1에게 메시지를 보내는 경우에는
			
			- SubThread2에서 만들 쓰레드
			HandlerThread hThread = new HandlerThread();
			hThread.start();

			- SubThread1에서 만들 핸들러와 루퍼
			Handler handler = new Handler( hThread.getLooper() );



-----------------------------------------------


#5. AsyncTask

##5.1 개념

 AsyncTask는 Thread나 Message 등을 사용하지 않고도 하나의 메인 클래스에서 UI작업과 background 작업을 할 수 있도록 합니다.


![](http://cfile23.uf.tistory.com/image/2420B240577D4A720F8136)

(1) aTask.execute() : thread.start()와 같이 AsyncTask에서는 execute()로 실행합니다.

(2) onPreExcetued() : AsyncTask로 작업을 시작하기 전에 실행되는 메소드입니다.

(3) doInBackground() : background 작업을 진행하는 곳입니다. 

(4) publishProgress() : donInBackground()에서 UI를 업데이트하려고 할 때, 호출되는 곳입니다.

(5) onProgressUpdate() : publcishProgress()가 호출될 때마다 자동으로 호출됩니다.

(6) onPostExcuted() : background 작업이 끝난 후, AsynTask의 결과 파라미터가 전달됩니다. 


##5.2 출력화면

![](http://i.imgur.com/Nfj8RgD.png)

START 버튼을 누르면 앱에 저장되어있는 .mp4파일을 다운 받으면서 SeekBar에 실시간으로 다운로드 실행이 표시됩니다. DELETE 버튼을 누르면 다운받은 파일이 삭제됩니다.

![](http://i.imgur.com/HW7wUJW.png)

환경설정에서 애플리케이션 정보를 확인하면 내장된 데이터가 증가한것을 확인할 수 있습니다.

##5.3 코드

##5.3.1 비디오 파일 앱 위치에 놓기

![](http://i.imgur.com/DOI3zWN.png)

[main] 폴더 하위에 사용할 동영상 파일을 저장합니다. 이 때, 반드시 [assets]라는 폴더명 하위에 두어야 합니다.

##5.3.2 AsyncTask 동작시키기

![](http://i.imgur.com/zm6Z81E.png)

AsyncTask 인스턴스에 파일명을 담아 execute()로 실행시킵니다.

##5.3.3 AsyncTask 내부 클래스

 			public class TestAsync extends AsyncTask<String, Integer, Boolean>

AsyncTask 클래스를 상속해야 합니다. 또한 3개의 인자타입을 받습니다. 왼쪽부터 doInBackground(), onProgressUpdate(), onPostExecute()의 인자로 각각 들어가게 됩니다.

(1) onPreExecute()

![](http://i.imgur.com/yXm99AX.png)

AsyncTask의 background 프로세스 전에 호출되는 함수입니다. 

					AssetManager manager = getAssets(); // 파일 사이즈를 입력받기 위한 함수

					InputStream is = manager.open("Dokkabi.mp4"); // 파일 사이즈를 가져오기 위해 파일 스트림 생성

					int fileSize = is.available(); // stream에 연결된 파일사이즈를 리턴해준다.

					progressBar.setMax(fileSize); // 프로그래스바의 최대값에 파일사이즈 입력

(2) doInBackground()

![](http://i.imgur.com/Cqg7SVw.png)

파일명을 가져와서 assetToDisk()로 전달합니다. 여기서는 파일 다운로드가 진행됩니다.

(3) onPostExecute()

![](http://i.imgur.com/Jvpv94C.png)

doInBackground 가 종료된 후에 호출되는 함수로써, doInBackground의 return 값을 받습니다. 다운로드가 완료되었을 경우, 텍스트뷰에 "완료되었습니다" 를 출력시킵니다.

   

(4) onProgressUpdate()

![](http://i.imgur.com/31A98OY.png)

main thread에서 실행되는 함수입니다. 넘어온 파일길이를 텍스트뷰와 SeekBark에 출력합니다.

(5) assetToDisk()

AsyncTask에서 제공하는게 아니라 사용자가 직접 만든 함수로써 assets에 있는 파일을 쓰기가능한 internal Storage로 복사합니다.

![](http://i.imgur.com/lljD0c9.png)
![](http://i.imgur.com/6eBNP8E.png)

					AssetManager manager = getAssets(); // 1. assets에 있는 파일을 filename으로 읽어온다.

					is = manager.open(filename); // 2. 파일 스트림 생성
					bis = new BufferedInputStream(is); // 3. 버퍼 스트림으로 래핑 ( 한번에 여러개의 데이터를 가져오기 위한 래핑)

					String targetFile = getFullPath(filename); // 4. 저장할 위치에 파일이 없으면 생성
               		File file = new File(targetFile);
              		if (!file.exists()) {
             	       file.createNewFile();
             		}

					fos = new FileOutputStream(file);  // 5. 쓰기 스트림을 생성
					bos = new BufferedOutputStream(fos); // 6. 버퍼 스트림으로 동시에 여러 개의 데이터를 쓰기위한 래핑 (래퍼 클래스???)
	
	                while ((read = bis.read(buffer, 0, 1024)) != -1) { // 읽어올 데이터가 없을때까지 반복문을 돌면서 읽고 쓴다.
	                    bos.write(buffer, 0, read);
	                    publishProgress(read); // AsyncTask의 onProgressUpdate()로 저장된 데이터 용량을 보냅니다.
	                }


##5.3.4 파일명의 전체 디렉토리 구조 가져오기

  		 private String getFullPath(String filename){        
   			     return getFilesDir().getAbsolutePath() + File.separator + filename;
  		  }






























