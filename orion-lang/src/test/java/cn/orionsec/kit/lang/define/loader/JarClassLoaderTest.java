package cn.orionsec.kit.lang.define.loader;

import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

import static org.junit.Assert.*;

/**
 * JarClassLoader 测试
 */
@SuppressWarnings("deprecation")
public class JarClassLoaderTest {

    @Test
    public void testLoadJar() throws Exception {
        File jar = createJar();
        URLClassLoader loader = JarClassLoader.loadJar(jar);
        try {
            assertTrue(loader instanceof JarClassLoader);
            assertEquals(1, loader.getURLs().length);
        } finally {
            loader.close();
        }
    }

    @Test
    public void testAddJar() throws Exception {
        File jar = createJar();
        JarClassLoader loader = new JarClassLoader();
        try {
            assertSame(loader, loader.addJar(jar));
            assertEquals(1, loader.getURLs().length);
        } finally {
            loader.close();
        }
    }

    @Test
    public void testLoadJarToForeignLoader() throws Exception {
        File jar = createJar();
        assertThrows(UnsupportedOperationException.class,
                () -> JarClassLoader.loadJar(new URLClassLoader(new URL[0]), jar));
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
