package media.vn.module_poster.service

import media.vn.module_poster.domain.dto.poster.PosterCreateInput
import media.vn.module_poster.domain.dto.poster.PosterDTO

interface PosterService {

    fun createPoster(input: PosterCreateInput): PosterDTO
}