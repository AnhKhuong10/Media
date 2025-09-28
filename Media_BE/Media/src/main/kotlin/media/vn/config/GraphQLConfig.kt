package media.vn.config

import graphql.scalars.ExtendedScalars
import org.springframework.context.annotation.Bean
import org.springframework.graphql.execution.RuntimeWiringConfigurer
import org.springframework.stereotype.Component

@Component
class GraphQLConfig {
    @Bean
    fun runtimeWiringConfigurer(): RuntimeWiringConfigurer =
        RuntimeWiringConfigurer { builder ->
            builder.scalar(ExtendedScalars.Date) // map LocalDate
                .scalar(ExtendedScalars.DateTime)  // map LocalDateTime
                .scalar(ExtendedScalars.Time) // map LocalTime

        }
}