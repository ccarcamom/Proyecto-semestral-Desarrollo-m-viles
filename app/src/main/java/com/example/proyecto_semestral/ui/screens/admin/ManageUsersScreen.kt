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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyecto_semestral.R
import com.example.proyecto_semestral.model.admin.AppUser
import com.example.proyecto_semestral.ui.components.BrandLogo
import com.example.proyecto_semestral.ui.components.admin.AdminNavigationToggle
import com.example.proyecto_semestral.ui.components.admin.AdminSearchField
import com.example.proyecto_semestral.ui.components.admin.AdminSection
import com.example.proyecto_semestral.ui.components.admin.BlockUserDialog
import com.example.proyecto_semestral.ui.components.admin.DeleteUserDialog
import com.example.proyecto_semestral.ui.components.admin.EditUserDialog
import com.example.proyecto_semestral.ui.components.admin.UserCard
import com.example.proyecto_semestral.ui.components.admin.UserStatusFilters
import com.example.proyecto_semestral.ui.theme.ProyectoSemestralTheme
import com.example.proyecto_semestral.viewmodel.admin.ManageUsersViewModel

@Composable
fun ManageUsersScreen(
    modifier: Modifier = Modifier,
    onNavigateToRecipes: () -> Unit = {},
    onLogout: () -> Unit = {},
    manageUsersViewModel: ManageUsersViewModel = viewModel()
) {
    val users by manageUsersViewModel.users.collectAsState()
    val visibleUsers = manageUsersViewModel.filteredUsers(users)
    var editingUser by remember { mutableStateOf<AppUser?>(null) }
    var deletingUser by remember { mutableStateOf<AppUser?>(null) }
    var blockingUser by remember { mutableStateOf<AppUser?>(null) }
    var editedName by rememberSaveable { mutableStateOf("") }
    var editedEmail by rememberSaveable { mutableStateOf("") }

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
                AdminSearchField(
                    value = manageUsersViewModel.query,
                    onValueChange = manageUsersViewModel::onQueryChange
                )
            }
            item {
                UserStatusFilters(
                    selectedFilter = manageUsersViewModel.selectedFilter,
                    onFilterSelected = manageUsersViewModel::onFilterSelected
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
                        isActive = !user.isBlocked,
                        onEdit = {
                            editedName = user.name
                            editedEmail = user.email
                            editingUser = user
                        },
                        onBlock = {
                            if (!user.isBlocked) {
                                blockingUser = user
                            } else {
                                manageUsersViewModel.unblockUser(user.id)
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
                if (manageUsersViewModel.updateUser(user.id, editedName, editedEmail)) {
                    editingUser = null
                }
            },
            onDismissRequest = { editingUser = null },
            saveEnabled = editedName.isNotBlank() && editedEmail.isNotBlank()
        )
    }
    deletingUser?.let { user ->
        DeleteUserDialog(
            userName = user.name,
            onConfirm = {
                manageUsersViewModel.deleteUser(user.id)
                deletingUser = null
            },
            onDismissRequest = { deletingUser = null }
        )
    }
    blockingUser?.let { user ->
        BlockUserDialog(
            userName = user.name,
            onConfirm = {
                manageUsersViewModel.blockUser(user.id)
                blockingUser = null
            },
            onDismissRequest = { blockingUser = null }
        )
    }
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

@Preview(showBackground = true, widthDp = 430, heightDp = 1000)
@Composable
private fun ManageUsersScreenPreview() {
    ProyectoSemestralTheme { ManageUsersScreen() }
}
