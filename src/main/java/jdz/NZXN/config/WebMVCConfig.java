
package jdz.NZXN.config;

import java.util.UUID;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import jdz.NZXN.entity.JSON;
import jdz.NZXN.entity.announcement.AnnouncementType;

@Configuration
@EnableWebMvc
public class WebMVCConfig extends WebMvcConfigurationSupport {
	@Override
	public FormattingConversionService mvcConversionService() {
		FormattingConversionService f = super.mvcConversionService();
		f.addConverter(new Converter<String, AnnouncementType>() {
			@Override
			public AnnouncementType convert(String source) {
				source = JSON.extractFirst(source);
				return AnnouncementType.of(source);
			}
		});
		f.addConverter(new Converter<String, UUID>() {
			@Override
			public UUID convert(String source) {
				source = JSON.extractFirst(source);
				return UUID.fromString(source);
			}
		});
		return f;
	}
}