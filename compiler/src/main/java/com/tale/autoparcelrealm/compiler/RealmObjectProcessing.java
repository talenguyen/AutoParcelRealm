/**
 * PrettyBundle
 *
 * Created by Giang Nguyen on 12/12/15.
 * Copyright (c) 2015 Umbala. All rights reserved.
 */

package com.tale.autoparcelrealm.compiler;

import com.google.auto.common.MoreElements;
import com.tale.autoparcelrealm.annotation.RealmObject;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import processorworkflow.AbstractComposer;
import processorworkflow.AbstractProcessing;
import processorworkflow.Errors;
import processorworkflow.ProcessingBuilder;

public class RealmObjectProcessing extends AbstractProcessing<RealmObjectSpec, State> {

  private final Set<RealmObjectExtractor> extractors;

  public RealmObjectProcessing(Elements elements, Types types, Errors errors, State state) {
    super(elements, types, errors, state);
    extractors = new HashSet<>();
  }

  @Override public Set<Class<? extends Annotation>> supportedAnnotations() {
    final Set set = new HashSet();
    set.add(RealmObject.class);
    return set;
  }

  @Override protected void processElements(Set<? extends Element> annotationElements) {
    super.processElements(annotationElements);
    if (errors.hasErrors()) {
      return;
    }

    processExtractors();
  }

  @Override public boolean processElement(Element element, Errors.ElementErrors elementErrors) {
    if (ElementKind.ANNOTATION_TYPE.equals(element.getKind())) {
      // @AutoComponent is applied on another annotation, find out the targets of that annotation
      Set<? extends Element> targetElements = roundEnvironment.getElementsAnnotatedWith(
          MoreElements.asType(element));
      for (Element targetElement : targetElements) {
        process(targetElement, element);
        if (errors.hasErrors()) {
          return false;
        }
      }
      return true;
    }

    process(element, element);

    if (errors.hasErrors()) {
      return false;
    }

    return true;
  }

  @Override public AbstractComposer<RealmObjectSpec> createComposer() {
    return new RealmObjectComposer(specs);
  }

  private void processExtractors() {
    for (RealmObjectExtractor extractor : extractors) {
      RealmObjectSpec spec = new Builder(extractor, errors).build();
      if (errors.hasErrors()) {
        return;
      }

      specs.add(spec);
    }
  }

  private void process(Element targetElement, Element element) {
    RealmObjectExtractor extractor = new RealmObjectExtractor(targetElement, element, types, elements, errors);
    if (errors.hasErrors()) {
      return;
    }

    extractors.add(extractor);
  }

  private class Builder extends ProcessingBuilder<RealmObjectExtractor, RealmObjectSpec> {

    public Builder(RealmObjectExtractor extractor, Errors errors) {
      super(extractor, errors);
    }

    @Override protected RealmObjectSpec build() {
      extractor.extract();
      final RealmObjectSpec realmObjectSpec = new RealmObjectSpec(
          RealmObjectClassNameUtil.getClassName(extractor.getRealmObjectElement()));
      realmObjectSpec.setFieldDataList(extractor.getFieldDataList());
      return realmObjectSpec;
    }
  }

}
