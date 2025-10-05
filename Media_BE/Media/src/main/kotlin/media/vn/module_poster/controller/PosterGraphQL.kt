package media.vn.module_poster.controller

import graphql.kickstart.tools.GraphQLMutationResolver
import media.vn.module_poster.domain.dto.poster.PosterCreateInput
import media.vn.module_poster.domain.dto.poster.PosterDTO
import media.vn.module_poster.domain.dto.poster.PosterPage
import media.vn.module_poster.service.PosterService
import org.springframework.data.domain.PageRequest
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
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

    @QueryMapping
    fun postersForHR(
        @Argument page: Int,
        @Argument size: Int,
        @Argument search: String?
    ): PosterPage {
        val pageable = PageRequest.of(page, size)
        val result = posterService.getPagePosterForHr(search, pageable)
        return PosterPage(
            content = result.content,
            totalPages = result.totalPages,
            totalElements = result.totalElements,
            pageSize = result.size,
            pageNumber = result.number
        )
    }

    @QueryMapping
    fun postersForUser(
        @Argument search: String?
    ): List<PosterDTO>{
        val result = posterService.getPagePosterForUser(search)
        return result
    }


}