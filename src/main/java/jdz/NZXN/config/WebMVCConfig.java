
package jdz.NZXN.config;

import java.util.UUID;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import jdz.NZXN.entity.announcement.AnnouncementType;

@Configuration
public class WebMVCConfig extends WebMvcConfigurationSupport {	
	@Override
	public FormattingConversionService mvcConversionService() {
		FormattingConversionService f = super.mvcConversionService();
		f.addConverter(new Converter<String, AnnouncementType>() {
			@Override
			public AnnouncementType convert(String source) {
				return AnnouncementType.of(source);
			}
		});
		f.addConverter(new Converter<String, UUID>() {
			@Override
			public UUID convert(String source) {
				return UUID.fromString(source);
			}
		});
		return f;
	}

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }
}