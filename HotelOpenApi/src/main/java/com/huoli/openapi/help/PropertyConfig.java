package com.huoli.openapi.help;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
  * 
  * 项目名称：HotelOpenApi
  * 类名称：PropertyConfig
  * 类描述：
  * 创建人：dongjun
  * 创建时间：2012-10-31 上午10:34:10
  * 修改人：dongjun
  * 修改时间：2012-10-31 上午10:34:10
  * 修改备注：
  * @version  v1.0
  *
 */
@Retention(RetentionPolicy.RUNTIME)  
@Target(ElementType.FIELD)  
public @interface PropertyConfig {  
  String value() default "";  

  boolean required() default true;  
}  