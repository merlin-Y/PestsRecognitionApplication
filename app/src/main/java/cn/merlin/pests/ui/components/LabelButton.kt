package cn.merlin.pests.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun LabelButton(icon : ImageVector, label : String, modifier: Modifier = Modifier ,onClick:() -> Unit){
    Button(onClick =  onClick , shape = MaterialTheme.shapes.extraLarge, modifier = modifier, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(0.dp),) {
            Icon(icon, label, tint = MaterialTheme.colorScheme.onSurface)
            Text(text = label, color = MaterialTheme.colorScheme.onSurface)
        }
    }
}