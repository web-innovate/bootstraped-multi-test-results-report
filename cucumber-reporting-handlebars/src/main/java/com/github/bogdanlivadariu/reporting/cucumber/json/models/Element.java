package com.github.bogdanlivadariu.reporting.cucumber.json.models;

import static com.github.bogdanlivadariu.reporting.cucumber.helpers.Constants.FAILED;
import static com.github.bogdanlivadariu.reporting.cucumber.helpers.Constants.PASSED;
import static com.github.bogdanlivadariu.reporting.cucumber.helpers.Constants.SKIPPED;
import static com.github.bogdanlivadariu.reporting.cucumber.helpers.Constants.UNDEFINED;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.github.bogdanlivadariu.reporting.cucumber.helpers.SpecialProperties;
import com.github.bogdanlivadariu.reporting.cucumber.helpers.SpecialProperties.SpecialKeyProperties;

/**
 * Represents a Scenario
 */
public class Element {

    private String name;

    private String id;

    private int line;

    private String keyword;

    private String description;

    private String type;

    private Tag[] tags;

    private Step[] steps;

    private long totalDuration;

    private String overallStatus = PASSED;

    private int stepsPassedCount;

    private int stepsFailedCount;

    private int stepsSkippedCount;

    private int stepsUndefinedCount;

    private String uniqueID;

    private List<Embedding> embeddings = new ArrayList<>();

    public void postProcess(SpecialProperties props) {
        uniqueID = UUID.randomUUID().toString();
        List<String> stepStatuses = new ArrayList<>();
        if (steps != null) {

            for (Step step : steps) {
                totalDuration += step.getResult().getDuration();
                String actualResultStatus = step.getResult().getStatus();
                stepStatuses.add(step.getResult().getStatus());

                Embedding[] embeddings = step.getEmbeddings();
                if (embeddings != null) {
                    this.embeddings.addAll(Arrays.asList(embeddings));
                }

                switch (actualResultStatus) {
                    case PASSED:
                        stepsPassedCount++;
                        break;
                    case FAILED:
                        stepsFailedCount++;
                        break;
                    case SKIPPED:
                        stepsSkippedCount++;
                        break;
                    case UNDEFINED:
                        stepsUndefinedCount++;
                        break;
                    default:
                        break;
                }
            }
        }
        if (stepStatuses.contains(UNDEFINED)) {
            boolean ignoreUndefinedSteps =
                props.getPropertyValue(SpecialKeyProperties.IGNORE_UNDEFINED_STEPS);
            overallStatus = ignoreUndefinedSteps ? PASSED : FAILED;
        }
        if (stepStatuses.contains(FAILED) ||
            stepStatuses.contains(SKIPPED)) {
            overallStatus = FAILED;
        }
    }

    public String getOverallStatus() {
        return overallStatus;
    }

    public long getTotalDuration() {
        return totalDuration;
    }

    public List<Embedding> getEmbeddings() {
        return embeddings;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public int getLine() {
        return line;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public Tag[] getTags() {
        return tags;
    }

    public Step[] getSteps() {
        return steps;
    }

    public int getStepsSkippedCount() {
        return stepsSkippedCount;
    }

    public int getStepsUndefinedCount() {
        return stepsUndefinedCount;
    }

    public int getStepsFailedCount() {
        return stepsFailedCount;
    }

    public int getStepsPassedCount() {
        return stepsPassedCount;
    }

    public int getStepsTotalCount() {
        int stepTotalCount = 0;
        try {
            stepTotalCount = steps.length;
        } catch (Exception e) {
            stepTotalCount = 0;
        }
        return stepTotalCount;
    }

    public String getUniqueID() {
        return uniqueID;
    }

}
