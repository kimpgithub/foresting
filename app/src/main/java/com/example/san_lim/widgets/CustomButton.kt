package com.example.san_lim.widgets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.san_lim.ui.theme.ColorPalette

@Composable
fun RegionSelectButton(
    region: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    CustomButton(
        onClick = onClick,
        enabled = true,
        isSelected = isSelected,
        text = region,
        modifier = modifier
    )
}

@Composable
fun AutoRecommendButton(
    onClick: () -> Unit,
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    CustomButton(
        onClick = onClick,
        enabled = isSelected,
        isSelected = isSelected,
        text = "자동 추천",
        modifier = modifier
    )
}

@Composable
fun CustomButton(
    onClick: () -> Unit,
    enabled: Boolean,
    isSelected: Boolean,
    text: String,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) ColorPalette.earthyDarkMoss else ColorPalette.softWhite
    val contentColor = if (isSelected) ColorPalette.softWhite else ColorPalette.earthyDarkMoss
    val borderColor = ColorPalette.earthyDarkMoss

    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.height(48.dp),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, borderColor),
        colors = ButtonDefaults.outlinedButtonColors(
            backgroundColor = backgroundColor,
            contentColor = contentColor,
            disabledContentColor = ColorPalette.lightCharcoal
        )
    ) {
        Text(
            text = text,
            fontSize = 16.sp
        )
    }
}