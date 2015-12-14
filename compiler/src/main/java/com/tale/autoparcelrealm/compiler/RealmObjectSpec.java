/**
 * PrettyBundle
 *
 * Created by Giang Nguyen on 12/12/15.
 * Copyright (c) 2015 Umbala. All rights reserved.
 */

package com.tale.autoparcelrealm.compiler;

import com.squareup.javapoet.ClassName;
import java.util.ArrayList;
import java.util.List;

public class RealmObjectSpec {
  private final ClassName className;
  private List<FieldData> fieldDataList;

  public RealmObjectSpec(ClassName className) {
    this.className = className;
    fieldDataList = new ArrayList<>();
  }

  public ClassName getClassName() {
    return className;
  }

  public void setFieldDataList(List<FieldData> fieldDataList) {
    this.fieldDataList = fieldDataList;
  }

  public List<FieldData> getFieldDataList() {
    return fieldDataList;
  }
}
