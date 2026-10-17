package cn.orionsec.kit.lang.define.loader;

import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * JarClassLoader 测试
 */
public class JarClassLoaderTest {

    @Test
    public void testAddJar() throws Exception {
        File jar = createJar();
        try (JarClassLoader loader = new JarClassLoader()) {
            assertSame(loader, loader.addJar(jar));
            assertEquals(1, loader.getURLs().length);
        }
    }

    private File createJar() throws Exception {
        File jar = File.createTempFile("orion-jar-loader", ".jar");
        jar.deleteOnExit();
        try (JarOutputStream out = new JarOutputStream(new FileOutputStream(jar))) {
            out.putNextEntry(new JarEntry("dummy.txt"));
            out.write("dummy".getBytes());
            out.closeEntry();
        }
        return jar;
    }

}
