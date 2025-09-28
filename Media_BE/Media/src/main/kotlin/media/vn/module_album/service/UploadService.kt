package media.vn.module_album.service

import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.Paths
import java.util.Base64
import java.util.UUID


@Service
class UploadService {

        private val URL = "http://localhost:8080/"
    private val uploadDir = "src/main/resources/public/uploads" // thư mục chứa ảnh

    fun saveBase64Image(base64: String, fileName: String): String {
        // Nếu thư mục chưa tồn tại thì tạo
        val dir = File(uploadDir)
        if (!dir.exists()) {
            dir.mkdirs()
        }

        // Tách prefix kiểu "data:image/png;base64,..." nếu có
        val cleanBase64 = base64.substringAfter("base64,", base64)

        // Giải mã base64
        val imageBytes = Base64.getDecoder().decode(cleanBase64)

        // Tạo file để lưu
        val file = File(dir, fileName)

        FileOutputStream(file).use { fos ->
            fos.write(imageBytes)
        }

        // Trả về đường dẫn HTTP local (vd http://localhost:8080/uploads/filename.png)
        return "http://localhost:8080/$uploadDir/${file.name}"
    }



    fun saveImage(file: MultipartFile): String {
        val dir = File(uploadDir)
        if (!dir.exists()) {
            dir.mkdirs()
        }

        val extension = file.originalFilename?.substringAfterLast(".", "") ?: "jpg"
        val uniqueName = UUID.randomUUID().toString() + "." + extension
        val path = Paths.get(dir.absolutePath, uniqueName)

        Files.write(path, file.bytes)

        // Trả về URL local, Spring Boot map thư mục uploads/ qua static resource
        return URL+uniqueName
    }
}
