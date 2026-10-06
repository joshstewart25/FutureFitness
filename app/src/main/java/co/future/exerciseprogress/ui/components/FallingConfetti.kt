package co.future.exerciseprogress.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.sin
import kotlin.random.Random

// A slow piece takes this long to fall once. Fast pieces fall twice in the same time.
private const val LOOP_DURATION_MILLIS = 3000

// Pieces start just above the top and end just below the bottom, so they enter and leave off screen.
private const val START_Y = -0.1f
private const val FALL_DISTANCE = 1.2f

private val confettiColors = listOf(
    Color(0xFFEF5350),
    Color(0xFFFFCA28),
    Color(0xFF66BB6A),
    Color(0xFF42A5F5),
    Color(0xFFAB47BC)
)

// Positions are fractions of the canvas size so the same piece works at any card size.
private class ConfettiPiece(
    val startX: Float,
    val headStart: Float,
    val fallsPerLoop: Int,
    val swayWidth: Float,
    val swayCount: Float,
    val totalRotation: Float,
    val color: Color
)

// Confetti that falls across its bounds and loops forever while it is on screen.
// Touches pass straight through, so it can sit on top of clickable content.
@Composable
fun FallingConfetti(
    modifier: Modifier = Modifier,
    pieceCount: Int = 24
) {
    val pieces = remember { List(pieceCount) { createRandomPiece() } }
    val loopTime = rememberInfiniteTransition(label = "confetti").animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(LOOP_DURATION_MILLIS, easing = LinearEasing)),
        label = "confettiLoopTime"
    )

    Canvas(modifier = modifier.clipToBounds()) {
        val pieceWidth = 6.dp.toPx()
        val pieceHeight = 4.dp.toPx()

        pieces.forEach { piece ->
            // Whole-number falls per loop keep the loop seamless. The restart happens off screen.
            val fallProgress = (loopTime.value * piece.fallsPerLoop + piece.headStart) % 1f
            val sway = sin(fallProgress * piece.swayCount * 2f * PI.toFloat()) * piece.swayWidth
            val center = Offset(
                x = (piece.startX + sway) * size.width,
                y = (START_Y + fallProgress * FALL_DISTANCE) * size.height
            )

            rotate(degrees = fallProgress * piece.totalRotation, pivot = center) {
                drawRect(
                    color = piece.color,
                    topLeft = Offset(center.x - pieceWidth / 2, center.y - pieceHeight / 2),
                    size = Size(pieceWidth, pieceHeight)
                )
            }
        }
    }
}

private fun createRandomPiece(): ConfettiPiece {
    return ConfettiPiece(
        startX = Random.nextFloat(),
        headStart = Random.nextFloat(),
        fallsPerLoop = Random.nextInt(1, 3),
        swayWidth = randomBetween(0.01f, 0.04f),
        swayCount = randomBetween(1f, 3f),
        totalRotation = randomBetween(-720f, 720f),
        color = confettiColors.random()
    )
}

private fun randomBetween(from: Float, until: Float): Float {
    return from + Random.nextFloat() * (until - from)
}
