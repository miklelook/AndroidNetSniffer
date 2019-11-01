package com.zanfou.polaris.compile;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.zanfou.annotation.Repository;
import com.zanfou.polaris.compile.base.BaseProcessor;
import com.zanfou.polaris.compile.utils.ServicesUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;


/**
 * @author shuxin.wei email:weishuxin@maoyan.com
 * @version v1.0.0
 * @date 2019-08-20
 * <p>
 * Domain 仓库接口注解解析器，自己维护缓存，不依赖Domain.class，暂时不用了，使用{@link DomainRepositoryProcessor}
 */
//@AutoService(Processor.class)
@Deprecated
public class RepositoryProcessor extends BaseProcessor {
    private TypeSpec.Builder typeSpecBuilder;
    private MethodSpec.Builder methodBuilder;
    private String hashMapFieldName = "domainRepositories";

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();


        ClassName hashMap = ClassName.get(HashMap.class);
        //构造返回值 Map<Class,Object>
        TypeName hashMapType = ParameterizedTypeName.get(ClassName.get(Map.class), ClassName.get(Class.class),
                ClassName.get(Object.class));

        FieldSpec.Builder hashMapFiled = FieldSpec.builder(hashMapType, hashMapFieldName, Modifier.FINAL,
                Modifier.PRIVATE
                , Modifier.STATIC);
        CodeBlock.Builder hashMapCode = CodeBlock.builder();
        hashMapCode.add("new $T<>()", hashMap);
        hashMapFiled.initializer(hashMapCode.build());
        //创建类
        typeSpecBuilder = TypeSpec.classBuilder("DomainRepository2")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);
        typeSpecBuilder.addField(hashMapFiled.build());
        //创建方法，设置返回值
        methodBuilder = MethodSpec.methodBuilder("inject")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
                .returns(hashMapType);

    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(Repository.class.getCanonicalName());
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (set.size() > 0) {
            addCode(roundEnvironment);
            createFile();
            return true;
        }
        return false;
    }

    private void addCode(RoundEnvironment roundEnvironment) {
        try {
            //获取注解的类的集合
            Set<? extends Element> domainSet = roundEnvironment.getElementsAnnotatedWith(Repository.class);
            for (Element element : domainSet) {
                //被注解类的匹配判断，是否符合要求，暂时不做校验，只处理类上的注解就可以了
                if (element.getKind() != ElementKind.CLASS) {
                    continue;
                }
                // 获取包装类类型，在本例中，即使用注解的类
                TypeElement typeElement = ServicesUtils.findEnclosingTypeElement(element);
                //获取注解的值，遍历添加到map中
                log("Compile class that use the Repository annotation：" + typeElement.getQualifiedName());
                AnnotationValue annotationValue = ServicesUtils.getAnnotationValue(Repository.class.getName(),
                        typeElement);
                if (annotationValue == null) {
                    continue;
                }
                List value = (List) annotationValue.getValue();
                for (Object repositoryInterface : value) {
                    log("Get the annotation's value：" + repositoryInterface);
                    methodBuilder.addStatement(hashMapFieldName + ".put(" + repositoryInterface.toString() +
                                    "," +
                                    "new $T())",
                            ClassName.get(elementUtils.getPackageOf(typeElement).getQualifiedName().toString(),
                                    typeElement.getSimpleName().toString()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createFile() {
        try {
            //添加返回值
            methodBuilder.addStatement("return " + hashMapFieldName);
            JavaFile javaFile = JavaFile.builder("com.zanfou.domain.repository",
                    typeSpecBuilder.addMethod(methodBuilder.build()).build()).build();
            javaFile.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

