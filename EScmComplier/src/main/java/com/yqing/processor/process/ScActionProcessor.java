package com.yqing.processor.process;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import com.yqing.annotations.ScActionAnnotation;
import com.yqing.processor.utils.Constants;
import com.yqing.processor.utils.SLogger;

import java.io.IOException;
import java.util.ArrayList;
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
        List<FieldSpec> fieldSpecs = this.getMethodStatement(roundEnv);
        this.generateClassFile(Constants.ROUTTABLE_PACKAGE, Constants.ROUT_TABLE, fieldSpecs);
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
            FieldSpec field = FieldSpec.
                    builder(String.class, actionName)
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                    .initializer("$S", clazzName)
                    .build();
            fieldSpecs.add(field);
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
