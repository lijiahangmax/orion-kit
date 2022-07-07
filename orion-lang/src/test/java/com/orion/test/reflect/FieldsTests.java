package com.orion.test.reflect;

import com.orion.lang.define.Console;
import com.orion.lang.utils.reflect.Fields;
import com.orion.lang.utils.reflect.Methods;
import com.orion.test.reflect.value.BaseTypeUser;
import com.orion.test.reflect.value.User;
import org.junit.Test;

/**
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2020/5/13 18:39
 */
public class FieldsTests {

    @Test
    public void getTests() {
        System.out.println(Fields.getFieldNameByMethodName("getName"));
        System.out.println(Fields.getFieldNameByMethodName("setName"));
        System.out.println(Fields.getFieldNameByMethodName("isName"));
        System.out.println(Fields.getFieldByMethod(User.class, Methods.getAccessibleMethod(User.class, "getId")));
        System.out.println(Fields.getFieldByMethodName(User.class, "getId"));
        System.out.println("------");
        System.out.println((Object) Fields.getFieldValue(new User(1L), "id"));
        System.out.println((Object) Fields.getFieldValue(new User(2L), Fields.getAccessibleField(User.class, "id")));
    }

    @Test
    public void setTests() {
        User user = new User();
        Fields.setFieldValue(user, "id", 1L);
        System.out.println(user.getId());
        Fields.setFieldValue(user, Fields.getAccessibleField(user.getClass(), "id"), 2L);
        System.out.println(user.getId());
        Fields.setFieldValueInfer(user, "id", "2");
        System.out.println(user.getId());
        Fields.setFieldValueInfer(user, Fields.getAccessibleField(user.getClass(), "id"), 3);
        System.out.println(user.getId());
    }

    @Test
    public void getFieldTests() {
        System.out.println("------");
        Fields.getFields(User.class).forEach(Console::trace);
        System.out.println("------");
        Fields.getFieldMap(User.class).forEach(Console::trace);
        System.out.println("------");
        Fields.getStaticFields(User.class).forEach(Console::trace);
    }

    @Test
    public void getBaseFieldTests() {
        BaseTypeUser u = new BaseTypeUser();
        Fields.setFieldValueInfer(u, "id", 1L);
        Fields.setFieldValue(u, "time", 2L);
        Fields.setFieldValue(u, "sex", true);
        Fields.setFieldValue(u, "name", 'n');
        System.out.println(u.toString());
        System.out.println((Object) Fields.getFieldValue(u, "id"));
        System.out.println((Object) Fields.getFieldValue(u, "time"));
        System.out.println((Object) Fields.getFieldValue(u, "sex"));
        System.out.println((Object) Fields.getFieldValue(u, "name"));
    }

}
