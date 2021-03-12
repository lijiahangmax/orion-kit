package com.orion.dom;

import com.orion.utils.Exceptions;
import com.orion.utils.Strings;
import com.orion.utils.Valid;
import com.orion.utils.collect.Lists;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * XML流
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/3/24 13:00
 */
@SuppressWarnings("ALL")
public class DomStream {

    private Element element;

    public DomStream(String xml) {
        this(DomExt.toDocument(xml).getRootElement());
    }

    public DomStream(Element element) {
        Valid.notNull(element, "the element is null");
        this.element = element;
    }

    // -------------------- child --------------------

    public DomStream childFirst() {
        List<Element> elements = this.element.elements();
        if (elements != null && elements.size() >= 1) {
            this.element = elements.get(0);
            return this;
        }
        throw Exceptions.argument("not found child element");
    }

    public DomStream childLast() {
        List<Element> elements = this.element.elements();
        if (elements != null && elements.size() >= 1) {
            this.element = elements.get(elements.size() - 1);
            return this;
        }
        throw Exceptions.argument("not found child element");
    }

    public DomStream childFirst(String tag) {
        Element element = this.element.element(tag);
        if (element != null) {
            this.element = element;
            return this;
        }
        throw Exceptions.argument(Strings.format("not found child element tag: {}", tag));
    }

    public DomStream childLast(String tag) {
        List<Element> elements = this.element.elements(tag);
        if (elements.size() >= 1) {
            this.element = elements.get(elements.size() - 1);
            return this;
        }
        throw Exceptions.argument(Strings.format("not found child element tag: {}", tag));
    }

    public DomStream child() {
        return child(0);
    }

    public DomStream child(int index) {
        List<Element> elements = this.element.elements();
        if (elements != null && elements.size() > index) {
            this.element = elements.get(index);
            return this;
        }
        throw Exceptions.argument(Strings.format("not found child element index: {}", index));
    }

    public DomStream child(String tag) {
        Element element = this.element.element(tag);
        if (element != null) {
            this.element = element;
            return this;
        }
        throw Exceptions.argument(Strings.format("not found child element tag: {}", tag));
    }

    public DomStream child(String tag, int index) {
        List<Element> elements = this.element.elements(tag);
        if (elements != null && elements.size() > index) {
            this.element = elements.get(index);
            return this;
        }
        throw Exceptions.argument(Strings.format("not found child element tag: {}, index: {}", tag, index));
    }

    public DomStream child(String tag, String attrKey) {
        return child(tag, 0, attrKey, null);
    }

    public DomStream child(String tag, String attrKey, String attrValue) {
        return child(tag, 0, attrKey, attrValue);
    }

    public DomStream child(String tag, int index, String attrKey) {
        return child(tag, index, attrKey, null);
    }

    public DomStream child(String tag, int index, String attrKey, String attrValue) {
        List<Element> elements = this.element.elements(tag);
        int i = 0;
        for (Element element : elements) {
            String attribute = DomExt.getAttribute(element, attrKey);
            if (attribute != null) {
                if (attrValue == null) {
                    if (i++ == index) {
                        this.element = element;
                        return this;
                    }
                } else if (attrValue.trim().equals(attribute.trim())) {
                    if (i++ == index) {
                        this.element = element;
                        return this;
                    }
                }
            }
        }
        if (attrValue == null) {
            throw Exceptions.argument(Strings.format("not found child element tag: {}, key: {}, index: {}", tag, attrKey, index));
        } else {
            throw Exceptions.argument(Strings.format("not found child element tag: {}, key: {}, value: {}, index: {}", tag, attrKey, attrValue, index));
        }
    }

    // -------------------- parent --------------------

    public DomStream parentFirst() {
        Element parent = this.element.getParent();
        while (true) {
            if (parent != null) {
                Element p = parent.getParent();
                if (p == null) {
                    this.element = parent;
                    return this;
                } else {
                    parent = p;
                }
            } else {
                return this;
            }
        }
    }

    public DomStream parent() {
        Element parent = this.element.getParent();
        if (parent != null) {
            this.element = parent;
            return this;
        }
        throw Exceptions.argument("not found parent element");
    }

    public DomStream parent(int index) {
        if (index < 0) {
            throw Exceptions.argument("not found parent element index: {}" + index);
        }
        for (int i = 0; i < index + 1; i++) {
            Element parent = this.element.getParent();
            if (parent != null) {
                this.element = parent;
            } else {
                throw Exceptions.argument(Strings.format("not found parent element index: {}", i));
            }
        }
        return this;
    }

    public DomStream parent(String tag) {
        return parent(tag, 0);
    }

    public DomStream parent(String tag, int index) {
        if (tag == null) {
            throw Exceptions.argument("not found parent element because tag is null");
        }
        int i = 0;
        Element parent = this.element.getParent();
        while (true) {
            if (parent != null) {
                if (parent.getName().trim().equals(tag.trim())) {
                    if (i++ == index) {
                        this.element = parent;
                        return this;
                    } else {
                        parent = parent.getParent();
                    }
                } else {
                    parent = parent.getParent();
                }
            } else {
                throw Exceptions.argument(Strings.format("not found parent element tag: {}, index: {}", tag, index));
            }
        }
    }

    public DomStream parent(String tag, String attrKey) {
        return parent(tag, 0, attrKey, null);
    }

    public DomStream parent(String tag, String attrKey, String attrValue) {
        return parent(tag, 0, attrKey, attrValue);
    }

    public DomStream parent(String tag, int index, String attrKey) {
        return parent(tag, index, attrKey, null);
    }

    public DomStream parent(String tag, int index, String attrKey, String attrValue) {
        if (tag == null) {
            throw Exceptions.argument("not found parent element because tag is null");
        }
        Element parent = this.element.getParent();
        int i = 0;
        while (true) {
            if (parent != null) {
                if (parent.getName().trim().equals(tag.trim())) {
                    if (attrKey != null) {
                        String attribute = DomExt.getAttribute(parent, attrKey);
                        if (attribute != null) {
                            if (attrValue == null) {
                                if (i++ == index) {
                                    this.element = parent;
                                    return this;
                                }
                            } else if (attribute.trim().equals(attrValue.trim())) {
                                if (i++ == index) {
                                    this.element = parent;
                                    return this;
                                }
                            }
                        }
                    } else {
                        this.element = parent;
                        return this;
                    }
                }
                parent = parent.getParent();
            } else {
                throw Exceptions.argument(Strings.format("not found parent element tag: {}, key: {}, value: {}, index: {}", tag, attrKey, attrValue, index));
            }
        }
    }

    // -------------------- next --------------------

    public DomStream nextLast() {
        Element parent = this.element.getParent();
        List<Element> elements = parent.elements();
        this.element = elements.get(elements.size() - 1);
        return this;
    }

    public DomStream next() {
        return next(0);
    }

    public DomStream next(int index) {
        if (index < 0) {
            throw Exceptions.argument(Strings.format("not found next element index: {}", index));
        }
        Element parent = this.element.getParent();
        List<Element> elements = parent.elements();
        int len = elements.size();
        if (len == 1) {
            throw Exceptions.argument("not found next element");
        }
        if (len < index + 1) {
            throw Exceptions.argument(Strings.format("not found next element index: {}", index));
        }
        int thisIndex = 0;
        for (int i = 0; i < len; i++) {
            if (elements.get(i).equals(this.element)) {
                thisIndex = i;
                break;
            }
        }
        int nextIndex = thisIndex + index + 1;
        if (len <= nextIndex) {
            throw Exceptions.argument(Strings.format("not found next element index: {}, thisIndex: {}, nextIndex: {}, count: {}", index, thisIndex, nextIndex, len));
        }
        this.element = elements.get(nextIndex);
        return this;
    }

    public DomStream next(String tag) {
        return next(tag, 0);
    }

    public DomStream next(String tag, int index) {
        if (tag == null) {
            throw Exceptions.argument("not found next element because tag is null");
        }
        Element parent = this.element.getParent();
        List<Element> elements = parent.elements();
        int len = elements.size();
        if (len == 1) {
            throw Exceptions.argument("not found next element");
        }
        if (len < index + 1) {
            throw Exceptions.argument(Strings.format("not found next element tag: {}, index: {}", tag, index));
        }
        int thisIndex = 0;
        for (int i = 0; i < len; i++) {
            if (elements.get(i).equals(this.element)) {
                thisIndex = i;
                break;
            }
        }
        int is = 0;
        for (int i = thisIndex + 1; i < len; i++) {
            Element nowElement = elements.get(i);
            if (nowElement.getName().trim().equals(tag.trim())) {
                if (is++ == index) {
                    this.element = nowElement;
                    return this;
                }
            }
        }
        throw Exceptions.argument(Strings.format("not found next element tag: {}, index: {}", tag, index));
    }

    public DomStream next(String tag, String attrKey) {
        return next(tag, 0, attrKey, null);
    }

    public DomStream next(String tag, String attrKey, String attrValue) {
        return next(tag, 0, attrKey, attrValue);
    }

    public DomStream next(String tag, int index, String attrKey) {
        return next(tag, index, attrKey, null);
    }

    public DomStream next(String tag, int index, String attrKey, String attrValue) {
        if (tag == null) {
            throw Exceptions.argument("not found next element because tag is null");
        }
        Element parent = this.element.getParent();
        List<Element> elements = parent.elements();
        int len = elements.size();
        if (len == 1) {
            throw Exceptions.argument("not found next element");
        }
        if (len < index + 1) {
            throw Exceptions.argument(Strings.format("not found next element tag: {}, index: {}, key: {}, value: {}", tag, index, attrKey, attrValue));
        }
        int thisIndex = 0;
        for (int i = 0; i < len; i++) {
            if (elements.get(i).equals(this.element)) {
                thisIndex = i;
                break;
            }
        }
        int is = 0;
        for (int i = thisIndex + 1; i < len; i++) {
            Element nowElement = elements.get(i);
            if (nowElement.getName().trim().equals(tag.trim())) {
                if (attrKey != null) {
                    String attribute = DomExt.getAttribute(nowElement, attrKey);
                    if (attribute != null) {
                        if (attrValue == null) {
                            if (is++ == index) {
                                this.element = nowElement;
                                return this;
                            }
                        } else if (attribute.trim().equals(attrValue.trim())) {
                            if (is++ == index) {
                                this.element = nowElement;
                                return this;
                            }
                        }
                    }
                } else {
                    if (is++ == index) {
                        this.element = nowElement;
                        return this;
                    }
                }
            }
        }
        throw Exceptions.argument(Strings.format("not found next element tag: {}, index: {}, key: {}, value: {}", tag, index, attrKey, attrValue));
    }

    // -------------------- prev --------------------

    public DomStream prevFirst() {
        Element parent = this.element.getParent();
        List<Element> elements = parent.elements();
        this.element = elements.get(0);
        return this;
    }

    public DomStream prev() {
        return prev(0);
    }

    public DomStream prev(int index) {
        if (index < 0) {
            throw Exceptions.argument(Strings.format("not found previous element index: {}", index));
        }
        Element parent = this.element.getParent();
        List<Element> elements = parent.elements();
        int len = elements.size();
        if (len == 1) {
            throw Exceptions.argument("not found previous element");
        }
        if (len < index + 1) {
            throw Exceptions.argument(Strings.format("not found previous element index: {}", index));
        }
        int thisIndex = 0;
        for (int i = 0; i < len; i++) {
            if (elements.get(i).equals(this.element)) {
                thisIndex = i;
                break;
            }
        }
        int prevIndex = thisIndex - index - 1;
        if (len <= prevIndex || prevIndex < 0) {
            throw Exceptions.argument(Strings.format("not found previous element index: {}, thisIndex: {}, prevIndex: {}, count: {}", index, thisIndex, prevIndex, len));
        }
        this.element = elements.get(prevIndex);
        return this;
    }

    public DomStream prev(String tag) {
        return prev(tag, 0);
    }

    public DomStream prev(String tag, int index) {
        if (index < 0) {
            throw Exceptions.argument(Strings.format("not found previous element index: {}", index));
        }
        Element parent = this.element.getParent();
        List<Element> elements = parent.elements();
        int len = elements.size();
        if (len == 1) {
            throw Exceptions.argument("not found previous element");
        }
        if (len < index + 1) {
            throw Exceptions.argument(Strings.format("not found previous element index: {}", index));
        }
        int thisIndex = 0;
        for (int i = 0; i < len; i++) {
            if (elements.get(i).equals(this.element)) {
                thisIndex = i;
                break;
            }
        }

        int is = 0;
        for (int i = thisIndex - 1; i >= 0; i--) {
            Element nowElement = elements.get(i);
            if (nowElement.getName().trim().equals(tag.trim())) {
                if (is++ == index) {
                    this.element = nowElement;
                    return this;
                }
            }
        }
        throw Exceptions.argument(Strings.format("not found previous element tag: {}, index: {}", tag, index));
    }

    public DomStream prev(String tag, String attrKey) {
        return prev(tag, 0, attrKey, null);
    }

    public DomStream prev(String tag, String attrKey, String attrValue) {
        return prev(tag, 0, attrKey, attrValue);
    }

    public DomStream prev(String tag, int index, String attrKey) {
        return prev(tag, index, attrKey, null);
    }

    public DomStream prev(String tag, int index, String attrKey, String attrValue) {
        if (tag == null) {
            throw Exceptions.argument("not found previous element because tag is null");
        }
        Element parent = this.element.getParent();
        List<Element> elements = parent.elements();
        int len = elements.size();
        if (len == 1) {
            throw Exceptions.argument("not found previous element");
        }
        if (len < index + 1) {
            throw Exceptions.argument(Strings.format("not found previous element tag: {}, index: {}, key: {}, value: {}", tag, index, attrKey, attrValue));
        }
        int thisIndex = 0;
        for (int i = 0; i < len; i++) {
            if (elements.get(i).equals(this.element)) {
                thisIndex = i;
                break;
            }
        }
        int is = 0;
        for (int i = thisIndex - 1; i >= 0; i--) {
            Element nowElement = elements.get(i);
            if (nowElement.getName().trim().equals(tag.trim())) {
                if (attrKey != null) {
                    String attribute = DomExt.getAttribute(nowElement, attrKey);
                    if (attribute != null) {
                        if (attrValue == null) {
                            if (is++ == index) {
                                this.element = nowElement;
                                return this;
                            }
                        } else if (attribute.trim().equals(attrValue.trim())) {
                            if (is++ == index) {
                                this.element = nowElement;
                                return this;
                            }
                        }
                    }
                } else {
                    if (is++ == index) {
                        this.element = nowElement;
                        return this;
                    }
                }
            }
        }
        throw Exceptions.argument(Strings.format("not found previous element tag: {}, index: {}, key: {}, value: {}", tag, index, attrKey, attrValue));
    }

    // -------------------- result --------------------

    public List<Element> childs() {
        List<Element> elements = this.element.elements();
        if (elements != null) {
            return elements;
        }
        return new ArrayList<>();
    }

    public List<Element> childs(String tag) {
        List<Element> elements = this.element.elements(tag);
        if (elements != null) {
            return elements;
        }
        return new ArrayList<>();
    }

    public int childCount() {
        return Lists.size(this.element.elements());
    }

    public int childCount(String tag) {
        return Lists.size(this.element.elements(tag));
    }

    public List<Element> parents() {
        List<Element> list = new ArrayList<>();
        Element parent = this.element.getParent();
        while (true) {
            if (parent != null) {
                list.add(parent);
                parent = parent.getParent();
            } else {
                break;
            }
        }
        return list;
    }

    public List<Element> parents(String tag) {
        List<Element> list = new ArrayList<>();
        Element parent = this.element.getParent();
        while (true) {
            if (parent != null) {
                if (tag != null && parent.getName().trim().equals(tag.trim())) {
                    list.add(parent);
                }
                parent = parent.getParent();
            } else {
                break;
            }
        }
        return list;
    }

    public int parentCount() {
        return this.parents().size();
    }

    public int parentCount(String tag) {
        return this.parents(tag).size();
    }

    public List<Element> nexts() {
        List<Element> list = new ArrayList<>();
        Element parent = this.element.getParent();
        List<Element> elements = parent.elements();
        for (int i = 0, len = elements.size(), thisIndex = -1; i < len; i++) {
            if (thisIndex == -1) {
                if (elements.get(i).equals(this.element)) {
                    thisIndex = i;
                }
            } else {
                list.add(elements.get(i));
            }
        }
        return list;
    }

    public List<Element> nexts(String tag) {
        List<Element> list = new ArrayList<>();
        Element parent = this.element.getParent();
        List<Element> elements = parent.elements();
        for (int i = 0, len = elements.size(), thisIndex = -1; i < len; i++) {
            if (thisIndex == -1) {
                if (elements.get(i).equals(this.element)) {
                    thisIndex = i;
                }
            } else {
                Element thisElement = elements.get(i);
                if (tag == null) {
                    list.add(thisElement);
                } else if (thisElement.getName().trim().equals(tag.trim())) {
                    list.add(thisElement);
                }
            }
        }
        return list;
    }

    public int nextCount() {
        return this.nexts().size();
    }

    public int nextCount(String tag) {
        return this.nexts(tag).size();
    }

    public List<Element> prevs() {
        List<Element> list = new ArrayList<>();
        Element parent = this.element.getParent();
        List<Element> elements = parent.elements();
        int thisIndex = -1;
        for (int i = 0, len = elements.size(); i < len; i++) {
            if (thisIndex == -1 && elements.get(i).equals(this.element)) {
                thisIndex = i;
            }
        }
        for (int i = thisIndex - 1; i >= 0; i--) {
            list.add(elements.get(i));
        }
        return list;
    }

    public List<Element> prevs(String tag) {
        List<Element> list = new ArrayList<>();
        Element parent = this.element.getParent();
        List<Element> elements = parent.elements();
        int thisIndex = -1;
        for (int i = 0, len = elements.size(); i < len; i++) {
            if (thisIndex == -1 && elements.get(i).equals(this.element)) {
                thisIndex = i;
            }
        }
        for (int i = thisIndex - 1; i >= 0; i--) {
            Element thisElement = elements.get(i);
            if (tag == null) {
                list.add(thisElement);
            } else if (thisElement.getName().trim().equals(tag.trim())) {
                list.add(thisElement);
            }
        }
        return list;
    }

    public int prevCount() {
        return this.prevs().size();
    }

    public int prevCount(String tag) {
        return this.prevs(tag).size();
    }

    /**
     * 获取当前元素同级的索引
     *
     * @return 索引
     */
    public int getThisElementIndex() {
        Element parent = this.element.getParent();
        List<Element> elements = parent.elements();
        for (int i = 0, len = elements.size(); i < len; i++) {
            if (elements.get(i).equals(this.element)) {
                return i;
            }
        }
        return 0;
    }

    /**
     * 获取当前 Element
     *
     * @return Element
     */
    public Element getElement() {
        return element;
    }

    /**
     * 获取当前属性
     *
     * @param key value
     * @return value
     */
    public String getAttribute(String key) {
        return DomExt.getAttribute(this.element, key);
    }

    /**
     * 获取当前属性
     *
     * @return value
     */
    public Map<String, String> getAttributes() {
        return DomExt.getAttributes(this.element);
    }

    /**
     * 获取element值
     *
     * @return element
     */
    public String getValue() {
        return element.getStringValue();
    }

}