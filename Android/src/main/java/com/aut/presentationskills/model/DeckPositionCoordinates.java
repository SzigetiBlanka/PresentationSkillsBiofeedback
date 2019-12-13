package com.aut.presentationskills.model;


import com.google.gson.annotations.SerializedName;

import java.util.Objects;

/**
 * DeckPositionCoordinates
 */
public class DeckPositionCoordinates {

    @SerializedName("x")
    private Double x = null;

    @SerializedName("y")
    private Double y = null;

    /**
     * Get x
     * @return x
     **/
    public Double getX() {
        return x;
    }
    public void setX(Double x) {
        this.x = x;
    }

    /**
     * Get y
     * @return y
     **/
    public Double getY() {
        return y;
    }
    public void setY(Double y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DeckPositionCoordinates deckPositionCoordinates = (DeckPositionCoordinates) o;
        return Objects.equals(this.x, deckPositionCoordinates.x) &&
                Objects.equals(this.y, deckPositionCoordinates.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class DeckPositionCoordinates {\n");

        sb.append("    x: ").append(toIndentedString(x)).append("\n");
        sb.append("    y: ").append(toIndentedString(y)).append("\n");
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
