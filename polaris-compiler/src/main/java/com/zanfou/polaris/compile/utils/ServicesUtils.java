package com.zanfou.polaris.compile.utils;

import com.google.common.base.Charsets;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

/**
 * @author shuxin.wei email:weishuxin@maoyan.com
 * @version v1.0.0
 * @date 2019-08-20
 * <p>
 * 工具类
 *
 * <pre>{@code
 * javax.lang.model.type.MirroredTypesException: Attempt to access Class objects for TypeMirrors [com.zanfou.domain
 * .product.ProductRepository]
 *
 * solution:
 * https://area-51.blog/2009/02/13/getting-class-values-from-annotations-in-an-annotationprocessor/
 * }</pre>
 */
public class ServicesUtils {
    /**
     * 找到使用注解的Element
     *
     * @param element
     * @return
     */
    public static TypeElement findEnclosingTypeElement(Element element) {
        while (element != null && !(element instanceof TypeElement)) {
            element = element.getEnclosingElement();
        }
        return (TypeElement) element;
    }

    /**
     * 获取Annotation value
     *
     * @param annotationName
     * @param typeElement
     * @return
     */
    public static AnnotationValue getAnnotationValue(String annotationName, TypeElement typeElement) {
        for (AnnotationMirror am : typeElement.getAnnotationMirrors()) {
            if (annotationName.equals(am.getAnnotationType().toString())) {
                for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry :
                        am.getElementValues().entrySet()) {
                    if ("value".equals(entry.getKey().getSimpleName().toString())) {
                        return entry.getValue();
                    }
                }
            }
        }
        return null;
    }

    public static final String SERVICES_PATH = "META-INF/services/";

    /**
     * 获取Service文件路径
     *
     * @param serviceName
     * @return
     */
    public static String getPath(String serviceName) {
        return SERVICES_PATH + serviceName;
    }

    /**
     * 读取旧的Service
     *
     * @param input
     * @return
     */
    public static Set<String> readServiceFile(InputStream input) {
        HashSet<String> serviceClasses = new HashSet<>();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(input, Charsets.UTF_8))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    serviceClasses.add(line);
                }
            }
        } catch (Exception e) {
        }
        return serviceClasses;
    }

    /**
     * 生成Service
     *
     * @param services
     * @param output
     * @throws IOException
     */
    public static void writeServiceFile(Collection<String> services, OutputStream output) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, Charsets.UTF_8));
        for (String service : services) {
            writer.write(service);
            writer.newLine();
        }
        writer.flush();
    }
}
