package com.zanfou.polaris.compile.filedinject;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.zanfou.polaris.annotation.field.FieldInject;
import com.zanfou.polaris.annotation.router.Route;
import com.zanfou.polaris.annotation.utils.Constants;
import com.zanfou.polaris.compile.base.BaseProcessor;
import com.zanfou.polaris.compile.utils.ServicesUtils;
import com.zanfou.polaris.compile.utils.TypeUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;


/**
 * @author shuxin.wei email:weishuxin@maoyan.com
 * @version v1.0.0
 * @date 2019-09-09
 * <p>
 * 路由{@link Route}注解-解析器
 */
@AutoService(Processor.class)
public class FieldInjectProcessor extends BaseProcessor {
    private Map<String, InjectClassInfo> injectedClassInfos;
    private TypeUtils typeConverter;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        injectedClassInfos = new HashMap<>();
        typeConverter = new TypeUtils(typeUtils, elementUtils);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(FieldInject.class.getCanonicalName());
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (set.size() > 0) {
            generate(roundEnvironment.getElementsAnnotatedWith(FieldInject.class));
            return true;
        }
        return false;
    }

    private void generate(Set<? extends Element> elements) {
        for (Element element : elements) {
            FieldInject route = element.getAnnotation(FieldInject.class);
            if (!element.getKind().isField()) {
                continue;
            }
            TypeElement typeElement = ServicesUtils.findEnclosingTypeElement(element);
            if (!element.getModifiers().contains(Modifier.PUBLIC)) {
                throw new RuntimeException(typeElement.getQualifiedName() + "#" + element.getSimpleName()
                        + " modifier must be Public");
            }
            String injectedClassName = typeElement.getSimpleName().toString();
            String generateClassName = String.format(Constants.FILE_NAME_INJECT_FORMAT, injectedClassName);
            log(injectedClassName + ":" + generateClassName + ":" + element.getSimpleName());

            InjectClassInfo injectClassInfo = injectedClassInfos.get(injectedClassName);
            if (injectClassInfo == null) {
                injectClassInfo = new InjectClassInfo(typeElement.getQualifiedName().toString(), injectedClassName,
                        generateClassName);
                injectedClassInfos.put(injectedClassName, injectClassInfo);
            }
            MethodSpec.Builder methodBuilder = injectClassInfo.methodBuilder;

            TypeMirror typeMirror = element.asType();
            //添加注释
            String typeName = typeMirror.toString();
            methodBuilder.addStatement("// fieldName:"
                    + element.getSimpleName()
                    + "  fieldType:"
                    + typeName
                    + "  kindName:"
                    + typeMirror.getKind().name()
                    + "  typeKind:"
                    + typeMirror.getKind().ordinal()
                    + "  defaultValue:"
                    + route.defaultValue()
            );
            //构造 object.productId = (boolean)bundle.getBoolean("productId",false); 代码
            //强转的最简单，但是不能设置默认值
//            String format = injectClassInfo.object + ".%s = %s" + injectClassInfo.bundle + ".%s(\"%s\")";
//            boolean defaultValueOfBundle = typeConverter.isDefaultValueOfBundle(typeMirror);
//            methodBuilder.addStatement(String.format(
//                    format,
//                    element.getSimpleName(),
//                    defaultValueOfBundle ? "" : "(" + typeMirror.toString() + ")",
//                    defaultValueOfBundle ? "get" + typeConverter.getTypeName(typeMirror) : "get",
//                    element.getSimpleName())
//            );
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("\"");
            stringBuilder.append(element.getSimpleName());
            stringBuilder.append("\"");
            if (route.value().length > 0) {
                for (String s : route.value()) {
                    if (s.length() == 0) {
                        continue;
                    }
                    if (stringBuilder.length() > 0) {
                        stringBuilder.append(",");
                    }
                    stringBuilder.append("\"");
                    stringBuilder.append(s);
                    stringBuilder.append("\"");
                }
            }
            int index = typeName.indexOf("<");
            String className = index > 0 ? typeName.substring(0, index) : typeName;
            // Object anIntValue = getData(new String[]{"anInt"}, bundle, jsonObject,int.class);
            String valueName = element.getSimpleName() + "Value";
            String getValue = "Object " + valueName + " = getData(new String[]{" + stringBuilder.toString() + "}, bundle, jsonObject, " + className + ".class)";
            methodBuilder.addStatement(getValue);

            // if(anIntValue != null){
            //   object.anInt = (int) anIntValue;
            // }
            CodeBlock.Builder builder = CodeBlock.builder().beginControlFlow("if(" + valueName + " != null)");
            builder.addStatement(injectClassInfo.object + "." + element.getSimpleName() + " = (" + typeName + ")" + valueName);

            // else {
            //        object.anInt2 = null;
            // }
            if (!route.defaultValue()) {
                builder.nextControlFlow("else");
                if (typeMirror.getKind().isPrimitive()) {
                    if (typeMirror.getKind() == TypeKind.BOOLEAN) {
                        builder.addStatement(injectClassInfo.object + "." + element.getSimpleName() + " = false");
                    } else {
                        builder.addStatement(injectClassInfo.object + "." + element.getSimpleName() + " = (" + typeName + ")0");
                    }
                } else {
                    builder.addStatement(injectClassInfo.object + "." + element.getSimpleName() + " = null");
                }
            }
            builder.endControlFlow();
            methodBuilder.addCode(builder.build());
        }
        MethodSpec.Builder builder = MethodSpec.methodBuilder("creator")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(Class.class, "clazz")
                .returns(ClassName.bestGuess(Constants.IINJECT_CLASS_NAME));
        for (InjectClassInfo injectClassInfo : injectedClassInfos.values()) {
            createFile(injectClassInfo, injectClassInfo.methodBuilder.build());
            builder.addCode(CodeBlock.builder().beginControlFlow(String.format("if(clazz == %s.class)", injectClassInfo.qualifiedName)).build());
            builder.addStatement("return new " + injectClassInfo.generateClassName + "()");
            builder.endControlFlow();
        }
        builder.addStatement("return null");
        createMapFile(builder.build());
    }

    private void createFile(InjectClassInfo injectClassInfo, MethodSpec... methods) {
        try {
            //创建类 MainActivityInject
            TypeSpec.Builder injectClassBuilder = TypeSpec.classBuilder(injectClassInfo.generateClassName)
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .superclass(ClassName.bestGuess(Constants.IINJECT_CLASS_NAME))
                    .addJavadoc("Inject field of " + injectClassInfo.injectedClassName + " by " + injectClassInfo.generateClassName + "!");
            for (MethodSpec method : methods) {
                injectClassBuilder.addMethod(method);
            }

            //创建文件 MainActivityInject.java
            JavaFile.Builder injectFileBuilder = JavaFile
                    .builder(Constants.INJECT_PACKAGE_NAME, injectClassBuilder.build())
                    .addFileComment("The code generated by polaris-compiler!");
            JavaFile javaFile = injectFileBuilder.build();
            javaFile.writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createMapFile(MethodSpec method) {
        try {
            //创建类 InjectFactory
            TypeSpec.Builder injectClassBuilder = TypeSpec.classBuilder(Constants.INJECTFACTORY_CLASS_SIMPLE_NAME)
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addSuperinterface(ClassName.bestGuess(Constants.FACTORY_CLASS_NAME))
                    .addJavadoc("Create inject parser!");
            injectClassBuilder.addMethod(method);
            //创建文件 InjectFactory.java
            JavaFile.Builder injectFileBuilder = JavaFile
                    .builder(Constants.INJECT_PACKAGE_NAME, injectClassBuilder.build())
                    .addFileComment("The code generated by polaris-compiler!");
            JavaFile javaFile = injectFileBuilder.build();
            javaFile.writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

