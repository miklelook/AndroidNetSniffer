package com.zanfou.polaris.compile.filedinject;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.zanfou.polaris.annotation.utils.Constants;

import javax.lang.model.element.Modifier;

/**
 * @author shuxin.wei email:weishuxin@maoyan.com
 * @version v1.0.0
 * @date 2019-10-12
 * <p>
 * 要Inject的类的相关信息
 */
public class InjectClassInfo {
    public final String object = "object";
    public final String bundle = "bundle";
    /**
     * 被注入的类名
     */
    public String injectedClassName;
    /**
     * 被注入的类全名
     */
    public String qualifiedName;
    /**
     * 生成的文件名
     */
    public String generateClassName;

    /**
     * 生成的方法
     */
    public final MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("inject")
            .addParameter(Object.class, "injectObject")
            .addParameter(ClassName.bestGuess(Constants.BUNDLE_CLASS_NAME), bundle)
            .addAnnotation(Override.class)
            .addModifiers(Modifier.PUBLIC)
            .addJavadoc("Oh, inject!");

    public InjectClassInfo(String qualifiedName, String injectedClassName, String generateClassName) {
        this.qualifiedName = qualifiedName;
        this.injectedClassName = injectedClassName;
        this.generateClassName = generateClassName;

        //类型强转检查 if(!(injectObject instanceof TestActivity)) {return;}
        methodBuilder.addCode(String.format("if(!(injectObject instanceof %s)) {return;}\n", injectedClassName));
        methodBuilder.addException(Exception.class);
        //MainActivity object = (MainActivity)injectObject;
        ClassName className = ClassName.bestGuess(qualifiedName);
        methodBuilder.addCode("$T " + object + " = ($T)injectObject;\n", className, className);

//        JSONObject jsonObject = null;
//        if (bundle.containsKey(Constants.POLARIS_JSON_QUERY)) {
//            jsonObject = new JSONObject(bundle.getString(Constants.POLARIS_JSON_QUERY));
//        }
        methodBuilder.addStatement("$T jsonObject = null", ClassName.get("org.json", "JSONObject"));
        methodBuilder.addCode(
                "if (bundle.containsKey($T.POLARIS_JSON_QUERY)) {\n"
                        + "    jsonObject = new JSONObject(bundle.getString(Constants.POLARIS_JSON_QUERY));\n"
                        + "}\n"
                , ClassName.get("com.zanfou.polaris.annotation.utils", "Constants"));

    }
}
