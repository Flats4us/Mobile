package com.example.flats4us21.data

data class UserOpinion(
    val rating: Int,
    val helpful: Boolean,
    val cooperative: Boolean,
    val tidy: Boolean,
    val friendly: Boolean,
    val respectingPrivacy: Boolean,
    val communicative: Boolean,
    val unfair: Boolean,
    val lackOfHygiene: Boolean,
    val untidy: Boolean,
    val conflicting: Boolean,
    val noisy: Boolean,
    val notFollowingTheArrangements: Boolean,
    val description: String
)
