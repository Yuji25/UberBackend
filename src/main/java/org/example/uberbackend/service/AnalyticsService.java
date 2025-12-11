package org.example.uberbackend.service;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AnalyticsService {

    @Autowired
    private MongoTemplate mongoTemplate;

    // API 1: Total earnings for a driver
    public Double getTotalEarnings(String driverUsername) {
        MatchOperation match = Aggregation.match(
            Criteria.where("driverUsername").is(driverUsername)
                .and("status").is("COMPLETED")
        );

        GroupOperation group = Aggregation.group()
            .sum("fare").as("total");

        Aggregation aggregation = Aggregation.newAggregation(match, group);

        Document result = mongoTemplate.aggregate(
            aggregation, "rides", Document.class
        ).getUniqueMappedResult();

        return result != null ? result.getDouble("total") : 0.0;
    }

    // API 2: Rides per day (GROUP BY createdDate)
    public List<Document> getRidesPerDay() {
        GroupOperation group = Aggregation.group("createdDate")
            .count().as("count");

        ProjectionOperation project = Aggregation.project()
            .and("_id").as("date")
            .and("count").as("ridesCount")
            .andExclude("_id");

        SortOperation sort = Aggregation.sort(Sort.Direction.DESC, "date");

        Aggregation aggregation = Aggregation.newAggregation(group, project, sort);

        return mongoTemplate.aggregate(
            aggregation, "rides", Document.class
        ).getMappedResults();
    }

    // API 3: Driver summary (total rides, completed, avg distance, total fare)
    public Document getDriverSummary(String driverUsername) {
        MatchOperation match = Aggregation.match(
            Criteria.where("driverUsername").is(driverUsername)
        );

        GroupOperation group = Aggregation.group()
            .count().as("totalRides")
            .sum(ConditionalOperators.when(
                Criteria.where("status").is("COMPLETED")
            ).then(1).otherwise(0)).as("completedRides")
            .avg("distanceKm").as("avgDistance")
            .sum("fare").as("totalFare");

        Aggregation aggregation = Aggregation.newAggregation(match, group);

        Document result = mongoTemplate.aggregate(
            aggregation, "rides", Document.class
        ).getUniqueMappedResult();

        return result != null ? result : new Document();
    }

    // API 4: User spending (total completed rides + total fare paid)
    public Document getUserSpending(String passengerUsername) {
        MatchOperation match = Aggregation.match(
            Criteria.where("passengerUsername").is(passengerUsername)
                .and("status").is("COMPLETED")
        );

        GroupOperation group = Aggregation.group()
            .count().as("totalCompletedRides")
            .sum("fare").as("totalSpent");

        Aggregation aggregation = Aggregation.newAggregation(match, group);

        Document result = mongoTemplate.aggregate(
            aggregation, "rides", Document.class
        ).getUniqueMappedResult();

        return result != null ? result : new Document();
    }

    // API 5: Status summary (count by status)
    public List<Document> getStatusSummary() {
        GroupOperation group = Aggregation.group("status")
            .count().as("count");

        ProjectionOperation project = Aggregation.project()
            .and("_id").as("status")
            .and("count").as("ridesCount")
            .andExclude("_id");

        Aggregation aggregation = Aggregation.newAggregation(group, project);

        return mongoTemplate.aggregate(
            aggregation, "rides", Document.class
        ).getMappedResults();
    }
}

