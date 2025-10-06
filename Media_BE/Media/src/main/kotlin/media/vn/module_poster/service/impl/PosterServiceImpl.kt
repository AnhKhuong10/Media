package media.vn.module_poster.service.impl

import media.vn.module_poster.domain.dto.poster.PosterCreateInput
import media.vn.module_poster.domain.dto.poster.PosterDTO
import media.vn.module_poster.domain.dto.poster.PosterUpdateInput
import media.vn.module_poster.domain.entity.Poster
import media.vn.module_poster.domain.entity.User
import media.vn.module_poster.repository.PosterRepository
import media.vn.module_poster.repository.UserRepository
import media.vn.module_poster.service.FileService
import media.vn.module_poster.service.PosterService
import media.vn.utils.exception.BusinessException
import media.vn.utils.exception.ErrorCode
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.net.URISyntaxException
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
    private val fileService: FileService,
) : PosterService {

    val uploadDir = Paths.get("src/main/resources/static/images")

    override fun createPoster(input: PosterCreateInput): PosterDTO {
        val user = userRepository.findById(input.userId).orElseThrow {
            BusinessException(ErrorCode.NOT_FOUND,"user not found")
        }

        val owner = userRepository.findById(input.ownerId).orElseThrow {
            BusinessException(ErrorCode.NOT_FOUND,"user not found")
        }

//        val userCreateName = SecurityUtil.getCurrentUserLogin().orElseThrow {
//            BusinessException(ErrorCode.UNAUTHORIZED,"User not logged in")
//        }

//        if (!Files.exists(uploadDir)) {
//            Files.createDirectories(uploadDir)
//        }
//        // xử lý file upload
//        var filePath: String? = null
//        input.file.let { file ->
//            val fileName = UUID.randomUUID().toString() + "_" + file.originalFilename
//            val target = uploadDir.resolve(fileName)
//            Files.copy(file.inputStream, target, StandardCopyOption.REPLACE_EXISTING)
//            println(" File saved at: $target")
//            filePath = "/images/$fileName"
//        }

        // --- Upload file ảnh ---
        var filePath: String? = null
        input.file?.let { file ->
            filePath = handleFileUpload(file, "poster")
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
            isDraft = input.isDraft,
            isDeleted = false,
            user = user,
            owner = owner,
            posterReactions = null
        )

        return posterRepository.save(poster).toPosterDTO()
    }

    override fun updatePoster(input: PosterUpdateInput): PosterDTO {
        val poster = posterRepository.findById(input.posterId)
            .orElseThrow { BusinessException(ErrorCode.NOT_FOUND,"poster not found") }

        val user = userRepository.findById(input.userId).orElseThrow {
            BusinessException(ErrorCode.NOT_FOUND,"user not found")
        }

        val owner = userRepository.findById(input.ownerId).orElseThrow {
            BusinessException(ErrorCode.NOT_FOUND,"user not found")
        }

        // Upload file mới
        var filePath: String? = poster.filePath
        input.file?.let { file ->
            filePath = handleFileUpload(file, "poster")
        }
        poster.title = input.title
        poster.content = input.content
        poster.filePath = filePath
        poster.posterType = input.posterType
        poster.companyName = input.companyName
        poster.updateDate = LocalDate.now()
        poster.updatedBy = owner.username
        poster.isDraft = input.isDraft
        poster.user = user
        poster.owner = owner

        return posterRepository.save(poster).toPosterDTO()
    }

    override fun getPagePosterForHr(search: String?, page: Pageable): Page<PosterDTO> {
        val posters = posterRepository.getListPoster(search, page)

        return posters.map { it.toPosterDTO() }
    }

    override fun getPagePosterForUser(search: String?): List<PosterDTO> {
        val posters = posterRepository.getListPoster(search, Pageable.unpaged()).content
        return posters.map { it.toPosterDTO() }
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
        updatedBy = this.updatedBy?:"",
        user = this.user.toUserPoster()
    )

    private fun User.toUserPoster(): PosterDTO.UserPoster {
        return PosterDTO.UserPoster(
            userId = this.userId,
            fullName = this.fullName,
            dob = this.dob,
            homeTown = this.homeTown,
            avatar = this.avatar,
            roleName = this.role.roleName
        )
    }

    /**
     *  Helper — upload file an toàn qua FileService
     */
    private fun handleFileUpload(file: MultipartFile, folder: String): String? {
        return try {
            val fileName = fileService.store(file, folder)
            "/$folder/$fileName"
        } catch (e: IOException) {
            throw BusinessException(ErrorCode.BAD_REQUEST, "Error saving file: ${e.message}")
        } catch (e: URISyntaxException) {
            throw BusinessException(ErrorCode.BAD_REQUEST, "Invalid file path: ${e.message}")
        }
    }

}