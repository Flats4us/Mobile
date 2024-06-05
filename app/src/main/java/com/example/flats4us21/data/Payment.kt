package com.example.flats4us21.data

data class Payment(
    val paymentId: Int,
    val paymentPurpose: Int,
    val amount: Int,
    val isPaid: Boolean,
    val createdDate: String,
    val paymentDate: String?
){
    val paymentType: PaymentType
        get() = PaymentType.fromInt(paymentPurpose) ?: PaymentType.CZYNSZ
}
