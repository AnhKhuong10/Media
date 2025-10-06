package media.vn.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@Configuration
class StaticResourcesWebConfiguration : WebMvcConfigurer {

    @Value("\${application.upload-file.base-uri}")
    private val baseURI: String? = null

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/Uploads/**")
            .addResourceLocations(baseURI)
    }
}