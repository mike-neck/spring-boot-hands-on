/*
 * Copyright 2015 Shinya Mochida
 * 
 * Licensed under the Apache License,Version2.0(the"License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,software
 * Distributed under the License is distributed on an"AS IS"BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jsug.infra.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
@Aspect
@Slf4j
public class HandlerExceptionResolverLoggingAspect {

    @Around("execution(* org.springframework.web.servlet.HandlerExceptionResolver.resolveException(..))")
    public Object logException(ProceedingJoinPoint joinPoint) throws Throwable {
        Object ret = joinPoint.proceed();
        Object[] args = joinPoint.getArgs();
        HttpServletRequest request = (HttpServletRequest) args[0];

        if(request.getAttribute("ERROR_LOGGED") == null) {
            Object handler = args[2];
            Exception exception = (Exception) args[3];
            log.info("Error occurred (method:[" + request.getMethod() +
                    "], url:[" + request.getRequestURI() +
                    "], handler:[" +handler +
                    "], exception[" + exception + "])");
            request.setAttribute("ERROR_LOGGED", true);
        }
        return ret;
    }
}
