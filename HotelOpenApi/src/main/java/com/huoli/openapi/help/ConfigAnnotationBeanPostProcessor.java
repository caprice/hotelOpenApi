package com.huoli.openapi.help;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import javax.annotation.Resource;

import org.springframework.beans.BeansException;
import org.springframework.beans.SimpleTypeConverter;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

/**
 * 
 * 
 * 项目名称：HotelOpenApi 类名称：ConfigAnnotationBeanPostProcessor 类描述： 创建人：dongjun
 * 创建时间：2012-10-31 上午10:34:05 修改人：dongjun 修改时间：2012-10-31 上午10:34:05 修改备注：
 * 
 * @version v1.0
 * 
 */

@Component
public class ConfigAnnotationBeanPostProcessor extends
		InstantiationAwareBeanPostProcessorAdapter {

	 @Resource(name="propertyConfigurer")
	 private ExtendedPropertyPlaceholderConfigurer propertyConfigurer;
//	@Resource
//	private PropertyPlaceholderConfigurer propertyPlaceholderConfigurer;
	private SimpleTypeConverter typeConverter = new SimpleTypeConverter();

	/**
	 * <p>
	 * 通过config配置变量,bean初始化以后设置properties文件里面的值
	 * </p>
	 * {@inheritDoc}
	 */
	@Override
	public boolean postProcessAfterInstantiation(final Object bean,
			String beanName) throws BeansException {
		ReflectionUtils.doWithFields(bean.getClass(),
				new ReflectionUtils.FieldCallback() {
					public void doWith(Field field)
							throws IllegalArgumentException,
							IllegalAccessException {
						PropertyConfig cfg = field
								.getAnnotation(PropertyConfig.class);
						if (cfg != null) {
							if (Modifier.isStatic(field.getModifiers())) {
								throw new IllegalStateException(
										"@PropertyConfig annotation is not supported on static fields");
							}

							String key = cfg.value().length() <= 0 ? field
									.getName() : cfg.value();
							Object value = propertyConfigurer.getProperty(key);
							if (cfg.required() && value == null) {
								throw new NullPointerException(bean.getClass()
										.getSimpleName()
										+ "."
										+ field.getName()
										+ " is requred,but not been configured");
							} else if (value != null) {
								Object _value = typeConverter
										.convertIfNecessary(value,
												field.getType());
								ReflectionUtils.makeAccessible(field);
								field.set(bean, _value);
							}
						}
					}
				});
		return true;
	}
}