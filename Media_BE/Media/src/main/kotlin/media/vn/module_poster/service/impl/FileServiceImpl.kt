package media.vn.module_poster.service.impl

import media.vn.module_poster.service.FileService
import org.springframework.core.io.InputStreamResource
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class FileServiceImpl : FileService {

    override fun store(multipartFile: MultipartFile, folder: String): String {
        TODO("Not yet implemented")
    }

    override fun getFileLength(fileName: String, folder: String): Long {
        TODO("Not yet implemented")
    }

    override fun getResource(fileName: String, folder: String): InputStreamResource {
        TODO("Not yet implemented")
    }
}