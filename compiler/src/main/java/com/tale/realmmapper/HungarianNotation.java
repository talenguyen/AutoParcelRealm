package com.tale.realmmapper;

/**
 * Little helper class to deal with hungarian notation
 *
 * @author Hannes Dorfmann
 */
public class HungarianNotation {

  private HungarianNotation() {
  }

  /**
   * Get the name of the field removing hungarian notation
   *
   * @param name The field name
   * @return the field name without hungarian notation
   */

  public static String removeNotation(String name) {
    if (name.matches("^m[A-Z]{1}")) {
      return name.substring(1, 2).toLowerCase();
    } else if (name.matches("m[A-Z]{1}.*")) {
      return name.substring(1, 2).toLowerCase() + name.substring(2);
    } else if (name.matches("[A-Z]{1}.*")) {
      return name.substring(0, 1).toLowerCase() + name.substring(1);
    }
    return name;
  }

  /**
   * Removes the hungarian notation from setter method
   *
   * @param methodName The name of the method
   * @return clean version without hungarian notation
   */
  public static String removeNotationFromSetterAndSetPrefix(String methodName) {

    if (methodName.matches("^set\\w+")) {
      String withoutSetPrefix = methodName.substring(3);

      if (Character.isLowerCase(withoutSetPrefix.charAt(0))) {
        return HungarianNotation.removeNotation(withoutSetPrefix);
      } else if (withoutSetPrefix.length() >= 2 && withoutSetPrefix.charAt(0) == 'M'
          && Character.isUpperCase(withoutSetPrefix.charAt(1))) {
        return Character.toLowerCase(withoutSetPrefix.charAt(1)) + withoutSetPrefix.substring(2);
      }

      return removeNotation(withoutSetPrefix);
    }
    return removeNotation(methodName);
  }

  public static String createSetterForFieldName(String fieldName) {
    return "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
  }
  public static String createGetterForFieldName(String fieldName) {
    return "get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
  }
}
