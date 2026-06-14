package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProductVisualDrawing(index: Int, category: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFF0F4F8),
                        Color(0xFFE2EAF4)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val cx = w / 2
            val cy = h / 2

            when (index) {
                1 -> { // Nordic Wool Trench Coat
                    // Draw outer coat body
                    val coatPath = Path().apply {
                        moveTo(cx - 25.dp.toPx(), cy - 40.dp.toPx())
                        lineTo(cx + 25.dp.toPx(), cy - 40.dp.toPx())
                        lineTo(cx + 35.dp.toPx(), cy + 50.dp.toPx())
                        lineTo(cx - 35.dp.toPx(), cy + 50.dp.toPx())
                        close()
                    }
                    drawPath(coatPath, Color(0xFF32363D))

                    // Collar lines
                    val collarLeft = Path().apply {
                        moveTo(cx, cy - 40.dp.toPx())
                        lineTo(cx - 20.dp.toPx(), cy - 15.dp.toPx())
                        lineTo(cx - 5.dp.toPx(), cy - 10.dp.toPx())
                        close()
                    }
                    drawPath(collarLeft, Color(0xFF4A4E57))

                    val collarRight = Path().apply {
                        moveTo(cx, cy - 40.dp.toPx())
                        lineTo(cx + 20.dp.toPx(), cy - 15.dp.toPx())
                        lineTo(cx + 5.dp.toPx(), cy - 10.dp.toPx())
                        close()
                    }
                    drawPath(collarRight, Color(0xFF4A4E57))

                    // Buttons
                    drawCircle(Color(0xFFD4AF37), 3.dp.toPx(), Offset(cx - 8.dp.toPx(), cy))
                    drawCircle(Color(0xFFD4AF37), 3.dp.toPx(), Offset(cx + 8.dp.toPx(), cy))
                    drawCircle(Color(0xFFD4AF37), 3.dp.toPx(), Offset(cx - 8.dp.toPx(), cy + 20.dp.toPx()))
                    drawCircle(Color(0xFFD4AF37), 3.dp.toPx(), Offset(cx + 8.dp.toPx(), cy + 20.dp.toPx()))
                }
                2 -> { // Noise Buds
                    // Charging pod case
                    drawRoundRect(
                        color = Color(0xFF1E2022),
                        topLeft = Offset(cx - 25.dp.toPx(), cy - 25.dp.toPx()),
                        size = Size(50.dp.toPx(), 50.dp.toPx()),
                        cornerRadius = CornerRadius(16.dp.toPx(), 16.dp.toPx())
                    )
                    // Metallic glowing ring
                    drawCircle(
                        color = Color(0xFF4A90E2),
                        radius = 16.dp.toPx(),
                        center = Offset(cx, cy),
                        style = Stroke(width = 2.dp.toPx())
                    )
                    // Indicator led pulse
                    drawCircle(
                        color = Color(0xFF67C23A),
                        radius = 3.dp.toPx(),
                        center = Offset(cx, cy + 16.dp.toPx())
                    )
                }
                3 -> { // Knit Sneakers
                    // Draw trainer sneaker sole
                    drawRoundRect(
                        color = Color.White,
                        topLeft = Offset(cx - 35.dp.toPx(), cy + 12.dp.toPx()),
                        size = Size(70.dp.toPx(), 12.dp.toPx()),
                        cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
                    )
                    // Sneaker body
                    val footPath = Path().apply {
                        moveTo(cx - 32.dp.toPx(), cy + 12.dp.toPx())
                        lineTo(cx + 32.dp.toPx(), cy + 12.dp.toPx())
                        lineTo(cx + 25.dp.toPx(), cy - 20.dp.toPx())
                        lineTo(cx - 10.dp.toPx(), cy - 18.dp.toPx())
                        lineTo(cx - 30.dp.toPx(), cy + 4.dp.toPx())
                        close()
                    }
                    drawPath(footPath, Color(0xFF6C8EBF))

                    // Lacing detail paths
                    drawLine(Color.White, Offset(cx - 5.dp.toPx(), cy - 5.dp.toPx()), Offset(cx + 10.dp.toPx(), cy - 12.dp.toPx()), strokeWidth = 2.dp.toPx())
                    drawLine(Color.White, Offset(cx - 8.dp.toPx(), cy + 1.dp.toPx()), Offset(cx + 6.dp.toPx(), cy - 6.dp.toPx()), strokeWidth = 2.dp.toPx())
                }
                4 -> { // Modern Minimalist Leather Watch
                    // Strap
                    drawRoundRect(
                        color = Color(0xFF8E523A),
                        topLeft = Offset(cx - 8.dp.toPx(), cy - 45.dp.toPx()),
                        size = Size(16.dp.toPx(), 90.dp.toPx()),
                        cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
                    )
                    // Outer Case Circle
                    drawCircle(Color(0xFFD4AF37), 30.dp.toPx(), Offset(cx, cy))
                    // Dial Face
                    drawCircle(Color.White, 26.dp.toPx(), Offset(cx, cy))
                    // Pointer Hands
                    drawLine(Color.Black, Offset(cx, cy), Offset(cx, cy - 18.dp.toPx()), strokeWidth = 2.dp.toPx())
                    drawLine(Color.Black, Offset(cx, cy), Offset(cx + 12.dp.toPx(), cy + 5.dp.toPx()), strokeWidth = 1.5.dp.toPx())
                    // Ticks
                    drawCircle(Color.Gray, 1.5.dp.toPx(), Offset(cx, cy - 22.dp.toPx()))
                    drawCircle(Color.Gray, 1.5.dp.toPx(), Offset(cx, cy + 22.dp.toPx()))
                    drawCircle(Color.Gray, 1.5.dp.toPx(), Offset(cx - 22.dp.toPx(), cy))
                    drawCircle(Color.Gray, 1.5.dp.toPx(), Offset(cx + 22.dp.toPx(), cy))
                }
                5 -> { // Terracotta Vase
                    // Vase body
                    val vasePath = Path().apply {
                        moveTo(cx - 10.dp.toPx(), cy - 30.dp.toPx())
                        lineTo(cx + 10.dp.toPx(), cy - 30.dp.toPx())
                        lineTo(cx + 22.dp.toPx(), cy + 5.dp.toPx())
                        lineTo(cx + 16.dp.toPx(), cy + 30.dp.toPx())
                        lineTo(cx - 16.dp.toPx(), cy + 30.dp.toPx())
                        lineTo(cx - 22.dp.toPx(), cy + 5.dp.toPx())
                        close()
                    }
                    drawPath(vasePath, Color(0xFFD67D65))

                    // Rib lines inside pot
                    for (i in -2..2) {
                        val dx = i * 6.dp.toPx()
                        drawLine(Color(0x3B000000), Offset(cx + dx, cy - 15.dp.toPx()), Offset(cx + dx, cy + 20.dp.toPx()), strokeWidth = 1.5.dp.toPx())
                    }

                    // Green organic grass peaking out
                    drawLine(Color(0xFF6B8672), Offset(cx, cy - 30.dp.toPx()), Offset(cx - 15.dp.toPx(), cy - 50.dp.toPx()), strokeWidth = 2.dp.toPx())
                    drawLine(Color(0xFF6B8672), Offset(cx, cy - 30.dp.toPx()), Offset(cx + 18.dp.toPx(), cy - 48.dp.toPx()), strokeWidth = 2.dp.toPx())
                    drawLine(Color(0xFF536E59), Offset(cx, cy - 30.dp.toPx()), Offset(cx - 2.dp.toPx(), cy - 54.dp.toPx()), strokeWidth = 2.dp.toPx())
                }
                6 -> { // T-Shirt Hanger
                    // Hanger wire
                    val hanger = Path().apply {
                        moveTo(cx, cy - 32.dp.toPx())
                        quadraticTo(cx - 15.dp.toPx(), cy - 18.dp.toPx(), cx - 35.dp.toPx(), cy - 12.dp.toPx())
                        lineTo(cx + 35.dp.toPx(), cy - 12.dp.toPx())
                        quadraticTo(cx + 15.dp.toPx(), cy - 18.dp.toPx(), cx, cy - 32.dp.toPx())
                    }
                    drawPath(hanger, Color(0xFF9E846A), style = Stroke(width = 2.dp.toPx()))

                    // Tee Body
                    val tee = Path().apply {
                        moveTo(cx - 35.dp.toPx(), cy - 12.dp.toPx())
                        lineTo(cx - 20.dp.toPx(), cy - 22.dp.toPx())
                        lineTo(cx + 20.dp.toPx(), cy - 22.dp.toPx())
                        lineTo(cx + 35.dp.toPx(), cy - 12.dp.toPx())
                        lineTo(cx + 45.dp.toPx(), cy + 2.dp.toPx())
                        lineTo(cx + 30.dp.toPx(), cy + 8.dp.toPx())
                        lineTo(cx + 28.dp.toPx(), cy + 45.dp.toPx())
                        lineTo(cx - 28.dp.toPx(), cy + 45.dp.toPx())
                        lineTo(cx - 30.dp.toPx(), cy + 8.dp.toPx())
                        lineTo(cx - 45.dp.toPx(), cy + 2.dp.toPx())
                        close()
                    }
                    drawPath(tee, Color(0xFF4C5CC2))
                }
                7 -> { // Slim Charger Dock
                    // Slim plate
                    drawRoundRect(
                        color = Color(0xFFA5B4C5),
                        topLeft = Offset(cx - 35.dp.toPx(), cy + 15.dp.toPx()),
                        size = Size(70.dp.toPx(), 8.dp.toPx()),
                        cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
                    )
                    // Standing charging phone box in slant
                    val phoneBox = Path().apply {
                        moveTo(cx - 20.dp.toPx(), cy + 15.dp.toPx())
                        lineTo(cx + 20.dp.toPx(), cy + 15.dp.toPx())
                        lineTo(cx + 15.dp.toPx(), cy - 25.dp.toPx())
                        lineTo(cx - 25.dp.toPx(), cy - 25.dp.toPx())
                        close()
                    }
                    drawPath(phoneBox, Color(0xFF1F2228))
                    // Screen glow indicator
                    drawRoundRect(
                        color = Color(0x7685DFFF),
                        topLeft = Offset(cx - 22.dp.toPx(), cy - 21.dp.toPx()),
                        size = Size(38.dp.toPx(), 32.dp.toPx()),
                        cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx())
                    )
                }
                8 -> { // Leather Loafers Shoes
                    // Sole block
                    drawRoundRect(
                        color = Color(0xFF151718),
                        topLeft = Offset(cx - 35.dp.toPx(), cy + 14.dp.toPx()),
                        size = Size(70.dp.toPx(), 14.dp.toPx()),
                        cornerRadius = CornerRadius(5.dp.toPx(), 5.dp.toPx())
                    )
                    // Loafer leather body
                    val shoe = Path().apply {
                        moveTo(cx - 32.dp.toPx(), cy + 14.dp.toPx())
                        lineTo(cx + 32.dp.toPx(), cy + 14.dp.toPx())
                        lineTo(cx + 20.dp.toPx(), cy - 14.dp.toPx())
                        lineTo(cx - 15.dp.toPx(), cy - 10.dp.toPx())
                        lineTo(cx - 32.dp.toPx(), cy + 6.dp.toPx())
                        close()
                    }
                    drawPath(shoe, Color(0xFF332014)) // Rich walnut brown
                    // Metal emblem ring across tongue
                    drawLine(Color(0xFFD4AF37), Offset(cx - 2.dp.toPx(), cy - 3.dp.toPx()), Offset(cx + 12.dp.toPx(), cy - 5.dp.toPx()), strokeWidth = 3.dp.toPx())
                }
                9 -> { // Vegan Leather Satchel
                    // Shoulder strap
                    drawLine(Color(0xFF5A3E25), Offset(cx - 30.dp.toPx(), cy - 45.dp.toPx()), Offset(cx - 22.dp.toPx(), cy), strokeWidth = 2.dp.toPx())
                    drawLine(Color(0xFF5A3E25), Offset(cx + 30.dp.toPx(), cy - 45.dp.toPx()), Offset(cx + 22.dp.toPx(), cy), strokeWidth = 2.dp.toPx())

                    // Satchel main flap bag
                    drawRoundRect(
                        color = Color(0xFF8B5A2B), // Cognac Tan Leather
                        topLeft = Offset(cx - 25.dp.toPx(), cy - 12.dp.toPx()),
                        size = Size(50.dp.toPx(), 40.dp.toPx()),
                        cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx())
                    )
                    // Forward hanging pouch flap envelope
                    val flap = Path().apply {
                        moveTo(cx - 25.dp.toPx(), cy - 12.dp.toPx())
                        lineTo(cx + 25.dp.toPx(), cy - 12.dp.toPx())
                        lineTo(cx + 25.dp.toPx(), cy + 10.dp.toPx())
                        lineTo(cx, cy + 22.dp.toPx())
                        lineTo(cx - 25.dp.toPx(), cy + 10.dp.toPx())
                        close()
                    }
                    drawPath(flap, Color(0xFF754C24))

                    // Solid gold clasp locker
                    drawRoundRect(
                        color = Color(0xFFD4AF37),
                        topLeft = Offset(cx - 4.dp.toPx(), cy + 16.dp.toPx()),
                        size = Size(8.dp.toPx(), 10.dp.toPx()),
                        cornerRadius = CornerRadius(1.dp.toPx(), 1.dp.toPx())
                    )
                }
                10 -> { // Brushed Gold Table Lamp
                    // Lamp base plate
                    drawRoundRect(
                        color = Color(0xFFC59F3F),
                        topLeft = Offset(cx - 25.dp.toPx(), cy + 30.dp.toPx()),
                        size = Size(50.dp.toPx(), 5.dp.toPx()),
                        cornerRadius = CornerRadius(1.dp.toPx(), 1.dp.toPx())
                    )
                    // Tall bent brass wire stems
                    val post = Path().apply {
                        moveTo(cx, cy + 30.dp.toPx())
                        lineTo(cx, cy - 20.dp.toPx())
                        quadraticTo(cx, cy - 35.dp.toPx(), cx - 18.dp.toPx(), cy - 35.dp.toPx())
                    }
                    drawPath(post, Color(0xFFD4AF37), style = Stroke(width = 3.dp.toPx()))

                    // Shading conoid lamp light shade
                    val hood = Path().apply {
                        moveTo(cx - 28.dp.toPx(), cy - 25.dp.toPx())
                        lineTo(cx - 8.dp.toPx(), cy - 25.dp.toPx())
                        lineTo(cx - 4.dp.toPx(), cy - 40.dp.toPx())
                        lineTo(cx - 32.dp.toPx(), cy - 40.dp.toPx())
                        close()
                    }
                    drawPath(hood, Color(0xFFC59F3F))

                    // Glowing warmth ambient gradient cone
                    val beam = Path().apply {
                        moveTo(cx - 28.dp.toPx(), cy - 25.dp.toPx())
                        lineTo(cx - 8.dp.toPx(), cy - 25.dp.toPx())
                        lineTo(cx + 8.dp.toPx(), cy + 28.dp.toPx())
                        lineTo(cx - 44.dp.toPx(), cy + 28.dp.toPx())
                        close()
                    }
                    drawPath(beam, Color(0x3BFFF2A1))
                }
                11 -> { // Smart Monitor LED Ambient Bar
                    // Desktop stand bottom
                    drawLine(Color(0xFF2D3035), Offset(cx - 15.dp.toPx(), cy + 25.dp.toPx()), Offset(cx + 15.dp.toPx(), cy + 25.dp.toPx()), strokeWidth = 3.dp.toPx())
                    drawLine(Color(0xFF2D3035), Offset(cx, cy + 25.dp.toPx()), Offset(cx, cy - 20.dp.toPx()), strokeWidth = 5.dp.toPx())

                    // Elegant wide bezel monitor line
                    drawRoundRect(
                        color = Color(0xFF0F1012),
                        topLeft = Offset(cx - 40.dp.toPx(), cy - 10.dp.toPx()),
                        size = Size(80.dp.toPx(), 30.dp.toPx()),
                        cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
                    )

                    // Top light bar
                    drawRoundRect(
                        color = Color(0xFFE2E4E6),
                        topLeft = Offset(cx - 30.dp.toPx(), cy - 25.dp.toPx()),
                        size = Size(60.dp.toPx(), 6.dp.toPx()),
                        cornerRadius = CornerRadius(1.dp.toPx(), 1.dp.toPx())
                    )

                    // LED colored glow reflecting softly above and below
                    drawCircle(Color(0x3BC65DFE), 22.dp.toPx(), Offset(cx, cy - 35.dp.toPx()))
                }
                12 -> { // Double-Walled Travel Thermos
                    // Tall thermal flask body cylinder
                    drawRoundRect(
                        color = Color(0xFF55695C), // Sage structural powder tone
                        topLeft = Offset(cx - 15.dp.toPx(), cy - 35.dp.toPx()),
                        size = Size(30.dp.toPx(), 70.dp.toPx()),
                        cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
                    )
                    // Silver thermal steel collar rim
                    drawRoundRect(
                        color = Color(0xFFB0B7BC),
                        topLeft = Offset(cx - 11.dp.toPx(), cy - 42.dp.toPx()),
                        size = Size(22.dp.toPx(), 7.dp.toPx()),
                        cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx())
                    )
                    // Dark screw lid cap
                    drawRoundRect(
                        color = Color(0xFF232527),
                        topLeft = Offset(cx - 8.dp.toPx(), cy - 48.dp.toPx()),
                        size = Size(16.dp.toPx(), 6.dp.toPx()),
                        cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx())
                    )
                    // Sturdy steel loop carry handle
                    drawCircle(
                        color = Color(0xFF232527),
                        radius = 8.dp.toPx(),
                        center = Offset(cx + 8.dp.toPx(), cy - 42.dp.toPx()),
                        style = Stroke(width = 2.dp.toPx())
                    )
                }
                else -> {
                    // Default fallback: beautiful placeholder e-commerce box vector shopping bag
                    drawRoundRect(
                        color = Color(0xFF1E2022),
                        topLeft = Offset(cx - 20.dp.toPx(), cy - 15.dp.toPx()),
                        size = Size(40.dp.toPx(), 45.dp.toPx()),
                        cornerRadius = CornerRadius(5.dp.toPx(), 5.dp.toPx())
                    )
                    // Bag arch handle
                    val handle = Path().apply {
                        moveTo(cx - 10.dp.toPx(), cy - 15.dp.toPx())
                        quadraticTo(cx, cy - 30.dp.toPx(), cx + 10.dp.toPx(), cy - 15.dp.toPx())
                    }
                    drawPath(handle, Color(0xFF1E2022), style = Stroke(width = 3.dp.toPx()))
                }
            }
        }

        // Mini Category Label Tag overlay at top corner
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(8.dp)
                .background(Color.White.copy(alpha = 0.85f), RoundedCornerShape(8.dp))
                .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Text(
                text = category.uppercase(),
                color = Color(0xFF1E2022),
                fontSize = 8.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        }
    }
}
