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

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

/**
 * User
 */

public class User {

  @SerializedName("_id")
  @Expose
  private String _id = null;

  @SerializedName("name")
  @Expose
  private String name = null;

  @SerializedName("googleId")
  @Expose
  private String googleId = null;

  @SerializedName("email")
  @Expose
  private String email = null;

  @SerializedName("token")
  @Expose
  private String token = null;

  @SerializedName("__v")
  @Expose
  private String __v = null;

  @SerializedName("createdAt")
  @Expose
  private String createdAt = null;

  @SerializedName("lastLoginAt")
  @Expose
  private String lastLoginAt = null;

  @SerializedName("role")
  @Expose
  private Role role = null;


  /**
  * Get _id
  * @return _id
  **/
  public String getId() { return _id; }
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
  public void setName(String name) { this.name = name; }

  /**
  * Get googleId
  * @return googleId
  **/
  public String getGoogleId() {
    return googleId;
  }
  public void setGoogleId(String googleId) {
    this.googleId = googleId;
  }

  /**
  * Get email
  * @return email
  **/
  public String getEmail() {
    return email;
  }
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * Get token
   * @return token
   **/
  public String getToken() {
    return token;
  }
  public void setToken(String token) {
    this.token = token;
  }

  /**
   * Get __v
   * @return __v
   **/
  public String get__v() {
    return __v;
  }
  public void set__v(String __v) {
    this.__v = __v;
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

  /**
  * Get lastLoginAt
  * @return lastLoginAt
  **/
  public String getLastLoginAt() {
    return lastLoginAt;
  }
  public void setLastLoginAt(String lastLoginAt) {
    this.lastLoginAt = lastLoginAt;
  }

  /**
  * Get role
  * @return role
  **/
  public Role getRole() {
    return role;
  }
  public void setRole(Role role) {
    this.role = role;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    User user = (User) o;
    return Objects.equals(this._id, user._id) &&
        Objects.equals(this.name, user.name) &&
        Objects.equals(this.token, user.token) &&
        Objects.equals(this.googleId, user.googleId) &&
        Objects.equals(this.email, user.email) &&
        Objects.equals(this.createdAt, user.createdAt) &&
        Objects.equals(this.lastLoginAt, user.lastLoginAt) &&
        Objects.equals(this.__v, user.__v) &&
        Objects.equals(this.role, user.role);
  }

  @Override
  public int hashCode() {
    return Objects.hash(_id, name, googleId, email, createdAt, lastLoginAt, role);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class User {\n");

    sb.append("    _id: ").append(toIndentedString(_id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    googleId: ").append(toIndentedString(googleId)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    lastLoginAt: ").append(toIndentedString(lastLoginAt)).append("\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
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
