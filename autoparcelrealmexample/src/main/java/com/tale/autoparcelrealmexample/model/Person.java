/**
 * AutoParcelRealm
 *
 * Created by Giang Nguyen on 12/13/15.
 * Copyright (c) 2015 Umbala. All rights reserved.
 */

package com.tale.autoparcelrealmexample.model;

import android.os.Parcelable;
import auto.parcel.AutoParcel;
import com.tale.prettybundle.RealmObject;

@RealmObject @AutoParcel public abstract class Person implements Parcelable {
  public abstract String name();

  public abstract int age();

  public static Person create(String name, int age) {
    return new AutoParcel_Person(name, age);
  }
}
