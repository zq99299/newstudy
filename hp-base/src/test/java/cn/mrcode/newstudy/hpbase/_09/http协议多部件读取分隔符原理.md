# http协议多部件读取分隔符原理

来看一个post的multipart/form-data的请求头
```
POST / HTTP/1.1
Host: localhost:6360
Connection: keep-alive
Content-Length: 343384
User-Agent: Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36
Cache-Control: no-cache
Origin: chrome-extension://fhbjgbiflinjbdggehcddcbncdddomop
Postman-Token: acab5971-9be5-21c4-6626-c17d6095ffd5
Content-Type: multipart/form-data; boundary=----WebKitFormBoundaryhsABXG11n88LPMza
Accept: */*
Accept-Encoding: gzip, deflate, br
Accept-Language: zh-CN,zh;q=0.9
```
文本和二进制在一个流中，需要关注以下几个属性
```
Content-Type: multipart/form-data; boundary=----WebKitFormBoundaryhsABXG11n88LPMza
Content-Length: 343384
```

再来看下几个子部件的请求头和假定内容；
第一个是一个普通的文本字段；
第二个是一个文件，图片，二进制内容；
需要额外注意的是boundary；这里的boundary比请求头里面的boundary多了两个--的前缀；解析的时候需要注意
```
------WebKitFormBoundarybJM8tewVjyBY68DJ
Content-Disposition: form-data; name="k2"

hello
------WebKitFormBoundarybJM8tewVjyBY68DJ
Content-Disposition: form-data; name="f3"; filename="Aoraki_ZH-CN7776353328_1920x1080.jpg"
Content-Type: image/jpeg

���� JFIF       �� C 
```

总结下格式：
```
POST / HTTP/1.1\r\n    - 第一行是固定的
请求头中的k,v属性\r\n
\r\n
body                   - 数据

multipart/form-data 的部件请求头：
--boundary\r\n
请求中的k,v属性；k:v[;k="v"]\r\n
\r\n
数据                  - 数据：如果是文件则是二进制数据，不是的话应该就是文本数据
```

## 解析核心思路

1. 从头开始读取；一次读取一个byte；
2. 寻找到分界点：
    比如总请求头的 \r\n\r\n   (最后一个kv换行+空行)
    比如多部件的下一个分界点 \r\n------WebKitFormBoundarybJM8tewVjyBY68DJ
3. 根据Content-Type（总请求头和多部件文件的都有）或则filename确定是什么类型的

## 核心匹配代码
每次从buffer中读取一个byte；并且需要判定当前buffer是否已用完，用完还需要从input中读取
```
  tail : 一次从input流中读取到的字节个数
  head ：当前读取的标志索引；当把当前buffer读取完成之后，则再次读取一次buffer
  public byte readByte() throws IOException {
        // Buffer depleted ? 缓冲已用完
        if (head == tail) {
            head = 0;
            // Refill.
            tail = input.read(buffer, head, bufSize);
            if (tail == -1) {
                // No more data available.
                throw new IOException("No more data is available");
            }
        }
        return buffer[head++];
    }
```
匹配大的请求头结尾分隔符；也就是找到body的开始索引
```
    byte[] HEADER_SEPARATOR = {'\r', '\n', '\r', '\n' };
    int i = 0; // 记录匹配的连续字符数量
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    while (i < HEADER_SEPARATOR.length) {
     byte b = readByte();
        if (b == HEADER_SEPARATOR[i]) {
            i++;
        } else {
            i = 0;
        }
        baos.write(b);
    }
```

寻找混合在二进制中的boundary；核心思想和上门的一致；只不过匹配变长了；
```
String boundaryHeadEnd = "\r\n" + boundaryHead;
byte[] boundaryHeadEndBytes = boundaryHeadEnd.getBytes();
 List<Byte> tempStore = new ArrayList<>();
 while (ni < boundaryHeadEndBytes.length) {
     byte b = readByte();
     if (b == boundaryHeadEndBytes[i]) {
         ni++;
         tempStore.add(b);  // 由于是在匹配分界，所以该数据不能写如二进制流中，先存储在临时容器中
     } else { // 中途发现和分解不完全匹配，那么就把临时容器中的字节全部取出来追加到之前的流中
         ni = 0;
         if (tempStore.isEmpty()) {
             os.write(b);
         } else {
             for (Byte aByte : tempStore) {
                 os.write(aByte);
             }
             os.write(b);
             tempStore.clear();
         }
     }
 }
```

demo版弄懂原理的代码在这里 cn.mrcode.newstudy.hpbase._09.WebServer2.parseMultipart；
