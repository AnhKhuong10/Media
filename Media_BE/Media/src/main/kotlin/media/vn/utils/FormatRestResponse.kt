package media.vn.utils

import media.vn.utils.annotation.ApiMessage
import media.vn.utils.annotation.RestResponse

import org.springframework.core.MethodParameter
import org.springframework.core.io.Resource
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.http.server.ServletServerHttpResponse
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice
import jakarta.servlet.http.HttpServletResponse

@ControllerAdvice
class FormatRestResponse : ResponseBodyAdvice<Any> {

    override fun supports(
        returnType: MethodParameter,
        converterType: Class<out HttpMessageConverter<*>>
    ): Boolean {
        return true
    }

    override fun beforeBodyWrite(
        body: Any?,
        returnType: MethodParameter,
        selectedContentType: MediaType,
        selectedConverterType: Class<out HttpMessageConverter<*>>,
        request: ServerHttpRequest,
        response: ServerHttpResponse
    ): Any? {
        val servletResponse: HttpServletResponse =
            (response as ServletServerHttpResponse).servletResponse
        val status = servletResponse.status

        val res = RestResponse<Any>()
        res.statusCode = status

        // Trả thẳng nếu là String hoặc Resource
        if (body is String || body is Resource) {
            return body
        }

        return if (status >= 400) {
            // case error: trả body trực tiếp
            body
        } else {
            res.data = body
            val message = returnType.getMethodAnnotation(ApiMessage::class.java)
            res.message = message?.value ?: "CALL API SUCCESS"
            println("Body in beforeBodyWrite: $body")
            res
        }
    }
}