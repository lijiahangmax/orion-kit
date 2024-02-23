package com.orion.test.ext;

import com.orion.lang.utils.ext.dom.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/8/2 11:51
 */
public class DomTests {

    String xml;

    @Before
    @Test
    public void build() {
        DomBuilder builder = DomBuilder.create();
        builder.createRootElement("root")
                .addChildNode(new DomElement("key", "v<>1").cdata())
                .addChildNode(new DomElement("key", "v2").addAttributes("name", "'$\"1").addAttributes("age", "2"))
                .addChildNode(new DomElement("key", "v3"))
                .addChildNode(new DomElement("key", "v4"))
                .addChildNode(new DomElement("key"))
                .addChildNode(new DomElement("group")
                        .addChildNode(new DomElement("name", "1"))
                        .addChildNode(new DomElement("name", "2"))
                        .addChildNode(new DomElement("name", "3"))
                        .addChildNode(new DomElement("sex")
                                .addChildNode(new DomElement("man").addAttributes("key", "1"))
                                .addChildNode(new DomElement("woman").addAttributes("key", "2"))));
        xml = builder.build().getFormatXml();
        System.out.println(xml);

    }

    @Test
    public void ext() {
        System.out.println(DomSupport.parseValue(DomSupport.toElement(xml), "key"));
        System.out.println(DomSupport.parseValue(DomSupport.toElement(xml), "group > name"));
    }

    @Test
    public void parse() {
        Map<String, DomNode> d = DomSupport.toDomNode(xml);
        System.out.println(d);
        d = DomSupport.toDomNode(xml, "group > sex");
        System.out.println(d);
    }

    @Test
    public void stream() {
        String attr = DomStream.of(xml).child("group")
                .child("name", 1)
                .next()
                .prev()
                .parent()
                .prev(3)
                .getAttribute("name");
        System.out.println(attr);
    }

    @Test
    public void stream1() {
        String attr = DomStream.of(this.xml).child("group")
                .child("name", 1)
                .next()
                .prev()
                .parent()
                .prev(3)
                .getAttribute("name");
        System.out.println(attr);
    }

}
