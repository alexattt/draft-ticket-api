package com.alexattt.ticketpriceapi.models;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * Represents a passenger in the ticket pricing system.
 * Contains personal information and luggage details needed for price calculation.
 */
@Schema(description = "Passenger information for ticket pricing calculation")
public class Passenger {

    /**
     * Full name of the passenger as it appears on travel documents
     */
    @Schema(description = "Passenger's full name as it appears on travel documents",
            example = "John Doe", required = true, minLength = 1, maxLength = 100)
    private String name;

    /**
     * Age of the passenger in years, used for determining fare categories
     */
    @Schema(description = "Passenger age in years (used for fare category determination)",
            example = "35", required = true, minimum = "0", maximum = "120")
    private int age;

    /**
     * List of luggage items associated with this passenger
     */
    @Schema(description = "List of luggage items belonging to this passenger")
    private List<LuggageItem> luggage;

    /**
     * Default constructor for JSON deserialization
     */
    public Passenger() {}

    /**
     * Constructor to create a passenger with all required information
     *
     * @param name    The passenger's full name
     * @param age     The passenger's age in years
     * @param luggage List of luggage items for this passenger
     */
    public Passenger(String name, int age, List<LuggageItem> luggage) {
        this.name = name;
        this.age = age;
        this.luggage = luggage;
    }

    /**
     * Gets the passenger's name
     *
     * @return The passenger's full name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the passenger's name
     *
     * @param name The passenger's full name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the passenger's age
     *
     * @return The passenger's age in years
     */
    public int getAge() {
        return age;
    }

    /**
     * Sets the passenger's age
     *
     * @param age The passenger's age in years
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Gets the list of luggage items for this passenger
     *
     * @return List of luggage items, may be null or empty
     */
    public List<LuggageItem> getLuggage() {
        return luggage;
    }

    /**
     * Sets the luggage items for this passenger
     *
     * @param luggage List of luggage items
     */
    public void setLuggage(List<LuggageItem> luggage) {
        this.luggage = luggage;
    }

    /**
     * Determines if the passenger is considered a child based on age
     * Used for applying child fare discounts and special handling requirements
     *
     * @return true if passenger is under 18 years old, false otherwise
     */
    @Schema(description = "Indicates whether the passenger is classified as a child (under 18)",
            example = "false")
    public boolean isChild() {
        return age < 18;
    }
}