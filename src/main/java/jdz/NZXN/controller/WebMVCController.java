
package jdz.NZXN.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import jdz.NZXN.entity.announcement.AnnouncementType;

@Configuration
public class WebMVCController extends WebMvcConfigurationSupport {
	
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/home").setViewName("hello");
		registry.addViewController("/").setViewName("hello");
		registry.addViewController("/hello").setViewName("hello");
		registry.addViewController("/login").setViewName("login");
		registry.addViewController("/register/success").setViewName("registerSuccess");
	}
	
	@Override
	public FormattingConversionService mvcConversionService() {
		FormattingConversionService f = super.mvcConversionService();
		f.addConverter(new Converter<String, AnnouncementType>() {
			@Override
			public AnnouncementType convert(String source) {
				return AnnouncementType.of(source);
			}
		});
		return f;
	}
}