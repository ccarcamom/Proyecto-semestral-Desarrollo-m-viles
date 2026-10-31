package com.example.proyecto_semestral.ui.screens.admin

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyecto_semestral.R
import com.example.proyecto_semestral.ui.components.BrandLogo
import com.example.proyecto_semestral.ui.components.admin.AdminNavigationToggle
import com.example.proyecto_semestral.ui.components.admin.AdminSearchField
import com.example.proyecto_semestral.ui.components.admin.AdminSection
import com.example.proyecto_semestral.ui.components.admin.BlockUserDialog
import com.example.proyecto_semestral.ui.components.admin.DeleteUserDialog
import com.example.proyecto_semestral.ui.components.admin.EditUserDialog
import com.example.proyecto_semestral.ui.components.admin.UserCard
import com.example.proyecto_semestral.ui.components.admin.UserStatusFilter
import com.example.proyecto_semestral.ui.components.admin.UserStatusFilters
import com.example.proyecto_semestral.ui.theme.ProyectoSemestralTheme

data class AdminUser(
    val id: String,
    val name: String,
    val email: String,
    val isActive: Boolean
)

@Composable
fun ManageUsersScreen(
    modifier: Modifier = Modifier,
    initialUsers: List<AdminUser> = sampleAdminUsers,
    onNavigateToRecipes: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val users = remember(initialUsers) { mutableStateListOf<AdminUser>().apply { addAll(initialUsers) } }
    var query by rememberSaveable { mutableStateOf("") }
    var selectedFilter by rememberSaveable { mutableStateOf(UserStatusFilter.ALL) }
    var editingUser by remember { mutableStateOf<AdminUser?>(null) }
    var deletingUser by remember { mutableStateOf<AdminUser?>(null) }
    var blockingUser by remember { mutableStateOf<AdminUser?>(null) }
    var editedName by rememberSaveable { mutableStateOf("") }
    var editedEmail by rememberSaveable { mutableStateOf("") }

    val visibleUsers = users.filter { user ->
        val matchesQuery = query.isBlank() ||
            user.name.contains(query, ignoreCase = true) ||
            user.email.contains(query, ignoreCase = true)
        val matchesStatus = when (selectedFilter) {
            UserStatusFilter.ALL -> true
            UserStatusFilter.ACTIVE -> user.isActive
            UserStatusFilter.BLOCKED -> !user.isActive
        }
        matchesQuery && matchesStatus
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                start = 20.dp,
                end = 20.dp,
                top = 12.dp,
                bottom = 28.dp
            ),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item { AdminHeader(onLogout = onLogout) }
            item {
                Column {
                    Text(
                        text = "ADMINISTRACIÓN",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelLarge.copy(letterSpacing = 2.5.sp)
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = "Gestionar usuarios",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontSize = 36.sp,
                            lineHeight = 42.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = "Administra el acceso a la aplicación.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 19.sp)
                    )
                }
            }
            item {
                AdminNavigationToggle(
                    selectedSection = AdminSection.USERS,
                    onSectionSelected = { if (it == AdminSection.RECIPES) onNavigateToRecipes() }
                )
            }
            item {
                AdminSearchField(value = query, onValueChange = { query = it })
            }
            item {
                UserStatusFilters(
                    selectedFilter = selectedFilter,
                    onFilterSelected = { selectedFilter = it }
                )
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Usuarios registrados",
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "${visibleUsers.size} usuarios",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            if (visibleUsers.isEmpty()) {
                item {
                    Text(
                        text = "No se encontraron usuarios.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(vertical = 24.dp)
                    )
                }
            } else {
                items(visibleUsers, key = { it.id }) { user ->
                    UserCard(
                        name = user.name,
                        email = user.email,
                        isActive = user.isActive,
                        onEdit = {
                            editedName = user.name
                            editedEmail = user.email
                            editingUser = user
                        },
                        onBlock = {
                            if (user.isActive) {
                                blockingUser = user
                            } else {
                                users.replace(user.copy(isActive = true))
                            }
                        },
                        onDelete = { deletingUser = user }
                    )
                }
            }
            item {
                AdminInfoBanner("Los cambios de acceso se aplican al guardar.")
            }
        }
    }

    editingUser?.let { user ->
        EditUserDialog(
            name = editedName,
            email = editedEmail,
            onNameChange = { editedName = it },
            onEmailChange = { editedEmail = it },
            onSave = {
                users.replace(user.copy(name = editedName.trim(), email = editedEmail.trim()))
                editingUser = null
            },
            onDismissRequest = { editingUser = null },
            saveEnabled = editedName.isNotBlank() && editedEmail.isNotBlank()
        )
    }
    deletingUser?.let { user ->
        DeleteUserDialog(
            userName = user.name,
            onConfirm = {
                users.removeAll { it.id == user.id }
                deletingUser = null
            },
            onDismissRequest = { deletingUser = null }
        )
    }
    blockingUser?.let { user ->
        BlockUserDialog(
            userName = user.name,
            onConfirm = {
                users.replace(user.copy(isActive = false))
                blockingUser = null
            },
            onDismissRequest = { blockingUser = null }
        )
    }
}

private fun MutableList<AdminUser>.replace(updated: AdminUser) {
    val index = indexOfFirst { it.id == updated.id }
    if (index >= 0) this[index] = updated
}

@Composable
internal fun AdminHeader(onLogout: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BrandLogo(modifier = Modifier.weight(1f))
        TextButton(onClick = onLogout) {
            Image(
                painter = painterResource(R.drawable.logout),
                contentDescription = "Cerrar sesión",
                modifier = Modifier.size(30.dp)
            )
        }
    }
}

@Composable
internal fun AdminInfoBanner(message: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = MaterialTheme.shapes.medium
    ) {
        Text(
            text = "ⓘ  $message",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(16.dp)
        )
    }
}

private val sampleAdminUsers = listOf(
    AdminUser("1", "Ana Martínez", "ana.martinez@correo.cl", true),
    AdminUser("2", "Carlos Rojas", "carlos.rojas@correo.cl", true),
    AdminUser("3", "María López", "maria.lopez@correo.cl", false)
)

@Preview(showBackground = true, widthDp = 430, heightDp = 1000)
@Composable
private fun ManageUsersScreenPreview() {
    ProyectoSemestralTheme { ManageUsersScreen() }
}
