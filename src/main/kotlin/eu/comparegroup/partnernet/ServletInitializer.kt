package kotlin.eu.comparegroup.partnernet

import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer

class ServletInitializer : SpringBootServletInitializer() {

	override fun configure(application: SpringApplicationBuilder): SpringApplicationBuilder {
		return application.sources(PartnernetSpringBootVuejsApplication::class.java)
	}

}
