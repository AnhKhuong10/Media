package media.vn.module_poster.repository

import media.vn.module_poster.domain.entity.Poster
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PosterRepository : JpaRepository<Poster, Long> {
}