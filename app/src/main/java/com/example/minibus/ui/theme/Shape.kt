package com.example.minibus.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes

import androidx.compose.ui.unit.dp


val Shapes = Shapes(
    small = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
    extraSmall = RoundedCornerShape(bottomStart = 16.dp, topStart = 16.dp ),
    extraLarge = RoundedCornerShape(bottomEnd = 16.dp, topEnd = 16.dp ),
    large = RoundedCornerShape(16.dp),
    medium = RoundedCornerShape(8.dp)
)


