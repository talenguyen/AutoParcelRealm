package com.tale.realmmapper;

import org.junit.Assert;
import org.junit.Test;

/**
 * PrettyBundle
 *
 * Created by Giang Nguyen on 12/12/15.
 * Copyright (c) 2015 Umbala. All rights reserved.
 */
public class HungarianNotationTest {

  @Test public void testCreateSetterForFieldName() throws Exception {
    Assert.assertEquals("setFoo", HungarianNotation.createSetterForFieldName("foo"));
  }
}