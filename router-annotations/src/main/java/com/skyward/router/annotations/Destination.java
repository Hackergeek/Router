package com.skyward.router.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface Destination {
    /**
     * 当前页面的URL，不能为空
     * @return 页面URL
     */
    String url();

    /**
     * 对于当前页面的中文描述
     * @return 例如"我的页面"
     */
    String description();
}
