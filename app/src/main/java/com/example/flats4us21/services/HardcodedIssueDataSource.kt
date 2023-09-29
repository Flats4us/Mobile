package com.example.flats4us21.services

import com.example.flats4us21.data.Issue

object HardcodedIssueDataSource: IssueDataSource {
    private val issues : MutableList<Issue> = mutableListOf()

    override fun addIssue(issue: Issue) {
        issues.add(issue)
    }


}