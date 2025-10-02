package media.vn.module_poster.controller

import graphql.kickstart.tools.GraphQLMutationResolver
import media.vn.module_poster.domain.dto.poster.PosterCreateInput
import media.vn.module_poster.domain.dto.poster.PosterDTO
import media.vn.module_poster.service.PosterService
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.stereotype.Controller
import org.springframework.web.multipart.MultipartFile

@Controller
class PosterMutation(
    private val posterService: PosterService
): GraphQLMutationResolver {

    fun createPoster(@Argument input: PosterCreateInput): PosterDTO {
        val file: MultipartFile? = input.file

        println("Upload file: ${file?.originalFilename}")

        file?.let {
            val path = "uploads/${it.originalFilename}"
            it.transferTo(java.io.File(path))
        }

        return posterService.createPoster(input)
    }
}