package com.jaywithabeanie.easyforge.internal;

import com.jaywithabeanie.easyforge.api.annotation.EasyForgeItems;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;

public class EasyForgeScanner {

    public static void scan() {
        try (ScanResult result = new ClassGraph().enableAnnotationInfo().scan()) {
            result.getClassesWithAnnotation(
                EasyForgeItems.class.getName()
            ).forEach(classInfo -> {
                try {
                    Class.forName(classInfo.getName());
                }
                catch (ClassNotFoundException e) {
                    throw new RuntimeException(
                        "Failed to load EasyForge registration class: "
                            + classInfo.getName(),
                        e
                    );
                }
            });
        }
    }

}
