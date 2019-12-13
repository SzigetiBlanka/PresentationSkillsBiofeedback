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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Video
 */

public class Video {

  @SerializedName("_id")
  private String _id = null;

  @SerializedName("name")
  private String name = null;

  @SerializedName("sceneId")
  private String sceneId = null;

  @SerializedName("videoUrl")
  private String videoUrl = null;

  @SerializedName("deckPositions")
  private List<DeckPosition> deckPositions = null;

  @SerializedName("labels")
  private List<VideoLabels> labels = null;

  @SerializedName("createdAt")
  private String createdAt = null;
  public Video _id(String _id) {
    this._id = _id;
    return this;
  }

  

  /**
  * Get _id
  * @return _id
  **/
  public String getId() {
    return _id;
  }
  public void setId(String _id) {
    this._id = _id;
  }

  /**
  * Get name
  * @return name
  **/
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }

  /**
  * Get sceneId
  * @return sceneId
  **/
  public String getSceneId() {
    return sceneId;
  }
  public void setSceneId(String sceneId) {
    this.sceneId = sceneId;
  }

  /**
  * Get videoUrl
  * @return videoUrl
  **/
  public String getVideoUrl() {
    return videoUrl;
  }
  public void setVideoUrl(String videoUrl) {
    this.videoUrl = videoUrl;
  }

  public Video addLabelsItem(VideoLabels labelsItem) {
    if (this.labels == null) {
      this.labels = new ArrayList<VideoLabels>();
    }
    this.labels.add(labelsItem);
    return this;
  }

  /**
   * Get DeckPositions
   * @return DeckPositions
   **/
  public List<DeckPosition> getDeckPositions() {
    return deckPositions;
  }
  public void setDeckPositions(List<DeckPosition> deckPositions) {
    this.deckPositions = deckPositions;
  }

  /**
  * Get labels
  * @return labels
  **/
  public List<VideoLabels> getLabels() {
    return labels;
  }
  public void setLabels(List<VideoLabels> labels) {
    this.labels = labels;
  }

  /**
  * Get createdAt
  * @return createdAt
  **/
  public String getCreatedAt() {
    return createdAt;
  }
  public void setCreatedAt(String createdAt) {
    this.createdAt = createdAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Video video = (Video) o;
    return Objects.equals(this._id, video._id) &&
        Objects.equals(this.name, video.name) &&
        Objects.equals(this.sceneId, video.sceneId) &&
        Objects.equals(this.videoUrl, video.videoUrl) &&
        Objects.equals(this.labels, video.labels) &&
        Objects.equals(this.createdAt, video.createdAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(_id, name, sceneId, videoUrl, labels, createdAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Video {\n");

    sb.append("    _id: ").append(toIndentedString(_id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    sceneId: ").append(toIndentedString(sceneId)).append("\n");
    sb.append("    videoUrl: ").append(toIndentedString(videoUrl)).append("\n");
    sb.append("    labels: ").append(toIndentedString(labels)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
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