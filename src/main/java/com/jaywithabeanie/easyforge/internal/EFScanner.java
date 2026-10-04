package com.jaywithabeanie.easyforge.internal;

import com.jaywithabeanie.easyforge.api.annotation.EasyForge;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;

public class EFScanner {

    public static void scan(String basePackage) {
        try (ScanResult result = new ClassGraph().enableAnnotationInfo().acceptPackages(basePackage).scan()) {
            result.getClassesWithAnnotation(
                EasyForge.class.getName()
            ).forEach(classInfo -> {
                try {
                    Class.forName(classInfo.getName());
                }
                catch (ClassNotFoundException e) {
                    throw new RuntimeException(
                        "Failed to load EasyForge registration class: " + classInfo.getName(),
                        e
                    );
                }
            });
        }
    }

}
