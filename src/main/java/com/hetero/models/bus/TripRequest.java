package com.hetero.models.bus;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;


public class TripRequest {

        @JsonProperty("source_id")
        private int sourceId;

        @JsonProperty("destination_id")
        private int destinationId;

        @JsonProperty("date_of_journey")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private Date dateOfJourney;

        // Default constructor (required for Jackson)
        public TripRequest() {
        }

        // Getters and Setters
        public int getSourceId() {
                return sourceId;
        }

        public void setSourceId(int sourceId) {
                this.sourceId = sourceId;
        }

        public int getDestinationId() {
                return destinationId;
        }

        public void setDestinationId(int destinationId) {
                this.destinationId = destinationId;
        }

        public Date getDateOfJourney() {
                return dateOfJourney;
        }

        public void setDateOfJourney(Date dateOfJourney) {
                this.dateOfJourney = dateOfJourney;
        }

        @Override
        public String toString() {
                return "TripRequest{" +
                        "sourceId=" + sourceId +
                        ", destinationId=" + destinationId +
                        ", dateOfJourney='" + dateOfJourney + '\'' +
                        '}';
        }
}
