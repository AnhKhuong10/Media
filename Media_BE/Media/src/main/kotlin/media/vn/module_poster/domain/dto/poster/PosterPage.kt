package media.vn.module_poster.domain.dto.poster

data class PosterPage(
    val content: List<PosterDTO>,
    val totalPages: Int,
    val totalElements: Long,
    val pageNumber: Int,
    val pageSize: Int,
)


