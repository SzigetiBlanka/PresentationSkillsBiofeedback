/*
 * Biofeedback
 * This a documentation for the biofeedback Rest API 
 *
 * OpenAPI spec version: 1.0.0-oas3
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package com.aut.presentationskills.model;

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Gets or Sets MeasurementStatus
 */
@JsonAdapter(MeasurementStatus.Adapter.class)
public enum MeasurementStatus {
  CREATED("Created"),
  ASSIGNED("Assigned"),
  READY("Ready"),
  STARTED("Started"),
  CANCELLED("Cancelled"),
  FINISHED("Finished"),
  EVAULATED("Evaulated");

  private String value;

  MeasurementStatus(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  public static MeasurementStatus fromValue(String text) {
    for (MeasurementStatus b : MeasurementStatus.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }

  public static class Adapter extends TypeAdapter<MeasurementStatus> {
    @Override
    public void write(final JsonWriter jsonWriter, final MeasurementStatus enumeration) throws IOException {
      jsonWriter.value(enumeration.getValue());
    }

    @Override
    public MeasurementStatus read(final JsonReader jsonReader) throws IOException {
      String value = jsonReader.nextString();
      return MeasurementStatus.fromValue(String.valueOf(value));
    }
  }
}
