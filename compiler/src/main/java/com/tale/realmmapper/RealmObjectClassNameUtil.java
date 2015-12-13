package com.tale.realmmapper;

import com.google.auto.common.MoreElements;
import com.squareup.javapoet.ClassName;
import javax.lang.model.element.Element;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public class RealmObjectClassNameUtil {

    public static ClassName getClassName(Element element) {
        String pkg = MoreElements.getPackage(element).getQualifiedName().toString();
        String name = element.getSimpleName().toString();
        return ClassName.get(pkg, getSimpleName(name));
    }

    public static ClassName getClassName(ClassName elementClassName) {
        return ClassName.get(elementClassName.packageName(), getSimpleName(
            elementClassName.simpleName()));
    }

    public static String getSimpleName(String elementName) {
        return elementName;
    }
}
