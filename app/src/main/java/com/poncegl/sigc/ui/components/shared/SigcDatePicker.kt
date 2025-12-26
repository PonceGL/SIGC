package com.poncegl.sigc.ui.components.shared

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SigcDatePicker(
    label: String,
    selectedDate: Long?,
    onDateSelected: (Long?) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    minDateMillis: Long? = null,
    maxDateMillis: Long? = null,
    colors: DatePickerColors = DatePickerDefaults.colors()
) {
    var showModal by remember { mutableStateOf(false) }

    val dateStr = remember(selectedDate) {
        selectedDate?.let {
            val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            formatter.timeZone = TimeZone.getTimeZone("UTC")
            formatter.format(Date(it))
        } ?: ""
    }

    Box(modifier = modifier) {
        OutlinedTextField(
            value = dateStr,
            onValueChange = { },
            label = { Text(label) },
            readOnly = true,
            enabled = enabled,
            trailingIcon = {
                IconButton(onClick = { if (enabled) showModal = true }, enabled = enabled) {
                    Icon(Icons.Default.DateRange, contentDescription = "Seleccionar fecha")
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,

                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,

                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,

                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,

                disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                disabledBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.38f),
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f),
                disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f)
            ),
            singleLine = true
        )

        if (enabled) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable { showModal = true }
            )
        }
    }

    if (showModal) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate ?: System.currentTimeMillis(),
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    val minCheck = minDateMillis?.let { utcTimeMillis >= it } ?: true
                    val maxCheck = maxDateMillis?.let { utcTimeMillis <= it } ?: true
                    return minCheck && maxCheck
                }

                override fun isSelectableYear(year: Int): Boolean {
                    val minYearCheck = minDateMillis?.let {
                        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                        calendar.timeInMillis = it
                        year >= calendar.get(Calendar.YEAR)
                    } ?: true

                    val maxYearCheck = maxDateMillis?.let {
                        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                        calendar.timeInMillis = it
                        year <= calendar.get(Calendar.YEAR)
                    } ?: true

                    return minYearCheck && maxYearCheck
                }
            }
        )

        DatePickerDialog(
            onDismissRequest = { showModal = false },
            confirmButton = {
                TextButton(onClick = {
                    onDateSelected(datePickerState.selectedDateMillis)
                    showModal = false
                }) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showModal = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                colors = colors
            )
        }
    }
}
