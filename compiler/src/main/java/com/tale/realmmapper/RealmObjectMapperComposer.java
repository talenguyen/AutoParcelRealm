/**
 * PrettyBundle
 *
 * Created by Giang Nguyen on 12/12/15.
 * Copyright (c) 2015 Umbala. All rights reserved.
 */

package com.tale.realmmapper;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import java.util.List;
import javax.lang.model.element.Modifier;
import processorworkflow.AbstractComposer;

public class RealmObjectMapperComposer extends AbstractComposer<RealmObjectSpec> {

  public RealmObjectMapperComposer(List<RealmObjectSpec> specs) {
    super(specs);
  }

  @Override protected JavaFile compose(RealmObjectSpec spec) {
    final String modelClassName = spec.getClassName().simpleName();
    final String ackageName = spec.getClassName().packageName();
    final ClassName modelType = ClassName.get(ackageName, modelClassName);

    final TypeSpec.Builder builder =
        TypeSpec.classBuilder("Realm" + modelClassName + "$$Mapper")
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL);

    final ClassName realmObjectType = ClassName.get(ackageName, "Realm" + modelClassName);

    // [Begin Build createRealmObjectFromObject]
    // Create method builder
    MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("createRealmObjectFromObject")
        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
        .returns(realmObjectType)
        .addParameter(modelType, "object")
        .addStatement("$T realmObject = new $T()", realmObjectType, realmObjectType);

    // Add setter
    for (FieldData field : spec.getFieldDataList()) {
      methodBuilder.addStatement("realmObject.$N(object.$N())", HungarianNotation.createSetterForFieldName(field.name), field.name);
    }
    methodBuilder.addStatement("return realmObject");

    builder.addMethod(methodBuilder.build());
    // [End Build from method]

    // [Begin Build createObjectFromRealmObject]
    // Create method builder
    methodBuilder = MethodSpec.methodBuilder("createObjectFromRealmObject")
        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
        .returns(modelType)
        .addParameter(realmObjectType, "realmObject");

    // Build create statement
    StringBuilder sb = new StringBuilder(modelClassName + ".create(");
    // Add params
    final List<FieldData> fieldDataList = spec.getFieldDataList();
    final int size = fieldDataList.size();
    for (int i = 0; i < size; i++) {
      final FieldData field= fieldDataList.get(i);
      sb.append("realmObject."); // to generate "AutoObject.create(realmObject."
      sb.append(HungarianNotation.createGetterForFieldName(field.name)); // to generate "AutoObject.create(realmObject.get[field.name]
      sb.append("()"); // to generate "AutoObject.create(realmObject.get[field.name]()"
      if (i < size - 1) {
        sb.append(", ");
      }
    }
    sb.append(")");
    methodBuilder.addStatement("$T object = $N", modelType, sb.toString());
    methodBuilder.addStatement("return object");

    builder.addMethod(methodBuilder.build());
    // [End Build from method]

    TypeSpec typeSpec = builder.build();
    return JavaFile.builder(spec.getClassName().packageName(), typeSpec).build();
  }
}
