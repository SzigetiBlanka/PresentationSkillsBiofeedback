package com.aut.presentationskills.model;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

/**
 * DeckPosition
 */

public class DeckPosition {

    @SerializedName("topLeft")
    private DeckPositionCoordinates topLeft = null;

    @SerializedName("bottomRight")
    private DeckPositionCoordinates bottomRight = null;

    @SerializedName("type")
    private Deck.DeckTpye type = null;


    /**
     * Get topLeft
     * @return topLeft
     **/
    public DeckPositionCoordinates getTopLeft() {
        return topLeft;
    }
    public void setTopLeft(DeckPositionCoordinates topLeft) {
        this.topLeft = topLeft;
    }

    /**
     * Get bottomRight
     * @return bottomRight
     **/
    public DeckPositionCoordinates getBottomRight() {
        return bottomRight;
    }
    public void setBottomRight(DeckPositionCoordinates bottomRight) {
        this.bottomRight = bottomRight;
    }

    /**
     * Get type
     * @return type
     **/
    public Deck.DeckTpye getType() {
        return type;
    }
    public void setType(Deck.DeckTpye type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DeckPosition deckPosition = (DeckPosition) o;
        return Objects.equals(this.topLeft, deckPosition.topLeft) &&
                Objects.equals(this.bottomRight, deckPosition.bottomRight)&&
                Objects.equals(this.type, deckPosition.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(topLeft, bottomRight,type);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class DeckPosition {\n");

        sb.append("    topLeft: ").append(toIndentedString(topLeft)).append("\n");
        sb.append("    bottomRight: ").append(toIndentedString(bottomRight)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
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
