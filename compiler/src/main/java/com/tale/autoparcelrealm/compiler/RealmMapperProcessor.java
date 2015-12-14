/**
 * PrettyBundle
 *
 * Created by Giang Nguyen on 12/12/15.
 * Copyright (c) 2015 Umbala. All rights reserved.
 */

package com.tale.autoparcelrealm.compiler;

import com.google.auto.service.AutoService;
import java.util.LinkedList;
import javax.annotation.processing.Processor;
import processorworkflow.AbstractProcessing;
import processorworkflow.AbstractProcessor;
import processorworkflow.Logger;

@AutoService(Processor.class)
public class RealmMapperProcessor extends AbstractProcessor<State> {

  @Override protected State processingState() {
    return new State();
  }
  @Override
  protected LinkedList<AbstractProcessing> processings() {
    LinkedList<AbstractProcessing> processings = new LinkedList<>();
    processings.add(new RealmObjectProcessing(elements, types, errors, state));
    processings.add(new RealmObjectMapperProcessing(elements, types, errors, state));
    return processings;
  }

  public RealmMapperProcessor() {

    // don't forget to disable logging before releasing
    // find a way to have the boolean set automatically via gradle
    Logger.init("RealmMapper Processor", false);
  }
}
