package com.example.docexpiry

import androidx.annotation.DrawableRes
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.ShapeAppearanceModel

// Helper object to manage per-card-type visual properties
object CardTypeVisuals {
    data class VisualTemplate(
        @DrawableRes val backgroundRes: Int?,
        @DrawableRes val logoRes: Int?,
        val cornerRadius: Float = 8f,
        val isCircular: Boolean = false
    )

    private val templates = mapOf(
        "Aadhaar" to VisualTemplate(
            backgroundRes = R.drawable.bg_aadhaar,
            logoRes = R.drawable.logo_aadhaar,
            cornerRadius = 999f,
            isCircular = true
        ),
        "PAN" to VisualTemplate(
            backgroundRes = R.drawable.bg_pan,
            logoRes = R.drawable.logo_pan,
            cornerRadius = 12f,
            isCircular = false
        ),
        "Voter ID" to VisualTemplate(
            backgroundRes = R.drawable.bg_voter_id,
            logoRes = R.drawable.logo_voter_id,
            cornerRadius = 12f,
            isCircular = false
        ),
        "Driving License" to VisualTemplate(
            backgroundRes = R.drawable.bg_driving_license,
            logoRes = R.drawable.logo_driving_license,
            cornerRadius = 12f,
            isCircular = false
        ),
        "Passport" to VisualTemplate(
            backgroundRes = R.drawable.bg_passport,
            logoRes = R.drawable.logo_passport,
            cornerRadius = 999f,
            isCircular = true
        ),
        "Birth Certificate" to VisualTemplate(
            backgroundRes = R.drawable.bg_birth_certificate,
            logoRes = R.drawable.logo_birth_certificate,
            cornerRadius = 12f,
            isCircular = false
        ),
        "Ration Card" to VisualTemplate(
            backgroundRes = R.drawable.bg_ration_card,
            logoRes = R.drawable.logo_ration_card,
            cornerRadius = 12f,
            isCircular = false
        ),
        "Academic Certificate" to VisualTemplate(
            backgroundRes = R.drawable.bg_academic_certificate,
            logoRes = R.drawable.logo_academic_certificate,
            cornerRadius = 12f,
            isCircular = false
        )
    )

    fun getTemplate(cardType: String): VisualTemplate =
        templates[cardType] ?: VisualTemplate(null, null, 8f, false)

    fun getShapeAppearanceModel(cardType: String): ShapeAppearanceModel {
        val template = getTemplate(cardType)
        return ShapeAppearanceModel.builder()
            .setAllCorners(CornerFamily.ROUNDED, template.cornerRadius)
            .build()
    }
}

