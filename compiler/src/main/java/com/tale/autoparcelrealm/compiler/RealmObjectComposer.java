/**
 * PrettyBundle
 *
 * Created by Giang Nguyen on 12/12/15.
 * Copyright (c) 2015 Umbala. All rights reserved.
 */

package com.tale.autoparcelrealm.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import java.util.List;
import javax.lang.model.element.Modifier;
import processorworkflow.AbstractComposer;

public class RealmObjectComposer extends AbstractComposer<RealmObjectSpec> {

  public RealmObjectComposer(List<RealmObjectSpec> specs) {
    super(specs);
  }

  @Override protected JavaFile compose(RealmObjectSpec spec) {

    final ClassName superclass = ClassName.get("io.realm", "RealmObject");
    final TypeSpec.Builder builder =
        TypeSpec.classBuilder("Realm" + spec.getClassName().simpleName())
            .superclass(superclass)
            .addModifiers(Modifier.PUBLIC);

    // Build fields
    for (FieldData field : spec.getFieldDataList()) {
      // Build fields
      final FieldSpec fieldSpec = FieldSpec.builder(field.type, field.name, Modifier.PRIVATE).build();
      builder.addField(fieldSpec);

      // Build setter
      final MethodSpec setterMethodSpec =
          MethodSpec.methodBuilder(HungarianNotation.createSetterForFieldName(field.name))
              .addModifiers(Modifier.PUBLIC)
              .addParameter(field.type, field.name)
              .addStatement("this.$N = $N", field.name, field.name)
              .build();
      builder.addMethod(setterMethodSpec);

      // Build getter
      final MethodSpec getterMethodSpec =
          MethodSpec.methodBuilder(HungarianNotation.createGetterForFieldName(field.name))
              .addModifiers(Modifier.PUBLIC)
              .returns(field.type)
              .addStatement("return $N", field.name)
              .build();

      builder.addMethod(getterMethodSpec);
    }

    TypeSpec typeSpec = builder.build();
    return JavaFile.builder(spec.getClassName().packageName(), typeSpec).build();  }
}
