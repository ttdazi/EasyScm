package com.yqing.processor;

import com.yqing.annotations.ScActionAnnotation;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.MethodSpec.Builder;
import com.squareup.javapoet.TypeSpec;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

@SupportedAnnotationTypes({"com.yqing.annotations.ScActionAnnotation"})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class ScActionProcessor extends AbstractProcessor {
    private static final String KEY_MODULE_NAME = "moduleName";
    private Filer mFiler;
    private SLogger logger;

    public ScActionProcessor() {
    }

    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.mFiler = processingEnv.getFiler();
        this.logger = new SLogger(processingEnv.getMessager());
        this.logger.info(">>> ScActionProcessor init. <<<");
    }

    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        this.logger.info(">>> ScActionProcessor process. <<<");
        this.praseAction(roundEnv);
        return true;
    }

    private void test(RoundEnvironment roundEnv) {
        for (Iterator var2 = roundEnv.getElementsAnnotatedWith(ScActionAnnotation.class).iterator(); var2.hasNext(); System.out.println("------------------------------")) {
            Element element = (Element) var2.next();
            System.out.println("------------------------------");
            if (element.getKind() == ElementKind.CLASS) {
                TypeElement typeElement = (TypeElement) element;
                System.out.println(typeElement.getSimpleName());
                System.out.println((typeElement.getAnnotation(ScActionAnnotation.class)).value());
            }
        }

    }

    private void praseAction(RoundEnvironment roundEnv) {
        String moduleName = this.getModuleName(roundEnv);
        String className = this.getGeneratedClassName(moduleName);
        String packageName = "com.zzy.processor.generated";
        String methodStatement = this.getMethodStatement(roundEnv);
        this.generateClassFile(packageName, className, methodStatement);
    }

    private String getMethodStatement(RoundEnvironment roundEnv) {
        StringBuilder builder = (new StringBuilder()).append("return \"");
        Map<String, String> actionMap = new HashMap();
        Iterator var4 = roundEnv.getElementsAnnotatedWith(ScActionAnnotation.class).iterator();

        while (var4.hasNext()) {
            Element element = (Element) var4.next();
            TypeElement typeElement = (TypeElement) element;
            String actionName = (typeElement.getAnnotation(ScActionAnnotation.class)).value();
            String clazzName = typeElement.getQualifiedName().toString();
            this.logger.info(">>> found action :" + actionName + " in class :" + clazzName);
            actionMap.put(actionName, clazzName);
        }

        builder.append(actionMap.toString());
        builder.append("\"");
        return builder.toString();
    }

    private void generateClassFile(String packageName, String fileName, String methodStatement) {
        Builder methodBuilder = MethodSpec.methodBuilder("toString").addModifiers(new Modifier[]{Modifier.PUBLIC}).addStatement(methodStatement, new Object[0]).returns(String.class).addAnnotation(Override.class);
        try {
            JavaFile.builder(packageName, TypeSpec.classBuilder(fileName).addJavadoc("this file is a generated Class,pls don't modify!", new Object[0]).addModifiers(new Modifier[]{Modifier.PUBLIC}).addMethod(methodBuilder.build()).build()).build().writeTo(this.mFiler);
        } catch (Exception var6) {
        }

    }

    private String getGeneratedClassName(String moduleName) {
        return moduleName + "GeneratedActionMap";
    }

    private String getModuleName(RoundEnvironment roundEnv) {
        String moduleName = "";
        Map<String, String> options = this.processingEnv.getOptions();
        if (options != null && options.containsKey("moduleName")) {
            moduleName = (String) options.get("moduleName");
        }

        return moduleName;
    }

    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotataions = new LinkedHashSet();
        annotataions.add(ScActionAnnotation.class.getCanonicalName());
        return annotataions;
    }

    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
