package media.vn.utils.annotation

data class RestResponse<T>(
    var statusCode: Int = 200,
    var error: String? = null,
    var message: Any? = null,
    var data: T? = null
)