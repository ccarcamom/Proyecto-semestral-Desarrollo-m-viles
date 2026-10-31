package com.example.proyecto_semestral.ui.screens.recipe

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyecto_semestral.model.admin.AppRecipe
import com.example.proyecto_semestral.ui.components.AppTopBar
import com.example.proyecto_semestral.ui.components.EmbeddedVideoPlayer
import com.example.proyecto_semestral.ui.components.IngredientRow
import com.example.proyecto_semestral.ui.components.PreparationStep
import com.example.proyecto_semestral.ui.theme.ProyectoSemestralTheme
import com.example.proyecto_semestral.viewmodel.RecipeDetailViewModel

@Composable
fun RecipeDetailScreen(
    recipeId: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    recipeDetailViewModel: RecipeDetailViewModel = viewModel()
) {
    val recipes by recipeDetailViewModel.recipes.collectAsState()
    val recipe = recipes.firstOrNull { it.id == recipeId }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        AppTopBar(
            title = "Receta",
            onBackClick = onBack
        )

        if (recipe == null) {
            MissingRecipeState(onBack = onBack, modifier = Modifier.weight(1f))
        } else {
            RecipeContent(
                recipe = recipe,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun RecipeContent(
    recipe: AppRecipe,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showVideo by rememberSaveable(recipe.id) { mutableStateOf(false) }

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(bottom = 32.dp)
    ) {
        Image(
            painter = coil.compose.rememberAsyncImagePainter(
                model = recipe.imageUri ?: recipe.imageRes,
                error = painterResource(recipe.imageRes)
            ),
            contentDescription = recipe.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(256.dp),
            contentScale = ContentScale.Crop
        )

        Column(modifier = Modifier.padding(horizontal = 26.dp)) {
            Spacer(modifier = Modifier.height(28.dp))
            Text(
                text = recipe.category.uppercase(),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.8.sp
                )
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = recipe.title,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontSize = 36.sp,
                    lineHeight = 40.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = recipe.description,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 21.sp,
                    lineHeight = 30.sp
                )
            )

            if (recipe.duration.isNotBlank() || recipe.yield.isNotBlank()) {
                Spacer(modifier = Modifier.height(24.dp))
                RecipeMetadata(recipe)
            }

            Spacer(modifier = Modifier.height(8.dp))
            if (recipe.videoUrl.isNotBlank()) {
                if (showVideo) {
                    EmbeddedVideoPlayer(videoUrl = recipe.videoUrl)
                    Spacer(modifier = Modifier.height(12.dp))
                }
                Button(
                    onClick = { showVideo = !showVideo },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = if (showVideo) "Ocultar video" else "Iniciar video")
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
            Button(
                onClick = {
                    val ingredients = recipe.ingredients.joinToString("\n") { ingredient ->
                        "• ${ingredient.name}: ${ingredient.amount}"
                    }
                    val shareText = buildString {
                        appendLine(recipe.title)
                        appendLine(recipe.description)
                        if (recipe.videoUrl.isNotBlank()) {
                            appendLine(recipe.videoUrl)
                        }
                        appendLine()
                        if (recipe.duration.isNotBlank()) appendLine("Duración: ${recipe.duration}")
                        if (recipe.yield.isNotBlank()) appendLine("Rendimiento: ${recipe.yield}")
                        if (ingredients.isNotBlank()) {
                            appendLine()
                            appendLine("Ingredientes:")
                            append(ingredients)
                        }
                    }
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_SUBJECT, recipe.title)
                        putExtra(Intent.EXTRA_TEXT, shareText)
                    }
                    context.startActivity(
                        Intent.createChooser(shareIntent, "Compartir receta")
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Compartir receta")
            }

            if (recipe.ingredients.isNotEmpty()) {
                Spacer(modifier = Modifier.height(28.dp))
                SectionTitle(text = "Ingredientes")
                Spacer(modifier = Modifier.height(10.dp))
                recipe.ingredients.forEach { ingredient ->
                    IngredientRow(
                        name = ingredient.name,
                        amount = ingredient.amount
                    )
                }
            }

            if (recipe.steps.isNotEmpty()) {
                Spacer(modifier = Modifier.height(28.dp))
                SectionTitle(text = "Paso a paso")
                Spacer(modifier = Modifier.height(20.dp))
                recipe.steps.forEachIndexed { index, step ->
                    PreparationStep(
                        stepNumber = index + 1,
                        description = step
                    )
                    if (index != recipe.steps.lastIndex) {
                        Spacer(modifier = Modifier.height(22.dp))
                    }
                }
            }

            if (recipe.suppliesTitle.isNotBlank() || recipe.suppliesDescription.isNotBlank()) {
                Spacer(modifier = Modifier.height(28.dp))
                SuppliesCard(recipe.suppliesTitle, recipe.suppliesDescription)
            }

        }
    }
}

@Composable
private fun RecipeMetadata(recipe: AppRecipe) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (recipe.duration.isNotBlank()) {
            Text(
                text = "◷  ${recipe.duration}",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 19.sp)
            )
        }
        if (recipe.yield.isNotBlank()) {
            Text(
                text = "♨  ${recipe.yield}",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 19.sp)
            )
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.headlineMedium.copy(
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
    )
}

@Composable
private fun SuppliesCard(title: String, description: String) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(22.dp)) {
            Text(
                text = "INSUMOS PARA TU CREACIÓN",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.4.sp
                )
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = title,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = description,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp, lineHeight = 27.sp)
            )
        }
    }
}

@Composable
private fun MissingRecipeState(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "No encontramos esta receta.",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "El contenido puede haber sido movido o ya no estar disponible.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onBack) {
            Text(text = "Volver")
        }
    }
}

@Preview(showBackground = true, widthDp = 400, heightDp = 900)
@Composable
private fun RecipeDetailScreenPreview() {
    ProyectoSemestralTheme {
        RecipeDetailScreen(
            recipeId = "galletas-chips-chocolate",
            onBack = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MissingRecipeStatePreview() {
    ProyectoSemestralTheme {
        MissingRecipeState(onBack = {})
    }
}
