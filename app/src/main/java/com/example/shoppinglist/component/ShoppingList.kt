package com.example.shoppinglist.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.shoppinglist.ui.theme.ShoppingListTheme
import kotlinx.coroutines.delay

@Composable
fun ShoppingList(items: List<String>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items, key = { it }) { item ->
            AnimatedShoppingListItem(item = item)
        }
    }
}

@Composable
fun AnimatedShoppingListItem(item: String) {
    var isVisible by remember { mutableStateOf(false) }

    // Trigger animasi saat item baru muncul
    LaunchedEffect(item) {
        delay(100)
        isVisible = true
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(600)) +
                expandVertically(animationSpec = tween(600))
    ) {
        ShoppingListItem(item)
    }
}

@Composable
fun ShoppingListItem(item: String) {
    var isSelected by remember { mutableStateOf(false) }

    // Animasi warna background
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) {
            MaterialTheme.colorScheme.primaryContainer   // biru muda container
        } else {
            MaterialTheme.colorScheme.surface            // surface putih/light
        },
        animationSpec = tween(300),
        label = "backgroundColor"
    )

    // Animasi warna teks
    val contentColor by animateColorAsState(
        targetValue = if (isSelected) {
            MaterialTheme.colorScheme.onPrimaryContainer // teks di atas biru
        } else {
            MaterialTheme.colorScheme.onSurface
        },
        animationSpec = tween(300),
        label = "contentColor"
    )

    // Animasi elevasi
    val elevation by animateDpAsState(
        targetValue = if (isSelected) 8.dp else 2.dp,
        animationSpec = tween(200),
        label = "elevation"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation, RoundedCornerShape(12.dp), clip = false)
            .clickable { isSelected = !isSelected },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor,
            contentColor = contentColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = CardDefaults.outlinedCardBorder(enabled = true)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Nama item + ikon kiri
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Item Icon",
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = item,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                    color = contentColor
                )
            }

            // Ikon kanan (Add → Check)
            Icon(
                imageVector = if (isSelected) Icons.Default.Check else Icons.Default.Add,
                contentDescription = if (isSelected) "Selected" else "Add",
                tint = if (isSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShoppingListPreviewLight() {
    ShoppingListTheme(darkTheme = false) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ShoppingList(
                items = listOf("Beras", "Telur", "Minyak", "Apel", "Daging Sapi")
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShoppingListPreviewDark() {
    ShoppingListTheme(darkTheme = true) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ShoppingList(
                items = listOf("Beras", "Telur", "Minyak", "Apel", "Daging Sapi")
            )
        }
    }
}
