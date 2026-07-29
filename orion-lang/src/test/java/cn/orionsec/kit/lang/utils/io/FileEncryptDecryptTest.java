package cn.orionsec.kit.lang.utils.io;

import cn.orionsec.kit.lang.utils.io.crypto.FileDecrypt;
import cn.orionsec.kit.lang.utils.io.crypto.FileEncrypt;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Files;

import static org.junit.Assert.*;

/**
 * FileEncrypt / FileDecrypt 文件加密解密测试
 */
public class FileEncryptDecryptTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Test
    public void testEncryptAndDecrypt() throws Exception {
        String password = "testPassword123!";
        String originalContent = "hello world, this is encrypted content";

        // 创建源文件
        File srcFile = tempFolder.newFile("source.txt");
        Files.write(srcFile.toPath(), originalContent.getBytes());

        // 加密
        File encryptedFile = tempFolder.newFile("encrypted.dat");
        FileEncrypt encrypt = new FileEncrypt(srcFile, encryptedFile, password);
        Boolean encResult = encrypt.call();
        assertTrue(encResult);
        assertTrue(encryptedFile.length() > 0);

        // 解密
        File decryptedFile = tempFolder.newFile("decrypted.txt");
        FileDecrypt decrypt = new FileDecrypt(encryptedFile, decryptedFile, password);
        Boolean decResult = decrypt.call();
        assertTrue(decResult);

        // 验证解密后的内容
        String decryptedContent = new String(Files.readAllBytes(decryptedFile.toPath()));
        assertEquals(originalContent, decryptedContent);
    }

    @Test
    public void testEncryptWithStreams() throws Exception {
        String password = "streamPass";
        String content = "stream encryption test";

        ByteArrayInputStream in = new ByteArrayInputStream(content.getBytes());
        ByteArrayOutputStream encOut = new ByteArrayOutputStream();

        FileEncrypt encrypt = new FileEncrypt(in, encOut, password);
        Boolean result = encrypt.call();
        assertTrue(result);
        assertTrue(encOut.size() > 0);

        // 解密
        ByteArrayInputStream encIn = new ByteArrayInputStream(encOut.toByteArray());
        ByteArrayOutputStream decOut = new ByteArrayOutputStream();
        FileDecrypt decrypt = new FileDecrypt(encIn, decOut, password);
        Boolean decResult = decrypt.call();
        assertTrue(decResult);
        assertEquals(content, decOut.toString());
    }

    @Test
    public void testEncryptWrongPassword() throws Exception {
        String password = "correctPassword";
        String wrongPassword = "wrongPassword";
        String content = "sensitive data";

        // 加密
        ByteArrayInputStream in = new ByteArrayInputStream(content.getBytes());
        ByteArrayOutputStream encOut = new ByteArrayOutputStream();
        FileEncrypt encrypt = new FileEncrypt(in, encOut, password);
        assertTrue(encrypt.call());

        // 用错误密码解密
        ByteArrayInputStream encIn = new ByteArrayInputStream(encOut.toByteArray());
        ByteArrayOutputStream decOut = new ByteArrayOutputStream();
        FileDecrypt decrypt = new FileDecrypt(encIn, decOut, wrongPassword);
        Boolean result = decrypt.call();
        // 解密应失败或内容不匹配
        if (result) {
            assertNotEquals(content, decOut.toString());
        } else {
            assertFalse(result);
        }
    }
}
