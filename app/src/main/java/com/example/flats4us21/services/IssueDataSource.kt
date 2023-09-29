package com.example.flats4us21.services

import com.example.flats4us21.data.Issue

interface IssueDataSource {
    fun addIssue(issue: Issue)
}