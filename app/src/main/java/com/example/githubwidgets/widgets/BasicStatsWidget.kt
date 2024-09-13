package com.example.githubwidgets.widgets

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            fun DrawContributionDots() {
                val drawableList = listOf(
                    R.drawable.contribution_dot_1,
                    R.drawable.contribution_dot_2,
                    R.drawable.contribution_dot_3,
                    R.drawable.contribution_dot_4
                )
                Column(GlanceModifier.fillMaxHeight()) {
                    repeat(7) {
                        Row(GlanceModifier.fillMaxWidth()) {
                            repeat(1) {
                                val randomDrawable = drawableList.random()
                                Image(
                                    provider = ImageProvider(randomDrawable),
                                    contentDescription = null
                                )
                                if (it < 19) {
                                    Spacer(GlanceModifier.size(4.dp))
                                }
                            }
                        }
                        if (it < 6) {
                            Spacer(GlanceModifier.size(4.dp))
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
                                "Aniruddha Mukherjee", style = TextStyle(
                                    color = GlanceTheme.colors.primary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                )
                            )
                            Text(
                                "amkhrjee", style = TextStyle(
                                    color = GlanceTheme.colors.secondary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    fontFamily = androidx.glance.text.FontFamily.Monospace
                                )
                            )
                        }
                    }
                    Spacer(GlanceModifier.size(4.dp))
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
                        DrawContributionDots()
                    }
                }
            }
        }
    }
}


