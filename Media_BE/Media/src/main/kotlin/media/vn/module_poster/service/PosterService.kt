package media.vn.module_poster.service

import media.vn.module_poster.domain.dto.poster.PosterCreateInput
import media.vn.module_poster.domain.dto.poster.PosterDTO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface PosterService {

    fun createPoster(input: PosterCreateInput): PosterDTO
    fun getPagePosterForHr(search: String?, page: Pageable): Page<PosterDTO>

    fun getPagePosterForUser(search: String?): List<PosterDTO>
}