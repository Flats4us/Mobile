package com.example.flats4us.services

import com.example.flats4us.data.Issue

object HardcodedIssueDataSource: IssueDataSource {
    private val issues : MutableList<Issue> = mutableListOf()

    override fun addIssue(issue: Issue) {
        issues.add(issue)
    }


}