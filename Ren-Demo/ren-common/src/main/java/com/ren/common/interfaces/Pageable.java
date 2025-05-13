package com.ren.common.interfaces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) // 标注在方法上
@Retention(RetentionPolicy.RUNTIME) // 运行时生效
public @interface Pageable {
}