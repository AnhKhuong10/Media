package media.vn.module_poster.service

import org.springframework.core.io.InputStreamResource
import org.springframework.web.multipart.MultipartFile

interface FileService {

    fun store(multipartFile: MultipartFile, folder: String): String

    fun getFileLength(fileName: String, folder: String): Long

    fun getResource(fileName: String, folder: String): InputStreamResource
}