package cn.orionsec.kit.lang.utils.ext;

import com.vdurmont.emoji.Emoji;
import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * EmojiExt 单元测试
 */
public class EmojiExtTest {

    @Test
    public void testIsEmoji() {
        assertTrue(EmojiExt.isEmoji("\uD83D\uDC36"));
        assertFalse(EmojiExt.isEmoji("hello"));
    }

    @Test
    public void testGetByTag() {
        Set<Emoji> emojis = EmojiExt.getByTag("happy");
        assertNotNull(emojis);
    }

    @Test
    public void testListTags() {
        Collection<String> tags = EmojiExt.listTags();
        assertNotNull(tags);
        assertFalse(tags.isEmpty());
    }

    @Test
    public void testGet() {
        Emoji emoji = EmojiExt.get("dog");
        assertNotNull(emoji);
    }

    @Test
    public void testList() {
        Collection<Emoji> emojis = EmojiExt.list();
        assertNotNull(emojis);
        assertFalse(emojis.isEmpty());
    }

    @Test
    public void testSomeEmoji() {
        assertTrue(EmojiExt.someEmoji("hello \uD83D\uDC36"));
        assertFalse(EmojiExt.someEmoji("hello world"));
    }

    @Test
    public void testJustEmoji() {
        assertTrue(EmojiExt.justEmoji("\uD83D\uDC36\uD83D\uDC31"));
        assertFalse(EmojiExt.justEmoji("hello \uD83D\uDC36"));
    }

    @Test
    public void testToUnicode() {
        String result = EmojiExt.toUnicode(":dog:");
        assertNotNull(result);
        assertTrue(result.contains("\uD83D\uDC36"));
    }

    @Test
    public void testToAlias() {
        String result = EmojiExt.toAlias("\uD83D\uDC36");
        assertNotNull(result);
        assertTrue(result.contains(":dog:"));
    }

    @Test
    public void testToHtmlHex() {
        String result = EmojiExt.toHtmlHex("\uD83D\uDC36");
        assertNotNull(result);
        assertTrue(result.contains("&#x"));
    }

    @Test
    public void testToHtml() {
        String result = EmojiExt.toHtml("\uD83D\uDC36");
        assertNotNull(result);
        assertTrue(result.contains("&#"));
    }

    @Test
    public void testClearAllEmoji() {
        String result = EmojiExt.clearAllEmoji("hello \uD83D\uDC36 world");
        assertEquals("hello  world", result);
    }

    @Test
    public void testExtractEmoji() {
        List<String> emojis = EmojiExt.extractEmoji("hello \uD83D\uDC36 world \uD83D\uDC31");
        assertEquals(2, emojis.size());
    }
}
