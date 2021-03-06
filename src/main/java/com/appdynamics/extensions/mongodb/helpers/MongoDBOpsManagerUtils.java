/*
 * Copyright 2018. AppDynamics LLC and its affiliates.
 * All Rights Reserved.
 * This is unpublished proprietary source code of AppDynamics LLC and its affiliates.
 * The copyright notice above does not evidence any actual or intended publication of such source code.
 *
 */

package com.appdynamics.extensions.mongodb.helpers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class MongoDBOpsManagerUtils {
    private static final Logger logger = LoggerFactory.getLogger(MongoDBOpsManagerUtils.class);

    public static JsonNode getJsonNode(CloseableHttpResponse response) throws IOException {
        String data = EntityUtils.toString(response.getEntity(), "UTF-8");
        return getJsonNode(data);
    }

    private static JsonNode getJsonNode(String data) throws IOException {
        if (data == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(data, JsonNode.class);
    }

    public static BigDecimal convertDoubleToBigDecimal(Double value) {
        return new BigDecimal(Math.round(value));
    }

    public static boolean isValidationSuccessful(List<Map> configuredMetrics, List<JsonNode> mongoEntitiesInApplication, String entityType) {
        if (configuredMetrics == null || configuredMetrics.isEmpty()) {
            logger.debug("No metrics have been configured for : " + entityType + " in your config.yml.");
            return false;
        } else if (mongoEntitiesInApplication == null || mongoEntitiesInApplication.isEmpty()) {
            logger.debug("No " + entityType + " found for the current Mongo Deployment");
            return false;
        }
        return true;
    }

    public static List<JsonNode> getMeasurementsOnlyForCurrentMetricType(String metricType, List<JsonNode> allMeasurements) {
        List<JsonNode> measurementsForCurrentMetricType = Lists.newArrayList();
        for (JsonNode node : allMeasurements) {
            if (node.findValue("name").asText().toLowerCase().contains(metricType.toLowerCase())) {
                measurementsForCurrentMetricType.add(node);
            }
        }
        return measurementsForCurrentMetricType;
    }
}

