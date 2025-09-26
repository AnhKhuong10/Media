package media.vn.module_poster.domain.dto.auth

data class ResLoginDTO(
    val accessToken: String,
    val user: UserLogin
){
    data class UserLogin(
        val userId: Long,
        val username: String,
        val fullName: String?,
        val email: String?,
        val role: String
    )
}
