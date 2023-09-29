package com.example.flats4us21.viewmodels

import androidx.lifecycle.ViewModel
import com.example.flats4us21.data.Issue
import com.example.flats4us21.services.HardcodedIssueDataSource
import com.example.flats4us21.services.IssueDataSource

class ITIssueReportViewModel: ViewModel() {
    private val issueRepository : IssueDataSource = HardcodedIssueDataSource

    private var _issue: String = ""
    var issue: String
    get() = _issue
    set(value) {
        _issue = value
    }

    private var _description: String = ""
    var description: String
    get() = _description
    set(value) {
        _description = value
    }

    fun createIssue(){
        val issue = Issue(
            issue,
            description
        )
        issueRepository.addIssue(issue)
    }
}