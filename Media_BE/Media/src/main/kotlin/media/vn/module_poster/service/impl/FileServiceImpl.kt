package media.vn.module_poster.service.impl

import media.vn.module_poster.service.FileService
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.InputStreamResource
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.net.URI
import java.net.URISyntaxException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

@Service
class FileServiceImpl : FileService {

    @Value("\${application.upload-file.base-uri}")
    private lateinit var baseURI: String

    //Tạo thư mục nếu chưa tồn tại
    override fun createDirectory(folder: String) {
        val uri = URI(folder)
        val path = Paths.get(uri)
        val dir = File(path.toString())

        if (!dir.isDirectory) {
            try {
                Files.createDirectories(dir.toPath()) // tạo được cả cây thư mục
                println(">>> CREATED NEW DIRECTORY SUCCESSFULLY: ${dir.toPath()}")
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else {
            println(">>> SKIP MAKING DIRECTORY, ALREADY EXISTS")
        }
    }


    /**
     * Lưu file upload vào thư mục chỉ định
     * Trả về tên file sau khi lưu
     */
    @Throws(URISyntaxException::class, IOException::class)
    override fun store(multipartFile: MultipartFile, folder: String): String {
        // Chuẩn hóa tên file
        val originalName = multipartFile.originalFilename ?: "unnamed"
        val sanitizedName = originalName.trim()
            .replace("\\s+".toRegex(), "_") // thay khoảng trắng bằng "_"
            .replace("[^a-zA-Z0-9._-]".toRegex(), "") // loại bỏ ký tự đặc biệt

        // Gắn timestamp để tránh trùng
        val finalFilename = "${System.currentTimeMillis()}-$sanitizedName"

        // Tạo đường dẫn
        val uri = URI("$baseURI$folder/$finalFilename")
        val path = Paths.get(uri)

        // Đảm bảo thư mục tồn tại
        val dirUri = URI("$baseURI$folder")
        createDirectory(dirUri.toString())

        // Copy file vào thư mục
        multipartFile.inputStream.use { input: InputStream ->
            Files.copy(input, path, StandardCopyOption.REPLACE_EXISTING)
        }

        println(">>> FILE SAVED SUCCESSFULLY: $path")

        return finalFilename
    }

    /**
     * Lấy kích thước file (nếu không tồn tại trả về 0)
     */
    @Throws(URISyntaxException::class)
    override fun getFileLength(fileName: String, folder: String): Long {
        val uri = URI("$baseURI$folder/$fileName")
        val path = Paths.get(uri)
        val file = File(path.toString())

        return if (!file.exists() || file.isDirectory) 0 else file.length()
    }

    /**
     * Đọc file và trả về dạng InputStreamResource (dùng cho download)
     */
    @Throws(URISyntaxException::class, FileNotFoundException::class)
    override fun getResource(fileName: String, folder: String): InputStreamResource {
        val uri = URI("$baseURI$folder/$fileName")
        val path = Paths.get(uri)
        val file = File(path.toString())
        return InputStreamResource(FileInputStream(file))
    }
}