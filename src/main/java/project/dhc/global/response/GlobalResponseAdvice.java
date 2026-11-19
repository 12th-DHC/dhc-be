package project.dhc.global.response;


import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

// 모든 Controller의 응답공통처리
@NullMarked // 적용된 범위 내의 모든 타입, 메서드 매개변수, 반환값 등을 기본적으로 Null-safe로 지정하는 애노테이션
@RestControllerAdvice
public class GlobalResponseAdvice implements ResponseBodyAdvice<Object> {

    // 공통 응답 처리를 적용할 Controller를 결정
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return returnType.hasMethodAnnotation(ApiResponseMessage.class); // @ApiResponseMessage가 붙은 메서드만 처리
    }

    // Controller가 반환한 응답을 실제로 가공
    @Override
    public @Nullable Object beforeBodyWrite(
            @Nullable Object body, // Controller가 반환한 실제 데이터
            MethodParameter returnType, // 실행된 Controller 메서드 정보
            MediaType selectedContentType, // 응답 데이터의 타입
            Class<? extends HttpMessageConverter<?>> selectedConverterType, // 응답 변환기
            ServerHttpRequest request, // 현재 요청 정보
            ServerHttpResponse response // 현재 응답 정보
    ) {
        ApiResponseMessage annotation = returnType.getMethodAnnotation(ApiResponseMessage.class); // Controller에 붙어 있는 @ApiResponseMessage 가져오기

        if (annotation == null) {
            return body;
        }

        // 데이터를 ApiResponse로 감싸서 반환
        return new ApiResponse<>(
                body,
                annotation.value()
        );
    }
}
