package cn.orionsec.kit.lang.annotation;

import org.junit.Test;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.junit.Assert.*;

/**
 * 注解元数据验证测试
 */
public class AnnotationMetadataTest {

    @Test
    public void testAliasRetention() {
        Retention retention = Alias.class.getAnnotation(Retention.class);
        assertNotNull(retention);
        assertEquals(RetentionPolicy.CLASS, retention.value());
    }

    @Test
    public void testAliasDocumented() {
        Documented documented = Alias.class.getAnnotation(Documented.class);
        assertNotNull(documented);
    }

    @Test
    public void testAliasTarget() {
        Target target = Alias.class.getAnnotation(Target.class);
        assertNotNull(target);
        assertTrue(target.value().length > 0);
    }

    @Test
    public void testBetaRetention() {
        Retention retention = Beta.class.getAnnotation(Retention.class);
        assertNotNull(retention);
        assertEquals(RetentionPolicy.CLASS, retention.value());
    }

    @Test
    public void testBetaDocumented() {
        Documented documented = Beta.class.getAnnotation(Documented.class);
        assertNotNull(documented);
    }

    @Test
    public void testDocumentRetention() {
        Retention retention = Document.class.getAnnotation(Retention.class);
        assertNotNull(retention);
        assertEquals(RetentionPolicy.CLASS, retention.value());
    }

    @Test
    public void testDocumentDocumented() {
        Documented documented = Document.class.getAnnotation(Documented.class);
        assertNotNull(documented);
    }

    @Test
    public void testKeepRetention() {
        Retention retention = Keep.class.getAnnotation(Retention.class);
        assertNotNull(retention);
        assertEquals(RetentionPolicy.RUNTIME, retention.value());
    }

    @Test
    public void testKeepDocumented() {
        Documented documented = Keep.class.getAnnotation(Documented.class);
        assertNotNull(documented);
    }

    @Test
    public void testRemovedRetention() {
        Retention retention = Removed.class.getAnnotation(Retention.class);
        assertNotNull(retention);
        assertEquals(RetentionPolicy.CLASS, retention.value());
    }

    @Test
    public void testRemovedDocumented() {
        Documented documented = Removed.class.getAnnotation(Documented.class);
        assertNotNull(documented);
    }

    @Test
    public void testSinceRetention() {
        Retention retention = Since.class.getAnnotation(Retention.class);
        assertNotNull(retention);
        assertEquals(RetentionPolicy.CLASS, retention.value());
    }

    @Test
    public void testSinceDocumented() {
        Documented documented = Since.class.getAnnotation(Documented.class);
        assertNotNull(documented);
    }

    @Test
    public void testAnnotationsAreAnnotations() {
        assertTrue(Alias.class.isAnnotation());
        assertTrue(Beta.class.isAnnotation());
        assertTrue(Document.class.isAnnotation());
        assertTrue(Keep.class.isAnnotation());
        assertTrue(Removed.class.isAnnotation());
        assertTrue(Since.class.isAnnotation());
    }

}
