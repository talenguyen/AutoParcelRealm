/**
 * PrettyBundle
 *
 * Created by Giang Nguyen on 12/12/15.
 * Copyright (c) 2015 Umbala. All rights reserved.
 */

package com.tale.realmmapper;

import com.squareup.javapoet.TypeName;
import com.tale.prettybundle.RealmObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import processorworkflow.AbstractExtractor;
import processorworkflow.Errors;
import processorworkflow.ExtractorUtils;

public class RealmObjectExtractor extends AbstractExtractor {

  private TypeMirror targetTypeMirror;
  private List<FieldData> fieldDataList;
  private final Element realmObjectElement;

  public RealmObjectExtractor(Element realmObjectElement, Element element, Types types, Elements elements, Errors errors) {
    super(element, types, elements, errors);
    this.realmObjectElement = realmObjectElement;
  }

  public Element getRealmObjectElement() {
    return realmObjectElement;
  }

  public List<FieldData> getFieldDataList() {
    return fieldDataList;
  }

  @Override public void extract() {
    targetTypeMirror = ExtractorUtils.getValueFromAnnotation(element, RealmObject.class, "target");
    if (targetTypeMirror == null) {
      targetTypeMirror = realmObjectElement.asType();
    }

    final List<ExecutableElement> abstractGetters = findAbstractGetters(element);
    fieldDataList = new ArrayList<>(abstractGetters.size());
    for (ExecutableElement abstractGetter : abstractGetters) {
      final TypeMirror returnType = abstractGetter.getReturnType();
      final TypeName type = TypeName.get(returnType);
      final String name = abstractGetter.getSimpleName().toString();
      final FieldData fieldData = new FieldData(type, name);
      fieldDataList.add(fieldData);
    }
  }

  private List<ExecutableElement> findAbstractGetters(Element element) {
    final List<? extends Element> enclosedElements = element.getEnclosedElements();
    final List<ExecutableElement> result = new ArrayList<>();
    for (Element enclosedElement : enclosedElements) {
      // Only select methods
      if (enclosedElement instanceof ExecutableElement) {
        final ExecutableElement executableElement = (ExecutableElement) enclosedElement;
        if (hasReturnType(executableElement) && isAbstractMethod(executableElement)) {
          result.add(executableElement);
        }
      }
    }
    return result;
  }

  private boolean isAbstractMethod(ExecutableElement executableElement) {
    final Set<Modifier> modifiers = executableElement.getModifiers();
    for (Modifier modifier : modifiers) {
      if (modifier == Modifier.ABSTRACT) {
        return true;
      }
    }
    return false;
  }

  private boolean hasReturnType(ExecutableElement executableElement) {
    return executableElement.getReturnType().getKind() != TypeKind.VOID;
  }
}
