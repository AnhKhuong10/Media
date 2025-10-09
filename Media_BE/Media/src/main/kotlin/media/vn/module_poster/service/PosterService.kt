package media.vn.module_poster.service

import media.vn.module_poster.domain.dto.poster.PosterCreateInput
import media.vn.module_poster.domain.dto.poster.PosterDTO
import media.vn.module_poster.domain.dto.poster.PosterUpdateInput
import media.vn.module_poster.domain.entity.Poster
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity

interface PosterService {

    fun createPoster(input: PosterCreateInput): PosterDTO

    fun updatePoster(input: PosterUpdateInput): PosterDTO

    fun getPagePosterForHr(search: String?, page: Pageable): Page<PosterDTO>

    fun getPagePosterForUser(search: String?): List<PosterDTO>

    fun getListPosterDelete(): List<PosterDTO>
}