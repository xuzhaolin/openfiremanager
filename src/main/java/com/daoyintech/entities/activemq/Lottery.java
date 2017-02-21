package com.daoyintech.entities.activemq;

import com.daoyintech.entities.PlayType;

import java.io.Serializable;

/**
 * Created by xuzhaolin on 2017/2/16.
 */

public class Lottery implements Serializable{
    private String issue;
    private PlayType playType;
    private String resultAt;
    private Integer resultNumber;
    private String resultCodes;
    private String resultSourceCodes;

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public PlayType getPlayType() {
        return playType;
    }

    public void setPlayType(PlayType playType) {
        this.playType = playType;
    }

    public String getResultAt() {
        return resultAt;
    }

    public void setResultAt(String resultAt) {
        this.resultAt = resultAt;
    }

    public Integer getResultNumber() {
        return resultNumber;
    }

    public void setResultNumber(Integer resultNumber) {
        this.resultNumber = resultNumber;
    }

    public String getResultCodes() {
        return resultCodes;
    }

    public void setResultCodes(String resultCodes) {
        this.resultCodes = resultCodes;
    }

    public String getResultSourceCodes() {
        return resultSourceCodes;
    }

    public void setResultSourceCodes(String resultSourceCodes) {
        this.resultSourceCodes = resultSourceCodes;
    }

}
