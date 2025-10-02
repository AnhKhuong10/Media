package media.vn.module_poster.domain.dto.user

import media.vn.utils.constants.GenderEnum
import java.time.LocalDate

data class UserDTO(
    val userId: Long,
    val username: String,
    val fullName: String,
    val gender: GenderEnum,
    val email: String,
    val phone: String,
    val dob: LocalDate?,
    val roleName: String,
    val homeTown: String?,
    val createDate: LocalDate?,
     val avatar: String?,
)
