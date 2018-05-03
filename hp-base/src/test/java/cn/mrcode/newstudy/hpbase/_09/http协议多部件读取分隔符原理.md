fileUpload工具类中跟读源码整理：
```
  tail : 一次读取的buffer大小(工具类中默认是4092)
  每次从buffer中读取一个byte；
  head ：当前读取的标志索引；当把当前buffer读取完成之后，则再次读取一次buffer
  public byte readByte() throws IOException {
        // Buffer depleted ?
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
读取一个部件头：`Content-Disposition: form-data; name="k1"\r\n\r\n`;读取的数据存储在ByteArrayOutputStream中；
如下面的文件部件头:
`Content-Disposition: form-data; name="k3"; filename="power d链接数据库.txt"\r\nContent-Type: text/plain\r\n\r\n`
读取完成之后；解析的时候是按行再解析到map中的，在其他地方再按每行进行解析

核心思路：
    每次读取一个byte；读取一个就与固定的 HEADER_SEPARATOR /r/n/r/n数组的第一个字符比较，如果相等，则i+1;
    假设读取到了一次/r，下一次就对比是否是/n；如果不是，就把i重置为0（这个就是协议的死规定，换行+空行前面的就是头部）

```java
        int i = 0;
        byte b;
        // to support multi-byte characters
        // 用来存储部件头
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int size = 0;
        while (i < HEADER_SEPARATOR.length) {
            try {
                b = readByte();
            } catch (FileUploadIOException e) {
                // wraps a SizeException, re-throw as it will be unwrapped later
                throw e;
            } catch (IOException e) {
                throw new MalformedStreamException("Stream ended unexpectedly");
            }
            if (++size > HEADER_PART_SIZE_MAX) {
                throw new MalformedStreamException(
                        format("Header section has more than %s bytes (maybe it is not properly terminated)",
                               Integer.valueOf(HEADER_PART_SIZE_MAX)));
            }
            if (b == HEADER_SEPARATOR[i]) {
                i++;
            } else {
                i = 0;
            }
            baos.write(b);
        }
```

怎么判断是否是一个文件？
    如果是文件的话，在不部件头里面，会存在Content-type:xxx；
    所以拿到部件头之后，还需要对头里面进行解析；获取到文件名称和文件类型

找下一个分隔符：
协议规定如下：
```
部件头\r\n
空行
内容\r\n
------WebKitFormBoundary2JqRGwLMgEHKw8ml  所以我们要找到这里的分隔符
```
```
private void findSeparator() {
            // 记录下一个部件开始的位置
            pos = MultipartStream.this.findSeparator();
            if (pos == -1) {
                if (tail - head > keepRegion) {
                    pad = keepRegion;
                } else {
                    pad = tail - head;
                }
            }
        }
protected int findSeparator() {
        int first;
        int match = 0;
        int maxpos = tail - boundaryLength;
        // bounary = \r\n------WebKitFormBoundary2JqRGwLMgEHKw8ml
        for (first = head; first <= maxpos && match != boundaryLength; first++) {
            // 先找到第一个/r
            first = findByte(boundary[0], first);
            if (first == -1 || first > maxpos) {
                return -1;
            }
             // 然后从buffer中往下找，并匹配这个串
            for (match = 1; match < boundaryLength; match++) {
                if (buffer[first + match] != boundary[match]) {
                    break;
                }
            }
        }
        if (match == boundaryLength) {
            return first - 1;
        }
        // 如果当前的buffer块没有找到 则返回-1
        return -1;
    }
```

没有完全的看懂怎么解析的：大体上来总结下已看懂的：

1. 从上往下读取
2. 每次读取一个byte，然后找到\r\n\r\n的地方；也就是上面 读取一个部件头的代码；
    核心思想是:
    ```
    HEADER_SEPARATOR = {\r,\n,\r,\n}
    ByteArrayOutputStream baos;
     while (i < HEADER_SEPARATOR.length) {
        byte b = readByte();
        if (b == HEADER_SEPARATOR[i]) {
                i++;
            } else {
                i = 0;
            }
            baos.write(b);
        }
     }
     如果找到了换行+空行那么该循环会退出；而找到的头也在baos里面了
    ```
3. 找到下一个boundary分界的位置
    ```
    \r\n------WebKitFormBoundary2JqRGwLMgEHKw8ml
    核心思路如第2步里面； 一个byte的对比，直到都ok。那么就找到了body的结束位置；
    有几个地方没有看懂：
    1. 找分界的时候没有读取input流的操作，只在buffer中 肯定会有问题
    2. 读取的这些数据是存在什么地方的？
    ````