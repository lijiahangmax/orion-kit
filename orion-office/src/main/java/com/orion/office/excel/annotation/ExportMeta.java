package com.orion.office.excel.annotation;

import com.orion.lang.utils.Strings;

import java.lang.annotation.*;

/**
 * excel 元数据注解
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/3/8 18:06
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExportMeta {

    /**
     * 作者
     * <p>
     * 为空则使用默认作者
     *
     * @see com.orion.office.KitOfficeConfiguration#EXCEL_DEFAULT_AUTHOR
     */
    String author() default Strings.EMPTY;

    /**
     * 标题
     */
    String title() default Strings.EMPTY;

    /**
     * 主体
     */
    String subject() default Strings.EMPTY;

    /**
     * 关键字
     */
    String keywords() default Strings.EMPTY;

    /**
     * 修订
     */
    String revision() default Strings.EMPTY;

    /**
     * 描述
     */
    String description() default Strings.EMPTY;

    /**
     * 分类
     */
    String category() default Strings.EMPTY;

    /**
     * 公司
     */
    String company() default Strings.EMPTY;

    /**
     * 经理
     */
    String manager() default Strings.EMPTY;

    /**
     * 应用
     * <p>
     * 为空则使用默认应用
     *
     * @see com.orion.office.KitOfficeConfiguration#EXCEL_DEFAULT_APPLICATION
     */
    String application() default Strings.EMPTY;

    /**
     * 修订人
     */
    String modifiedUser() default Strings.EMPTY;

    /**
     * 内容状态
     */
    String contentStatus() default Strings.EMPTY;

    /**
     * 内容类型
     */
    String contentType() default Strings.EMPTY;

    /**
     * 标识符
     */
    String identifier() default Strings.EMPTY;

}
