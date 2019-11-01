package com.zanfou.polaris.compile.base;

import com.zanfou.polaris.annotation.utils.PolarisLog;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;


/**
 * @author shuxin.wei email:weishuxin@maoyan.com
 * @version v1.0.0
 * @date 2019-09-09
 * <p>
 */
public abstract class BaseProcessor extends AbstractProcessor {
    private static final String TAG = BaseProcessor.class.getSimpleName();
    /**
     * 处理Element的工具类
     */
    protected Elements elementUtils;
    /**
     * 生成文件的工具
     */
    protected Filer filer;
    /**
     * 日志信息的输出
     */
    protected Messager messager;

    /**
     * 类处理工具
     */
    protected Types typeUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
        typeUtils = processingEnv.getTypeUtils();
    }


    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }


    /**
     * 打印日志
     *
     * @param object
     */
    protected void log(Object object) {
        PolarisLog.log(object);
    }
}

