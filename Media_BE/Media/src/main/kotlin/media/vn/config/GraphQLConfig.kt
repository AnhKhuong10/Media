package media.vn.config

import graphql.scalars.ExtendedScalars
import graphql.schema.GraphQLScalarType
import org.springframework.context.annotation.Bean
import org.springframework.graphql.execution.RuntimeWiringConfigurer
import graphql.schema.Coercing
import org.springframework.context.annotation.Configuration
import org.springframework.web.multipart.MultipartFile

@Configuration
class GraphQLConfig {
    @Bean
    fun runtimeWiringConfigurer(): RuntimeWiringConfigurer =
        RuntimeWiringConfigurer { builder ->
            builder.scalar(ExtendedScalars.Date) // map LocalDate
                .scalar(ExtendedScalars.DateTime)  // map LocalDateTime
                .scalar(ExtendedScalars.Time) // map LocalTime
                .scalar(UploadScalar.INSTANCE)
        }

    @Bean
    fun uploadScalar(): GraphQLScalarType {
        return UploadScalar.INSTANCE
    }
}

object UploadScalar {
    val INSTANCE: GraphQLScalarType = GraphQLScalarType.newScalar()
        .name("Upload")
        .description("Custom scalar for file upload")
        .coercing(object : Coercing<MultipartFile, Any> {
            override fun parseValue(input: Any): MultipartFile {
                return input as MultipartFile
            }

            override fun serialize(dataFetcherResult: Any): Any {
                return dataFetcherResult
            }

            override fun parseLiteral(input: Any): MultipartFile {
                throw UnsupportedOperationException("Upload literal not supported")
            }
        })
        .build()
}
