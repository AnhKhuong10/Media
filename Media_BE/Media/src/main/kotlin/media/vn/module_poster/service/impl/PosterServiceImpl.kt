package media.vn.module_poster.service.impl

import media.vn.module_poster.domain.dto.poster.PosterCreateInput
import media.vn.module_poster.domain.dto.poster.PosterDTO
import media.vn.module_poster.domain.entity.Poster
import media.vn.module_poster.repository.PosterRepository
import media.vn.module_poster.repository.UserRepository
import media.vn.module_poster.service.PosterService
import media.vn.utils.exception.BusinessException
import media.vn.utils.exception.ErrorCode
import org.springframework.stereotype.Service
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@Service
class PosterServiceImpl (
    private val posterRepository: PosterRepository,
    private val userRepository: UserRepository,
) : PosterService {

    val uploadDir = Paths.get("src/main/resources/static/images")

    override fun createPoster(input: PosterCreateInput): PosterDTO {
        val user = userRepository.findById(input.userId).orElseThrow {
            BusinessException(ErrorCode.NOT_FOUND,"user not found")
        }

//        val userCreateName = SecurityUtil.getCurrentUserLogin().orElseThrow {
//            BusinessException(ErrorCode.UNAUTHORIZED,"User not logged in")
//        }

        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir)
        }
        // xử lý file upload
        var filePath: String? = null
        input.file?.let { file ->
            val fileName = UUID.randomUUID().toString() + "_" + file.originalFilename
            val target = uploadDir.resolve(fileName)
            Files.copy(file.inputStream, target, StandardCopyOption.REPLACE_EXISTING)
            filePath = "/images/$fileName"
        }

        val poster = Poster(
            title = input.title,
            content = input.content,
            filePath = filePath,
            posterType = input.posterType,
            companyName = input.companyName,
            createDate = LocalDate.now(),
            updateDate = null,
            createdBy = "HR",
            updatedBy = null,
            isDraft = false,
            isDeleted = false,
            user = user,
            posterReactions = null,
        )

        return posterRepository.save(poster).toPosterDTO()
    }

    private fun Poster.toPosterDTO()= PosterDTO (
        posterId = this.posterId,
        title= this.title,
        content = this.content,
        posterType = this.posterType,
        companyName = this.companyName,
        createDate = this.createDate,
        updateDate = this.updateDate,
        createdBy = this.createdBy?:"",
        updatedBy = this.updatedBy?:""
    )
}