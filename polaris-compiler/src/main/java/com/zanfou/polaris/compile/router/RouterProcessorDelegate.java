package com.zanfou.polaris.compile.router;


import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.zanfou.polaris.annotation.keep.Keep;
import com.zanfou.polaris.annotation.router.AbsRouter;
import com.zanfou.polaris.annotation.router.RouteType;
import com.zanfou.polaris.annotation.router.RouterParams;
import com.zanfou.polaris.annotation.router.RouterPool;
import com.zanfou.polaris.annotation.utils.Constants;
import com.zanfou.polaris.annotation.utils.RouterUtils;
import com.zanfou.polaris.compile.utils.ServicesUtils;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

/**
 * @author shuxin.wei email:weishuxin@maoyan.com
 * @version v1.0.0
 * @date 2019-09-10
 * <p>
 * 处理解析
 */
public class RouterProcessorDelegate {
    private final ProcessingEnvironment processingEnv;
    private final Elements elementUtils;
    private final Filer filer;
    private final Messager messager;
    private final Types typeUtils;
    /**
     * 保存待创建的Router文件
     * FileName:RouterJavaFileCreator
     */
    private Map<String, RouterJavaFileCreator> fileCreators = new HashMap<>();
    /**
     * 保存已创建的Router文件，用于创建RouterPool
     * FileName:GroupName
     */
    private Map<String, String> routerFilePath = new HashMap<>();
    /**
     * 保存所有的Path，用于查重
     * Path:ClassName
     */
    private Map<String, String> paths = new HashMap<>();

    public RouterProcessorDelegate(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
        typeUtils = processingEnv.getTypeUtils();
    }

    /**
     * 解析分发Element
     *
     * @param roundEnvironment
     * @param element
     * @param routeParams
     */
    public void dispatchElement(RoundEnvironment roundEnvironment, Element element, RouterParams routeParams) {
        //Activity
        if (isSubtype(element, RouteType.ACTIVITY)) {
            routeParams.setRouteType(RouteType.ACTIVITY);
            handleActivity(roundEnvironment, element, routeParams);
        }
        //Fragment
        else if (isSubtype(element, RouteType.FRAGMENT)) {
            routeParams.setRouteType(RouteType.FRAGMENT);
            handleFragment(roundEnvironment, element, routeParams);
        }
        //Androidx Fragment
        else if (isSubtype(element, RouteType.FRAGMENT_X)) {
            routeParams.setRouteType(RouteType.FRAGMENT_X);
            handleFragment(roundEnvironment, element, routeParams);
        }
        //Service
        else {
            routeParams.setRouteType(RouteType.SERVICE);
            handleService(roundEnvironment, element, routeParams);
        }

    }

    /**
     * 解析完成，生成文件
     */
    public void processingOver() {
        for (RouterJavaFileCreator javaFileCreator : fileCreators.values()) {
            try {
                String fileName = javaFileCreator.create(filer);
                messager.printMessage(Diagnostic.Kind.NOTE, "File '" + fileName + "' created success!");
                routerFilePath.put(fileName, javaFileCreator.getGroupName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            messager.printMessage(Diagnostic.Kind.NOTE, "File '" + createRouterPoolFile() + "' created success!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建保存所有路由映射的文件，这样加载时，不用反射所有的路由映射文件了，只需要反射这一个文件就可以，性能提升并且复杂度降低很多；
     * 在调用到路由映射时，路由组才会进行加载；也就是路由加载会分两级；
     * 1. 加载所有路由映射文件，相当于加载路由索引；
     * 2. 根据路由索引进行查找路由信息，路由组初次被查找才会被加载；
     *
     * @throws IOException
     */
    private String createRouterPoolFile() throws IOException {
        String fieldName = "allRouters";
        String parameterName = "group";

        //构造 private final Map<String,BaseRouter> allRouter = new LinkedHashMap<>(1,0.75f,true);
        FieldSpec.Builder routesFiled = FieldSpec.builder(
                ParameterizedTypeName.get(ClassName.get(Map.class),
                        TypeName.get(String.class),
                        TypeName.get(AbsRouter.class)),
                fieldName,
                Modifier.FINAL, Modifier.PRIVATE);
        routesFiled.initializer(CodeBlock.builder().add("new $T<>(1,0.75f,true)", ClassName.get(LinkedHashMap.class)).build());

        //构造 public RouterPool(){allRouter.put(Path,BaseRouter);}
        String codeFormat = "%s.put(\"%s\",new %s())";
        MethodSpec.Builder routerPoolConstructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("Load all router map into the list!");
        for (String path : routerFilePath.keySet()) {
            routerPoolConstructor.addStatement(String.format(codeFormat, fieldName, routerFilePath.get(path), path));
        }

        //构造 public BaseRouter findRouterByGroup(String group){return allRouters.get(group);}
        MethodSpec.Builder findRouterByGroup = MethodSpec.methodBuilder(Constants.ROUTER_POOL_FIND_ROUTER_METHOD_NAME)
                .addParameter(ParameterSpec.builder(TypeName.get(String.class), parameterName).build())
                .addModifiers(Modifier.PUBLIC)
                .returns(AbsRouter.class)
                .addAnnotation(Override.class)
                .addStatement(String.format("return %s.get(%s)", fieldName, parameterName))
                .addJavadoc(String.format("Find router from %s by group name!", fieldName));

        //创建类
        TypeSpec.Builder routerPoolClassBuilder = TypeSpec.classBuilder(Constants.ROUTER_POOL_FILE_NAME)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addField(routesFiled.build())
                .addMethod(routerPoolConstructor.build())
                .addMethod(findRouterByGroup.build())
                .addSuperinterface(RouterPool.class)
                .addAnnotation(Keep.class)
                .addJavadoc("Registered all router map into " + Constants.ROUTER_POOL_FILE_NAME + "!");

        //创建文件 RouterPool
        TypeSpec typeSpec = routerPoolClassBuilder.build();
        JavaFile.Builder routerPoolFileBuilder = JavaFile.builder(Constants.PACKAGE_NAME,
                typeSpec)
                .addFileComment("The code generated by polaris-compiler!");
        JavaFile javaFile = routerPoolFileBuilder.build();
        javaFile.writeTo(filer);
        return javaFile.packageName + "." + typeSpec.name;
    }


    /**
     * 生成Fragment路由
     *
     * @param roundEnvironment 注解上下文
     * @param element          被注解的类
     * @param routeParams      路由参数
     */
    private void handleFragment(RoundEnvironment roundEnvironment, Element element, RouterParams routeParams) {
        handleElement(roundEnvironment, element, routeParams, RouteType.FRAGMENT_X.getSimpleName());
    }

    /**
     * 生成Activity路由
     *
     * @param roundEnvironment 注解上下文
     * @param element          被注解的类
     * @param routeParams      路由参数
     */
    private void handleActivity(RoundEnvironment roundEnvironment, Element element, RouterParams routeParams) {
        handleElement(roundEnvironment, element, routeParams, RouteType.ACTIVITY.getSimpleName());
    }

    /**
     * 生成Service路由
     *
     * @param roundEnvironment 注解上下文
     * @param element          被注解的类
     * @param routeParams      路由参数
     */
    private void handleService(RoundEnvironment roundEnvironment, Element element, RouterParams routeParams) {
        handleElement(roundEnvironment, element, routeParams, RouteType.SERVICE.getSimpleName());
    }

    /**
     * 处理Element
     *
     * @param roundEnvironment
     * @param element
     * @param routeParams
     * @param type
     */
    private void handleElement(RoundEnvironment roundEnvironment,
                               Element element,
                               RouterParams routeParams,
                               String type) {

        //routes.put(path,Fragment.class) ;
        for (URI uri : routeParams.getPaths()) {
            String targetClassName = ServicesUtils.findEnclosingTypeElement(element).getQualifiedName().toString();

            String uriStr = uri.toString();
            String clazzName = paths.get(uriStr);
            if (clazzName != null) {
                throw new RuntimeException(String.format(
                        "The '%s' path is duplication,please check these files again!%n%s(%s.java:1)%n%s(%s.java:1)",
                        uriStr,
                        clazzName, clazzName.substring(clazzName.lastIndexOf(".") + 1),
                        targetClassName, element.getSimpleName())
                );
            }
            paths.put(uriStr, targetClassName);

            String routeParamsString = RouterParams.fillRouteParamsString(routeParams, uriStr, targetClassName);
            String format = String.format("%s.put(\"%s\"," + routeParamsString + ")", Constants.FIELD_ROUTES, uri);

            String groupName = RouterUtils.getGroupByPath(uri.getPath());
            String fileName = String.format(Constants.FILE_NAME_ROUTER_FORMAT, RouterUtils.getGroupUpper(groupName));
            RouterJavaFileCreator activityCreator = fileCreators.get(fileName);
            if (activityCreator == null) {
                activityCreator = new RouterJavaFileCreator(routeParams, groupName);
                fileCreators.put(fileName, activityCreator);
            }
            MethodSpec.Builder methodBuilder = activityCreator.getMethodBuilder();
            methodBuilder.addStatement(format, RouterParams.class, RouteType.class);
        }
    }

    /**
     * 判断被注解类的类型
     */
    private boolean isSubtype(Element element, RouteType routeType) {
        return typeUtils.isSubtype(element.asType(), elementUtils.getTypeElement(routeType.getName()).asType());
    }
}
