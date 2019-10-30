package com.aidazi.processor.process;

import com.aidazi.annotations.ScActionAnnotation;
import com.aidazi.processor.utils.Constants;
import com.aidazi.processor.utils.SLogger;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

public class ScActionProcessor extends AbstractProcessor {
    private Filer mFiler;
    private SLogger logger;
    HashMap<String, String> hashMap = new HashMap<>();
    StringBuilder sb = new StringBuilder();

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


    private void praseAction(RoundEnvironment roundEnv) {
        StringBuilder methodStrStatement = getMethodStrStatement(roundEnv);
        generateclassToStringFile(Constants.ROUTTABLE_PACKAGE, Constants.ROUT_TABLE, methodStrStatement);
        //        List<FieldSpec> fieldSpecs = this.getMethodStatement(roundEnv);
        //this.generateClassFile(Constants.ROUTTABLE_PACKAGE, Constants.ROUT_TABLE, fieldSpecs);
    }

    private void generateclassToStringFile(String packageName, String fileName, StringBuilder sb) {
        if (sb.length() == 0) return;
        MethodSpec methodSpec = MethodSpec.methodBuilder("toString").addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addStatement("return $S", sb.toString()).returns(String.class).build();
        TypeSpec typeSpec = TypeSpec.classBuilder(fileName).addMethod(methodSpec)
                .addJavadoc(Constants.CLASSDOC).addModifiers(Modifier.PUBLIC).build();
        try {
            JavaFile.builder(packageName, typeSpec).build().writeTo(this.mFiler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private StringBuilder getMethodStrStatement(RoundEnvironment roundEnv) {
        StringBuilder sb = new StringBuilder();
        Iterator var4 = roundEnv.getElementsAnnotatedWith(ScActionAnnotation.class).iterator();
        while (var4.hasNext()) {
            Element element = (Element) var4.next();
            TypeElement typeElement = (TypeElement) element;
            String actionName = (typeElement.getAnnotation(ScActionAnnotation.class)).value();
            String clazzName = typeElement.getQualifiedName().toString();
            this.logger.info(">>> found action :" + actionName + " in class :" + clazzName);
            sb.append(actionName + "=" + clazzName + ",");
            this.logger.info(">>>-----map :" + " in class :" + sb);
        }
        return sb;
    }

    private List<FieldSpec> getMethodStatement(RoundEnvironment roundEnv) {
        List<FieldSpec> fieldSpecs = new ArrayList<>();
        Iterator var4 = roundEnv.getElementsAnnotatedWith(ScActionAnnotation.class).iterator();
        while (var4.hasNext()) {
            Element element = (Element) var4.next();
            TypeElement typeElement = (TypeElement) element;
            String actionName = (typeElement.getAnnotation(ScActionAnnotation.class)).value();
            String clazzName = typeElement.getQualifiedName().toString();
            this.logger.info(">>> found action :" + actionName + " in class :" + clazzName);
            sb.append(actionName + "=" + clazzName + ",");
            FieldSpec field = FieldSpec.
                    builder(String.class, actionName)
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                    .initializer("$S", clazzName)
                    .build();
            fieldSpecs.add(field);
            this.logger.info(">>>-----map :" + " in class :" + hashMap.toString());
        }

        return fieldSpecs;
    }

    private void generateClassFile(String packageName, String fileName, List<FieldSpec> fieldSpecs) {
        if (fieldSpecs == null || fieldSpecs.size() == 0) return;
        TypeSpec typeSpec = TypeSpec.classBuilder(fileName).addJavadoc(Constants.CLASSDOC)
                .addModifiers(Modifier.PUBLIC)
                .addFields(fieldSpecs).build();
        try {
            JavaFile.builder(packageName, typeSpec).build().writeTo(this.mFiler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> stringSet = new LinkedHashSet<>();
        stringSet.add(Constants.ANNOTATION_CLASSSTR);
        return stringSet;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
