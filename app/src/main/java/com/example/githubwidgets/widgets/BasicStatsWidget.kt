package com.example.githubwidgets.widgets

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.ColorFilter
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.components.Scaffold
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxHeight
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.example.githubwidgets.R

class BasicStatsWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            @Composable
            fun StatColumn(stat: String, title: String) {
                Column(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = GlanceModifier.padding(8.dp)
                ) {
                    Text(
                        text = stat,
                        style = TextStyle(
                            color = GlanceTheme.colors.primary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        )
                    )
                    Text(
                        text = title,
                        style = TextStyle(color = GlanceTheme.colors.secondary)
                    )
                }
            }

            @Composable
            fun DrawContributionDots(weeks: Int) {
                val primaryPalette = GlanceTheme.colors.primary.getColor(context)
                val colors = listOf(
                    primaryPalette,
                    primaryPalette.copy(alpha = 0.80f),
                    primaryPalette.copy(alpha = 0.60f),
                    primaryPalette.copy(alpha = 0.50f),
                    primaryPalette.copy(alpha = 0.40f),
                )
                GlanceTheme {
                    Column(GlanceModifier.fillMaxHeight()) {
                        repeat(7) {
                            Row() {
                                repeat(weeks) {
                                    Image(
                                        provider = ImageProvider(R.drawable.contribution_dot),
                                        contentDescription = null,
                                        modifier = GlanceModifier.padding(4.dp),
                                        colorFilter = ColorFilter.tint(ColorProvider(colors.random()))
                                    )
                                }
                            }
                        }
                    }
                }
            }


            Scaffold(
                backgroundColor = GlanceTheme.colors.widgetBackground
            ) {
                Column(GlanceModifier.padding(vertical = 16.dp)) {
                    Row {
                        Image(
                            provider = ImageProvider(R.drawable.test_image),
                            contentDescription = "My image",
                            modifier = GlanceModifier.size(50.dp).cornerRadius(25.dp)
                        )
                        Spacer(GlanceModifier.size(8.dp))
                        Column {
                            Text(
                                "John Doe", style = TextStyle(
                                    color = GlanceTheme.colors.primary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                )
                            )
                            Text(
                                "john_doe", style = TextStyle(
                                    color = GlanceTheme.colors.secondary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    fontFamily = androidx.glance.text.FontFamily.Monospace
                                )
                            )
                        }
                    }
                    Spacer(GlanceModifier.size(6.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = GlanceModifier.fillMaxWidth()
                    ) {
                        Column(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            StatColumn("480", "Followers")
                            StatColumn("1.2K", "Stars")
                        }
                        Spacer(GlanceModifier.size(4.dp))
                        DrawContributionDots(10)
                        DrawContributionDots(2)
                    }
                }
            }
        }
    }
}


