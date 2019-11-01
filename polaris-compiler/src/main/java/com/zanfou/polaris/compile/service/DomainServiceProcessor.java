package com.zanfou.polaris.compile.service;

import com.google.auto.service.AutoService;
import com.zanfou.polaris.annotation.service.LoaderService;
import com.zanfou.polaris.compile.base.BaseProcessor;
import com.zanfou.polaris.compile.utils.ServicesUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

/**
 * @author shuxin.wei email:weishuxin@maoyan.com
 * @version v1.0.0
 * @date 2019-08-20
 * <p>
 * Domain ServiceLoader 接口文件自动生成解析器
 */
@AutoService(Processor.class)
public class DomainServiceProcessor extends BaseProcessor {
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(LoaderService.class.getCanonicalName());
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        try {
            //获取注解的类的集合
            Set<? extends Element> domainSet = roundEnvironment.getElementsAnnotatedWith(LoaderService.class);
            for (Element element : domainSet) {
                //被注解类的匹配判断，是否符合要求，暂时不做校验，只处理类上的注解就可以了
                if (element.getKind() == ElementKind.CLASS) {
                    // 获取包装类类型，即使用注解的类
                    TypeElement typeElement = ServicesUtils.findEnclosingTypeElement(element);
                    //获取注解的值，遍历添加到map中
                    log("Compile class that use the LoaderService annotation ：" + typeElement.getQualifiedName());
                    Filer filer = this.processingEnv.getFiler();
                    List<? extends TypeMirror> interfaces = typeElement.getInterfaces();
                    for (TypeMirror anInterface : interfaces) {
                        String resourceFile = ServicesUtils.getPath(anInterface.toString());
                        this.log("Working on resource file: " + resourceFile);

                        try {
                            FileObject existingFile = filer.getResource(StandardLocation.CLASS_OUTPUT, "",
                                    resourceFile);
                            Set<String> oldServices = null;
                            try {
                                this.log("Looking for existing resource file at " + existingFile.toUri());
                                oldServices = ServicesUtils.readServiceFile(existingFile.openInputStream());
                                this.log("Existing service entries: " + oldServices);

                            } catch (IOException e) {
                                this.log("Resource file did not already exist.");
                            }
                            if (oldServices == null) {
                                oldServices = new HashSet<>();
                            }
                            oldServices.add(typeElement.getQualifiedName().toString());
                            FileObject fileObject = filer.createResource(StandardLocation.CLASS_OUTPUT, "",
                                    resourceFile);
                            OutputStream outputStream = fileObject.openOutputStream();
                            ServicesUtils.writeServiceFile(oldServices, outputStream);
                            outputStream.flush();
                            outputStream.close();
                            log("Wrote to: " + fileObject.toUri());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
