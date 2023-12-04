package org.http11.channel;

// 1. What is Channel?
// 채널은 한마디로 서버와 클라이언트간의 통신수단을 나타냅니다.
// 좀 더 깊이 있게 본다면 하드웨어 장비, 파일, 네트워크 소켓 혹은 프로그램 컴포넌트와 같이 읽기나 쓰기 등
// 한 개 이상의 뚜렷한 입출력 작업을 수행할 수 있는 개방된 연결을 나타냅니다.

// 2. 채널의 역할
// 채널은 비동기적으로 닫히고 중단(interrupt)될 수 있습니다.
// 따라서 한 스레드가 한 채널에서 하나의 입출력 작업으로 블록화하면 다른 스레드가 그 채널을 닫을 수도 있습니다.
// 비슷하게 한 스레드가 한 채널의 입출력 작업으로 블록화되면 다른 스레드가 블룩화된 스레드를 중단시킬 수 있습니다.
// 그래서 파일 입출력에서 블록화된 스레드를 언제든지 중지시킬 수 있게 되었으며,
// 이를 이용해서 네트워크에서 non-blocking 입출력이 가능해졌습니다.

// 3. 채널의 특징읽기 쓰기를 동시에 할 수 있다.
// Buffer 클래스를 사용하므로 데이터형에 맞는, 전용 메모리 공간을 가지고 있다.블로킹된 스레드를 깨우거나 중단시킬수 있습니다.