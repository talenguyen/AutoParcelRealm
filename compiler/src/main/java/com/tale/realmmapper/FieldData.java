/**
 * PrettyBundle
 *
 * Created by Giang Nguyen on 12/12/15.
 * Copyright (c) 2015 Umbala. All rights reserved.
 */

package com.tale.realmmapper;

import com.squareup.javapoet.TypeName;

public class FieldData {
  public final TypeName type;
  public final String name;

  public FieldData(TypeName type, String name) {
    this.type = type;
    this.name = name;
  }
}
