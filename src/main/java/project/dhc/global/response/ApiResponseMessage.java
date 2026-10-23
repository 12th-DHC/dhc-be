package project.dhc.global.response;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) // Controller의 응답 메시지를 지정하는 어노테이션
@Retention(RetentionPolicy.RUNTIME) // 실행 중에도 남아있게 하는 어노테이션
public @interface ApiResponseMessage {

    String value();
}