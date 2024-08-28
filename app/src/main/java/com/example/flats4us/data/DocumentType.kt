package com.example.flats4us.data

import android.content.Context
import com.example.flats4us.R

enum class DocumentType(val value: Int) {
    ID(0),
    StudentCard(1),
    Passport(2);

    fun toLocalizedString(context: Context): String {
        return when (this) {
            DocumentType.ID -> context.getString(R.string.document_id)
            DocumentType.StudentCard -> context.getString(R.string.document_student_card)
            DocumentType.Passport -> context.getString(R.string.document_passport)
        }
    }

    override fun toString(): String {
        return value.toString()
    }

    companion object {
        fun fromValue(value: Int): DocumentType {
            return DocumentType.values().firstOrNull { it.value == value }
                ?: throw IllegalArgumentException("No enum constant with value $value")
        }

        fun fromLocalizedString(context: Context, localizedString: String): DocumentType {
            return when (localizedString) {
                context.getString(R.string.document_id) -> ID
                context.getString(R.string.document_student_card) -> StudentCard
                context.getString(R.string.document_passport) -> Passport
                else -> throw IllegalArgumentException("No enum constant with localized name $localizedString")
            }
        }

        fun getStudentDocuments(): Array<DocumentType> {
            return arrayOf(StudentCard)
        }

        fun getOwnerDocuments(): Array<DocumentType> {
            return arrayOf(ID, Passport)
        }
    }
}
