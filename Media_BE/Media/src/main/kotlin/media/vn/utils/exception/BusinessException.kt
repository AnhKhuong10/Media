package media.vn.utils.exception

class BusinessException(
    val errorCode: ErrorCode,
    override val message: String,
    val subCode: String? = null
) : RuntimeException(message)