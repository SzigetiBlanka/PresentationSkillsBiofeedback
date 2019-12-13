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

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

/**
 * VideoLabels
 */

public class VideoLabels {

  @SerializedName("timestamp")
  private String timestamp = null;

  @SerializedName("label")
  private String label = null;

  /**
  * Get timestamp
  * @return timestamp
  **/
  public String getTimestamp() {
    return timestamp;
  }
  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  /**
  * Get label
  * @return label
  **/
  public String getLabel() {
    return label;
  }
  public void setLabel(String label) {
    this.label = label;
  }
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VideoLabels videoLabels = (VideoLabels) o;
    return Objects.equals(this.timestamp, videoLabels.timestamp) &&
        Objects.equals(this.label, videoLabels.label);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timestamp, label);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VideoLabels {\n");

    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    label: ").append(toIndentedString(label)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}