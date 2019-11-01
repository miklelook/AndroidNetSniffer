package com.zanfou.polaris.compile.router;

import com.google.auto.service.AutoService;
import com.zanfou.polaris.annotation.router.Route;
import com.zanfou.polaris.annotation.router.RouterParams;
import com.zanfou.polaris.compile.base.BaseProcessor;

import java.util.Collections;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;


/**
 * @author shuxin.wei email:weishuxin@maoyan.com
 * @version v1.0.0
 * @date 2019-09-09
 * <p>
 * 路由{@link Route}注解-解析器
 */
@AutoService(Processor.class)
public class RouterProcessor extends BaseProcessor {
    /**
     * 解析代理
     */
    protected RouterProcessorDelegate processorDelegate;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        processorDelegate = new RouterProcessorDelegate(processingEnv);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(Route.class.getCanonicalName());
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (set.size() > 0) {
            Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(Route.class);
            for (Element element : elements) {
                Route route = element.getAnnotation(Route.class);
                RouterParams routeParams = new RouterParams(route);
                processorDelegate.dispatchElement(roundEnvironment, element, routeParams);
            }
            processorDelegate.processingOver();
            return true;
        }
        return false;
    }
}

