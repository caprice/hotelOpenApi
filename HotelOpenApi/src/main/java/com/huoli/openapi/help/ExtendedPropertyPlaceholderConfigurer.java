package com.huoli.openapi.help;

import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.stereotype.Component;
/**
 * 
  * 
  * 项目名称：HotelOpenApi
  * 类名称：ExtendedPropertyPlaceholderConfigurer
  * 类描述：
  * 创建人：dongjun
  * 创建时间：2012-10-31 上午10:33:37
  * 修改人：dongjun
  * 修改时间：2012-10-31 上午10:33:37
  * 修改备注：
  * @version  v1.0
  *
 */
@Component  
public class ExtendedPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {  
    private Properties props;  
  
    @Override  
    protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props)  
            throws BeansException {  
        super.processProperties(beanFactory, props);  
        this.props = props;  
    }  
  
    public Object getProperty(String key) {  
        return props.get(key);  
    }  
}  