package com.example.flats4us.services

import com.example.flats4us.data.Issue

interface IssueDataSource {
    fun addIssue(issue: Issue)
}