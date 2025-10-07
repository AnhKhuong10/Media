package media.vn.module_poster.repository

import media.vn.module_poster.domain.entity.Poster
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface PosterRepository : JpaRepository<Poster, Long> {

    @Query("""
    SELECT * FROM Poster p
    WHERE (:search IS NULL 
       OR LOWER(CAST(p.title AS TEXT)) LIKE LOWER(CONCAT('%', :search, '%'))
       OR LOWER(CAST(p.company_name AS TEXT)) LIKE LOWER(CONCAT('%', :search, '%'))
       OR LOWER(CAST(p.content AS TEXT)) LIKE LOWER(CONCAT('%', :search, '%'))
       OR LOWER(CAST(p.created_by AS TEXT)) LIKE LOWER(CONCAT('%', :search, '%')))
    ORDER BY p.poster_id
""", nativeQuery = true)
    fun getListPoster(
        @Param("search") search: String?,
        pageable: Pageable
    ): Page<Poster>
}