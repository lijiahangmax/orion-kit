 /*
  * Copyright (c) 2019 - present Jiahang Li, All rights reserved.
  *
  *   https://kit.orionsec.cn
  *
  * Members:
  *   Jiahang Li - ljh1553488six@139.com - author
  *
  * The MIT License (MIT)
  * Permission is hereby granted, free of charge, to any person obtaining a copy of
  * this software and associated documentation files (the "Software"), to deal in
  * the Software without restriction, including without limitation the rights to
  * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
  * the Software, and to permit persons to whom the Software is furnished to do so,
  * subject to the following conditions:
  *
  * The above copyright notice and this permission notice shall be included in all
  * copies or substantial portions of the Software.
  *
  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
  * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
  * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
  * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
  * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
  */
 package cn.orionsec.kit.net.telnet;

import cn.orionsec.kit.lang.exception.TimeoutException;
import cn.orionsec.kit.lang.utils.collect.Lists;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;

 import static org.junit.Assert.*;

 /**
  * TelnetReads test
  *
  * @author Jiahang Li
  * @version 1.0.0
  * @since 2026/10/2
  */
 public class TelnetReadsTests {

     private static final String CHARSET = "UTF-8";

     @Test
     public void testReadUntilResultMatched() throws IOException {
         InputStream in = new ByteArrayInputStream("abc login: ".getBytes(CHARSET));
         TelnetReadResult result = TelnetReads.readUntilResult(in, Lists.singleton("login:"), CHARSET, 1000, 4096);
         assertFalse(result.isEof());
         assertFalse(result.isTimeout());
         // 返回内容截至 pattern 末尾
         assertEquals("abc login:", result.getContent());
     }

     @Test
     public void testReadUntilResultStripAnsi() throws IOException {
         // CSI / OSC 转义序列剔除
         InputStream in = new ByteArrayInputStream("\u001B[6nbanner\u001B]0;title\u0007 login: ".getBytes(CHARSET));
         TelnetReadResult result = TelnetReads.readUntilResult(in, Lists.singleton("login:"), CHARSET, 1000, 4096);
         assertEquals("banner login:", result.getContent());
     }

     @Test
     public void testReadUntilResultEof() throws IOException {
         InputStream in = new ByteArrayInputStream("abc".getBytes(CHARSET));
         TelnetReadResult result = TelnetReads.readUntilResult(in, Lists.singleton("login:"), CHARSET, 1000, 4096);
         assertTrue(result.isEof());
         assertFalse(result.isTimeout());
         assertEquals("abc", result.getContent());
     }

     @Test
     public void testReadUntilResultTimeout() throws IOException {
         InputStream in = new DataThenTimeoutInputStream("abc");
         TelnetReadResult result = TelnetReads.readUntilResult(in, Lists.singleton("login:"), CHARSET, 1000, 4096);
         assertFalse(result.isEof());
         assertTrue(result.isTimeout());
         assertEquals("abc", result.getContent());
     }

     @Test(expected = TimeoutException.class)
     public void testReadUntilThrowTimeout() throws IOException {
         InputStream in = new DataThenTimeoutInputStream("abc");
         TelnetReads.readUntil(in, Lists.singleton("login:"), CHARSET, 1000, 4096);
     }

     @Test
     public void testReadUntilIdleResultEof() throws IOException {
         InputStream in = new ByteArrayInputStream("banner\nprompt".getBytes(CHARSET));
         TelnetReadResult result = TelnetReads.readUntilIdleResult(in, CHARSET, 50, 1000, 4096);
         assertTrue(result.isEof());
         assertFalse(result.isTimeout());
         assertEquals("banner\nprompt", result.getContent());
     }

     @Test
     public void testReadUntilIdleResultTimeout() throws IOException {
         InputStream in = new AlwaysTimeoutInputStream();
         TelnetReadResult result = TelnetReads.readUntilIdleResult(in, CHARSET, 10, 50, 4096);
         assertFalse(result.isEof());
         assertTrue(result.isTimeout());
         assertEquals("", result.getContent());
     }

     @Test
     public void testReadUntilDecidedImmediately() throws IOException {
         InputStream in = new ByteArrayInputStream("banner prompt".getBytes(CHARSET));
         TelnetReadResult result = TelnetReads.readUntilDecided(in, CHARSET, 50, 1000, 4096, text -> text.contains("prompt"));
         assertFalse(result.isEof());
         assertFalse(result.isTimeout());
         assertEquals("banner prompt", result.getContent());
     }

     @Test
     public void testReadUntilDecidedWaitsThroughTimeout() throws IOException {
         // 有数据不算超时, 等待到最大时长返回
         InputStream in = new DataThenTimeoutInputStream("partial");
         TelnetReadResult result = TelnetReads.readUntilDecided(in, CHARSET, 20, 200, 4096, text -> false);
         assertFalse(result.isEof());
         assertFalse(result.isTimeout());
         assertEquals("partial", result.getContent());
     }

     @Test
     public void testReadUntilDecidedTimeoutWithoutData() throws IOException {
         // 未收到任何数据且未关闭: 视为读取超时
         InputStream in = new AlwaysTimeoutInputStream();
         TelnetReadResult result = TelnetReads.readUntilDecided(in, CHARSET, 10, 50, 4096, text -> false);
         assertFalse(result.isEof());
         assertTrue(result.isTimeout());
         assertEquals("", result.getContent());
     }

     @Test
     public void testReadUntilDecidedEof() throws IOException {
         InputStream in = new ByteArrayInputStream("abc".getBytes(CHARSET));
         TelnetReadResult result = TelnetReads.readUntilDecided(in, CHARSET, 20, 200, 4096, text -> false);
         assertTrue(result.isEof());
         assertEquals("abc", result.getContent());
     }

     @Test
     public void testReadUntilDecidedWaitsForDelayedEvidence() throws IOException {
         // 模拟密码错误: 数据输出后静默, 数秒后(此处为一次超时)才输出失败信息
         InputStream in = new ScriptedInputStream(
                 "banner\r\n\r\n".getBytes(CHARSET),
                 new SocketTimeoutException(),
                 "Login incorrect\r\nlogin: ".getBytes(CHARSET));
         TelnetReadResult result = TelnetReads.readUntilDecided(in, CHARSET, 30, 2000, 4096,
                 text -> text.contains("Login incorrect"), null);
         assertFalse(result.isTimeout());
         assertEquals("banner\r\n\r\nLogin incorrect\r\nlogin: ", result.getContent());
     }

     @Test
     public void testReadUntilDecidedSettleOnIdle() throws IOException {
         // 静默结算回调
         InputStream in = new DataThenTimeoutInputStream("root@host:~# ");
         TelnetReadResult result = TelnetReads.readUntilDecided(in, CHARSET, 20, 5000, 4096,
                 text -> false, text -> text.endsWith("# "));
         assertFalse(result.isTimeout());
         assertEquals("root@host:~# ", result.getContent());
     }

     /**
      * 先返回一次数据 之后读取永久超时
      */
     private static class DataThenTimeoutInputStream extends InputStream {

         private final byte[] data;

         private boolean first = true;

         private DataThenTimeoutInputStream(String content) {
             this.data = content.getBytes(java.nio.charset.StandardCharsets.UTF_8);
         }

         @Override
         public int read() throws IOException {
             throw new SocketTimeoutException();
         }

         @Override
         public int read(byte[] b, int off, int len) throws IOException {
             if (first) {
                 first = false;
                 System.arraycopy(data, 0, b, off, data.length);
                 return data.length;
             }
             throw new SocketTimeoutException();
         }

     }

     /**
      * 读取永久超时
      */
     private static class AlwaysTimeoutInputStream extends InputStream {

         @Override
         public int read() throws IOException {
             throw new SocketTimeoutException();
         }

         @Override
         public int read(byte[] b, int off, int len) throws IOException {
             throw new SocketTimeoutException();
         }

     }

     /**
      * 按脚本步骤返回: byte[] 步骤返回数据, IOException 步骤抛出, 步骤用尽返回 EOF
      */
     private static class ScriptedInputStream extends InputStream {

         private final Object[] steps;

         private int index;

         private ScriptedInputStream(Object... steps) {
             this.steps = steps;
         }

         @Override
         public int read() throws IOException {
             throw new UnsupportedOperationException();
         }

         @Override
         public int read(byte[] b, int off, int len) throws IOException {
             if (index >= steps.length) {
                 return -1;
             }
             Object step = steps[index++];
             if (step instanceof IOException) {
                 throw (IOException) step;
             }
             byte[] data = (byte[]) step;
             System.arraycopy(data, 0, b, off, data.length);
             return data.length;
         }

     }

 }
