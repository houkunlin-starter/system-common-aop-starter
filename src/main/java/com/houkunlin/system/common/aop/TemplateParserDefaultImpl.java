package com.houkunlin.system.common.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.StandardReflectionParameterNameDiscoverer;
import org.springframework.expression.*;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.lang.NonNull;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 表达式（模板）解析器默认实现
 *
 * @author HouKunLin
 */
@Slf4j
@RequiredArgsConstructor
public class TemplateParserDefaultImpl implements TemplateParser<EvaluationContext>, BeanFactoryAware, InitializingBean {
    private final ExpressionParser parser = new SpelExpressionParser();
    private final ParameterNameDiscoverer discoverer = new StandardReflectionParameterNameDiscoverer();
    private final ParserContext parserContext;
    private BeanResolver beanResolver = null;
    /**
     * 模板字符串需要的最小长度。
     * 模板字符串最少需要一个前后缀，再加一个变量信息长度，变量信息至少两个字符（#a），不存在只有一个字符的顶级变量
     * 例如：最小长度为5，是因为一个 SpEL 表达式最少需要 #{#a} 个字符
     */
    private int spelStrMinLen = 5;

    @Override
    public void setBeanFactory(@NonNull BeanFactory beanFactory) throws BeansException {
        this.beanResolver = new BeanFactoryResolver(beanFactory);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        assert beanResolver != null;
        // 模板字符串最少需要一个前后缀，再加一个变量信息长度，变量信息至少两个字符（#a），不存在只有一个字符的顶级变量
        // 例如：最小长度为5，是因为一个 SpEL 表达式最少需要 #{#a} 个字符
        this.spelStrMinLen = (parserContext.getExpressionPrefix() + parserContext.getExpressionSuffix()).length() + 2;
    }

    @Override
    public EvaluationContext createContext(ProceedingJoinPoint pjp, Object result, Exception exception) {
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        RootObject rootObject = new RootObject(method, pjp.getArgs(), pjp.getTarget(), pjp.getTarget().getClass(), result, exception);
        final StandardEvaluationContext context = new StandardEvaluationContext(rootObject);
        context.setBeanResolver(beanResolver);

        Object[] args = pjp.getArgs();
        // 参照 org.springframework.context.expression.MethodBasedEvaluationContext.lazyLoadArguments
        if (!ObjectUtils.isEmpty(args)) {
            String[] paramNames = discoverer.getParameterNames(method);
            int paramCount = (paramNames != null ? paramNames.length : method.getParameterCount());
            int argsCount = args.length;

            for (int i = 0; i < paramCount; i++) {
                Object value = null;
                if (argsCount > paramCount && i == paramCount - 1) {
                    // 将剩余参数公开为最后一个参数的vararg数组
                    value = Arrays.copyOfRange(args, i, argsCount);
                } else if (argsCount > i) {
                    // 找到实际参数-否则保留为null
                    value = args[i];
                }
                context.setVariable("a" + i, value);
                context.setVariable("p" + i, value);
                if (paramNames != null && paramNames[i] != null) {
                    context.setVariable(paramNames[i], value);
                }
            }
        }
        try {
            context.registerFunction("hasText", StringUtils.class.getMethod("hasText", CharSequence.class));
            context.registerFunction("trimWhitespace", StringUtils.class.getMethod("trimWhitespace", String.class));
        } catch (NoSuchMethodException ignored) {
        }
        return context;
    }

    @Override
    public boolean isTemplate(String template) {
        return !template.isBlank() && template.length() >= spelStrMinLen && template.contains(parserContext.getExpressionPrefix());
    }

    @Override
    public String parseTemplate(String template, EvaluationContext evaluationContext) {
        try {
            return parser.parseExpression(template, parserContext).getValue(evaluationContext, String.class);
        } catch (EvaluationException | ParseException e) {
            if (log.isErrorEnabled()) {
                log.error("SpEL 解析错误：{}", template, e);
            }
            return template;
        }
    }
}
