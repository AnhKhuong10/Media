package media.vn

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(
	exclude = [
		org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration::class,
		org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration::class
	]
)
class MediaApplication

fun main(args: Array<String>) {
	runApplication<MediaApplication>(*args)
}
